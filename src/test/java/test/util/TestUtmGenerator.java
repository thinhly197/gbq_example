package test.util;

import com.ascend.data.model.User;
import com.ascend.data.util.AutoReloadProperty;
import com.ascend.data.util.BigQueryExecutor;
import com.ascend.data.util.SqlBuilder;
import com.ascend.data.util.UtmGenerator;
import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

import java.io.IOException;

/**
 * Created by thinhly on 3/24/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AutoReloadProperty.class, SqlBuilder.class, BigQueryExecutor.class})
public class TestUtmGenerator {

    @Test
    public void testSetUtmForUser() throws Exception {
        PowerMockito.mockStatic(AutoReloadProperty.class);
        PowerMockito.when(AutoReloadProperty.class, "getProperty", Matchers.anyString()).thenReturn("123");

        PowerMockito.mockStatic(SqlBuilder.class);
        PowerMockito.when(SqlBuilder.class, "getHashIdFromEmailSql").thenReturn("Something here");

        String jsonValue = "{\"kind\":\"bigquery#queryResponse\",\"schema\":{\"fields\":[{\"name\":\"" +
                "hash_sso_id\",\"type\":\"STRING\",\"mode\":\"NULLABLE\"}]},\"jobReference\":{\"projectId\":\"" +
                "itruemart-973\",\"jobId\":\"job_2zmI_VOf-wsrealXTfsMnHJWjsM\"},\"totalRows\":\"1\",\"rows\"" +
                ":[{\"f\":[{\"v\":\"5f80990b1cb344e12f104b6c4bd83a58\"}]}],\"totalBytesProcessed\":\"0\",\"" +
                "jobComplete\":true,\"cacheHit\":true}\n";
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(jsonValue, JsonElement.class);

        PowerMockito.mockStatic(BigQueryExecutor.class);
        PowerMockito.when(BigQueryExecutor.class, "sendJsonRequestBody", Matchers.anyString(), Matchers.anyString())
                .thenReturn(element);

        User user = new User();
        user.setEmail("lyhungthinh@gmail.com");
        UtmGenerator.setUtmForUser(user);
    }

    @Test
    public void testGetHashSsoIdFromEmailException() throws Exception {
        String email  = "lyhungthinh@gmail.com";
        IOException ex = mock(IOException.class);
        PowerMockito.mockStatic(SqlBuilder.class);
        PowerMockito.when(SqlBuilder.class, "getHashIdFromEmailSql").thenThrow(ex);

        UtmGenerator utm = new UtmGenerator();
        utm.getHashSsoIdFromEmail(email);
    }

    @Test
    public void testSetUtmForUserEmpty() throws Exception {
        PowerMockito.mockStatic(AutoReloadProperty.class);
        PowerMockito.when(AutoReloadProperty.class, "getProperty", Matchers.anyString()).thenReturn("123");

        PowerMockito.mockStatic(SqlBuilder.class);
        PowerMockito.when(SqlBuilder.class, "getHashIdFromEmailSql").thenReturn("Something here");

        String jsonValue = "{\"kind\":\"bigquery#queryResponse\",\"schema\":{\"fields\":[{\"name\":\"" +
                "hash_sso_id\",\"type\":\"STRING\",\"mode\":\"NULLABLE\"}]},\"jobReference\":{\"projectId\":\"" +
                "itruemart-973\",\"jobId\":\"job_2zmI_VOf-wsrealXTfsMnHJWjsM\"},\"totalRows\":\"1\",\"rows-test\"" +
                ":[{\"f\":[{\"v\":\"5f80990b1cb344e12f104b6c4bd83a58\"}]}],\"totalBytesProcessed\":\"0\",\"" +
                "jobComplete\":true,\"cacheHit\":true}\n";
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(jsonValue, JsonElement.class);

        PowerMockito.mockStatic(BigQueryExecutor.class);
        PowerMockito.when(BigQueryExecutor.class, "sendJsonRequestBody", Matchers.anyString(), Matchers.anyString())
                .thenReturn(element);

        User user = new User();
        user.setEmail("lyhungthinh@gmail.com");
        UtmGenerator.setUtmForUser(user);
    }

    @Test
    public void testSetUtmForUserEmptyEmail() throws Exception {
        PowerMockito.mockStatic(AutoReloadProperty.class);
        PowerMockito.when(AutoReloadProperty.class, "getProperty", Matchers.anyString()).thenReturn("123");

        PowerMockito.mockStatic(SqlBuilder.class);
        PowerMockito.when(SqlBuilder.class, "getHashIdFromEmailSql").thenReturn("Something here");

        String jsonValue = "{\"kind\":\"bigquery#queryResponse\",\"schema\":{\"fields\":[{\"name\":\"" +
                "hash_sso_id\",\"type\":\"STRING\",\"mode\":\"NULLABLE\"}]},\"jobReference\":{\"projectId\":\"" +
                "itruemart-973\",\"jobId\":\"job_2zmI_VOf-wsrealXTfsMnHJWjsM\"},\"totalRows\":\"1\",\"rows-test\"" +
                ":[{\"f\":[{\"v\":\"5f80990b1cb344e12f104b6c4bd83a58\"}]}],\"totalBytesProcessed\":\"0\",\"" +
                "jobComplete\":true,\"cacheHit\":true}\n";
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(jsonValue, JsonElement.class);

        PowerMockito.mockStatic(BigQueryExecutor.class);
        PowerMockito.when(BigQueryExecutor.class, "sendJsonRequestBody", Matchers.anyString(), Matchers.anyString())
                .thenReturn(element);

        User user = new User();
        user.setEmail(null);
        UtmGenerator.setUtmForUser(user);
    }

    @Test
    public void testConfigUtmCampaign() throws Exception {
        UtmGenerator.configUtmCampaignName();
    }
}
