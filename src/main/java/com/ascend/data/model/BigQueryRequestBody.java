package com.ascend.data.model;

import lombok.Data;

/**
 * Created by thinhly on 3/17/16.
 */
@Data
public class BigQueryRequestBody {
    /*
    {
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
     */

    @Data
    private class DefaultDataset {
        String datasetId; //[Required]
        String projectId;
    }

    String kind = "bigquery#queryRequest";
    String query; //[Required]
    int maxResult;

    DefaultDataset defaultDataset;
    int timeoutMs = 20000;
    boolean dryRun = false;
    boolean preserveNulls; //[Deprecated]
    boolean useQueryCache = true;

    public void setDefaultDataset(String project, String datasetId){
        defaultDataset = new DefaultDataset();
        defaultDataset.setDatasetId(datasetId);
        defaultDataset.setProjectId(project);
    }
}
