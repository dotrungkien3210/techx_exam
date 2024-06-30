package project.pdfToElastic.core.pdfProcessing;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.pdfToElastic.config.Config;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class ContentsExtractor implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(ContentsExtractor.class);

    public ContentsExtractor() {
    }

    public String extractByIndex(String document, int start, int end) {
        String extractedText = "";

        if (start != -1 && end != -1 && start < end) {
            // Tìm end
            int nextLineStart = document.indexOf("\n", end);
            if (nextLineStart != -1) {
                // Lấy vị trí bắt đầu của dòng tiếp theo
                int nextLineIndex = nextLineStart + 1;
                extractedText = document.substring(start, nextLineIndex);
            } else {
                log.error("Cannot find row after last.");
            }
        } else {
            log.error("Cannot find Start line or end line index");
        }

        if (!extractedText.isEmpty()) {
            return extractedText;
        }
        return null;
    }

    public void readFullContents() {
        try {
            PDDocument document = PDDocument.load(new File(Config.INPUT_DOCUMENT_PATH));

            if (!document.isEncrypted()) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}