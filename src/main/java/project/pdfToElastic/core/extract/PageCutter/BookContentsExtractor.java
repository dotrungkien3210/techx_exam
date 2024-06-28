package project.pdfToElastic.core.extract.PageCutter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class BookContentsExtractor implements Serializable {
    public String extractContent(String keyStart, String keyend){
        String extractedText = "";

        String filePath = "input/s3-userguide.pdf";
        try {

            PDDocument document = PDDocument.load(new File(filePath));

            // Kiểm tra mã hóa
            if (!document.isEncrypted()) {

                PDFTextStripper pdfStripper = new PDFTextStripper();

                String text = pdfStripper.getText(document);

                // Tìm vị trí
                int start = text.indexOf(keyStart);
                int end = text.lastIndexOf(keyend);

                // Cắt đoạn văn bản
                if (start != -1 && end != -1 && start < end) {
                    // Tìm end
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

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!extractedText.isEmpty()){
            return extractedText;
        }
        return null;
    }

    public void readFullContents(){
        String filePath = "input/bobsleigh_tutorial.pdf";
        try {
            PDDocument document = PDDocument.load(new File(filePath));

            if (!document.isEncrypted()) {
                // cắt text
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);

                System.out.println(text);
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String extractByPageNumber(int startPage,int endPage){
        String content = "";

        String filePath = "input/s3-userguide.pdf";
        try {
            PDDocument document = PDDocument.load(new File(filePath));
            // Check có bị mã hóa không
            if (!document.isEncrypted()) {

                PDFTextStripper pdfStripper = new PDFTextStripper();

                pdfStripper.setStartPage(startPage);  // start
                pdfStripper.setEndPage(endPage);   // end

                // cắt text
                content = pdfStripper.getText(document);

                System.out.println(content);
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}