package project.pdfToElastic.core.extract.PageCutter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BoldTextExtractor {

    public static void main(String[] args) {
        try {
            String filePath = "input/s3-userguide.pdf";

            PDDocument document = PDDocument.load(new File(filePath));
            PDFTextStripper stripper = new PDFTextStripper() {
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    if (isBold(textPositions)) {
                        System.out.println("Bold Text: " + text);
                    }
                    super.writeString(text, textPositions);
                }
            };

            stripper.getText(document);

            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to check if text is bold
    private static boolean isBold(List<TextPosition> textPositions) {
        for (TextPosition textPosition : textPositions) {
            if (textPosition.getFont().getFontDescriptor().isForceBold()) {
                return true;
            }
        }
        return false;
    }
}
