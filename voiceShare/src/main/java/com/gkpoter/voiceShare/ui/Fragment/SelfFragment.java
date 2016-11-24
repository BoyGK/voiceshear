package com.gkpoter.voiceShare.ui.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.listener.RefashListener;
import com.gkpoter.voiceShare.model.Model;

import com.gkpoter.voiceShare.service.Service;
import com.gkpoter.voiceShare.ui.UserActivity;
import com.gkpoter.voiceShare.ui.self.*;
import com.gkpoter.voiceShare.util.DataUtil;
import com.gkpoter.voiceShare.util.HttpRequest;
import com.gkpoter.voiceShare.util.PictureUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by dy on 2016/10/19.
 */
public class SelfFragment extends Fragment implements OnClickListener {

    private ProgressBar progressBar;
    private RelativeLayout layout, layout_show;
    private ImageView userImage;
    private TextView userName,level,signature;
    public static RefashListener signRefsh;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout,relativeLayout_bg;
    private EditText video_title;
    private Button quit,ok;

    private RefashListener refashListener=new RefashListener() {
        @Override
        public void back() {
            DataUtil util = new DataUtil("user", getActivity());
            if("".equals(util.getData("user_signature", ""))){
                signature.setText("点击编辑个性签名...");
            }else{
                signature.setText("        "+util.getData("user_signature", ""));
            }
            signRefsh.back();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        linearLayout= (LinearLayout) getView().findViewById(R.id.up_video_LinearLayout);
        relativeLayout= (RelativeLayout) getView().findViewById(R.id.up_video_RelativeLayout);
        relativeLayout_bg= (RelativeLayout) getView().findViewById(R.id.up_video_bg);
        video_title= (EditText) getView().findViewById(R.id.up_video_edit_title);
        quit= (Button) getView().findViewById(R.id.up_video_quit);
        ok= (Button) getView().findViewById(R.id.up_video_ok);
        getView().findViewById(R.id.self_user_main).setOnClickListener(this);
        getView().findViewById(R.id.self_to_about).setOnClickListener(this);
        getView().findViewById(R.id.self_information).setOnClickListener(this);
        getView().findViewById(R.id.self_mysterious_).setOnClickListener(this);
        getView().findViewById(R.id.self_news_Back_saying).setOnClickListener(this);
        getView().findViewById(R.id.self_setting).setOnClickListener(this);
        getView().findViewById(R.id.self_up_uservideo).setOnClickListener(this);
        getView().findViewById(R.id.self_user_sign_day).setOnClickListener(this);
        getView().findViewById(R.id.self_user_image).setOnClickListener(this);
        getView().findViewById(R.id.self_user_signature).setOnClickListener(this);
        progressBar = (ProgressBar) getView().findViewById(R.id.up_uservideo_progressbar);
        layout = (RelativeLayout) getView().findViewById(R.id.self_up_uservideo_progressbar);
        layout_show = (RelativeLayout) getView().findViewById(R.id.self_up_uservideo);
        userImage = (ImageView) getView().findViewById(R.id.self_user_image);
        userName = (TextView) getView().findViewById(R.id.self_user_Name);
        level= (TextView) getView().findViewById(R.id.self_user_level);
        signature= (TextView) getView().findViewById(R.id.self_user_signature);
        final DataUtil util = new DataUtil("user", getActivity());
        userName.setText(util.getData("user_name", ""));
        level.setText("Lv "+util.getData("user_level","0"));
        if("".equals(util.getData("user_signature", ""))){
            signature.setText("点击编辑个性签名...");
        }else{
            signature.setText("        "+util.getData("user_signature", ""));
        }
        SignatureActivity.refashListener=refashListener;
        PictureUtil pictureUtil = new PictureUtil();
        Bitmap bitmap = pictureUtil.getPicture(Environment.getExternalStorageDirectory().getPath()+"/voiceshare", util.getData("user_name", "")+"_voiceShare");
        if (bitmap == null) {
            final Timer timer = new Timer();
            final Handler myHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 1) {
                        if(userImage.getWidth()!=0) {
                            Picasso.with(getContext()).load(util.getData("user_photo",""))
                                    .resize(userImage.getWidth(), userImage.getHeight()).into(userImage);
                            timer.cancel();
                        }
                    }
                }
            };
            TimerTask task = new TimerTask(){
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    myHandler.sendMessage(message);
                }
            };
            //延迟每次延迟10 毫秒 隔1秒执行一次
            timer.schedule(task,10,100);
        } else {
            BitmapDrawable bd= new BitmapDrawable(bitmap);
            userImage.setBackground(bd);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.self_user_main:
                DataUtil util = new DataUtil("user", getActivity());
                DataUtil util_ = new DataUtil("user_focus", getActivity());
                util_.clearData();
                util_.saveData("user_id", util.getData("user_id", "") + "");
                util_.saveData("user_name", util.getData("user_name", "") + "");
                util_.saveData("user_photo", util.getData("user_photo", "") + "");
                util_.saveData("user_signature", util.getData("user_signature", "") + "");
                util_.saveData("user_selfbackgroung", util.getData("user_selfbackgroung", "") + "");
                util_.saveData("user_focus", util.getData("user_focus", "") + "");
                util_.saveData("user_vip", util.getData("user_vip", "") + "");
                util_.saveData("user_logday", util.getData("user_logday", "") + "");
                util_.saveData("user_level", util.getData("user_level", "") + "");
                startActivity(new Intent(getActivity(), UserActivity.class));
                break;
            case R.id.self_to_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.self_information:
                startActivity(new Intent(getActivity(), InformationActivity.class));
                break;
            case R.id.self_mysterious_:
                startActivity(new Intent(getActivity(), MysteriousActivity.class));
                break;
            case R.id.self_news_Back_saying:
                startActivity(new Intent(getActivity(), NewsBackActivity.class));
                break;
            case R.id.self_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.self_up_uservideo:
                upVideo();
                layout_show.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                break;
            case R.id.self_user_sign_day:sign();
                break;
            case R.id.self_user_image:
                changeUserPicture();
                break;
            case R.id.self_user_signature:
                signature_();
                break;
        }
    }

    private void signature_() {
        Intent intent = new Intent(getActivity(),SignatureActivity.class);
        startActivity(intent);
    }

    private void changeUserPicture() {
        final DataUtil util = new DataUtil("user", getActivity());
        final PictureUtil pictureUtil = new PictureUtil();
        final Bitmap[] bitmap = new Bitmap[1];
        final File[] file = new File[1];
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
                file[0] =new File(resultList.get(0).getPhotoPath());
                bitmap[0] = BitmapFactory.decodeFile(resultList.get(0).getPhotoPath());
                userImage.setImageBitmap(bitmap[0]);
                pictureUtil.savePicture(bitmap[0],Environment.getExternalStorageDirectory().getPath()+"/voiceshare",util.getData("user_name","")+"_voiceShare");
                Service mServes=new Service();
                RequestParams params=new RequestParams();
                params.put("UserId",util.getData("user_id",""));
                try {
                    params.put("UserImage",file[0]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mServes.post(getContext(), "change_picture", params, new Listener() {
                    @Override
                    public void onSuccess(Object object) {
                        Toast.makeText(getActivity(), "更改成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(getActivity(),msg + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

    private void sign() {
        final DataUtil util_=new DataUtil("user_",getActivity());
        DataUtil util=new DataUtil("user",getActivity());
        Calendar calendar=Calendar.getInstance();
        final String day = calendar.get(Calendar.DAY_OF_MONTH)+"";
        if(day.equals(util_.getData("day_",""))){
            Toast.makeText(getActivity(), "今天已经签到！", Toast.LENGTH_SHORT).show();
        }else{
            Service service=new Service();
            RequestParams params=new RequestParams();
            params.put("UserId",util.getData("user_id",""));
            service.post(getActivity(), "log_day", params, new Listener() {
                @Override
                public void onSuccess(Object object) {
                    util_.saveData("day_",day);
                    Toast.makeText(getActivity(), "签到成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String msg) {
                    Toast.makeText(getActivity(),msg + "", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private final int IMAGE_CODE = 1;

    private void upVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "选择视频"), IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != NewsBackActivity.RESULT_OK) {
            layout_show.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            return;
        }
        final Uri uri = data.getData();
        if (requestCode == IMAGE_CODE) {
            try {
                relativeLayout_bg.setVisibility(View.VISIBLE);
                relativeLayout_bg.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        relativeLayout.setVisibility(View.GONE);
                        relativeLayout_bg.setVisibility(View.GONE);
                        layout_show.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.GONE);
                    }
                });
                relativeLayout.setVisibility(View.VISIBLE);
                ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!"".equals(video_title.getText().toString()+"")){
                            Video(video_title.getText().toString()+"",uri);
                            relativeLayout.setVisibility(View.GONE);
                            relativeLayout_bg.setVisibility(View.GONE);
                            video_title.setText("");
                        }
                    }
                });
                quit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        video_title.setText("");
                        relativeLayout.setVisibility(View.GONE);
                        relativeLayout_bg.setVisibility(View.GONE);
                        layout_show.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.GONE);
                    }
                });
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        }
    }
    private void Video(String title,Uri uri){
        File file = null;
        try {
            file = new File(new URI(uri.toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        MediaMetadataRetriever metadataRetriever=new MediaMetadataRetriever();
        metadataRetriever.setDataSource(getContext(),uri);
        Bitmap bitmap=metadataRetriever.getFrameAtTime();
        PictureUtil pictureUtil=new PictureUtil();
        pictureUtil.savePicture(bitmap,Environment.getExternalStorageDirectory().getPath()
                +"/voiceshare","video_image_only.jpg");
        File file1=new File(Environment.getExternalStorageDirectory().getPath()
                +"/voiceshare/video_image_only.jpg");
        RequestParams params = new RequestParams();
        DataUtil util = new DataUtil("user", getActivity());
        try {
            params.put("UserVideo", file);
            params.put("VideoImage",file1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("UserId", util.getData("user_id", ""));
        params.put("VideoInformation",title);
        HttpRequest.post(getActivity(), "up_video", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    Model model = new Gson().fromJson(new String(bytes), Model.class);
                    if (model.getState() == 1) {
                        Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), model.getMsg() + "", Toast.LENGTH_SHORT).show();
                        getView().findViewById(R.id.self_up_uservideo).setVisibility(View.VISIBLE);
                        layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                Log.d("information", count + "");
                progressBar.setProgress(count);
                if (count == 100) {
                    progressBar.setProgress(0);
                    layout_show.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                }
            }
        });
    }
}
