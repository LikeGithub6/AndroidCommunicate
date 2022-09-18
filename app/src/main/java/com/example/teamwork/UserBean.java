package com.example.teamwork;

public class UserBean {
    private String id;
    private String password;
    private String nicheng;
    private String sex;
    private String tupian;
    private String gexing;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserBean(String id, String password, String nicheng, String sex, String tupian, String gexing) {
        this.id = id;
        this.password = password;
        this.nicheng = nicheng;
        this.sex = sex;
        this.tupian = tupian;
        this.gexing = gexing;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNicheng() {
        return nicheng;
    }

    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTupian() {
        return tupian;
    }

    public void setTupian(String tupian) {
        this.tupian = tupian;
    }

    public String getGexing() {
        return gexing;
    }

    public void setGexing(String gexing) {
        this.gexing = gexing;
    }
}
