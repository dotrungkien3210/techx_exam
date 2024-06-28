package project.pdfToElastic.core.extract.PageCutter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import project.pdfToElastic.core.extract.FunctionSupport.CollectionsProcessing;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ContentsExtractor {
    public String extractContent(String keyStart, String keyend){
        String extractedText = "";
        // Đường dẫn tới tệp PDF
        String filePath = "input/s3-userguide.pdf";

        try {
            // Tải tài liệu PDF
            PDDocument document = PDDocument.load(new File(filePath));

            // Kiểm tra xem tài liệu có bị mã hóa không
            if (!document.isEncrypted()) {
                // Sử dụng PDFTextStripper để trích xuất văn bản từ tài liệu
                PDFTextStripper pdfStripper = new PDFTextStripper();

                // Trích xuất văn bản từ toàn bộ tài liệu
                String text = pdfStripper.getText(document);


                // Tìm vị trí của "Table of Contents" và "AWS Glossary"
                int start = text.indexOf(keyStart);
                int end = text.indexOf(keyend);

                // Cắt đoạn văn bản
                if (start != -1 && end != -1 && start < end) {
                    // Tìm vị trí end table of contents
                    int nextLineStart = text.indexOf("\n", end);
                    if (nextLineStart != -1) {
                        // Lấy vị trí bắt đầu của dòng tiếp theo
                        int nextLineIndex = nextLineStart + 1;

                        // Cắt text
                        extractedText = text.substring(start, nextLineIndex);
//                        System.out.println(extractedText);
                    } else {
                        System.out.println("Cannot find row after last.");
                    }
                } else {
                    System.out.println("Cannot find Start line or end line index");
                }
            } else {
                System.out.println("Cannot read document.");
            }

            // Đóng tài liệu
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!extractedText.isEmpty()){
            return extractedText;
        }
        return null;
    }




    public static void main(String[] args) {
        ContentsExtractor contentsExtractor = new ContentsExtractor();
        CollectionsProcessing listMapProcessing = new CollectionsProcessing();
        String tableOfContents = contentsExtractor.extractContent("Table of Contents", "AWS Glossary ...");

        if (!tableOfContents.isEmpty()){
            listMapProcessing.contentsFormatted(tableOfContents);
        }
        else {
            System.out.println("Không cắt được đoạn văn bản");
        }
    }
}