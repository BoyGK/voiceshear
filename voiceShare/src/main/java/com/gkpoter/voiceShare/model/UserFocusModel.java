package com.gkpoter.voiceShare.model;

import com.gkpoter.voiceShare.bean.User;

import java.util.List;

/**
 * Created by dy on 2016/11/2.
 */
public class UserFocusModel {

    private List<User> focus;
    private Integer state;
    private String msg;

    public List<User> getFocus() {
        return focus;
    }

    public void setFocus(List<User> focus) {
        this.focus = focus;
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
