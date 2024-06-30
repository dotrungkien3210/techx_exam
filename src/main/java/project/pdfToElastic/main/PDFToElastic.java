package project.pdfToElastic.main;

import project.pdfToElastic.utils.PDFUtils;
import project.pdfToElastic.core.pdfProcessing.ContentsExtractor;
import project.pdfToElastic.core.spark.SparkProcessing;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class PDFToElastic {

    /**
     * Step 1 thực hiện gọi tới hàm để cắt phần mục lục của quyển sách ra
     * Chọn KeyStart là Table of Contents vì quyển sách nào cũng có từ này trước khi bắt đầu mục lục
     * Chọn KeyEnd là ....... vì phần lớn sách dùng nhiều dấu ..... ở mục lục và không xài sau đó ở bất cứ đâu nữa
     * Ngoài ra mục lục và trang thực tế có sự chênh lệch nên ta phải căn lại số trang để đảm bảo crawl đúng
     * <p>
     * Step 2: đóng gói lại thành object và
     */

    public void startProcessing() throws IOException {
        // Step 1
        ContentsExtractor contentsExtractor = new ContentsExtractor();
        PDFUtils pdfUtils = new PDFUtils();
        int startTableOfContents = pdfUtils.findFirstKeywordIndex("Table of Contents");
        int endTableOfContents = pdfUtils.findLastKeywordIndex("..........");
        String tableOfContents = contentsExtractor.extractByIndex(pdfUtils.getDocument(), startTableOfContents, endTableOfContents);
        List<String> extractContent = pdfUtils.contentsFormatted(tableOfContents);
        int startContentsPageIndex = pdfUtils.findPageIndexFromCharacterPosition(endTableOfContents);
        // Step 2
        SparkProcessing sparkProcessing = new SparkProcessing();
        sparkProcessing.start(extractContent, startContentsPageIndex);
    }

    public static void main(String[] args) throws IOException {
        PDFToElastic pdfToElastic = new PDFToElastic();
        pdfToElastic.startProcessing();
    }
}
