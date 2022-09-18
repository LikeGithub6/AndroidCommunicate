package com.example.teamwork;

public class LuntanBean {
    private String id;
    private String title;
    private String zuozhe;
    private String time;
    private String looknum;

    public LuntanBean(String id, String title, String zuozhe, String time, String looknum) {
        this.id = id;
        this.title = title;
        this.zuozhe = zuozhe;
        this.time = time;
        this.looknum = looknum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZuozhe() {
        return zuozhe;
    }

    public void setZuozhe(String zuozhe) {
        this.zuozhe = zuozhe;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLooknum() {
        return looknum;
    }

    public void setLooknum(String looknum) {
        this.looknum = looknum;
    }
}
