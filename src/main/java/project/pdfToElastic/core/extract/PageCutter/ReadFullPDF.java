package project.pdfToElastic.core.extract.PageCutter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class ReadFullPDF {
    public void readFullContents(){
        // Đường dẫn tới tệp PDF
        String filePath = "input/bobsleigh_tutorial.pdf";

        try {
            // Tải tài liệu PDF
            PDDocument document = PDDocument.load(new File(filePath));

            // Kiểm tra xem tài liệu có bị mã hóa không
            if (!document.isEncrypted()) {
                // Sử dụng PDFTextStripper để trích xuất văn bản từ tài liệu
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);

                // In nội dung ra console
                System.out.println(text);
            }

            // Đóng tài liệu
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
