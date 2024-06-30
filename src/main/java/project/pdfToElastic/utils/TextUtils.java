package project.pdfToElastic.utils;

import java.io.Serializable;

public class TextUtils implements Serializable {
    public TextUtils() {

    }

    /**
     * function này sẽ cố gắng xử lý và extract tất cả thông tin về một object java để push
     *
     * @param currentSessionHeader : Tên tiêu đề đoạn văn
     * @param nextSessionHeader    : Tên tiêu đề tiếp theo
     *  Hiện phải đặt một đoạn try catch vì lỗi format (trong phần problem readme)
     */

    public String getParagraphContents(String documents, String currentSessionHeader, String nextSessionHeader) {
        int paragraphStartPoint = documents.indexOf(currentSessionHeader);
        int paragraphEndPoint = documents.indexOf("\n" + nextSessionHeader);
        try {
            return documents.substring(paragraphStartPoint, paragraphEndPoint);
        } catch (Exception ignored) {
        }
        return "";
    }

    /**
     * Đoạn code này lấy ra lineNumber của header trong document
     *
     * @param documents:           nội dung page
     * @param currentSessionHeader : tên của header
     */
    public int getLineNumber(String documents, String currentSessionHeader) {
        String[] paragraphSplitted = documents.split("\n");
        int lineNumber = 0;
        for (String line : paragraphSplitted) {
            lineNumber++;
            if (line.equals(currentSessionHeader)) {
                break;
            }
        }
        return lineNumber;
    }
}
