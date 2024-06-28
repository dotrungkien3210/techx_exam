package project.pdfToElastic.core.extract.PageCutter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class FixPageNumber {
    public void extractFixPage(){
        // Đường dẫn tới tệp PDF
        String filePath = "input/s3-userguide.pdf";

        try {
            // Tải tài liệu PDF
            PDDocument document = PDDocument.load(new File(filePath));

            // Kiểm tra xem tài liệu có bị mã hóa không
            if (!document.isEncrypted()) {
                // Sử dụng PDFTextStripper để trích xuất văn bản từ tài liệu
                PDFTextStripper pdfStripper = new PDFTextStripper();

                // Thiết lập phạm vi trang cần đọc
                pdfStripper.setStartPage(1);  // Trang bắt đầu (PDFBox sử dụng 1-based indexing)
                pdfStripper.setEndPage(1);   // Trang kết thúc

                // Trích xuất văn bản từ các trang đã chỉ định
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

    public static void main(String[] args) {
        FixPageNumber fixPageNumber = new FixPageNumber();
        fixPageNumber.extractFixPage();
    }
}
