package com.gkpoter.voiceShare.ui.self;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.ui.LoginActivity;
import com.gkpoter.voiceShare.util.DataUtil;
import com.gkpoter.voiceShare.util.FinishListActivity;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by dy on 2016/10/22.
 */
public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        FinishListActivity.getInstance().addActivity(this);

        init();
    }

    private void init() {

        final TextView textView= (TextView) findViewById(R.id.delete_text);
        findViewById(R.id.setting_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.setting_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataUtil util=new DataUtil("userlogin",getApplicationContext());
                util.clearData();
                FinishListActivity.getInstance().exit();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            }
        });
        File file= new File(Environment.getExternalStorageDirectory().getPath()+"/voiceshare");
        if (file.exists()){
            File[] files = file.listFiles();
            double num = 0;
            for (int i = 0; i < files.length; i++) {
                num += files[i].length();
            }
            DecimalFormat df = new DecimalFormat("0.00");
            textView.setText(df.format(Double.parseDouble(String.valueOf(num/1024/1024))) + " M");
        }else{
            textView.setText("0.0 M");
        }
        findViewById(R.id.delete_).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f= new File(Environment.getExternalStorageDirectory().getPath()+"/voiceshare");
                File[] files = f.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
                textView.setText("0.0 M");
            }
        });
    }
}
