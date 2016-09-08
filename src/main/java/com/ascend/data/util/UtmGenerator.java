package com.ascend.data.util;

import com.ascend.data.model.User;
//import com.ascendcorp.dd.connector.BigQueryTableReader;
import com.google.api.services.bigquery.Bigquery;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by thinhly on 3/17/16.
 */
public class UtmGenerator {

    private static final String USER_MAPPING_DATASET = "dd_user_mapping";
    private static String UTM_CAMPAIGN_NAME = "";

    public static void configUtmCampaignName(){
        if("".equals(UTM_CAMPAIGN_NAME)){
            DateTime dateTime = new DateTime();
            UTM_CAMPAIGN_NAME = dateTime.toString("YYYYMMddHHmm_")
                    + AutoReloadProperty.getProperty("utmCampaign");
            LogUtil.getInstance().logInfo(UtmGenerator.class, "*** UTM CAMPAIGN name: " + UTM_CAMPAIGN_NAME);
        }
    }

    public static String getUtmCampaignName(){
        return UTM_CAMPAIGN_NAME;
    }

    public static String getHashSsoIdFromEmail(String email){
        String query = null;
        JsonElement element = null;
        String hashSsoId = "";
        try {
            query = SqlBuilder.getHashIdFromEmailSql().replace("#EMAIL", email);
            element = BigQueryExecutor.sendJsonRequestBody(USER_MAPPING_DATASET, query);

            if(element.getAsJsonObject().has("rows")) {
                JsonObject f = element.getAsJsonObject().get("rows").getAsJsonArray()
                        .get(0).getAsJsonObject();
                hashSsoId = f.getAsJsonObject().get("f").getAsJsonArray()
                        .get(0).getAsJsonObject().get("v").getAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashSsoId;
    }

    public static void setUtmForUser(User user){
        user.setUtmSource(AutoReloadProperty.getProperty("utmSource"));
        user.setUtmMedium(AutoReloadProperty.getProperty("utmMedium"));
        user.setUtmCampaign(UTM_CAMPAIGN_NAME);
        if(user.getEmail() != null && !user.getEmail().equals("")){
            user.setHashSsoId(getHashSsoIdFromEmail(user.getEmail()));
            String unsubscribeLink = AutoReloadProperty.getProperty("unsubscribeServer");
            String hashEmail = new Md5Hash(user.getEmail()).toString();
            user.setUnsubscribeUrl(unsubscribeLink + hashEmail);
        }
    }
}
