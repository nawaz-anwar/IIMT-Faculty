package com.coetusstudio.iimtufaculty.Model;

public class SessionalTitle {

    String sessionalTitle, id;

    public SessionalTitle(String sessionalTitle, String id) {
        this.sessionalTitle = sessionalTitle;
        this.id = id;
    }

    public SessionalTitle() {
    }

    public String getSessionalTitle() {
        return sessionalTitle;
    }

    public void setSessionalTitle(String sessionalTitle) {
        this.sessionalTitle = sessionalTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
