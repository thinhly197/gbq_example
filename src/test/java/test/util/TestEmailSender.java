package test.util;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.ascend.data.util.AutoReloadProperty;
import com.ascend.data.util.EmailSender;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.nlab.smtp.pool.SmtpConnectionPool;
import org.nlab.smtp.transport.connection.ClosableSmtpConnection;
import org.nlab.smtp.transport.factory.SmtpConnectionFactories;
import org.nlab.smtp.transport.factory.SmtpConnectionFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import test.UnitTest;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.lang.reflect.*;
import java.util.*;

/**
 * Created by thinhly on 3/24/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AutoReloadProperty.class, Session.class, System.class, Properties.class})
@PowerMockIgnore("javax.management.*")
public class TestEmailSender {

    @Test
    public void TestEmailSender1() throws Exception{
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("PORT"), 25);
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("HOST"), "Host");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_PASSWORD"), "password");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_USERNAME"), "username");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("isSMTP"), true);
        Address address[] = new Address[]{new InternetAddress("abc@abc.com")};

        Properties props = mock(Properties.class);

        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getProperties()).thenReturn(props);

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", 25);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");

        Session b = PowerMockito.mock(Session.class);
        PowerMockito.mockStatic(Session.class);
        Method method = Whitebox.getMethod(Session.class, "getDefaultInstance", Properties.class, Authenticator.class);
        PowerMockito.doReturn(b).when(Session.class, method)
                .withArguments(Matchers.any(Properties.class), Matchers.any(PasswordAuthentication.class));

        PowerMockito.mockStatic(AutoReloadProperty.class);
        PowerMockito.when(AutoReloadProperty.getProperty("isSMTP")).thenReturn("true");

        EmailSender em = new EmailSender();
        EmailSender.setEmailConfiguration("host", "username", "password", 25);
        EmailSender.close();
    }

    @Test
    public void TestEmailSender2() throws Exception{
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("PORT"), 25);
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("HOST"), "Host");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_PASSWORD"), "password");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_USERNAME"), "username");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("isSMTP"), true);

        Address address[] = new Address[]{new InternetAddress("abc@abc.com")};

        Properties props = mock(Properties.class);

        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getProperties()).thenReturn(props);

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", 25);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");

        Session b = PowerMockito.mock(Session.class);
        PowerMockito.mockStatic(Session.class);
        Method method = Whitebox.getMethod(Session.class, "getDefaultInstance", Properties.class, Authenticator.class);
        PowerMockito.doReturn(b).when(Session.class, method)
                .withArguments(Matchers.any(Properties.class), Matchers.any(PasswordAuthentication.class));

        PowerMockito.mockStatic(AutoReloadProperty.class);
        PowerMockito.when(AutoReloadProperty.getProperty("emailSender")).thenReturn("sender@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailDeveloper")).thenReturn("receiver@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailMaketer")).thenReturn("maketer@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailSubject")).thenReturn("Unit Testing");

        SmtpConnectionPool p = mock(SmtpConnectionPool.class);
        ClosableSmtpConnection connection = mock(ClosableSmtpConnection.class);
        when(p.borrowObject()).thenReturn(connection);
        when(connection.getSession()).thenReturn(b);
        EmailSender.setSmtpConnectionPool(p);
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "unsubscribe");

        PowerMockito.when(AutoReloadProperty.getProperty("emailReceivers"))
                .thenReturn("maketer@gmail.com;receiver@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("isTest")).thenReturn("true");
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "unsubscribe");
    }

    @Test
    public void TestEmailSender3() throws Exception{
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("PORT"), 25);
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("HOST"), null);
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_PASSWORD"), "password");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_USERNAME"), "username");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("isSMTP"), true);

        Address address[] = new Address[]{new InternetAddress("abc@abc.com")};

        Properties props = mock(Properties.class);

        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getProperties()).thenReturn(props);

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", 25);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");

        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "unsubscribe");
    }

    @Test
    public void TestEmailSender4() throws Exception{
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("PORT"), 25);
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("HOST"), "host");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_PASSWORD"), "password");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_USERNAME"), "username");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("isSMTP"), false);

        PowerMockito.mockStatic(AutoReloadProperty.class);
        PowerMockito.when(AutoReloadProperty.getProperty("emailSender")).thenReturn("sender@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailDeveloper")).thenReturn("receiver@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailMaketer")).thenReturn("maketer@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailSubject")).thenReturn("Unit testing EmailSender");

//        AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(
//                new BasicAWSCredentials("AKIAIO3J4E5QHEPFT5XQ", "pzJnxm8DWljkI9YkfoHaDGej3kruC7hcin1hqz2U"));
//        SendEmailResult rs = new SendEmailResult();
//        when(client.sendEmail(Matchers.anyObject())).thenReturn(Matchers.anyObject());

        AmazonSimpleEmailServiceClient client = mock(AmazonSimpleEmailServiceClient.class);
        EmailSender.setAwsSesClient(client);
        SendEmailResult rs = new SendEmailResult();
        when(client.sendEmail(Matchers.any())).thenReturn(rs);

        List<String> list = mock(List.class);
        List<String> destinations = new ArrayList();
        destinations.add("maketer@gmail.com");
        destinations.add("receiver@gmail.com");
        when(list.iterator()).thenReturn(destinations.iterator());
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");

        PowerMockito.when(AutoReloadProperty.getProperty("emailReceivers"))
                .thenReturn("maketer@gmail.com;receiver@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("isTest")).thenReturn("true");
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");
    }

    @Test
    public void TestEmailSender5() throws Exception{
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("PORT"), 25);
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("HOST"), "host");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_PASSWORD"), "password");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_USERNAME"), "username");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("isSMTP"), false);

        PowerMockito.mockStatic(AutoReloadProperty.class);
        PowerMockito.when(AutoReloadProperty.getProperty("emailSender")).thenReturn("sender@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailDeveloper")).thenReturn("receiver@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailMaketer")).thenReturn("maketer@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailSubject")).thenReturn("Unit testing EmailSender");

        AmazonSimpleEmailServiceClient client = mock(AmazonSimpleEmailServiceClient.class);
        EmailSender.setAwsSesClient(client);

        List<String> list = mock(List.class);
        List<String> destinations = new ArrayList();
        destinations.add("maketer@gmail.com");
        destinations.add("receiver@gmail.com");
        when(list.iterator()).thenReturn(destinations.iterator());
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");

        PowerMockito.when(AutoReloadProperty.getProperty("emailReceivers"))
                .thenReturn("maketer@gmail.com;receiver@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("isTest")).thenReturn("true");
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");
    }

    @Test
    public void TestEmailSender6a() throws Exception{
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("PORT"), 25);
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("HOST"), "host");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_PASSWORD"), "password");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_USERNAME"), "username");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("isSMTP"), false);

        PowerMockito.mockStatic(AutoReloadProperty.class);
        PowerMockito.when(AutoReloadProperty.getProperty("emailSender")).thenReturn("sender@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailSubject")).thenReturn("Unit testing EmailSender");
        PowerMockito.when(AutoReloadProperty.getProperty("isTest")).thenReturn("false");

        AmazonSimpleEmailServiceClient client = mock(AmazonSimpleEmailServiceClient.class);
        EmailSender.setAwsSesClient(client);
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");

        AmazonServiceException ex1 = mock(AmazonServiceException.class);
        when(client.sendEmail(Matchers.any())).thenThrow(ex1);
        when(ex1.getErrorCode()).thenReturn("Throttling");
        when(ex1.getMessage()).thenReturn("Maximum sending rate exceeded.");
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");
    }

    @Test
    public void TestEmailSender6b() throws Exception{
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("PORT"), 25);
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("HOST"), "host");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_PASSWORD"), "password");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_USERNAME"), "username");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("isSMTP"), false);

        PowerMockito.mockStatic(AutoReloadProperty.class);
        PowerMockito.when(AutoReloadProperty.getProperty("emailSender")).thenReturn("sender@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailSubject")).thenReturn("Unit testing EmailSender");
        PowerMockito.when(AutoReloadProperty.getProperty("emailReceivers")).thenReturn("receiver@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("isTest")).thenReturn("true");

        AmazonSimpleEmailServiceClient client = mock(AmazonSimpleEmailServiceClient.class);
        EmailSender.setAwsSesClient(client);
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");

        AmazonServiceException ex1 = mock(AmazonServiceException.class);
        when(client.sendEmail(Matchers.any())).thenThrow(ex1);
        when(ex1.getErrorCode()).thenReturn("Throttling");
        when(ex1.getMessage()).thenReturn("Maximum sending rate exceeded.");
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");
    }

    @Test
    public void TestEmailSender7() throws Exception{
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("PORT"), 25);
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("HOST"), "host");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_PASSWORD"), "password");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_USERNAME"), "username");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("isSMTP"), false);

        PowerMockito.mockStatic(AutoReloadProperty.class);
        PowerMockito.when(AutoReloadProperty.getProperty("emailSender")).thenReturn("sender@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailDeveloper")).thenReturn("receiver@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailMaketer")).thenReturn("maketer@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailSubject")).thenReturn("Unit testing EmailSender");

        AmazonSimpleEmailServiceClient client = mock(AmazonSimpleEmailServiceClient.class);
        EmailSender.setAwsSesClient(client);

        List<String> list = mock(List.class);
        List<String> destinations = new ArrayList();
        destinations.add("maketer@gmail.com");
        when(list.iterator()).thenReturn(destinations.iterator());
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");

        PowerMockito.when(AutoReloadProperty.getProperty("emailReceivers"))
                .thenReturn("maketer@gmail.com;receiver@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("isTest")).thenReturn("true");

        AmazonServiceException ex1 = mock(AmazonServiceException.class);
        when(client.sendEmail(Matchers.any())).thenThrow(ex1);
        when(ex1.getErrorCode()).thenReturn("Throttling");
        when(ex1.getMessage()).thenReturn("Maximum sending rate exceeded.");
        PowerMockito.spy(Thread.class);
        PowerMockito.doThrow(new InterruptedException()).when(Thread.class);
        Thread.sleep(Matchers.anyLong());

        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");
    }

    @Test
    public void TestEmailSender8() throws Exception{
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("PORT"), 25);
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("HOST"), "host");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_PASSWORD"), "password");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("SMTP_USERNAME"), "username");
        UnitTest.setFinalStatic(EmailSender.class.getDeclaredField("isSMTP"), false);

        PowerMockito.mockStatic(AutoReloadProperty.class);
        PowerMockito.when(AutoReloadProperty.getProperty("emailSender")).thenReturn("sender@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailDeveloper")).thenReturn("receiver@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailMaketer")).thenReturn("maketer@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("emailSubject")).thenReturn("Unit testing EmailSender");

        AmazonSimpleEmailServiceClient client = mock(AmazonSimpleEmailServiceClient.class);
        EmailSender.setAwsSesClient(client);

        List<String> list = mock(List.class);
        List<String> destinations = new ArrayList();
        destinations.add("maketer@gmail.com");
        when(list.iterator()).thenReturn(destinations.iterator());
        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");

        PowerMockito.when(AutoReloadProperty.getProperty("emailReceivers"))
                .thenReturn("maketer@gmail.com;receiver@gmail.com");
        PowerMockito.when(AutoReloadProperty.getProperty("isTest")).thenReturn("true");

        AmazonServiceException ex1 = mock(AmazonServiceException.class);
        when(client.sendEmail(Matchers.any())).thenThrow(ex1);
        when(ex1.getErrorCode()).thenReturn("Throttling something");
        when(ex1.getMessage()).thenReturn("Maximum sending rate exceeded.");

        EmailSender.sendRecommendEmail("emailaddress@company.com", "Content Test", "");
    }
}
