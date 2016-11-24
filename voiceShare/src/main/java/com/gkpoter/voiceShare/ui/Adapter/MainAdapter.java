package com.gkpoter.voiceShare.ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.model.MainVideoModel;
import com.gkpoter.voiceShare.ui.MainVideoActivity;
import com.gkpoter.voiceShare.ui.UserActivity;
import com.gkpoter.voiceShare.util.DataUtil;
import com.gkpoter.voiceShare.util.PicassoTransform;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dy on 2016/10/19.
 */
public class MainAdapter extends BaseAdapter{

    private int mStart, mEnd;
    private MainVideoModel data;
    private Context context;
    public boolean mFirstIn;
    OrientationUtils orientationUtils;

    public void setData(MainVideoModel data) {
        this.data = data;
    }

    public MainAdapter(MainVideoModel data, Context context, PullToRefreshListView listView){
        this.data=data;
        this.context=context;
    }

    @Override
    public int getCount() {
        return data.getVideoData().size();
    }

    @Override
    public Object getItem(int i) {
        return data.getVideoData().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.listview_adapter_main,null);
            viewHolder = new ViewHolder();

            viewHolder.imageView= (ImageView) view.findViewById(R.id.video_main);
            viewHolder.videoTitle= (TextView) view.findViewById(R.id.video_title);
            viewHolder.userImage= (ImageView) view.findViewById(R.id.main_data_userImage);
            viewHolder.userName= (TextView) view.findViewById(R.id.main_data_userName);
            viewHolder.layout= (RelativeLayout) view.findViewById(R.id.video_layout);
//            viewHolder.videoView= (VideoView) view.findViewById(R.id.video_main_);
            viewHolder.videodate= (TextView) view.findViewById(R.id.main_data_date);
            viewHolder.videoPlayer= (StandardGSYVideoPlayer) view.findViewById(R.id.video_main_);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.videoPlayer.onVideoPause();
            viewHolder.layout.setVisibility(View.VISIBLE);
            viewHolder.videoPlayer.setVisibility(View.GONE);
        }
        touchClick(view,i);

        viewHolder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataUtil util_=new DataUtil("user_focus",context);
                util_.clearData();
                util_.saveData("user_id",data.getUserData().get(i).getUserId()+"");
                util_.saveData("user_name",data.getUserData().get(i).getUserName()+"");
                util_.saveData("user_photo",data.getUserData().get(i).getUserPhoto()+"");
                util_.saveData("user_signature",data.getUserData().get(i).getSignature()+"");
                util_.saveData("user_selfbackgroung",data.getUserData().get(i).getSelfBackgroung()+"");
                util_.saveData("user_focus",data.getUserData().get(i).getFocus()+"");
                util_.saveData("user_vip",data.getUserData().get(i).getVip()+"");
                util_.saveData("user_logday",data.getUserData().get(i).getLogDay()+"");
                util_.saveData("user_level",data.getUserData().get(i).getLevel()+"");
                context.startActivity(new Intent(context, UserActivity.class));
            }
        });

        final Timer timer = new Timer();
        final ViewHolder finalViewHolder1 = viewHolder;
        final Handler myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1) {
                    if(finalViewHolder1.imageView.getWidth()!=0) {
                        Picasso.with(context).load(data.getVideoData().get(i).getImagePath()+"")
                                .resize(finalViewHolder1.imageView.getWidth(), finalViewHolder1.imageView.getHeight()).into(finalViewHolder1.imageView);
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

        Transformation transformation=new PicassoTransform();
        Picasso.with(context).load(data.getUserData().get(i).getUserPhoto()+"").transform(transformation).into(viewHolder.userImage);

        viewHolder.videoTitle.setText(data.getVideoData().get(i).getVideoInformation());
        viewHolder.userName.setText(data.getUserData().get(i).getUserName());
        viewHolder.videodate.setText("上传时间 : "+data.getVideoData().get(i).getUpTime());

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalViewHolder.layout.setVisibility(View.GONE);
                finalViewHolder.videoPlayer.setVisibility(View.VISIBLE);
                String url = data.getVideoData().get(i).getVideoPath();
                finalViewHolder.videoPlayer.setUp(url, true, "");
                //设置返回键
                finalViewHolder.videoPlayer.getBackButton().setVisibility(View.VISIBLE);
                //是否可以滑动调整
                finalViewHolder.videoPlayer.setIsTouchWiget(true);
                finalViewHolder.videoPlayer.startPlayLogic();
            }
        });

        return view;
    }

    private void touchClick(View view, final int i){
        view.findViewById(R.id.news_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataUtil util=new DataUtil("video_data",context);
                util.clearData();
                util.saveData("video_path",data.getVideoData().get(i).getVideoPath());
                util.saveData("video_id",data.getVideoData().get(i).getVideoId()+"");
                util.saveData("user_id",data.getUserData().get(i).getUserId()+"");
                util.saveData("user_image",data.getUserData().get(i).getUserPhoto());
                util.saveData("user_name",data.getUserData().get(i).getUserName());
                context.startActivity(new Intent(context,MainVideoActivity.class));
            }
        });
    }

    public class ViewHolder{
        public ImageView imageView,userImage;
        public TextView videoTitle,userName,videodate;
        public RelativeLayout layout;
        //public VideoView videoView;
        public StandardGSYVideoPlayer videoPlayer;
    }

}
