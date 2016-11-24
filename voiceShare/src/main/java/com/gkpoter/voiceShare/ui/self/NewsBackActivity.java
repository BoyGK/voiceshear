package com.gkpoter.voiceShare.ui.self;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.model.Model;
import com.gkpoter.voiceShare.util.DataUtil;
import com.gkpoter.voiceShare.util.HttpRequest;
import com.gkpoter.voiceShare.util.PictureUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by dy on 2016/10/22.
 */
public class NewsBackActivity extends Activity {


    private ImageView image;
    private EditText bug_text;
    private TextView up_bug;
    private Bitmap bitmap;
    private ProgressBar progress;
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsback_activity);

        init();
        showImage();
        upBug();

    }

    private void init() {
        bug_text= (EditText) findViewById(R.id.bug_text);
        up_bug= (TextView) findViewById(R.id.bug_up);
        image= (ImageView) findViewById(R.id.self_news_Back_upPhoto);
        progress= (ProgressBar) findViewById(R.id.bug_progress);
        findViewById(R.id.self_news_Back_).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 回调图片
     */
    private final int IMAGE_CODE=1;
    private void showImage() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FunctionConfig functionConfig = new FunctionConfig.Builder()
                        .setEnableEdit(true)//开启编辑功能
                        .setEnableCrop(true)//开启裁剪功能
                        .setEnableRotate(true)//开启旋转功能
                        .setEnableCamera(true)//开启相机功能
                        .setCropWidth(400)//裁剪宽度
                        .setCropHeight(400)//裁剪高度
                        .setCropSquare(true)//裁剪正方形
                        .setEnablePreview(true)//是否开启预览功能
                        .build();

                GalleryFinal.openGallerySingle(IMAGE_CODE,functionConfig, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        file=new File(resultList.get(0).getPhotoPath());
                        bitmap = BitmapFactory.decodeFile(resultList.get(0).getPhotoPath());
                        image.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });
            }
        });
    }

    private void upBug(){
        final DataUtil util=new DataUtil("user",getApplicationContext());
        up_bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                RequestParams params=new RequestParams();
                try {
                    params.put("BugImage", file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                params.put("BugText", bug_text.getText().toString() + "");
                params.put("UserId", util.getData("user_id",""));
                HttpRequest.post(getApplication(), "up_bug", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        Model model=new Gson().fromJson(new String(bytes),Model.class);
                        if(new Integer(1).equals(model.getState())){
                            bug_text.setText("");
                            image.setImageBitmap(null);
                            progress.setVisibility(View.GONE);
                            Toast.makeText(NewsBackActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
                        }else{
                            bug_text.setText("");
                            image.setImageBitmap(null);
                            progress.setVisibility(View.GONE);
                            Toast.makeText(NewsBackActivity.this,model.getMsg() + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        Toast.makeText(NewsBackActivity.this,"网络请求失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                        super.onProgress(bytesWritten, totalSize);
                        int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                        Log.d("information", count+"");
                        progress.setProgress(count);
                    }
                });

            }
        });
    }
}
