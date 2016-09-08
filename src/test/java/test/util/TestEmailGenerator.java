package test.util;

import static org.mockito.Mockito.*;

import com.ascend.data.model.Product;
import com.ascend.data.model.User;
import com.ascend.data.util.EmailGenerator;
import com.ascend.data.util.FreemakerConfiguration;
import com.ascend.data.util.ItmProductGetter;
import com.ascend.data.util.UserProductGetter;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinhly on 3/16/16.
 */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({FreemakerConfiguration.class, ItmProductGetter.class, TestUserProductGetter.class})
//public class TestEmailGenerator {

//    private static final String testEmail = "lyhungthinh@gmail.com";

//    @Test
//    public void testGenerateEmailHtml() throws Exception {
//        //Template template = mock(Template.class);
//
//        PowerMockito.mockStatic(FreemakerConfiguration.class);
//        PowerMockito.when(FreemakerConfiguration.class, "getEmailTemplate").thenReturn(null);
//        EmailGenerator.generateEmailHtml(testEmail);
//
//        Template template = mock(Template.class);
//        PowerMockito.when(FreemakerConfiguration.class, "getEmailTemplate").thenReturn(template);
//
//        PowerMockito.mockStatic(TestUserProductGetter.class);
//        List<String> productList = new ArrayList<>();
//        productList.add("2859504411476");
//        productList.add("2914894080480");
//        PowerMockito.when(TestUserProductGetter.class, "getProductListFromEmail", Matchers.anyString()).thenReturn(productList);
//
//        Mockito.doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                return null;
//            }
//        }).when(template).setAutoFlush(true);
//
//        Mockito.doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                return null;
//            }
//        }).when(template).process(Matchers.anyMap(), Matchers.anyObject());
//
//        Mockito.doNothing().when(template).process(Matchers.anyObject(), Matchers.anyObject());
//        EmailGenerator.generateEmailHtml(testEmail);
//    }

//    @Test
//    public void testGenerateEmailHtmlBiggerThan9() throws Exception {
//        PowerMockito.mockStatic(FreemakerConfiguration.class);
//        Template template2 = mock(Template.class);
//
//        PowerMockito.mockStatic(ItmProductGetter.class);
//        String temp = "2859504411476, 2914894080480, " +
//                "2859504411476, 2914894080480, 2418676334458, 2447351894437," +
//                "2272563055722, 2114417819556, 2698563067930, 2528898663473";
//        String[] temp1 = temp.split(",");
//        List<Product> abc = new ArrayList<>();
//        for(int i= 0; i < temp1.length; i++) {
//            Product a = new Product();
//            a.setPkey(temp1[i].trim());
//            abc.add(a);
//        }
//
//        Whitebox.setInternalState(EmailGenerator.class, "productList", temp);
//        PowerMockito.when(ItmProductGetter.class, "getProductDetail", Matchers.anyString())
//                .thenAnswer(new Answer<Product>() {
//                    int counter = 0;
//                    @Override
//                    public Product answer(InvocationOnMock invocation) throws Throwable {
//                        counter ++;
//                        return abc.get(counter-1);
//                    }
//                });
//
//        PowerMockito.when(FreemakerConfiguration.class, "getEmailTemplate").thenReturn(template2);
//        Mockito.doNothing().when(template2).process(Matchers.anyObject(), Matchers.anyObject());
//        EmailGenerator.generateEmailHtml(testEmail);
//    }
//
//    @Test
//    public void testGenerateEmailHtmlException1() throws Exception {
//        PowerMockito.mockStatic(FreemakerConfiguration.class);
//        Template template = mock(Template.class);
//        PowerMockito.when(FreemakerConfiguration.class, "getEmailTemplate").thenReturn(template);
//        Whitebox.setInternalState(EmailGenerator.class, "productList", "2526095620889, 2137056019944");
//        TemplateException ex = mock(TemplateException.class);
//        Mockito.doNothing().when(ex).printStackTrace();
//        Mockito.doThrow(ex).when(template).process(Matchers.anyObject(), Matchers.anyObject());
//        EmailGenerator.generateEmailHtml(testEmail);
//
//        IOException ex2 = mock(IOException.class);
//        Mockito.doNothing().when(ex2).printStackTrace();
//        Mockito.doThrow(ex2).when(template).process(Matchers.anyObject(), Matchers.anyObject());
//        EmailGenerator.generateEmailHtml(testEmail);
//    }
//}
