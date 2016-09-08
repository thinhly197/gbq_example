package com.ascend.data.util;

import com.ascendcorp.dd.connector.BigQueryTableReader;
import com.google.api.services.bigquery.Bigquery;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import scala.collection.Iterator;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinhly on 3/18/16.
 */
public class UserProductGetter {
    private static final String EDM_RECOMENDER_DATASET = "dd_email_recommender";

//    public static List<String> getProductListFromEmail(String email){
//        String query = null;
//        JsonElement element = null;
//        List<String> productLists = new ArrayList<>();
//
//        try {
//            query = SqlBuilder.getProductFromEmailSqlAll().replace("#EMAIL", email);
//            element = BigQueryExecutor.sendJsonRequestBody(EDM_RECOMENDER_DATASET, query);
//            if(element.getAsJsonObject().has("rows")) {
//                int totalRows = element.getAsJsonObject().get("totalRows").getAsInt();
//                for(int i = 0; i < totalRows; i++){
//                    JsonObject f = element.getAsJsonObject().get("rows").getAsJsonArray()
//                            .get(i).getAsJsonObject();
//                    String productPKey = f.getAsJsonObject().get("f").getAsJsonArray()
//                            .get(0).getAsJsonObject().get("v").getAsString();
//                    productLists.add(productPKey);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return productLists;
//    }
}
