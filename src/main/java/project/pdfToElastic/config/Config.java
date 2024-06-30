package project.pdfToElastic.config;

public class Config {
    public static String INPUT_DOCUMENT = "input/s3-userguide.pdf";
    public static int MINIMUM_MATCH = 1;
    public static String INPUT_KW = "input/input.txt";


    // elasticsearch
    public static String ELASTIC_HOST = "localhost";
    public static int ELASTIC_PORT = 9200;
    public static String ELASTIC_INDEX = "test_index2";
}
