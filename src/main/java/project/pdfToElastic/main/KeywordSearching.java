package project.pdfToElastic.main;

import org.elasticsearch.action.search.SearchRequest;
import project.pdfToElastic.config.Config;
import project.pdfToElastic.utils.ElasticUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KeywordSearching {

    public KeywordSearching() {

    }

    public void start() {
        ElasticUtils elasticUtils = new ElasticUtils();
        List<String> kws = loadListKeyWord();
        SearchRequest searchRequest = elasticUtils.keywordToQuery(kws, Config.MINIMUM_MATCH);
        elasticUtils.fetchRecord(searchRequest);

    }

    public List<String> loadListKeyWord() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Config.INPUT_KW_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(lines);
    }

    public static void main(String[] args) {
        KeywordSearching keywordSearching = new KeywordSearching();
        keywordSearching.start();
    }
}
