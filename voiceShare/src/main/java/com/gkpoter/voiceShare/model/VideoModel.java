package com.gkpoter.voiceShare.model;

import com.gkpoter.voiceShare.bean.Video;

import java.util.List;

/**
 * Created by dy on 2016/11/3.
 */
public class VideoModel {

    private List<Video> videoData;
    private String msg;
    private Integer state;

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

    public List<Video> getVideoData() {
        return videoData;
    }

    public void setVideoData(List<Video> videoData) {
        this.videoData = videoData;
    }
}
