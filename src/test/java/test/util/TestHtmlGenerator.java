package test.util;

import com.ascend.data.HtmlGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinhly on 4/19/16.
 */
public class TestHtmlGenerator {

    @org.junit.Test
    public void testSendingEmail() throws Exception{
        List<String> listProduct = new ArrayList<>();
        HtmlGenerator.SendingEmail se = new HtmlGenerator.SendingEmail("test@company.com", listProduct);
        se.run();
    }


}
