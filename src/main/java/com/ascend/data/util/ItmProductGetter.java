package com.ascend.data.util;

/**
 * Created by thinhly on 3/11/16.
 */

import com.ascend.data.model.Product;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.StringReader;

public class ItmProductGetter {
    private static LogUtil log = LogUtil.getInstance();
    private static String ITM_URL = "";
    private static String ITM_STOCK_URL = "";
    private static final String APPLICATION_JSON = "application/json";

    private static final String ITM_PRODUCT_URL_PREFIX = "http://www.itruemart.com/products/";
    private static final String ITM_PRODUCT_URL_SUFFIX = ".html";

    private static Client client = null;

    public static Client getClient() {
        if(client == null){
            client = Client.create();
            ITM_URL = AutoReloadProperty.getProperty("itmProductServer");
            ITM_STOCK_URL = AutoReloadProperty.getProperty("itmStockServer");
        }
        return client;
    }

    public static boolean checkOutOfStock(String pkey){
        Client clientLocal = getClient();
        ITM_STOCK_URL = "http://pcms-report.itruemart.com/api/45311375168544/inventories/promotions/";
        WebResource webResource = clientLocal.resource(ITM_STOCK_URL + pkey);
        ClientResponse response = webResource.accept(APPLICATION_JSON).get(ClientResponse.class);

        if (response.getStatus() != 200) {
            String msg = "Failed : HTTP error code : " + response.getStatus();
            log.logError(ItmProductGetter.class, msg);
            throw new RuntimeException(msg);
        }
        String output = response.getEntity(String.class);
        JsonReader jsonReader = new JsonReader(new StringReader(output));
        jsonReader.setLenient(true);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonReader);

        if(element.getAsJsonObject().has("data") && element.getAsJsonObject().has("code")
                && element.getAsJsonObject().get("code").getAsInt() == 200) {
            JsonObject data = (JsonObject) element.getAsJsonObject().get("data");
            JsonObject pkeyData = (JsonObject) data.get(pkey);

            String inStockValue = pkeyData.get("in_stock").getAsString();
            if("Y".equals(inStockValue.toUpperCase())){
                return true;
            }
        }
        return false;
    }

    public static Product getProductDetail(String productPkey) {
        Product p = new Product();
        if(!checkOutOfStock(productPkey)){
            p.setId(0);
            return p;
        }

        Client clientLocal = getClient();
        // Use it if this url need to the authentication
        //client.addFilter(new HTTPBasicAuthFilter(adminUsername, adminAuthToken));
        WebResource webResource = clientLocal.resource(ITM_URL + productPkey);
        ClientResponse response = webResource.accept(APPLICATION_JSON).get(ClientResponse.class);

        if (response.getStatus() != 200) {
            String msg = "Failed : HTTP error code : " + response.getStatus();
            log.logError(ItmProductGetter.class, msg);
            throw new RuntimeException(msg);
        }
        log.logInfo(ItmProductGetter.class, "Get product info of " + productPkey + " OK. " + response.toString());
        String output = response.getEntity(String.class);
        JsonReader jsonReader = new JsonReader(new StringReader(output));
        jsonReader.setLenient(true);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonReader);

        int delaytime = Integer.parseInt(AutoReloadProperty.getProperty("delaytimeForITM"));
        try {
            Thread.sleep(delaytime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(element.getAsJsonObject().has("data") && element.getAsJsonObject().has("code")
                && element.getAsJsonObject().get("code").getAsInt() == 200) {
            JsonObject data = (JsonObject) element.getAsJsonObject().get("data");
            JsonObject imageCover = (JsonObject) data.get("image_cover");
            JsonObject thumbnails = (JsonObject) imageCover.get("thumbnails");
            JsonObject netPrice = (JsonObject) data.get("net_price_range");
            JsonObject salePrice = (JsonObject) data.get("special_price_range");
            JsonObject discount = (JsonObject) data.get("percent_discount");
            JsonObject installment = (JsonObject) data.get("installment");

            p.setId(data.get("id").getAsInt());
            p.setPkey(data.get("pkey").getAsString());
            p.setTitle(data.get("title").getAsString());
            if(Slug.iTruemartSlug(p.getTitle()).length() == 0){
                p.setUrl(ITM_PRODUCT_URL_PREFIX + p.getPkey() + ITM_PRODUCT_URL_SUFFIX);
            } else {
                p.setUrl(ITM_PRODUCT_URL_PREFIX + Slug.iTruemartSlug(p.getTitle())
                        + "-" + p.getPkey() + ITM_PRODUCT_URL_SUFFIX);
            }
            p.setInstallment(installment.get("allow").getAsString());
            p.setThumbnail(thumbnails.get("medium").getAsString().replace("//", "http://"));
            p.setMasterPrice(netPrice.get("min").getAsInt());
            p.setSalePrice(salePrice.get("min").getAsInt());
            p.setPercentOfDiscount((int) discount.get("max").getAsFloat());
            //(p.getMasterPrice() - p.getSalePrice()) / p.getMasterPrice() * 100
        } else {
            p.setId(0);
        }
        return p;
    }
}
