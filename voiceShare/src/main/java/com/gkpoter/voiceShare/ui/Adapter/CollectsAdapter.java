package com.gkpoter.voiceShare.ui.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

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
        util = new DataUtil("user_focus", context);
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

        Bitmap bitmap = pictureUtil.getPicture(Environment.getExternalStorageDirectory().getPath()+"/voiceshare", util.getData("user_name", "")+"_voiceShare");
        if (bitmap == null) {
            new photoAsyncTask(viewHolder.userImage).execute(util.getData("user_photo", ""));
        } else {
            BitmapDrawable bd= new BitmapDrawable(bitmap);
            viewHolder.userImage.setBackground(bd);
        }
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
            File file=new File(Environment.getExternalStorageDirectory().getPath()+"/voiceshare");
            if(!file.isFile()){
                try {
                    file.mkdirs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            pictureUtil.savePicture(bitmap,Environment.getExternalStorageDirectory().getPath()+"/voiceshare",util.getData("user_name","")+"_voiceShare");
            if(key) {
                this.imageView.setImageBitmap(PhotoCut.toRoundBitmap(bitmap));
            }else{
                BitmapDrawable bd= new BitmapDrawable(bitmap);
                this.imageView.setBackground(bd);
            }
        }
    }
}
