package project.pdfToElastic.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import project.pdfToElastic.model.DocumentObject;

import java.io.IOException;
import java.util.List;

public class ElasticUtils {
    RestHighLevelClient client;
    public ElasticUtils(){
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        client = new RestHighLevelClient(builder);
    }

    public void pushToElastic(List<DocumentObject> documentObjects) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        BulkRequest bulkRequest = new BulkRequest();

        for (DocumentObject documentObject : documentObjects) {
            String jsonString = objectMapper.writeValueAsString(documentObject);
            IndexRequest indexRequest = new IndexRequest("test_index2")
                    .id(String.valueOf(documentObject.getDocumentID()))
                    .source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        if (!bulkResponse.hasFailures()) {
            System.out.println("All documents inserted successfully");
        } else {
            System.err.println("Failures occurred while inserting documents: " + bulkResponse.buildFailureMessage());
        }

        client.close();
    }

}
