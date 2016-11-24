package com.gkpoter.voiceShare.service;

import android.content.Context;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.model.UserModel;
import com.gkpoter.voiceShare.util.HttpRequest;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

/**
 * Created by dy on 2016/10/25.
 */
public class LoginService {

    public void post(Context context, String url, RequestParams params, final Listener listener) {
        HttpRequest.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                UserModel model = new Gson().fromJson(new String(bytes), UserModel.class);
                if(new Integer(1).equals(model.getState())){
                    listener.onSuccess(model);
                }else{
                    listener.onError(model.getMsg());
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                listener.onError("网络请求失败");
            }
        });
    }
}
