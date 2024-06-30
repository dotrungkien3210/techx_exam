package project.pdfToElastic.core.extract.PageCutter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import project.pdfToElastic.config.Config;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class ContentsExtractor implements Serializable {

    public ContentsExtractor() {
    }

    public String extractByIndex(String document,int start, int end) {
        String extractedText = "";

        if (start != -1 && end != -1 && start < end) {
            // Tìm end
            int nextLineStart = document.indexOf("\n", end);
            if (nextLineStart != -1) {
                // Lấy vị trí bắt đầu của dòng tiếp theo
                int nextLineIndex = nextLineStart + 1;
                extractedText = document.substring(start, nextLineIndex);
            } else {
                System.out.println("Cannot find row after last.");
            }
        } else {
            System.out.println("Cannot find Start line or end line index");
        }

        if (!extractedText.isEmpty()) {
            return extractedText;
        }
        return null;
    }

    public void readFullContents() {
        try {
            PDDocument document = PDDocument.load(new File(Config.INPUT_DOCUMENT));

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

}