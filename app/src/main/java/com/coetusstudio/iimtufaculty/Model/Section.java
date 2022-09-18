package com.coetusstudio.iimtufaculty.Model;

public class Section {

    String section, id;

    public Section(String section, String id) {
        this.section = section;
        this.id = id;
    }

    public Section() {
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
