package project.pdfToElastic.utils;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

public class SparkUtils {
    public SparkSession sparkSession;
    public JavaSparkContext sparkContext;

    public void initSpark(){
        sparkSession = SparkSession.builder()
                .appName("PDF to Elastic")
                .master("local")
                .getOrCreate();
    }

    public SparkSession getSparkSession() {
        return sparkSession;
    }
}
