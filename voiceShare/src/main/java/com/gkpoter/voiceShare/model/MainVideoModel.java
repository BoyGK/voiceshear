package com.gkpoter.voiceShare.model;

import com.gkpoter.voiceShare.bean.User;
import com.gkpoter.voiceShare.bean.Video;

import java.util.List;

/**
 * Created by dy on 2016/10/26.
 */
public class MainVideoModel {

    private List<Video> VideoData;
    private List<User> UserData;

    private String msg;
    private Integer state;

    public List<User> getUserData() {
        return UserData;
    }

    public void setUserData(List<User> userData) {
        UserData = userData;
    }

    public List<Video> getVideoData() {
        return VideoData;
    }

    public void setVideoData(List<Video> videoData) {
        VideoData = videoData;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
