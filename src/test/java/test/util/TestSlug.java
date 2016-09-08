package test.util;
import static org.junit.Assert.*;

import com.ascend.data.util.Slug;
import org.junit.Test;

public class TestSlug {

    @Test
    public void testMakeSlug(){
        Slug slug = new Slug();
        String result = slug.makeSlug("Test-*12");
        assertEquals("test-12", result);
    }

    @Test
    public void testItruemartSlug(){
        Slug slug = new Slug();
        String rs = slug.iTruemartSlug("-Test_*12");
        System.out.println(rs);
        assertEquals("test_12", rs);
    }
}
