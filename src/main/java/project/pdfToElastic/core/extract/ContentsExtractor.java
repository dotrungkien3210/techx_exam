package project.pdfToElastic.core.extract;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

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

                // Kiểm tra và trích xuất đoạn văn bản từ "Table of Contents" đến "AWS Glossary"
                if (start != -1 && end != -1 && start < end) {
                    // Tìm vị trí của ký tự xuống dòng ngay sau "AWS Glossary"
                    int nextLineStart = text.indexOf("\n", end);
                    if (nextLineStart != -1) {
                        // Lấy vị trí bắt đầu của dòng tiếp theo
                        int nextLineIndex = nextLineStart + 1;

                        // Trích xuất văn bản từ "Table of Contents" đến ngay trước dòng sau "AWS Glossary"
                        extractedText = text.substring(start, nextLineIndex);
//                        System.out.println(extractedText);
                    } else {
                        System.out.println("Không tìm thấy dòng sau 'AWS Glossary'.");
                    }
                } else {
                    System.out.println("Không tìm thấy đoạn 'Table of Contents' hoặc 'AWS Glossary', hoặc vị trí không hợp lệ.");
                }
            } else {
                System.out.println("Tài liệu bị mã hóa và không thể đọc được.");
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
    public void contentsFormatted(String tableOfContents){
        String removeDot = tableOfContents.replaceAll("\\.", "").replace("Table of Contents", "").replaceFirst("\n","");
        List<String> linesList = Arrays.asList(removeDot.split("\n"));
        Map<String, String> finalFormat = listToMap(linesList);
        System.out.println(finalFormat);
    }

    public Map<String, String> listToMap(List<String> list){
        // Khởi tạo Map để lưu kết quả
        Map<String, String> map = new HashMap<>();

        // Duyệt qua từng phần tử trong danh sách
        for (String item : list) {
            // Phân tách phần tử dựa trên hai dấu cách ("  ")
            String[] parts = item.split("  ");
            if (parts.length == 2) {
                String text = parts[0].trim();
                String number = parts[1].trim();
                // Lưu vào Map
                map.put(number, text);
            }
        }
        return map;
    }



    public static void main(String[] args) {
        ContentsExtractor contentsExtractor = new ContentsExtractor();
        String tableOfContents = contentsExtractor.extractContent("Table of Contents", "AWS Glossary ...");
        if (!tableOfContents.isEmpty()){
            contentsExtractor.contentsFormatted(tableOfContents);
        }

    }
}