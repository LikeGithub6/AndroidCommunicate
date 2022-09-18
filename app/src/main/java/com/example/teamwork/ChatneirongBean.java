package com.example.teamwork;

public class ChatneirongBean {
    private String sendid;
    private String time;
    private String neirong;

    public ChatneirongBean(String sendid, String time, String neirong) {
        this.sendid = sendid;
        this.time = time;
        this.neirong = neirong;
    }

    public String getSendid() {
        return sendid;
    }

    public void setSendid(String sendid) {
        this.sendid = sendid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNeirong() {
        return neirong;
    }

    public void setNeirong(String neirong) {
        this.neirong = neirong;
    }
}
