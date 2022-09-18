package com.coetusstudio.iimtufaculty.Model;

public class Attendance {

    String enrollment, name, attendance, absent, percent, atvalue;

    public Attendance(String enrollment, String name, String attendance, String absent, String percent, String atvalue) {
        this.enrollment = enrollment;
        this.name = name;
        this.attendance = attendance;
        this.absent = absent;
        this.percent = percent;
        this.atvalue = atvalue;
    }

    public Attendance() {
    }

    public Attendance(String enrollment, String name, String attendance, String absent, String percent) {
        this.enrollment = enrollment;
        this.name = name;
        this.attendance = attendance;
        this.absent = absent;
        this.percent = percent;
    }

    public Attendance(String atvalue) {
        this.atvalue = atvalue;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getAtvalue() {
        return atvalue;
    }

    public void setAtvalue(String atvalue) {
        this.atvalue = atvalue;
    }
}
