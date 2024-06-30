package project.pdfToElastic.utils;


import org.apache.spark.sql.SparkSession;


public class SparkUtils {
    public SparkSession sparkSession;

    public SparkUtils() {
        sparkSession = SparkSession.builder()
                .appName("PDF to Elastic")
                .master("local")
                .config("es.nodes", "localhost:9200")
                .config("es.index.auto.create", "true")
                .config("es.update.retry.on.conflict", "2")
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .config("es.write.operation", "upsert")
                .getOrCreate();
    }

    public SparkSession getSparkSession() {
        return sparkSession;
    }
}
