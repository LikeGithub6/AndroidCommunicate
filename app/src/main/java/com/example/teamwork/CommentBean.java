package com.example.teamwork;

import java.util.List;

public class CommentBean {
    private String id;
    private String nicheng;
    private String touxiang;
    private String neirong;
    private String time;
    private String pisize;
    private List<String> imglist;
    private String dianzan;
    private boolean dianmei;
    private String pinglun;

    public CommentBean(String id, String nicheng, String touxiang, String neirong, String time, String pisize, List<String> imglist, String dianzan) {
        this.id = id;
        this.nicheng = nicheng;
        this.touxiang = touxiang;
        this.neirong = neirong;
        this.time = time;
        this.pisize = pisize;
        this.imglist = imglist;
        this.dianzan = dianzan;
    }
    public CommentBean(){};
    public CommentBean(String id, String nicheng, String touxiang, String neirong, String time, String pisize, List<String> imglist, String dianzan, String pinglun) {
        this.id = id;
        this.nicheng = nicheng;
        this.touxiang = touxiang;
        this.neirong = neirong;
        this.time = time;
        this.pisize = pisize;
        this.imglist = imglist;
        this.dianzan = dianzan;
        this.pinglun = pinglun;
        this.dianmei=true;
    }

    public boolean isDianmei() {
        return dianmei;
    }

    public void setDianmei(boolean dianmei) {
        this.dianmei = dianmei;
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



    public CommentBean(String id, String nicheng, String touxiang, String neirong, String time, String pisize, List<String> imglist) {
        this.id = id;
        this.nicheng = nicheng;
        this.touxiang = touxiang;
        this.neirong = neirong;
        this.time = time;
        this.pisize = pisize;
        this.imglist = imglist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNicheng() {
        return nicheng;
    }

    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
    }

    public String getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
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

    public String getPisize() {
        return pisize;
    }

    public void setPisize(String pisize) {
        this.pisize = pisize;
    }

    public List<String> getImglist() {
        return imglist;
    }

    public void setImglist(List<String> imglist) {
        this.imglist = imglist;
    }


}
