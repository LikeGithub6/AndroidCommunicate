package com.example.teamwork;

import java.util.List;

public class MainTopicBean {
    private String id;
    private String touxiang;
    private String nicheng;
    private String neirong;
    private String topic;
    private String pisize;
    private String time;
    private List<String> imglist;
    private String dianzan;
    private String pinglun;
    private boolean dianmei;

    public MainTopicBean(String id, String touxiang, String nicheng, String neirong, String topic, String pisize, String time, List<String> imglist, String dianzan, String pinglun) {
        this.id = id;
        this.touxiang = touxiang;
        this.nicheng = nicheng;
        this.neirong = neirong;
        this.topic = topic;
        this.pisize = pisize;
        this.time = time;
        this.imglist = imglist;
        this.dianzan = dianzan;
        this.pinglun = pinglun;
        this.dianmei=false;
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

    public String getNeirong() {
        return neirong;
    }

    public void setNeirong(String neirong) {
        this.neirong = neirong;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPisize() {
        return pisize;
    }

    public void setPisize(String pisize) {
        this.pisize = pisize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getImglist() {
        return imglist;
    }

    public void setImglist(List<String> imglist) {
        this.imglist = imglist;
    }

    public String getDianzan() {
        return dianzan;
    }

    public void setDianzan(String dianzan) {
        this.dianzan = dianzan;
    }

    public String getPinglun() {
        return pinglun;
    }

    public void setPinglun(String pinglun) {
        this.pinglun = pinglun;
    }

    public boolean isDianmei() {
        return dianmei;
    }

    public void setDianmei(boolean dianmei) {
        this.dianmei = dianmei;
    }
}
