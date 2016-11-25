package com.gkpoter.voiceShare.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.listener.OnTransitionListener;
import com.gkpoter.voiceShare.model.RemarkModel;
import com.gkpoter.voiceShare.service.RemarkService;
import com.gkpoter.voiceShare.service.Service;
import com.gkpoter.voiceShare.ui.Adapter.VideoNewsAdapter;
import com.gkpoter.voiceShare.util.DataUtil;
import com.gkpoter.voiceShare.util.PhotoCut;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

/**
 * Created by dy on 2016/10/20.
 */
public class MainVideoActivity extends Activity {

    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";

    private ImageView top,userImage;
    private TextView userName;
    private PullToRefreshListView newsListview;
    private boolean topState = true;
    private VideoNewsAdapter adapter;
    private RemarkModel remark_data;

    private StandardGSYVideoPlayer videoPlayer;
    OrientationUtils orientationUtils;
    private boolean isTransition;
    private LinearLayout relativeLayout;

    private DataUtil util;
    private DataUtil util_star;

    private TextView upremark;
    private EditText editremark;
    private ImageView imageView;

    private interface AsyncBack{
        public void onBack();
    }
    private AsyncBack call=new AsyncBack() {
        @Override
        public void onBack() {
            adapter = new VideoNewsAdapter(remark_data, getApplicationContext(),newsListview);
            newsListview.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
        init();

        showNews();
    }

    private void play_video() {
        String url = util.getData("video_path","");
        try {
            videoPlayer.setUp(url, true, "");

            //设置返回键
            videoPlayer.getBackButton().setVisibility(View.VISIBLE);

            //设置旋转
            orientationUtils = new OrientationUtils(this, videoPlayer);

            //设置全屏按键功能
            videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    relativeLayout.setVisibility(View.GONE);
                    orientationUtils.resolveByClick();
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relativeLayout.setVisibility(View.GONE);
                    videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            orientationUtils.resolveByClick();
                        }
                    });
                }
            });

            //是否可以滑动调整
            videoPlayer.setIsTouchWiget(true);

            //设置返回按键功能
            videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            //过渡动画
            initTransition();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void view(){
        new photoAsyncTask(userImage).execute(util.getData("user_image",""));
        userName.setText(util.getData("user_name",""));
    }

    private void init() {
        imageView= (ImageView) findViewById(R.id.video_activity_full_video);
        relativeLayout= (LinearLayout) findViewById(R.id.video_visible_);
        util=new DataUtil("video_data",getApplicationContext());
        videoPlayer= (StandardGSYVideoPlayer) findViewById(R.id.video_activity_video);
        play_video();

        userImage= (ImageView) findViewById(R.id.video_main_userImage);
        userName= (TextView) findViewById(R.id.video_main_userName);
        view();

        upremark= (TextView) findViewById(R.id.video_main_up_news);
        editremark= (EditText) findViewById(R.id.video_main_news);
        upremark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Service service=new Service();
                RequestParams params=new RequestParams();
                if("".equals(editremark.getText().toString()+"")){
                    Toast.makeText(MainVideoActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    params.put("UserId",util.getData("user_id",""));
                    params.put("VideoId",util.getData("video_id",""));
                    params.put("RemarkInformation",editremark.getText().toString()+"");
                    Calendar c = Calendar.getInstance();
                    String time=c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);
                    params.put("RemarkTime",time);
                    service.post(getApplicationContext(), "remark", params, new Listener() {
                        @Override
                        public void onSuccess(Object object) {
                            editremark.setText("");
                            Toast.makeText(MainVideoActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            showNews();
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(MainVideoActivity.this, msg+"", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        top= (ImageView) findViewById(R.id.video_main_top);
        util_star=new DataUtil(util.getData("user_name","")+"star",getApplicationContext());
        if("yes".equals(util_star.getData(util.getData("video_id",""),""))){
            top.setBackgroundResource(R.drawable.top2);
            topState=false;
        }
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topState){
                    Service service=new Service();
                    RequestParams params=new RequestParams();
                    params.put("VideoTop","+");
                    params.put("VideoId",util.getData("video_id",""));
                    service.post(getApplicationContext(), "video_top", params, new Listener() {
                        @Override
                        public void onSuccess(Object object) {}
                        @Override
                        public void onError(String msg) {}
                    });
                    util_star.saveData(util.getData("video_id",""),"yes");
                    top.setBackgroundResource(R.drawable.top2);
                    topState=false;
                }else{
                    Service service=new Service();
                    RequestParams params=new RequestParams();
                    params.put("VideoTop","-");
                    params.put("VideoId",util.getData("video_id",""));
                    service.post(getApplicationContext(), "video_top", params, new Listener() {
                        @Override
                        public void onSuccess(Object object) {}
                        @Override
                        public void onError(String msg) {}
                    });
                    util_star.clearData();
                    top.setBackgroundResource(R.drawable.top1);
                    topState=true;
                }
            }
        });

    }

    private void showNews() {
        newsListview= (PullToRefreshListView) findViewById(R.id.video_main_newsList);
        RemarkService service=new RemarkService();
        RequestParams params=new RequestParams();
        params.put("VideoId",util.getData("video_id",""));
        service.get(getApplicationContext(), "getremark", params, new Listener() {
            @Override
            public void onSuccess(Object object) {
                remark_data= (RemarkModel) object;
                call.onBack();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(MainVideoActivity.this, msg+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class photoAsyncTask extends AsyncTask<String,Void,Bitmap> {

        private ImageView imageView;
        public photoAsyncTask(ImageView imageView) {
            this.imageView=imageView;
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
            this.imageView.setImageBitmap(PhotoCut.toRoundBitmap(bitmap));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            finish();
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }


    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(videoPlayer, IMG_TRANSITION);
            addTransitionListener();
            startPostponedEnterTransition();
        } else {
            videoPlayer.startPlayLogic();
//            videoPlayer.startWindowFullscreen(getApplicationContext(),false,false);
        }
    }

    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new OnTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    videoPlayer.startPlayLogic();
//                    videoPlayer.startWindowFullscreen(getApplicationContext(),false,false);
                    transition.removeListener(this);
                }
            });
            return true;
        }
        return false;
    }
}
