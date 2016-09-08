package com.ascend.data.model;

import lombok.Data;

import java.util.List;

/**
 * Created by thinhly on 3/17/16.
 */
import  java.util.*;

@Data
public class User {
    String email;
    String hashSsoId;

    /* UTM Tag Info */
    String UtmSource;
    String UtmMedium;
    String UtmCampaign;

    /* List of Recommended products */
    List<Product> products;

    /* Unsubscribe Link*/
    String unsubscribeUrl;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" Email: " + getEmail() + NEW_LINE);
        result.append(" Hash SSOID: " + getHashSsoId() + NEW_LINE);
        result.append(" UTM Source: " + getUtmSource() + NEW_LINE );
        result.append(" UTM Medium: " + getUtmMedium() + NEW_LINE);
        result.append(" UTM Campaign: " + getUtmCampaign() + NEW_LINE);
        result.append(" Product List: " + NEW_LINE);
        result.append("}");

        return result.toString();
    }

    public void printProductList(){
        getProducts().forEach(product -> product.toString());
    }
}
