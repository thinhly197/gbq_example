package test.util;

import com.ascend.data.util.AutoReloadProperty;
import com.ascend.data.util.SqlBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by thinhly on 3/24/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AutoReloadProperty.class})
public class TestSqlBuilder {
    @Test
    public void testGetSql() throws Exception {
        SqlBuilder sqlBuilder = new SqlBuilder();
        try {
            PowerMockito.mockStatic(AutoReloadProperty.class);
            PowerMockito.when(AutoReloadProperty.class, "getProperty", "sqlFolder")
                    .thenReturn("edm-html-generator/sql/");
            PowerMockito.when(AutoReloadProperty.class, "getProperty", "hashFromEmail")
                    .thenReturn("get_hashid_from_email.sql");
            PowerMockito.when(AutoReloadProperty.class, "getProperty", "allEmailProducts")
                    .thenReturn("get_product_from_email_list.sql");

            sqlBuilder.getHashIdFromEmailSql();
            sqlBuilder.getAllEmailProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
