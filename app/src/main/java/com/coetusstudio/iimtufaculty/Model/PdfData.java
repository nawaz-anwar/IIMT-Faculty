package com.coetusstudio.iimtufaculty.Model;

public class PdfData {

    String filename, fileurl, facultySection;

    public PdfData(String filename, String fileurl, String facultySection) {
        this.filename = filename;
        this.fileurl = fileurl;
        this.facultySection = facultySection;
    }

    public PdfData() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getFacultySection() {
        return facultySection;
    }

    public void setFacultySection(String facultySection) {
        this.facultySection = facultySection;
    }
}
