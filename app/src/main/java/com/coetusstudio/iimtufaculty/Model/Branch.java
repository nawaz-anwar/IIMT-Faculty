package com.coetusstudio.iimtufaculty.Model;

public class Branch {

    String branch, id;

    public Branch(String branch, String id) {
        this.branch = branch;
        this.id = id;
    }

    public Branch() {
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
