package com.gkpoter.voiceShare.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.listener.RefashListener;
import com.gkpoter.voiceShare.model.VideoModel;
import com.gkpoter.voiceShare.service.Service;
import com.gkpoter.voiceShare.service.VideoService;
import com.gkpoter.voiceShare.ui.Adapter.UserAdapter;
import com.gkpoter.voiceShare.util.DataUtil;
import com.gkpoter.voiceShare.util.PhotoCut;
import com.gkpoter.voiceShare.util.PictureUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dy on 2016/10/21.
 */
public class UserActivity extends Activity {

    private ImageView backtoCollects;
    private ImageView image_bg,image_user;
    private TextView focus,say;
    private VideoModel data;
    private PullToRefreshListView listView;
    private UserAdapter adapter;
    private Button bt;

    public static RefashListener refashListener;

    private DataUtil util;
    private PictureUtil pictureUtil;
    private boolean FOCUS_STATE=false;

    private interface CallBack{
        public void back();
    }
    private CallBack call=new CallBack() {
        @Override
        public void back() {
            adapter=new UserAdapter(data,getApplication(),listView);
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        init();
        backCollects();
        getData();
    }

    private void backCollects() {
        backtoCollects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
        say= (TextView) findViewById(R.id.userself_user_say);
        image_bg= (ImageView) findViewById(R.id.userself_bg_image);
        image_user= (ImageView) findViewById(R.id.userself_user_image);
        focus= (TextView) findViewById(R.id.userself_user_focuse);
        bt= (Button) findViewById(R.id.userself_user_focuse_bt);
        initView();

        backtoCollects= (ImageView) findViewById(R.id.user_main_back);
        listView= (PullToRefreshListView) findViewById(R.id.user_main_listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i--;
                DataUtil util=new DataUtil("video_data",getApplication());
                DataUtil util_=new DataUtil("user_focus",getApplicationContext());
                util.clearData();
                util.saveData("video_path",data.getVideoData().get(i).getVideoPath());
                util.saveData("video_id",data.getVideoData().get(i).getVideoId()+"");
                util.saveData("user_id",util_.getData("user_id",""));
                util.saveData("user_image",util_.getData("user_photo",""));
                util.saveData("user_name",util_.getData("user_name",""));
                startActivity(new Intent(getApplicationContext(),MainVideoActivity.class));
            }
        });
    }

    private void initView() {
        util=new DataUtil("user_focus",getApplication());
        final DataUtil util_=new DataUtil("user",getApplication());
        if(util.getData("user_id","").equals(util_.getData("user_id",""))){
            bt.setVisibility(View.GONE);
            say.setClickable(false);
            if("".equals(util.getData("user_signature", ""))){
                say.setText("点击编辑个性签名...");
            }else{
                say.setText("        "+util.getData("user_signature", ""));
            }
        }else{
            if("".equals(util.getData("user_signature", ""))){
                say.setText("这个人很懒,什么都没说哦...");
            }else{
                say.setText("        "+util.getData("user_signature", ""));
            }
        }
        Service service=new Service();
        RequestParams params=new RequestParams();
        params.put("UserId",util.getData("user_id",""));
        params.put("FocusId",util_.getData("user_id",""));
        service.post(getApplicationContext(), "fState", params, new Listener() {
            @Override
            public void onSuccess(Object object) {
                bt.setText("取消关注");
                FOCUS_STATE=true;
            }

            @Override
            public void onError(String msg) {
                bt.setText("关注");
                FOCUS_STATE=false;
                //Toast.makeText(UserActivity.this,msg+ "", Toast.LENGTH_SHORT).show();
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Service service=new Service();
                RequestParams params_=new RequestParams();
                params_.put("UserId",util.getData("user_id",""));
                params_.put("FocusId",util_.getData("user_id",""));
                if(FOCUS_STATE){
                    params_.put("Focus","F");
                    service.post(getApplicationContext(), "create_f", params_, new Listener() {
                        @Override
                        public void onSuccess(Object object) {
                            bt.setText("关注");
                            FOCUS_STATE=false;
                            refashListener.back();
                            //getData();
                        }

                        @Override
                        public void onError(String msg) {
                            bt.setText("取消关注");
                            FOCUS_STATE=true;
                            Toast.makeText(UserActivity.this,msg+ "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    params_.put("Focus","T");
                    service.post(getApplicationContext(), "create_f", params_, new Listener() {
                        @Override
                        public void onSuccess(Object object) {
                            bt.setText("取消关注");
                            FOCUS_STATE=true;
                            refashListener.back();
                            //getData();
                        }

                        @Override
                        public void onError(String msg) {
                            bt.setText("关注");
                            FOCUS_STATE=false;
                            Toast.makeText(UserActivity.this,msg+ "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        pictureUtil = new PictureUtil();
        Bitmap bitmap = pictureUtil.getPicture(Environment.getExternalStorageDirectory().getPath()+"/voiceshare", util.getData("user_name", "")+"_voiceShare");
        if (bitmap == null) {
            new photoAsyncTask(image_user,"_voiceShare").execute(util.getData("user_photo", ""));
        } else {
            BitmapDrawable bd= new BitmapDrawable(bitmap);
            image_user.setBackground(bd);
        }
        Bitmap bitmap_bg = pictureUtil.getPicture(Environment.getExternalStorageDirectory().getPath()+"/voiceshare", util.getData("user_name", "")+"_voiceShare_bg");
        if (bitmap_bg == null) {
            new photoAsyncTask(image_bg,"_voiceShare_bg").execute(util.getData("user_selfbackgroung", ""));
        } else {
            BitmapDrawable bd= new BitmapDrawable(bitmap_bg);
            image_bg.setBackground(bd);
        }
        focus.setText(util.getData("user_focus","")+" 人已关注");
    }

    public void getData(){
        VideoService service=new VideoService();
        RequestParams params=new RequestParams();
        params.put("UserId",util.getData("user_id",""));
        service.post(getApplicationContext(), "user_video", params, new Listener() {
            @Override
            public void onSuccess(Object object) {
                data = (VideoModel) object;
                call.back();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(getApplicationContext(),msg + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class photoAsyncTask extends AsyncTask<String,Void,Bitmap> {

        private ImageView imageView;
        private boolean key;
        private String name;
        public photoAsyncTask(ImageView imageView,String name) {
            this.imageView=imageView;
            this.key=false;
            this.name=name;
        }
        public photoAsyncTask(ImageView imageView,boolean key) {
            this.imageView=imageView;
            this.key=key;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url=strings[0];
            Bitmap bitmap=null;
            URLConnection connection;
            InputStream inputStream;
            try {
                connection=new URL(url).openConnection();
                inputStream=connection.getInputStream();

                bitmap= BitmapFactory.decodeStream(inputStream);

                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            File file=new File(Environment.getExternalStorageDirectory().getPath()+"/voiceshare");
            if(!file.isFile()){
                try {
                    file.mkdirs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            pictureUtil.savePicture(bitmap, Environment.getExternalStorageDirectory().getPath()+"/voiceshare",util.getData("user_name","")+name);
            if(key) {
                this.imageView.setImageBitmap(PhotoCut.toRoundBitmap(bitmap));
            }else{
                BitmapDrawable bd= new BitmapDrawable(bitmap);
                this.imageView.setBackground(bd);
            }
        }
    }
}
