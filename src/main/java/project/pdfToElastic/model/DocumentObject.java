package project.pdfToElastic.model;


import project.pdfToElastic.utils.StringHashUtils;

import java.io.Serializable;

public class DocumentObject implements Serializable{
    public String documentID;
    public int startPageNumber;
    public int headerLineNumber;
    public int endPageNumber;
    public String headerName;
    public String paragraphContent;


    public DocumentObject(int startPageNumber, int endPageNumber, int headerLineNumber, String paragraphContent, String headerName) {
        StringHashUtils stringHashUtils = new StringHashUtils();
        this.startPageNumber = startPageNumber;
        this.endPageNumber = endPageNumber;
        this.headerLineNumber = headerLineNumber;
        this.headerName = headerName;
        this.paragraphContent = paragraphContent;
        this.documentID = stringHashUtils.stringToSHA(headerName+startPageNumber);
    }

    public int getStartPageNumber() {
        return startPageNumber;
    }

    public void setStartPageNumber(int startPagesNumber) {
        this.startPageNumber = startPagesNumber;
    }

    public int getHeaderLineNumber() {
        return headerLineNumber;
    }

    public void setHeaderLineNumber(int headerLineNumber) {
        this.headerLineNumber = headerLineNumber;
    }

    public int getEndPageNumber() {
        return endPageNumber;
    }

    public void setEndPageNumber(int endPagesNumber) {
        this.endPageNumber = endPagesNumber;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getParagraphContent() {
        return paragraphContent;
    }

    public void setParagraphContent(String paragraphContent) {
        this.paragraphContent = paragraphContent;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    @Override
    public String toString() {
        return "DocumentObject{" +
                "startPagesNumber=" + startPageNumber +
                ", headerLineNumber=" + headerLineNumber +
                ", endPagesNumber=" + endPageNumber +
                ", headerName='" + headerName + '\'' +
                ", paragraphContent='" + paragraphContent + '\'' +
                '}';
    }
}
