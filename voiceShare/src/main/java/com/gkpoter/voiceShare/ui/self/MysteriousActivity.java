package com.gkpoter.voiceShare.ui.self;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.model.Model;
import com.gkpoter.voiceShare.service.Service;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

/**
 * Created by dy on 2016/10/22.
 */
public class MysteriousActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysterious_activity);

        init();
    }

    private void init() {
        findViewById(R.id.self_mysterious_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        webView= (WebView) findViewById(R.id.self_mysterious_webView);
        WebSettings setting=webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        Service service=new Service();
        RequestParams params=new RequestParams();
        service.post(getApplicationContext(), "ad_path", params, new Listener() {
            @Override
            public void onSuccess(Object object) {
                Model model= (Model) object;
                webView.loadUrl(model.getMsg());
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
