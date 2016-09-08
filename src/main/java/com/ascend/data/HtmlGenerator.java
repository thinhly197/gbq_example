package com.ascend.data;

import com.ascend.data.util.*;
import com.ascendcorp.dd.connector.BigQueryTableReader;
import com.google.api.services.bigquery.Bigquery;
import freemarker.template.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import scala.collection.Iterator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by thinhly on 3/8/16.
 */
public class HtmlGenerator {

    private static LogUtil log = LogUtil.getInstance();
    private static String EDM_CONFIG_FILE = "/edm-html-generator/config.properties";
    private static int MAX_PRODUCTS_PER_EMAIL; // the 9 product per email
    private static int LOADING_PRODUCTS_PER_EMAIL; // number of products (per email) to get as a buffer

    public static void main(String[] args)  throws ConfigurationException {

        /* start logging */
        log.logInfo(HtmlGenerator.class, "*** Email HTML generator application start running ***");

        AutoReloadProperty.setPropertiesFile(new PropertiesConfiguration(System.getProperty("user.home")
                + EDM_CONFIG_FILE));

        /* check for the hard-config-switch
            if off then we dont run the app at all */
        String configPower = AutoReloadProperty.getProperty("power");
        if("off".equals(configPower)){
            log.logInfo(HtmlGenerator.class, "Email HTML generator application stopped and did nothing. Power is off.");
            return;
        }

        /* check for number of items to be loaded to the email */
        MAX_PRODUCTS_PER_EMAIL = Integer.parseInt(AutoReloadProperty.getProperty("numberOfProducts"));
        if(MAX_PRODUCTS_PER_EMAIL != 9){
            log.logError(HtmlGenerator.class, "*** Number of Products per email is wrong (must be 9 products) ***");
            return;
        }
        LOADING_PRODUCTS_PER_EMAIL = Integer.parseInt(AutoReloadProperty.getProperty("numberOfLoadingProductPerEmail"));
        if(LOADING_PRODUCTS_PER_EMAIL <  MAX_PRODUCTS_PER_EMAIL) {
            log.logError(HtmlGenerator.class, "*** Number of Buffer Products cannot be less than Number of Products ***");
            return;
        }

        /* setting up the freemaker template */
        File fileTemplate = new File(System.getProperty("user.home") + "/"
                + AutoReloadProperty.getProperty("templateFolder"));
        if(!fileTemplate.exists()) {
            fileTemplate.mkdirs();
        }
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        FreemakerConfiguration.setFile(fileTemplate);
        FreemakerConfiguration.setConfig(fileTemplate, cfg);

        /* create log folder if not exist */
        File logFolder = new File(System.getProperty("user.home") + "/"
                + AutoReloadProperty.getProperty("logFolder"));
        if(!logFolder.exists()) {
            logFolder.mkdirs();
        }

        /* Config UTM Campaign name */
        UtmGenerator.configUtmCampaignName();

        /* Config email sender */
        EmailSender.setEmailConfiguration(AutoReloadProperty.getProperty("emailHost"),
                AutoReloadProperty.getProperty("smtpUsername"),
                AutoReloadProperty.getProperty("smtpPassword"),
                Integer.parseInt(AutoReloadProperty.getProperty("emailPort")));

        /* Get Query Strings */
        String query = null;
        String nonDupProductQuery = null;
        try {
            query = SqlBuilder.getAllEmailProducts(); // this is the all email and all buffer products
            nonDupProductQuery = SqlBuilder.getAllProductsNoDuplicate(); // list of all products (w/o dup)
        } catch (IOException e) {
            e.printStackTrace();
            EmailSender.close();
            log.logInfo(HtmlGenerator.class, "*** Email HTML generator application stopped. FAILED ***");
        }

        /* going to GBQ to get the data... */
        long numberOfEmail = 1;
        Bigquery bigquery = null;
        try {
            /* Init products cache*/
            ProductCache.initialize();

            bigquery = BigQueryExecutor.createAuthorizedClient();

            /*
            Load some products and put to the cache first
             we only get a limited number of products to be in the cache before
             we start sending out emails (still need to go get item anyway...)
             this number is configurable. it's called "firstProductCaching"
             in your config file
            */
            int itmNumberOfThread = Integer.parseInt(AutoReloadProperty.getProperty("itmNumberOfThread"));
            // this is product list
            BigQueryTableReader tableReader = new BigQueryTableReader("itruemart-973", nonDupProductQuery, bigquery);
            Iterator<String[]> i = tableReader.iterator();
            ExecutorService es = Executors.newFixedThreadPool(itmNumberOfThread);
            while (i.hasNext()){
                String[] row = i.next();
                es.execute(new LoadingProductCache(row[0]));
            }
            // Waiting all threads finish
            es.shutdown();
            while (!es.awaitTermination(60, TimeUnit.SECONDS)) ;

            /* Load email and its products for sending */
            // this is the email to prod map
            tableReader = new BigQueryTableReader("itruemart-973", query, bigquery);
            i = tableReader.iterator();
            int awsNumberOfThread = Integer.parseInt(AutoReloadProperty.getProperty("awsNumberOfThread"));
            String currentEmail = new String();
            List<String> currentProductList = new ArrayList();
            long total = 1;
            ExecutorService es2 = Executors.newFixedThreadPool(awsNumberOfThread);
            while (i.hasNext()) {
                String[] row = i.next();
                currentProductList.add(row[1]); // add product pkey
                if (total % LOADING_PRODUCTS_PER_EMAIL == 0) {
                    currentEmail = row[0];
                    es2.execute(new SendingEmail(currentEmail, currentProductList));
                    log.logInfo(HtmlGenerator.class, "New thread for sending email to customer " + currentEmail
                            + ", appended to pool at " + numberOfEmail++);
                    currentProductList = new ArrayList();
                }
                total++;
            }
            // Waiting all threads finish
            try {
                es2.shutdown();
                while (!es2.awaitTermination(60, TimeUnit.SECONDS)) ;
            } catch (InterruptedException e) {
                log.logInfo(HtmlGenerator.class, "*** Timeout while sending email for customer " + currentEmail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            EmailSender.close();
            log.logInfo(HtmlGenerator.class, "*** Email HTML generator application stopped. FAILED ***");
        }

        /* Send the email for reporter */
        try {
            EmailSender.sendReportEmail(UtmGenerator.getUtmCampaignName(), --numberOfEmail);
        } catch (Exception e) {
            e.printStackTrace();
            EmailSender.close();
            log.logError(HtmlGenerator.class, "*** Can not send the report email. Please check the log.");
        }

        EmailSender.close();
        AutoReloadProperty.setProperty("power", "off");
        log.logInfo(HtmlGenerator.class, "*** Email HTML generator application stopped. SUCCESS ***");
    }

    public static class SendingEmail implements Runnable {
        private String email;
        private List<String> productList;

        public SendingEmail(String email, List<String> productList) {
            this.email = email;
            this.productList = productList;
        }

        public void run() {
            EmailGenerator.generateEmailHtml(this.email, this.productList);
        }
    }

    public static class LoadingProductCache implements Runnable {
        private String pkey;

        public LoadingProductCache(String pkey){
            this.pkey = pkey;
        }

        public void run() {
            ProductCache.getProduct(this.pkey);
        }
    }
}
