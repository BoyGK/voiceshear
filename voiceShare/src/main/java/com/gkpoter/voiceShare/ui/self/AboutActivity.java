package com.gkpoter.voiceShare.ui.self;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import com.gkpoter.voiceShare.R;

/**
 * Created by dy on 2016/10/22.
 */
public class AboutActivity extends Activity {

    private ViewFlipper viewFlipper;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        init();

    }

    private void init() {

        viewFlipper= (ViewFlipper) findViewById(R.id.about_image_bg);

        ImageView imageView1=new ImageView(AboutActivity.this);
        ImageView imageView2=new ImageView(AboutActivity.this);
        imageView1.setBackgroundResource(R.drawable.about_1);
        imageView2.setBackgroundResource(R.drawable.about_2);
        viewFlipper.addView(imageView1);
        viewFlipper.addView(imageView2);
        nextView();

        back= (ImageView) findViewById(R.id.about_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x9527) {
                viewFlipper.showNext();//向右滑动
            }
        }
    };
    public void nextView(){
        new Thread(){
            @Override
            public void run() {
                try {
                    while(true){
                        Thread.sleep(3000);
                        handler.sendEmptyMessage(0x9527);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
