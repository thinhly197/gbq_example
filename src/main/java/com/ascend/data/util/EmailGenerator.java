package com.ascend.data.util;

import com.ascend.data.model.Product;
import com.ascend.data.model.User;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thinhly on 3/11/16.
 */
public class EmailGenerator {
    private static LogUtil log = LogUtil.getInstance();

    private static int NUMBER_OF_PRODUCT = 9;
    private static int PRODUCT_PER_ROW = 3;

    private static final String OUTPUT_HTML_FILE = "email_recommendation.html";
    private static final int DEFAULT_DELAY_TIME_FOR_ITM = 2000;

    // Real data
    private static List<Product> generateProjectListDetail(List<String> productList){
        try{
            NUMBER_OF_PRODUCT = Integer.parseInt(AutoReloadProperty.getProperty("numberOfProducts"));
            PRODUCT_PER_ROW = Integer.parseInt(AutoReloadProperty.getProperty("productsPerRow"));
        } catch(Exception ex){
            ex.printStackTrace();
        }

        List<Product> products = new ArrayList<>();
        int countAddedProduct = 0;
        for(int i = 0; i < productList.size(); i++){
            Product p = ProductCache.getProduct(productList.get(i));
            if(p != null && p.getId() != 0) {
                products.add(p);
                countAddedProduct++;
            } else {
                LogUtil.getInstance().logError(EmailGenerator.class,
                        "Don't have product info on PCMS API; pkey: " + productList.get(i));
            }

            if(countAddedProduct > NUMBER_OF_PRODUCT){
                break;
            }
        }
        return products;
    }

    public static boolean generateEmailHtml(String email, List<String> productList){
        Template emailTemp = FreemakerConfiguration.getEmailTemplate();

        if(emailTemp != null) {
            // Create data-model for email template
            Map<String, Object> root = new HashMap<>();

            // Configure all data for user
            User user = new User();
            user.setEmail(email);
            UtmGenerator.setUtmForUser(user);

            // Get product list
            List<Product> products = generateProjectListDetail(productList);
            user.setProducts(products);

            if(products == null || products.size() < NUMBER_OF_PRODUCT){
                log.logError(EmailGenerator.class, "Cannot find enough recommendation products for " + user.getEmail()
                        + ". Number of products: " + products.size());
                EmailSender.addFailureEmails(user.getEmail());
                return false;
            }

            //utm_source=Extmail&utm_medium=RecommendationEmail
            // &utm_content=5f80990b1cb344e12f104b6c4bd83a58
            // &utm_campaign=20160316_testemailnumberone

            StringBuffer utmLink = new StringBuffer();
            utmLink.append("?utm_source=").append(user.getUtmSource().trim());
            utmLink.append("&utm_medium=").append(user.getUtmMedium().trim());
            utmLink.append("&utm_content=").append(user.getHashSsoId().trim());
            utmLink.append("&utm_campaign=").append(user.getUtmCampaign().trim());

            int numberOfProduct = 0;
            int numberOfRows = 0;
            if(products.size() > NUMBER_OF_PRODUCT){
                numberOfProduct = NUMBER_OF_PRODUCT;
            } else {
                numberOfProduct = products.size();
            }
            numberOfRows = numberOfProduct/PRODUCT_PER_ROW + 1;

            List<ArrayList<Product>> rows = new ArrayList<>();
            for(int i = 0; i < numberOfRows; i++){
                List<Product> row = new ArrayList<>();
                for(int j = 0; j < PRODUCT_PER_ROW; j++){
                    if((i * PRODUCT_PER_ROW + j) < numberOfProduct){
                        row.add(products.get(i * PRODUCT_PER_ROW + j));
                    }
                }
                rows.add((ArrayList<Product>) row);
            }
            root.put("listOfProducts", rows);
            root.put("unsubscribeUrl", user.getUnsubscribeUrl());
            root.put("utmLink", utmLink.toString());

            Writer out = null;
            try {
                //out = new OutputStreamWriter(new FileOutputStream(OUTPUT_HTML_FILE), "utf-8");
                // out is your HTML string
                out = new StringWriter();
                emailTemp.setAutoFlush(true);
                emailTemp.process(root, out);
                EmailSender.sendRecommendEmail(user.getEmail(), out.toString(), user.getUnsubscribeUrl());
                out.close();
                return true;
            } catch (TemplateException e) {
                log.logError(EmailGenerator.class, "Something wrong when parse the HTML template\n"
                        + e.getMessage());
            } catch (IOException e) {
                log.logError(EmailGenerator.class, "Can not file the HTML file\n"
                        + e.getMessage());
            } catch (Exception e) {
                log.logError(EmailGenerator.class, "Can not send the email for customer " + user.getEmail()
                        + ". " + e.getMessage());
            }
        } else {
            log.logError(EmailGenerator.class, "The email template is empty. Check the template file again...");
        }

        return false;
    }
}
