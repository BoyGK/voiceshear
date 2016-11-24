package com.gkpoter.voiceShare.bean;

/**
 * Created by dy on 2016/10/26.
 */
public class Video {

    private Integer VideoId;
    private Integer UserId;
    private String ImagePath;
    private String VideoPath;
    private Integer VideoYearTop;
    private Integer VideoMonthTop;
    private Boolean StarState;
    private String UpTime;
    private String VideoInformation;

    public Integer getVideoId() {
        return VideoId;
    }

    public void setVideoId(Integer videoId) {
        VideoId = videoId;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public Boolean getStarState() {
        return StarState;
    }

    public void setStarState(Boolean starState) {
        StarState = starState;
    }

    public String getUpTime() {
        return UpTime;
    }

    public void setUpTime(String upTime) {
        UpTime = upTime;
    }

    public String getVideoInformation() {
        return VideoInformation;
    }

    public void setVideoInformation(String videoInformation) {
        VideoInformation = videoInformation;
    }

    public Integer getVideoMonthTop() {
        return VideoMonthTop;
    }

    public void setVideoMonthTop(Integer videoMonthTop) {
        VideoMonthTop = videoMonthTop;
    }

    public String getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(String videoPath) {
        VideoPath = videoPath;
    }

    public Integer getVideoYearTop() {
        return VideoYearTop;
    }

    public void setVideoYearTop(Integer videoYearTop) {
        VideoYearTop = videoYearTop;
    }
}
