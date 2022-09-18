package com.coetusstudio.iimtufaculty.Model;

public class Semester {

    String semester, id;

    public Semester(String semester, String id) {
        this.semester = semester;
        this.id = id;
    }

    public Semester() {
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
