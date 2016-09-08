package com.ascend.data.util;

/**
 * Created by thinhly on 3/11/16.
 */
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

public class Slug {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w_-]");
    private static final Pattern SEPARATORS = Pattern.compile("[\\s]");

    public static String makeSlug(String input) {
        String noseparators = SEPARATORS.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noseparators, Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("-{2,}","-").replaceAll("^-|-$","");
    }

    public static String iTruemartSlug(String str){
        // Need to replace HTML tag first
        String noHTMLString = str.replaceAll("\\<.*?>","");

        // Convert string by using these regex below
        String replace = "-";
        String t1 = noHTMLString.toLowerCase().replaceAll("&\\#\\d+?;","");
        String t2 = t1.replaceAll("&\\S+?;","");
        String t3 = t2.replaceAll("\\s+", replace);
        String t4 = t3.replaceAll("[^a-z0-9\\-\\._]","");
        String t5 = t4.replaceAll(replace + "+", replace);
        String t6 = t5.replaceAll(replace + "$", replace);
        String t7 = t6.replaceAll("^" + replace, replace);
        String t8 = t7.replaceAll("\\.+$", "");

        String t9;
        if(t8.startsWith("-", 0)){
            t9 = t8.replaceFirst("-", "");
            return t9.trim().replace("\\", "");
        }
        return t8.trim().replace("\\", "");
    }
}

