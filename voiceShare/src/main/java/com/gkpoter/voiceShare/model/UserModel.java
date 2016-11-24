package com.gkpoter.voiceShare.model;

import com.gkpoter.voiceShare.bean.User;

/**
 * Created by dy on 2016/10/25.
 */
public class UserModel extends User {
    private Integer state;
    private String msg;

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
