package test.util;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.ascend.data.model.Product;
import com.ascend.data.util.ItmProductGetter;
import com.ascend.data.util.LogUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;


public class TestItmProductGeter {

    @Test
    public void testGetProductDetailFail() throws Exception{
        Client client = mock(Client.class);
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        ClientResponse clientResponse = mock(ClientResponse.class);

        when(client.resource(Mockito.anyString())).thenReturn(webResource);
        when(webResource.accept(Mockito.anyString())).thenReturn(builder);
        when(builder.get(ClientResponse.class)).thenReturn(clientResponse);
        when(clientResponse.getStatus()).thenReturn(400);

        Field field = ItmProductGetter.class.getDeclaredField("client");
        field.setAccessible(true);
        field.set(null, client);

        LogUtil logger = mock(LogUtil.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(logger).logError(Mockito.any(), Mockito.anyString());

        Field log = ItmProductGetter.class.getDeclaredField("log");
        log.setAccessible(true);
        log.set(null, logger);

        ItmProductGetter itmProductGetter = new ItmProductGetter();

        try{
            Product p = itmProductGetter.getProductDetail("A");
        }catch (RuntimeException ex){
            assertNotNull(ex);
        }
    }

    @Test
    public void TestGetProductDetailNull() throws Exception{
        Client client = mock(Client.class);
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        ClientResponse clientResponse = mock(ClientResponse.class);

        when(client.resource(Mockito.anyString())).thenReturn(webResource);
        when(webResource.accept(Mockito.anyString())).thenReturn(builder);
        when(builder.get(ClientResponse.class)).thenReturn(clientResponse);
        when(clientResponse.getStatus()).thenReturn(200);
        when(clientResponse.getEntity(String.class)).thenReturn("{data:null}");

        Field field = ItmProductGetter.class.getDeclaredField("client");
        field.setAccessible(true);
        field.set(null, client);

        LogUtil logger = mock(LogUtil.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(logger).logInfo(Mockito.any(), Mockito.anyString());

        Field log = ItmProductGetter.class.getDeclaredField("log");
        log.setAccessible(true);
        log.set(null, logger);

        ItmProductGetter itmProductGetter = new ItmProductGetter();
        Product p = itmProductGetter.getProductDetail("A");
        assertEquals(null, p);
    }

    @Test
    public void TestGetProductDetailNull2() throws Exception{
        Client client = mock(Client.class);
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        ClientResponse clientResponse = mock(ClientResponse.class);

        when(client.resource(Mockito.anyString())).thenReturn(webResource);
        when(webResource.accept(Mockito.anyString())).thenReturn(builder);
        when(builder.get(ClientResponse.class)).thenReturn(clientResponse);
        when(clientResponse.getStatus()).thenReturn(200);
        when(clientResponse.getEntity(String.class)).thenReturn("{data:null,code:400}");

        Field field = ItmProductGetter.class.getDeclaredField("client");
        field.setAccessible(true);
        field.set(null, client);

        LogUtil logger = mock(LogUtil.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(logger).logInfo(Mockito.any(), Mockito.anyString());

        Field log = ItmProductGetter.class.getDeclaredField("log");
        log.setAccessible(true);
        log.set(null, logger);

        ItmProductGetter itmProductGetter = new ItmProductGetter();
        Product p = itmProductGetter.getProductDetail("A");
        assertEquals(null, p);
    }

    @Test
    public void TestGetProductDetailNull3() throws Exception{
        Client client = mock(Client.class);
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        ClientResponse clientResponse = mock(ClientResponse.class);

        when(client.resource(Mockito.anyString())).thenReturn(webResource);
        when(webResource.accept(Mockito.anyString())).thenReturn(builder);
        when(builder.get(ClientResponse.class)).thenReturn(clientResponse);
        when(clientResponse.getStatus()).thenReturn(200);
        when(clientResponse.getEntity(String.class)).thenReturn("{code:400}");

        Field field = ItmProductGetter.class.getDeclaredField("client");
        field.setAccessible(true);
        field.set(null, client);

        LogUtil logger = mock(LogUtil.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(logger).logInfo(Mockito.any(), Mockito.anyString());

        Field log = ItmProductGetter.class.getDeclaredField("log");
        log.setAccessible(true);
        log.set(null, logger);

        ItmProductGetter itmProductGetter = new ItmProductGetter();
        Product p = itmProductGetter.getProductDetail("A");
        assertEquals(null, p);
    }

    @Test
    public void TestGetProductDetailSuccess() throws Exception{
//        String output = "{\"status\":\"success\",\"code\":200,\"message\":\"200 OK\",\"data\":{\"id\":4778,\"pkey\":2528898663473,\"title\":\"STIEBEL\",\"translates\":[],\"type\":{\"th_TH\":\"\\u0e2a\\u0e48\\u0e07\\u0e02\\u0e2d\\u0e07\",\"en_US\":\"Shipping\"},\"image\":\"\\/\\/cdn-policy.itruemart.com\\/pcms\\/uploads\\/14-12-25\\/8242655bf7c146fd1c8f3dba08e6dfd6.png\"},\"style_types\":null,\"is_next_deal\":false,\"response_time\":\"2015-12-22 07:33:10\",\"payment_methods\":[{\"pkey\":157495024639618,\"name\":\"Wallet by TrueMoney\",\"code\":\"TMN_WALLET\",\"channel\":\"online\",\"transaction_fee\":3.75},{\"pkey\":155413837979192,\"name\":\"\\u0e1a\\u0e31\\u0e15\\u0e23\\u0e40\\u0e04\\u0e23\\u0e14\\u0e34\\u0e15\",\"code\":\"CCW\",\"channel\":\"online\",\"transaction_fee\":3.75},{\"pkey\":155613837979771,\"name\":\"Cash on delivery\",\"code\":\"COD\",\"channel\":\"offline\",\"transaction_fee\":15}],\"bank_installments\":[]}";
        String output = "{\"status\":\"success\",\"code\":200,\"message\":\"200 OK\",\"data\":{\"id\":4778,\"pkey\":2528898663473,\"title\":\"STIEBEL\",\"translates\":[],\"type\":{\"th_TH\":\"\\u0e2a\\u0e48\\u0e07\\u0e02\\u0e2d\\u0e07\",\"en_US\":\"Shipping\"},\"image\":\"\\/\\/cdn-policy.itruemart.com\\/pcms\\/uploads\\/14-12-25\\/8242655bf7c146fd1c8f3dba08e6dfd6.png\",\"image_cover\": {\n" +
                "\"normal\": \"//cdn-p1.itruemart.com/pcms/uploads/15-01-19/eaf6fe193bcb3e9b4e431ab4cb03c3a4.jpg\",\n" +
                "\"thumbnails\": {\n" +
                "\"small\": \"//cdn-p1.itruemart.com/pcms/uploads/15-01-19/eaf6fe193bcb3e9b4e431ab4cb03c3a4_s.jpg\",\n" +
                "\"medium\": \"//cdn-p1.itruemart.com/pcms/uploads/15-01-19/eaf6fe193bcb3e9b4e431ab4cb03c3a4_m.jpg\",\n" +
                "\"square\": \"//cdn-p1.itruemart.com/pcms/uploads/15-01-19/eaf6fe193bcb3e9b4e431ab4cb03c3a4_square.jpg\",\n" +
                "\"large\": \"//cdn-p1.itruemart.com/pcms/uploads/15-01-19/eaf6fe193bcb3e9b4e431ab4cb03c3a4_l.jpg\",\n" +
                "\"zoom\": \"//cdn-p1.itruemart.com/pcms/uploads/15-01-19/eaf6fe193bcb3e9b4e431ab4cb03c3a4_xl.jpg\"\n" +
                "}},\"installment\": {\n" +
                "\"allow\": false\n" +
                "},\"net_price_range\": {\n" +
                "\"max\": 490,\n" +
                "\"min\": 490\n" +
                "},\"special_price_range\": {\n" +
                "\"max\": 0,\n" +
                "\"min\": 0\n" +
                "},\n" +
                "\"percent_discount\": {\n" +
                "\"max\": 0,\n" +
                "\"min\": 0\n" +
                "}},\"style_types\":null,\"is_next_deal\":false,\"response_time\":\"2015-12-22 07:33:10\",\"payment_methods\":[{\"pkey\":157495024639618,\"name\":\"Wallet by TrueMoney\",\"code\":\"TMN_WALLET\",\"channel\":\"online\",\"transaction_fee\":3.75},{\"pkey\":155413837979192,\"name\":\"\\u0e1a\\u0e31\\u0e15\\u0e23\\u0e40\\u0e04\\u0e23\\u0e14\\u0e34\\u0e15\",\"code\":\"CCW\",\"channel\":\"online\",\"transaction_fee\":3.75},{\"pkey\":155613837979771,\"name\":\"Cash on delivery\",\"code\":\"COD\",\"channel\":\"offline\",\"transaction_fee\":15}],\"bank_installments\":[]}";

        Client client = mock(Client.class);
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        ClientResponse clientResponse = mock(ClientResponse.class);

        when(client.resource(Mockito.anyString())).thenReturn(webResource);
        when(webResource.accept(Mockito.anyString())).thenReturn(builder);
        when(builder.get(ClientResponse.class)).thenReturn(clientResponse);
        when(clientResponse.getStatus()).thenReturn(200);
        when(clientResponse.getEntity(String.class)).thenReturn(output);

        Field field = ItmProductGetter.class.getDeclaredField("client");
        field.setAccessible(true);
        field.set(null, client);

        LogUtil logger = mock(LogUtil.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(logger).logInfo(Mockito.any(), Mockito.anyString());

        Field log = ItmProductGetter.class.getDeclaredField("log");
        log.setAccessible(true);
        log.set(null, logger);

        ItmProductGetter itmProductGetter = new ItmProductGetter();
        Product p = itmProductGetter.getProductDetail("2528898663473");
        assertEquals(4778, p.getId());
        assertEquals("2528898663473", p.getPkey());
        assertEquals("STIEBEL", p.getTitle());
    }

    @Test
    public void TestGetConnection() throws Exception{
        Field field = ItmProductGetter.class.getDeclaredField("client");
        field.setAccessible(true);
        field.set(null, null);

        ItmProductGetter itmProductGetter = new ItmProductGetter();
        Client c = itmProductGetter.getClient();
        assertNotNull(c);
    }
}

