package com.gkpoter.voiceShare.service;

import android.content.Context;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.model.MainVideoModel;
import com.gkpoter.voiceShare.util.HttpRequest;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

/**
 * Created by dy on 2016/10/26.
 */
public class MainVideoService {

    public void get(Context context, String url, RequestParams params, final Listener listener) {
        HttpRequest.get(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    MainVideoModel model = new Gson().fromJson(new String(bytes), MainVideoModel.class);
                    if (new Integer(1).equals(model.getState())) {
                        listener.onSuccess(model);
                    } else {
                        listener.onError(model.getMsg());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                listener.onError("网络请求失败");
            }
        });
    }
}
