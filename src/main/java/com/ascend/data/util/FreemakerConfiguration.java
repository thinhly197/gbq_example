package com.ascend.data.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;

/**
 * Created by thinhly on 3/10/16.
 */
public class FreemakerConfiguration {

    private static LogUtil log = LogUtil.getInstance();
    private static File file;
    private static Configuration config;

    public static void setFile(File f){
        file = f;

        if(!file.exists()) {
            file.mkdirs();
        }
    }

    public static void setConfig(File f, Configuration cfg){
        config = configFreemakerTemplate(f, cfg);
    }

    public static Template getEmailTemplate() {
        if(config == null) {
            return null;
        }

        String emailTemplate = AutoReloadProperty.getProperty("emailTemplateFileName");
        log.logInfo(FreemakerConfiguration.class, "Get email HTML template. File "
                                                + file.getAbsolutePath() + emailTemplate);
        Template temp = null;
        try {
            temp = config.getTemplate(emailTemplate);
        } catch (IOException e) {
            log.logError(FreemakerConfiguration.class, "Can not get the template file");
            e.printStackTrace();
        }
        return temp;
    }

    public static Configuration configFreemakerTemplate(File template, Configuration cfg) {
        // Create your Configuration instance, and specify if up to what FreeMarker
        // version (here 2.3.22) do you want to apply the fixes that are not 100%
        // backward-compatible. See the Configuration JavaDoc for details.

//        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);

        // Specify the source where the template files come from. Here I set a
        // plain directory for it, but non-file-system sources are possible too:
        try {
            cfg.setDirectoryForTemplateLoading(template);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return cfg;
    }
}
