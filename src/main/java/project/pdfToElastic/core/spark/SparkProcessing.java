package project.pdfToElastic.core.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import project.pdfToElastic.core.extract.PageCutter.BookContentsExtractor;
import project.pdfToElastic.utils.SparkUtils;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.RowFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SparkProcessing {
    SparkSession spark;

    public SparkProcessing(){
        SparkUtils sparkUtils = new SparkUtils();
        spark = sparkUtils.getSparkSession();
    }

    public Dataset<Row> listToDataframe(List<String> listContent){
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

    public void extractContentByPage(Dataset<Row> df){
        BookContentsExtractor bookContentsExtractor = new BookContentsExtractor();
        df.foreach(row -> {
            System.out.println("Processing record: " + row);
            String currentSessionName = row.getAs("currentSessionName");
            int startPageNumber = row.getAs("startPageNumber");
            String nextSessionName = row.getAs("nextSessionName");
            int endPageNumber = row.getAs("endPageNumber");
//            String documents = bookContentsExtractor.extractByPageNumber(startPageNumber + 26,endPageNumber + 26);
        });
    }

    public void pushToElastic(){

    }

    public void start(List<String> listContent){
        Dataset<Row> df = listToDataframe(listContent);
        extractContentByPage(df);

    }
    
}
