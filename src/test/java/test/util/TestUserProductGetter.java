package test.util;

import com.ascend.data.util.AutoReloadProperty;
import com.ascend.data.util.BigQueryExecutor;
import com.ascend.data.util.SqlBuilder;
import com.ascend.data.util.UserProductGetter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.mockito.Mockito.mock;

/**
 * Created by thinhly on 3/24/16.
 */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({AutoReloadProperty.class, SqlBuilder.class, BigQueryExecutor.class})
//public class TestUserProductGetter {
//
//    @Test
//    public void testGetProductListFromEmail() throws Exception {
//        PowerMockito.mockStatic(AutoReloadProperty.class);
//        PowerMockito.when(AutoReloadProperty.class, "getProperty", Matchers.anyString()).thenReturn("123");
//
//        PowerMockito.mockStatic(SqlBuilder.class);
//        PowerMockito.when(SqlBuilder.class, "getProductFromEmailSql").thenReturn("Something here");
//
//        String jsonValue = "{\"kind\":\"bigquery#queryResponse\",\"schema\":{\"fields\":[{\"name\":\"" +
//                "hash_sso_id\",\"type\":\"STRING\",\"mode\":\"NULLABLE\"}]},\"jobReference\":{\"projectId\":\"" +
//                "itruemart-973\",\"jobId\":\"job_2zmI_VOf-wsrealXTfsMnHJWjsM\"},\"totalRows\":\"1\",\"rows\"" +
//                ":[{\"f\":[{\"v\":\"5f80990b1cb344e12f104b6c4bd83a58\"}]}],\"totalBytesProcessed\":\"0\",\"" +
//                "jobComplete\":true,\"cacheHit\":true}\n";
//        Gson gson = new Gson();
//        JsonElement element = gson.fromJson(jsonValue, JsonElement.class);
//
//        PowerMockito.mockStatic(BigQueryExecutor.class);
//        PowerMockito.when(BigQueryExecutor.class, "sendJsonRequestBody", Matchers.anyString(), Matchers.anyString())
//                .thenReturn(element);
//
//        UserProductGetter.getProductListFromEmail("lyhungthinh@gmail.com");
//    }
//
//    @Test
//    public void testGetHashSsoIdFromEmailException() throws Exception {
//        IOException ex = mock(IOException.class);
//        PowerMockito.mockStatic(SqlBuilder.class);
//        PowerMockito.when(SqlBuilder.class, "getProductFromEmailSql").thenThrow(ex);
//        UserProductGetter.getProductListFromEmail("lyhungthinh@gmail.com");
//    }
//}
