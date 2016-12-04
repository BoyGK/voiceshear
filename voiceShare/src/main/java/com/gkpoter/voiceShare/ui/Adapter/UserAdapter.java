package com.gkpoter.voiceShare.ui.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.model.VideoModel;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by dy on 2016/10/21.
 */
public class UserAdapter extends BaseAdapter{
    private VideoModel data;
    private Context context;

    public UserAdapter(VideoModel data, Context context){
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.listview_adapter_userself,null);
            viewHolder = new ViewHolder();
            viewHolder.image_bg= (ImageView) view.findViewById(R.id.list_userself_video_bg);
            viewHolder.star_num= (TextView) view.findViewById(R.id.list_userself_video_num);
            viewHolder.star_state= (TextView) view.findViewById(R.id.list_userself_video_state);
            viewHolder.video_title= (TextView) view.findViewById(R.id.user_video_title);
            viewHolder.video_time= (TextView) view.findViewById(R.id.user_video_time);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Timer timer = new Timer();
        final ViewHolder finalViewHolder1 = viewHolder;
        final int _i_=i;
        final Handler myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1) {
                    if(finalViewHolder1.image_bg.getWidth()!=0) {
                        Picasso.with(context).load(data.getVideoData().get(_i_).getImagePath())
                                .resize(finalViewHolder1.image_bg.getWidth(),finalViewHolder1.image_bg.getHeight()).into(finalViewHolder1.image_bg);
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

        viewHolder.video_title.setText(" "+data.getVideoData().get(i).getVideoInformation());
        viewHolder.video_time.setText(data.getVideoData().get(i).getUpTime());
        viewHolder.star_num.setText(data.getVideoData().get(i).getVideoYearTop()+"");
        if(data.getVideoData().get(i).getStarState()) {
            viewHolder.star_state.setText("+");
        }else{
            viewHolder.star_state.setText("-");
        }
        return view;
    }

    public class ViewHolder{
        public ImageView image_bg;
        public TextView star_num,star_state,video_title,video_time;
    }
}
