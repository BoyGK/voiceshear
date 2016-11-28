package com.gkpoter.voiceShare.ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.model.MainVideoModel;

import com.gkpoter.voiceShare.ui.UserActivity;
import com.gkpoter.voiceShare.util.DataUtil;
import com.gkpoter.voiceShare.util.PicassoTransform;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dy on 2016/10/21.
 */
public class TopRightAdapter extends BaseAdapter{
    private MainVideoModel data;
    private Context context;

    public void setData(MainVideoModel data) {
        this.data = data;
    }

    public TopRightAdapter(MainVideoModel data, Context context, ListView listView){
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
            view = LayoutInflater.from(context).inflate(R.layout.listview_adapter_topright,null);
            viewHolder = new ViewHolder();

            viewHolder.imageView= (ImageView) view.findViewById(R.id.top_rightList_video_bg);
            viewHolder.userImage= (ImageView) view.findViewById(R.id.top_rightList_userImage);
            viewHolder.userName= (TextView) view.findViewById(R.id.top_rightList_username);
            viewHolder.num= (TextView) view.findViewById(R.id.top_rightList_num);
            viewHolder.numstate= (TextView) view.findViewById(R.id.top_rightList_num_state);
            viewHolder.layout= (LinearLayout) view.findViewById(R.id.top_rightList_image);
            viewHolder.video_infor= (TextView) view.findViewById(R.id.top_rightList_video_infor);
            viewHolder.video_date= (TextView) view.findViewById(R.id.top_rightList_date);


            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.userName.setText(data.getUserData().get(i).getUserName());
        viewHolder.num.setText(data.getVideoData().get(i).getVideoMonthTop()+"");
        viewHolder.video_date.setText("上传时间 : "+data.getVideoData().get(i).getUpTime());
        viewHolder.video_infor.setText(data.getVideoData().get(i).getVideoInformation());
        if(data.getVideoData().get(i).getStarState()) {
            viewHolder.numstate.setText("+");
        }else{
            viewHolder.numstate.setText("-");
        }

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
        Picasso.with(context).load(data.getUserData().get(i).getUserPhoto()).transform(transformation).into(viewHolder.userImage);
        return view;
    }

    public class ViewHolder{
        public LinearLayout layout;
        public ImageView imageView,userImage;
        public TextView num,userName,numstate,video_infor,video_date;
    }
}
