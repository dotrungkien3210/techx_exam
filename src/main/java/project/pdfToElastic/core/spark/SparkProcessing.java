package project.pdfToElastic.core.spark;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import project.pdfToElastic.model.DocumentObject;
import project.pdfToElastic.utils.ElasticUtils;
import project.pdfToElastic.utils.PDFUtils;
import project.pdfToElastic.utils.SparkUtils;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.types.StructField;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import project.pdfToElastic.utils.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SparkProcessing implements Serializable {
    SparkSession spark;

    public SparkProcessing() {
        SparkUtils sparkUtils = new SparkUtils();
        spark = sparkUtils.getSparkSession();
    }

    /**
     * function này chuyển đổi từ list sang dataframe để tiện tính toán
     * @param listContent
     * @return
     */
    public Dataset<Row> listToDataframe(List<String> listContent) {
        List<Row> rows = new ArrayList<>();

        for (String line : listContent) {
            try {
                String[] parts = line.split("  ");
                if (parts.length == 4) { // check lenng = 4
                    Row row = RowFactory.create(
                            parts[0].trim(),
                            Integer.parseInt(parts[1].trim()),
                            parts[2].trim(),
                            Integer.parseInt(parts[3].trim())
                    );
                    rows.add(row);
                } else {
                    System.out.println("Invalid format for line: " + line);
                }
            } catch (Exception e) {
                System.out.println("Exception processing line: " + line + ", error: " + e.getMessage());
            }
        }

        StructType schema = DataTypes.createStructType(new StructField[]{
                DataTypes.createStructField("currentSessionName", DataTypes.StringType, false),
                DataTypes.createStructField("startPageNumber", DataTypes.IntegerType, false),
                DataTypes.createStructField("nextSessionName", DataTypes.StringType, false),
                DataTypes.createStructField("endPageNumber", DataTypes.IntegerType, false)
        });

        return spark.createDataFrame(rows, schema);
    }

    /**
     * Function này tận dụng khả năng xử lý phân tán của spark
     * @param df: dataframe truyền vào
     * @param startContentsPageIndex: độ chênh lệch giữa pageLineNumber và thực tế
     * @throws IOException
     */
    public Dataset<Row> extractContentByPage(Dataset<Row> df, int startContentsPageIndex) throws IOException {
        PDFUtils pdfUtils = new PDFUtils();
        TextUtils textUtils = new TextUtils();
        StructType paragraphSchema = new StructType()
                .add("startPageNumber", DataTypes.IntegerType)
                .add("endPageNumber", DataTypes.IntegerType)
                .add("headerLineNumber", DataTypes.IntegerType)
                .add("currentSessionHeader", DataTypes.StringType)
                .add("paragraphContent", DataTypes.StringType);

        Dataset<Row> paragraphDF = df.map((MapFunction<Row, Row>) row -> {
            String currentSessionHeader = row.getString(0);
            int startPageNumber = row.getInt(1);
            String nextSessionHeader = row.getString(2);
            int endPageNumber = row.getInt(3);
            String documents = pdfUtils.extractByPageNumber(startPageNumber + startContentsPageIndex, endPageNumber + startContentsPageIndex );
            String paragraphContent = textUtils.getParagraphContents(documents, currentSessionHeader, nextSessionHeader);
            int headerLineNumber = textUtils.getLineNumber(documents,currentSessionHeader);
            return RowFactory.create(startPageNumber,endPageNumber,headerLineNumber,currentSessionHeader,paragraphContent);
        }, RowEncoder.apply(paragraphSchema));
        return paragraphDF;
    }

    public void pushToElasticSearch(Dataset<Row> paragraphDF) throws IOException {
        ElasticUtils elasticUtils = new ElasticUtils();
        List<Row> rowList = paragraphDF.collectAsList();
        List<DocumentObject> documentObjects = rowList.stream()
                .map(row -> new DocumentObject(row.getInt(0),row.getInt(1),row.getInt(2), row.getString(3),row.getString(4)))
                .collect(Collectors.toList());
        elasticUtils.pushToElastic(documentObjects);
    }



    public void start(List<String> listContent, int startContentsPageIndex) throws IOException {
        Dataset<Row> df = listToDataframe(listContent);
        Dataset<Row> paragraphDF = extractContentByPage(df, startContentsPageIndex);
        pushToElasticSearch(paragraphDF);
    }


}
