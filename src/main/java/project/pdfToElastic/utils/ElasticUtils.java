package project.pdfToElastic.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import project.pdfToElastic.config.Config;
import project.pdfToElastic.model.DocumentObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ElasticUtils {
    RestHighLevelClient client;
    public ElasticUtils(){
        RestClientBuilder builder = RestClient.builder(new HttpHost(Config.ELASTIC_HOST, Config.ELASTIC_PORT, "http"));
        client = new RestHighLevelClient(builder);
    }

    public RestHighLevelClient getClient() {
        return client;
    }

    public void pushToElastic(List<DocumentObject> documentObjects) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        BulkRequest bulkRequest = new BulkRequest();

        for (DocumentObject documentObject : documentObjects) {
            String jsonString = objectMapper.writeValueAsString(documentObject);
            IndexRequest indexRequest = new IndexRequest(Config.ELASTIC_INDEX)
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


    public SearchRequest keywordToQuery(List<String> kws,int minimumMatch) {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(10000);
        sourceBuilder.timeout(new TimeValue(5, TimeUnit.MINUTES));

        BoolQueryBuilder bool = new BoolQueryBuilder()
                ;
        String[] includeFields = new String[]{"headerName", "paragraphContent"};
        String[] excludeFields = new String[]{};

        Map<String, Float> fields = new HashMap<>();
        fields.put("headerName", 1f);
        fields.put("paragraphContent", 1f);
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        if (minimumMatch == 1) {
            for (String kw : kws) {
                String st = "\"" + kw.replace(",", "\" OR \"") + "\"";
                QueryStringQueryBuilder stringQueryBuilder = QueryBuilders.queryStringQuery(st).fields(fields);
                qb.should(stringQueryBuilder);
            }
        } else {
            for (String kw : kws) {
                String st = "\"" + kw.replace(",", "\" OR \"") + "\"";
                QueryStringQueryBuilder stringQueryBuilder = QueryBuilders.queryStringQuery(st).fields(fields);
                qb.should(stringQueryBuilder);
            }
            qb.minimumShouldMatch(minimumMatch);
        }
        bool.must(qb);

        sourceBuilder.fetchSource(includeFields, excludeFields);
        sourceBuilder.query(bool);
        sourceBuilder.trackTotalHits(true);
        searchRequest
                .searchType(SearchType.QUERY_THEN_FETCH)
                .indices(Config.ELASTIC_INDEX)
                .source(new SearchSourceBuilder().from(0).size(10000).query(sourceBuilder.query()))
                .scroll(new TimeValue(60000));
        return searchRequest;
    }

    public void fetchRecord(SearchRequest searchRequest) {
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(searchResponse.getHits().getTotalHits());
            do {
                searchResponse.getHits().iterator()
                        .forEachRemaining(hit -> {
                            try {
                                Map<String, Object> source = hit.getSourceAsMap();
                                System.out.println(source.get("paragraphContent") + "\n");
                            } catch (Exception ignored) {
                            }
                        });
                String scrollid = searchResponse.getScrollId();
                searchResponse = client.scroll(new SearchScrollRequest(scrollid).scroll(new TimeValue(60000)), RequestOptions.DEFAULT);

            } while (searchResponse.getHits().getHits().length != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
