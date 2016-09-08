package com.ascend.data.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.nlab.smtp.pool.SmtpConnectionPool;
import org.nlab.smtp.transport.connection.ClosableSmtpConnection;
import org.nlab.smtp.transport.factory.SmtpConnectionFactories;
import org.nlab.smtp.transport.factory.SmtpConnectionFactory;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;

/**
 * Created by thinhly on 3/22/16.
 */
public class EmailSender {
    // Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
    private static String SMTP_USERNAME = null;  // Replace with your SMTP username.
    private static String SMTP_PASSWORD = null;  // Replace with your SMTP password.

    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    private static String HOST = null;

    // Port we will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
    // STARTTLS to encrypt the connection.
    private static int PORT = 25;

    private static SmtpConnectionPool smtpConnectionPool;

    // Instantiate an Amazon SES client, which will make the service call.
    // The service call requires your AWS credentials.
    // Because we're not providing an argument when instantiating the client, the SDK will attempt
    // to find your AWS credentials using the default credential provider chain.
    // The first place the chain looks for the credentials
    // is in environment variables AWS_ACCESS_KEY_ID and AWS_SECRET_KEY.
    private static AmazonSimpleEmailServiceClient client;

    private static boolean isSMTP;

    public static List<String> failureEmails = Collections.synchronizedList(new ArrayList<>());

    public static List<String> getFailureEmails(){
        return failureEmails;
    }

    public static void addFailureEmails(String email){
        failureEmails.add(email);
    }

