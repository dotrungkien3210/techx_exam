package project.pdfToElastic.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import project.pdfToElastic.config.Config;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;


public class PDFUtils implements Serializable {
    public static PDFTextStripper pdfStripper;
    public static PDDocument document;
    String text;

    public PDFUtils() throws IOException {
        try {
            document = PDDocument.load(new File(Config.INPUT_DOCUMENT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfStripper = new PDFTextStripper();
        text = pdfStripper.getText(document);
    }

    public void close() throws IOException {
        document.close();
    }

    public String getDocument(){
        return text;
    }

    public List<String> mergeAdjacentElements(List<String> originalList) {
        List<String> mergedList = new ArrayList<>();

        // Iterate through original list to merge adjacent elements
        for (int i = 0; i < originalList.size() - 1; i++) {
            String mergedItem = originalList.get(i) + "  " + originalList.get(i + 1).trim();
            mergedList.add(mergedItem);
        }

        return mergedList;
    }

    public List<String> contentsFormatted(String tableOfContents) throws IOException {
        List<String> filteredLines = new ArrayList<>();

        String[] linesList = tableOfContents.split("\n");
        for (String line : linesList) {
            if (line.contains(".....")) {
                filteredLines.add(line.replaceAll("\\.", "").replaceFirst("\n", "").trim());
            }
        }
        return reFormatList(filteredLines);
    }

    public List<String> reFormatList(List<String> list) throws IOException {
        PDFUtils listMapProcessing = new PDFUtils();
        return listMapProcessing.mergeAdjacentElements(list);
    }



    /**
     * Hàm tìm phần tử đầu tiên trong document
     * @param keyWord: phần tử cần tìm
     * @return: vị trí
     */

    public int findFirstKeywordIndex(String keyWord) throws IOException {
        String text = pdfStripper.getText(document);
        return text.indexOf(keyWord);
    }

    /**
     * Hàm tìm phần tử cuối cùng trong document
     * @param keyWord: phần tử cần tìm
     */

    public int findLastKeywordIndex(String keyWord) throws IOException {
        String text = pdfStripper.getText(document);
        return text.lastIndexOf(keyWord);
    }

    /**
     * Tại sao lại cần kiểm tra vị trí của page
     * pagePosition của sách trong file pdf và pagePosition trong thực tế không giống nhau
     * Như vậy ta phải tính tương quan giữa vị trí thực tế và vị trí gốc để cắt cho chuẩn
     * @return Số index trang thực tế trong văn bản
     */

    public int findPageIndexFromCharacterPosition(int charPosition) {
        int pageIndex = 0;
        int cumulativeCharCount = 0;
        try {
            while (pageIndex < document.getNumberOfPages()) {
                pdfStripper.setStartPage(pageIndex + 1);
                pdfStripper.setEndPage(pageIndex + 1);
                String pageText = pdfStripper.getText(document);
                int charCountOnPage = pageText.length();
                cumulativeCharCount += charCountOnPage;

                if (cumulativeCharCount >= charPosition) {
                    return pageIndex + 1;
                }

                pageIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String extractByPageNumber(int startPage, int endPage) {
        String content = "";

        try {
            // Check có bị mã hóa không
            if (!document.isEncrypted()) {

                PDFTextStripper pdfStripper = new PDFTextStripper();

                pdfStripper.setStartPage(startPage);  // start
                pdfStripper.setEndPage(endPage);   // end

                // cắt text
                content = pdfStripper.getText(document);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
