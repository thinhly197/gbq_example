package com.ascend.data.model;

import lombok.Data;

/**
 * Created by thinhly on 3/9/16.
 */
@Data
public class Product {
    int id;
    String pkey;
    String title;
    String thumbnail; // it is the location field in PCMS database
    String url;
    String installment;
    int salePrice;
    int masterPrice;
    int percentOfDiscount;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + NEW_LINE);
        result.append(" Id: " + getId() + NEW_LINE);
        result.append(" Pkey: " + getPkey() + NEW_LINE);
        result.append(" Title: " + getTitle() + NEW_LINE );
        result.append(" Thumbnail: " + getThumbnail() + NEW_LINE);
        result.append(" URL: " + getUrl() + NEW_LINE);
        result.append(" Installment: " + getInstallment() + NEW_LINE);
        result.append(" Sale Price: " + getSalePrice() + NEW_LINE );
        result.append(" Normal Price: " + getMasterPrice() + NEW_LINE);
        result.append(" Discount Percentage: " + getPercentOfDiscount() + NEW_LINE);
        result.append("}");

        return result.toString();
    }
}
