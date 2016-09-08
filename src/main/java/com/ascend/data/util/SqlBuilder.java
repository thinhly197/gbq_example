package com.ascend.data.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by thinhly on 3/18/16.
 */
public class SqlBuilder {
    public static String getSQL(String fileName) throws IOException {
        String path = System.getProperty("user.home") + "/" + AutoReloadProperty.getProperty("sqlFolder");
        byte[] encoded = Files.readAllBytes(Paths.get(path + fileName));
        return new String(encoded, Charset.defaultCharset());
    }

    public static String getHashIdFromEmailSql() throws IOException {
        String filename = AutoReloadProperty.getProperty("hashFromEmail");
        return getSQL(filename);
    }

    public static String getAllEmailProducts() throws IOException {
        String filename = AutoReloadProperty.getProperty("allEmailProducts");
        String numberOfProduct = AutoReloadProperty.getProperty("numberOfLoadingProductPerEmail");
        return getSQL(filename).replace("#NOP", numberOfProduct);
    }

    public static String getAllProductsNoDuplicate() throws IOException {
        String filename = AutoReloadProperty.getProperty("allProductsNoDup");
        String numberOfProduct = AutoReloadProperty.getProperty("numberOfLoadingProductPerEmail");
        String firstCaching = AutoReloadProperty.getProperty("firstProductCaching");
        return getSQL(filename).replace("#NOP", numberOfProduct).replace("#CACHING", firstCaching);
    }
}
