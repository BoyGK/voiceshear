package com.gkpoter.voiceShare.ui.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.model.RemarkModel;

import com.gkpoter.voiceShare.util.PhotoCut;
import com.gkpoter.voiceShare.util.PicassoTransform;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dy on 2016/10/20.
 */
public class VideoNewsAdapter extends BaseAdapter{
    private RemarkModel remark_data;
    private Context context;

    public VideoNewsAdapter(RemarkModel remark_data, Context context, PullToRefreshListView newsListview){
        this.remark_data=remark_data;
        this.context=context;

    }

    @Override
    public int getCount() {
        return remark_data.getRemarkData().size();
    }

    @Override
    public Object getItem(int i) {
        return remark_data.getRemarkData().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.video_newslist,null);
            viewHolder = new ViewHolder();

            viewHolder.userImage= (ImageView) view.findViewById(R.id.video_news_userImage);
            viewHolder.videoinfor= (TextView) view.findViewById(R.id.video_news_information);
            viewHolder.userName= (TextView) view.findViewById(R.id.video_news_userName);
            viewHolder.uptime= (TextView) view.findViewById(R.id.video_news_video_uptime);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Transformation transformation=new PicassoTransform();
        Picasso.with(context).load(remark_data.getRemarkData().get(i).getUserPhoto()).transform(transformation).into(viewHolder.userImage);
        viewHolder.videoinfor.setText("     "+remark_data.getRemarkData().get(i).getRemarkInformation());
        viewHolder.userName.setText(remark_data.getRemarkData().get(i).getUserName());
        viewHolder.uptime.setText(remark_data.getRemarkData().get(i).getRemarkTime());
        return view;
    }

    public class ViewHolder{
        public ImageView userImage;
        public TextView videoinfor,userName,uptime;
    }
}
