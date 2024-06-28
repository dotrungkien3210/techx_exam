package project.pdfToElastic.model;



public class DocumentObject {
    public String startPagesNumber;
    public String endPagesNumber;
    public String headerLineNumber;
    public String content;
    public String headerName;

    public DocumentObject(String startPagesNumber, String endPagesNumber, String headerLineNumber, String content, String headerName) {
        this.startPagesNumber = startPagesNumber;
        this.endPagesNumber = endPagesNumber;
        this.headerLineNumber = headerLineNumber;
        this.content = content;
        this.headerName = headerName;
    }

    public String getStartPagesNumber() {
        return startPagesNumber;
    }

    public String getEndPagesNumber() {
        return endPagesNumber;
    }

    public String getContent() {
        return content;
    }

    public String getHeaderLineNumber() {
        return headerLineNumber;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setStartPagesNumber(String startPagesNumber) {
        this.startPagesNumber = startPagesNumber;
    }

    public void setEndPagesNumber(String endPagesNumber) {
        this.endPagesNumber = endPagesNumber;
    }

    public void setHeaderLineNumber(String headerLineNumber) {
        this.headerLineNumber = headerLineNumber;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public String toString() {
        return "DocumentObject{" +
                "startPagesNumber='" + startPagesNumber + '\'' +
                ", endPagesNumber='" + endPagesNumber + '\'' +
                ", headerLineNumber='" + headerLineNumber + '\'' +
                ", content='" + content + '\'' +
                ", headerName='" + headerName + '\'' +
                '}';
    }
}
