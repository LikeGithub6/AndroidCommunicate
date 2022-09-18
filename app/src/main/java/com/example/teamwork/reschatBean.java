package com.example.teamwork;

public class reschatBean {
    private String heid;
    private String henicheng;
    private String hetp;
    private String type;
    private String neirong;
    private String time;
    private String nosee;

    public reschatBean(String heid, String henicheng, String hetp, String type, String neirong, String time, String nosee) {
        this.heid = heid;
        this.henicheng = henicheng;
        this.hetp = hetp;
        this.type = type;
        this.neirong = neirong;
        this.time = time;
        this.nosee = nosee;
    }

    public String getHeid() {
        return heid;
    }

    public void setHeid(String heid) {
        this.heid = heid;
    }

    public String getHenicheng() {
        return henicheng;
    }

    public void setHenicheng(String henicheng) {
        this.henicheng = henicheng;
    }

    public String getHetp() {
        return hetp;
    }

    public void setHetp(String hetp) {
        this.hetp = hetp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNeirong() {
        return neirong;
    }

    public void setNeirong(String neirong) {
        this.neirong = neirong;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNosee() {
        return nosee;
    }

    public void setNosee(String nosee) {
        this.nosee = nosee;
    }
}
