package com.example.teamwork;

public class MeguanzhuBean {
    private String id;
    private String touxiang;
    private String nicheng;
    private String sex;

    public MeguanzhuBean(String id, String touxiang, String nicheng, String sex) {
        this.id = id;
        this.touxiang = touxiang;
        this.nicheng = nicheng;
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
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
}
