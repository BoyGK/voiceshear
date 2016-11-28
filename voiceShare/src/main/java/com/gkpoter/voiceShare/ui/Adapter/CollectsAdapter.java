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
import android.widget.*;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.model.UserFocusModel;
import com.gkpoter.voiceShare.model.UserModel;
import com.gkpoter.voiceShare.util.DataUtil;
import com.gkpoter.voiceShare.util.PhotoCut;
import com.gkpoter.voiceShare.util.PictureUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dy on 2016/10/21.
 */
public class CollectsAdapter extends BaseAdapter {
    private UserFocusModel data;

    public void setData(UserFocusModel data) {
        this.data = data;
    }

    private Context context;
    private PictureUtil pictureUtil;
    private DataUtil util;

    public CollectsAdapter(UserFocusModel data, Context context){
        this.data=data;
        this.context=context;
    }

    @Override
    public int getCount() {
        return data.getFocus().size();
    }

    @Override
    public Object getItem(int i) {
        return data.getFocus().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //util = new DataUtil("user_focus", context);
        pictureUtil = new PictureUtil();
        ViewHolder viewHolder = null;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.listview_adapter_collects,null);
            viewHolder = new ViewHolder();
            viewHolder.userImage= (ImageView) view.findViewById(R.id.collects_listview_userImage);
            viewHolder.userName= (TextView) view.findViewById(R.id.collects_listview_userName);
            viewHolder.usersign= (TextView) view.findViewById(R.id.collects_listview_userSign);
            viewHolder.userfocus= (TextView) view.findViewById(R.id.collects_listview_userFocus);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Timer timer = new Timer();
        final ViewHolder finalViewHolder1 = viewHolder;
        final int i_=i;
        final Handler myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1) {
                    if(finalViewHolder1.userImage.getWidth()!=0) {
                        Picasso.with(context).load(data.getFocus().get(i_).getUserPhoto()+"")
                                .resize(finalViewHolder1.userImage.getWidth(), finalViewHolder1.userImage.getHeight()).into(finalViewHolder1.userImage);
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
        viewHolder.userName.setText(data.getFocus().get(i).getUserName());
        viewHolder.userfocus.setText(data.getFocus().get(i).getFocus()+" 人已关注");
        if("".equals(data.getFocus().get(i).getSignature()+"")){
            viewHolder.usersign.setText("这个人很懒哦,什么都没说...");
        }else {
            viewHolder.usersign.setText(data.getFocus().get(i).getSignature());
        }
        return view;
    }

    public class ViewHolder{
        public ImageView userImage;
        public TextView userName,usersign,userfocus;
    }

    class photoAsyncTask extends AsyncTask<String,Void,Bitmap> {

        private ImageView imageView;
        private boolean key;
        private PictureUtil pictureUtil;
        private DataUtil util;
        public photoAsyncTask(ImageView imageView) {
            this.imageView=imageView;
            this.key=false;
            util=new DataUtil("user_focus",context);
            pictureUtil = new PictureUtil();
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
//            File file=new File(Environment.getExternalStorageDirectory().getPath()+"/voiceshare");
//            if(!file.isFile()){
//                try {
//                    file.mkdirs();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            pictureUtil.savePicture(bitmap,Environment.getExternalStorageDirectory().getPath()+"/voiceshare",util.getData("user_name","")+"_voiceShare");
            if(key) {
                this.imageView.setImageBitmap(PhotoCut.toRoundBitmap(bitmap));
            }else{
                BitmapDrawable bd= new BitmapDrawable(bitmap);
                this.imageView.setBackground(bd);
            }
        }
    }
}
