package com.ascend.data.util;

import com.ascend.data.model.BigQueryRequestBody;
//import com.ascendcorp.dd.connector.BigQueryTableReader;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.BigqueryScopes;
import com.google.api.services.bigquery.model.Job;
import com.google.api.services.bigquery.model.JobConfiguration;
import com.google.api.services.bigquery.model.JobConfigurationQuery;
import com.google.api.services.bigquery.model.TableReference;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.util.Collection;

/**
 * Created by thinhly on 3/17/16.
 */
public class BigQueryExecutor {

    private static LogUtil log = LogUtil.getInstance();
    private static final String APPLICATION_NAME = "Email Recommendation Generator";
    private static final String PROJECT_NAME = "itruemart-973";
    private static final String APPLICATION_JSON = "application/json";

    /**
     * Execute the query statement and append the data to table on Big Query
     */
//    public static void executeBigQuery(String dataset, String tableId, String sqlQuery){
//        int queryPollInterval = 10 * 1000;
//        int queryTimeout = 1000 * 1000;
//
//        TableReference tableRef = new TableReference()
//                .setProjectId(PROJECT_NAME)
//                .setDatasetId(dataset)
//                .setTableId(tableId);
//        JobConfigurationQuery jobConfigQuery = new JobConfigurationQuery()
//                .setQuery(sqlQuery)
//                .setAllowLargeResults(true)
//                .setDestinationTable(tableRef)
//                .setWriteDisposition("WRITE_APPEND");
//
//        Job job = new Job().setConfiguration(new JobConfiguration().setQuery(jobConfigQuery));
//
//        try {
//            Bigquery bigquery = createAuthorizedClient();
//
//            job = bigquery.jobs().insert(PROJECT_NAME, job).execute();
//            String jobId = job.getJobReference().getJobId();
//
//            int retries = queryTimeout / queryPollInterval;
//            int retry = 1;
//            while (retry <= retries) {
//                String state = bigquery.jobs().get(PROJECT_NAME, jobId).execute().getStatus().getState();
//                if (state.equals("DONE")) {
//                    log.logInfo(BigQueryExecutor.class, "Table: " + tableId + ". State = " + state + ". Job finished: " + jobId);
//                    return;
//                }
//                Thread.sleep(queryPollInterval);
//                log.logInfo(BigQueryExecutor.class, "Table: " + tableId + ". State = " + state + ", poll = " + retry + " / " + retries);
//                retry += 1;
//            }
//            if (retry == retries){
//                throw new Exception("BigQuery executive timeout.");
//            }
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * In the request body, supply data with the following structure:
     *  {
             "kind": "bigquery#queryRequest",
             "query": string,
             "maxResults": unsigned integer,
             "defaultDataset": {
                 "datasetId": string,
                 "projectId": string
             },
             "timeoutMs": unsigned integer,
             "dryRun": boolean,
             "preserveNulls": boolean,
             "useQueryCache": boolean
         }

     * Result: JsonElement object for data result
     */
    public static JsonElement sendJsonRequestBody(String dataset, String sqlQuery) throws IOException {
        Gson gson = new Gson();

        BigQueryRequestBody req = new BigQueryRequestBody();
        req.setQuery(sqlQuery);
        req.setDefaultDataset(PROJECT_NAME, dataset);

        JsonElement res = executePostRequest(gson.toJson(req));
        System.out.println(res);
        return res;
    }

    private static JsonElement executePostRequest(String request) throws IOException {
        String strUrl = "https://www.googleapis.com/bigquery/v2/projects/" + PROJECT_NAME + "/queries";

        Bigquery bigquery = createAuthorizedClient();
        GenericUrl url = new GenericUrl(strUrl);
        HttpRequestFactory requestFactory = bigquery.getRequestFactory();
        HttpRequest httpRequest = requestFactory.buildPostRequest(url,
                    ByteArrayContent.fromString(APPLICATION_JSON, request));
        httpRequest.getHeaders().setContentType(APPLICATION_JSON);
        httpRequest.setConnectTimeout(3 * 60000);  // 3 minutes connect timeout
        httpRequest.setReadTimeout(3 * 60000);  // 3 minutes read timeout

        HttpResponse response = httpRequest.execute();
        if (response.getStatusCode() != 200) {
            String msg = "Failed : HTTP error code : " + response.getStatusCode();
            log.logError(BigQueryExecutor.class, msg);
            throw new RuntimeException(msg);
        }

        System.out.println(response.getContent());
        String output = response.parseAsString();
        log.logInfo(BigQueryExecutor.class, "Running query on Big Query is OK. " +
                                            "Response status: " + response.getStatusCode());
        response.disconnect();

        JsonReader jsonReader = new JsonReader(new StringReader(output));
        jsonReader.setLenient(true);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonReader);

        return element;
    }

    public static Bigquery createAuthorizedClient() throws IOException {
        // Create the credential
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        GoogleCredential credential =  GoogleCredential.getApplicationDefault(transport, jsonFactory);

        // Depending on the environment that provides the default credentials (e.g. Compute Engine, App
        // Engine), the credentials may require us to specify the scopes we need explicitly.
        // Check for this case, and inject the Bigquery scope if required.
        if (credential.createScopedRequired()) {
            Collection<String> bigqueryScopes = BigqueryScopes.all();
            credential = credential.createScoped(bigqueryScopes);
        }

        return new Bigquery.Builder(transport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }
}
