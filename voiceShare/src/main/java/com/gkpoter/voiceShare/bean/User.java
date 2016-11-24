package com.gkpoter.voiceShare.bean;

/**
 * Created by dy on 2016/10/25.
 */
public class User {

    private Integer UserId;
    private String UserEmail;
    private String PassWord;
    private String UserName;
    private String UserPhoto;
    private String Signature;
    private String SelfBackgroung;
    private Integer Focus;
    private Boolean IsVip;
    private Integer LogDay;
    private Integer Level;

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public Integer getFocus() {
        return Focus;
    }

    public void setFocus(Integer focus) {
        Focus = focus;
    }

    public Boolean getVip() {
        return IsVip;
    }

    public void setVip(Boolean vip) {
        IsVip = vip;
    }

    public Integer getLevel() {
        return Level;
    }

    public void setLevel(Integer level) {
        Level = level;
    }

    public Integer getLogDay() {
        return LogDay;
    }

    public void setLogDay(Integer logDay) {
        LogDay = logDay;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getSelfBackgroung() {
        return SelfBackgroung;
    }

    public void setSelfBackgroung(String selfBackgroung) {
        SelfBackgroung = selfBackgroung;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }
}
