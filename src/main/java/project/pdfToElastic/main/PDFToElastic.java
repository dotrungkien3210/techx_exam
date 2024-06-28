package project.pdfToElastic.main;

import project.pdfToElastic.core.extract.FunctionSupport.CollectionsProcessing;
import project.pdfToElastic.core.extract.PageCutter.BookContentsExtractor;
import project.pdfToElastic.core.spark.SparkProcessing;

import java.util.*;

public class PDFToElastic {

    /**
     * Đoạn code này thực hiện gọi tới hàm để cắt phần mục lục của quyển sách ra
     * Chọn KeyStart là Table of Contents vì quyển sách nào cũng có từ này trước khi bắt đầu mục lục
     * Chọn KeyEnd là ....... vì phần lớn sách dùng nhiều dấu ... ở mục lục và không xài sau đó ở bất cứ đâu nữa
     */

    public List<String> extractTableOfContents(){
        BookContentsExtractor contentsExtractor = new BookContentsExtractor();
        CollectionsProcessing listMapProcessing = new CollectionsProcessing();
        String tableOfContents = contentsExtractor.extractContent("Table of Contents", "..........");
        if (!tableOfContents.isEmpty()){
            return listMapProcessing.contentsFormatted(tableOfContents);
        }
        else {
            System.out.println("Không cắt được đoạn văn bản");
        }
        return null;
    }

    public static void main(String[] args) {
        PDFToElastic pdfToElastic = new PDFToElastic();
        List<String> extractContent = pdfToElastic.extractTableOfContents();
        SparkProcessing sparkProcessing = new SparkProcessing();
        sparkProcessing.start(extractContent);
    }
}