    public static void setEmailConfiguration(String host, String username, String password, int port){
        isSMTP = Boolean.parseBoolean(AutoReloadProperty.getProperty("isSMTP"));
        if(isSMTP) {
            SMTP_USERNAME = username;
            SMTP_PASSWORD = password;
            HOST = host;
            PORT = port;

            // Create a Properties object to contain connection configuration information.
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", PORT);

            // Set properties indicating that we want to use STARTTLS to encrypt the connection.
            // The SMTP session will begin on an unencrypted connection, and then the client
            // will issue a STARTTLS command to upgrade to an encrypted connection.
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.localhost", "localhost");
            props.put("mail.smtp.connectiontimeout", "10000");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.host", host);

            // Create a Session object to represent a mail session with the specified properties.
            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            username, password);
                }
            });

            //Declare the factory and the connection pool, usually at the application startup
            SmtpConnectionFactory factory = SmtpConnectionFactories.newSmtpFactory(session);
            smtpConnectionPool = new SmtpConnectionPool(factory);
        } else {
            client = new AmazonSimpleEmailServiceClient();
        }
    }

    public static void close(){
        if(isSMTP) {
            //Close the pool, usually when the application shutdown
            smtpConnectionPool.close();
        }
    }

    public static void sendRecommendEmail(String email, String body, String unsubscribeUrl) throws Exception{
        if(isSMTP){
            sendRecommendEmailSmtp(email, body, unsubscribeUrl);
        }else{
            sendRecommendEmailSesApi(email, body);
        }
    }

    private static String getDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void setSmtpConnectionPool(SmtpConnectionPool pool){
        EmailSender.smtpConnectionPool = pool;
    }

    public static void setAwsSesClient(AmazonSimpleEmailServiceClient client){
        EmailSender.client = client;
    }

    private static void sendRecommendEmailSmtp(String email, String body, String unsubscribeUrl) throws Exception {
        if(HOST == null || SMTP_USERNAME == null || SMTP_PASSWORD == null){
            return;
        }
        String configSubject = AutoReloadProperty.getProperty("emailSubject");
        boolean isTest = Boolean.parseBoolean(AutoReloadProperty.getProperty("isTest"));
        if(!isTest) {
            send(email, configSubject, body, unsubscribeUrl);
        } else {
            send(email, configSubject + " (" + getDateTime() + ") " + email, body, unsubscribeUrl);
        }
    }

    private static void send(String email, String subject, String body, String unsubscribeUrl)
            throws Exception{
        //Borrow an object in a try-with-resource statement or call `close` by yourself
        try (ClosableSmtpConnection transport = smtpConnectionPool.borrowObject()) {
            // Create a message with the specified information.
            MimeMessage msg = new MimeMessage(transport.getSession());
            msg.setHeader("Content-Type", "text/html; charset=UTF-8");
            msg.setHeader("List-Unsubscribe", "<" + unsubscribeUrl + ">");

            msg.setFrom(new InternetAddress(AutoReloadProperty.getProperty("emailSender")));
            String to = email;
            boolean isTest = Boolean.parseBoolean(AutoReloadProperty.getProperty("isTest"));
            if(!isTest) {
                msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            } else {
                String ccList = AutoReloadProperty.getProperty("emailReceivers");
                if(ccList != null && !ccList.equals("")){
                    msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ccList.replaceAll(";", ",")));
                }
            }

            //String encodedBody = new String (body.getBytes("UTF-8"),"UTF-8");
            msg.setSubject(subject, "UTF-8");
            msg.setContent(body, "text/html; charset=UTF-8");
            LogUtil.getInstance().logInfo(EmailSender.class, "Sent email time: "
                    + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
            transport.sendMessage(msg);
            LogUtil.getInstance().logInfo(EmailSender.class, "Finished sending email time: "
                    + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
        }
    }

    public static void sendReportEmail(String utmCampaign, long numberOfSentEmail) throws  Exception{
        String cfgSubject = "Report for Recommendation Email (" + getDateTime() + "). Campaign: " + utmCampaign;
        String[] reporters = AutoReloadProperty.getProperty("emailReporter").split(";");
        List<String> destinations = new ArrayList<>();
        for(int i = 0; i < reporters.length; i++){
            destinations.add(reporters[i]);
        }

        StringBuffer body = new StringBuffer();
        body.append("UTM campaign: ").append(utmCampaign).append(System.lineSeparator());
        body.append("Total sending emails: ").append(numberOfSentEmail).append(System.lineSeparator());
        if(failureEmails.isEmpty()) {
            body.append("Failure sending emails: None");
        } else {
            body.append("Failure sending emails: ").append(failureEmails.size()).append(System.lineSeparator());
            for(int i = 0; i < failureEmails.size(); i++){
                body.append(failureEmails.get(i)).append(System.lineSeparator());
            }
        }

        Content subject = new Content().withData(cfgSubject);
        Content textBody = new Content().withData(body.toString());
        Body sendingBody = new Body().withText(textBody);

        // Create a message with the specified subject and body.
        com.amazonaws.services.simpleemail.model.Message message =
                new com.amazonaws.services.simpleemail.model.Message()
                        .withSubject(subject)
                        .withBody(sendingBody);
        for (String destination : destinations) {
            // Construct an object to contain the recipient address.
            Destination dest = new Destination().withToAddresses(destination);
            SendEmailRequest request = new SendEmailRequest()
                    .withSource(AutoReloadProperty.getProperty("emailSender"))
                    .withDestination(dest)
                    .withMessage(message);
            int currentTry = 0;
            int maxRetries = 10;
            while (maxRetries-- > 0) {
                try {
                    currentTry++;
                    LogUtil.getInstance().logInfo(EmailSender.class, "Sending for " + destinations.toString());
                    //call Amazon SES to send the message
                    SendEmailResult result = client.sendEmail(request);
                    LogUtil.getInstance().logInfo(EmailSender.class, "Sent for " + destinations.toString()
                            + ". Sent status: " + result.getMessageId());
                    break;
                } catch (AmazonServiceException e) {
                    //retries only throttling errors
                    if ("Throttling".equals(e.getErrorCode()) && "Maximum sending rate exceeded.".equals(e.getMessage())) {
                        long backoffDuration = getSleepDuration(currentTry, 10, 5000);
                        LogUtil.getInstance().logInfo(EmailSender.class, "Maximum send rate exceeded when sending" +
                                " email to " + destination + ". " +
                                (maxRetries > 0 ? "Will retry after backoff." : "Will not retry after backoff."));
                        try {
                            Thread.sleep(backoffDuration);
                        } catch (InterruptedException e1) {
                            addFailureEmails(destination);
                            return;
                        }
                    } else {
                        LogUtil.getInstance().logInfo(EmailSender.class, "Unable to send email to: " + destination + ". "
                                + e.toString());
                        addFailureEmails(destination);
                        break;
                    }
                } catch (Exception e) {
                    LogUtil.getInstance().logInfo(EmailSender.class, "Unable to send email to: " + destination + ". "
                            + e.toString());
                    addFailureEmails(destination);
                    break;
                }
            }
        }
    }

    private static void sendRecommendEmailSesApi(String email, String body) throws Exception {
        String configSubject = AutoReloadProperty.getProperty("emailSubject");

        Content subject;
        List<String> destinations = new ArrayList<>(); //replace with your TO email addresses
        boolean isTest = Boolean.parseBoolean(AutoReloadProperty.getProperty("isTest"));
        if(!isTest) {
            destinations.add(email);
            subject = new Content().withData(configSubject);
        } else {
            String[] ccList = AutoReloadProperty.getProperty("emailReceivers").split(";");
            for(int i = 0; i < ccList.length; i++){
                destinations.add(ccList[i]);
            }
            subject = new Content().withData(configSubject + " (" + getDateTime() + ") " + email);
        }

        // Create the body of the message.
        Content textBody = new Content().withData(body);
        Body sendingBody = new Body().withHtml(textBody);

        // Create a message with the specified subject and body.
        com.amazonaws.services.simpleemail.model.Message message =
                new com.amazonaws.services.simpleemail.model.Message()
                        .withSubject(subject)
                        .withBody(sendingBody);
        for (String destination : destinations) {
            // Construct an object to contain the recipient address.
            Destination dest = new Destination().withToAddresses(destination);
            SendEmailRequest request  = new SendEmailRequest()
                    .withSource(AutoReloadProperty.getProperty("emailSender"))
                    .withDestination(dest)
                    .withMessage(message);
            int currentTry = 0;
            int maxRetries = 10;
            while(maxRetries-- > 0) {
                try {
                    currentTry++;
                    LogUtil.getInstance().logInfo(EmailSender.class, "Sending for " + destinations.toString());
                    //call Amazon SES to send the message
                    SendEmailResult result = client.sendEmail(request);
                    LogUtil.getInstance().logInfo(EmailSender.class, "Sent for " + destinations.toString()
                                                                + ". Sent status: " + result.getMessageId());
                    break;
                } catch (AmazonServiceException e) {
                    //retries only throttling errors
                    if ("Throttling".equals(e.getErrorCode()) && "Maximum sending rate exceeded.".equals(e.getMessage())) {
                        long backoffDuration = getSleepDuration(currentTry, 10, 5000);
                        LogUtil.getInstance().logInfo(EmailSender.class, "Maximum send rate exceeded when sending" +
                                " email to " + destination + ". " +
                                (maxRetries > 0 ? "Will retry after backoff." : "Will not retry after backoff."));
                        try {
                            Thread.sleep(backoffDuration);
                        } catch (InterruptedException e1) {
                            addFailureEmails(destination);
                            return;
                        }
                    } else {
                        LogUtil.getInstance().logInfo(EmailSender.class, "Unable to send email to: " + destination + ". "
                                + e.toString());
                        addFailureEmails(destination);
                        break;
                    }
                } catch(Exception e) {
                    LogUtil.getInstance().logInfo(EmailSender.class, "Unable to send email to: " + destination + ". "
                            + e.toString());
                    addFailureEmails(destination);
                    break;
                }
            }
            if(maxRetries == 0){
                LogUtil.getInstance().logInfo(EmailSender.class, "Sending email failure. Reach number of retries. " +
                        "Email: " + destination);
                addFailureEmails(destination);
            }
        }
    }

    public static long getSleepDuration(int currentTry, long minSleepMillis, long maxSleepMillis) {
        //Exponential backoff algorithm
        //currentTry = Math.max(0, currentTry);
        long currentSleepMillis = (long) (minSleepMillis * Math.pow(2, currentTry));
        return Math.min(currentSleepMillis, maxSleepMillis);
    }
}
