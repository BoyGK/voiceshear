package com.gkpoter.voiceShare.bean;

/**
 * Created by dy on 2016/10/27.
 */
public class Remark {
    private String UserName;
    private String UserPhoto;
    private String RemarkInformation;
    private String RemarkTime;

    public String getRemarkInformation() {
        return RemarkInformation;
    }

    public void setRemarkInformation(String remarkInformation) {
        RemarkInformation = remarkInformation;
    }

    public String getRemarkTime() {
        return RemarkTime;
    }

    public void setRemarkTime(String remarkTime) {
        RemarkTime = remarkTime;
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
