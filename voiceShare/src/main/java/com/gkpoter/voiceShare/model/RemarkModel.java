package com.gkpoter.voiceShare.model;

import com.gkpoter.voiceShare.bean.Remark;

import java.util.List;

/**
 * Created by dy on 2016/10/27.
 */
public class RemarkModel {
    private List<Remark> RemarkData;

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

    public List<Remark> getRemarkData() {
        return RemarkData;
    }

    public void setRemarkData(List<Remark> remarkData) {
        RemarkData = remarkData;
    }
}
