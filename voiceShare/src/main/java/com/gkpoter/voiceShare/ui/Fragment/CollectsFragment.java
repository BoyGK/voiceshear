package com.gkpoter.voiceShare.ui.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.listener.RefashListener;
import com.gkpoter.voiceShare.model.UserFocusModel;

import com.gkpoter.voiceShare.service.UserFocusService;
import com.gkpoter.voiceShare.ui.Adapter.CollectsAdapter;
import com.gkpoter.voiceShare.ui.CollectsSearchActivity;
import com.gkpoter.voiceShare.ui.MainActivity;
import com.gkpoter.voiceShare.ui.UserActivity;
import com.gkpoter.voiceShare.ui.self.SignatureActivity;
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

/**
 * Created by dy on 2016/10/19.
 */
public class CollectsFragment extends Fragment {

    private ImageView userImage;
    private TextView userName,focus,signature;
    private PictureUtil pictureUtil;
    private DataUtil util;

    private ImageView searchFriend;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserFocusModel data;
    private CollectsAdapter adapter;
    private LinearLayout userSelf;

    private RefashListener signRefsh=new RefashListener() {
        @Override
        public void back() {
            util = new DataUtil("user", getActivity());
            if("".equals(util.getData("user_signature",""))){
                signature.setText("暂无签名...");
            }else {
                signature.setText(util.getData("user_signature", ""));
            }
        }
    };
    private RefashListener refashListener=new RefashListener() {
        @Override
        public void back() {
            getData();
        }
    };
    private interface CallBack{
        public void back();
    }

    public CallBack call=new CallBack() {
        @Override
        public void back() {
            adapter=new CollectsAdapter(data,getActivity());
            listView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collects, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        viewClick();

        UserActivity.refashListener=refashListener;
        SelfFragment.signRefsh=signRefsh;
    }

    private void viewClick() {
        listView= (ListView) getView().findViewById(R.id.collects_main_listView);
        swipeRefreshLayout= (SwipeRefreshLayout) getView().findViewById(R.id.collects_main_listView_SwipeRefreshLayout);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataUtil util_=new DataUtil("user_focus",getActivity());
                util_.clearData();
                util_.saveData("user_id",data.getFocus().get(i).getUserId()+"");
                util_.saveData("user_name",data.getFocus().get(i).getUserName()+"");
                util_.saveData("user_photo",data.getFocus().get(i).getUserPhoto()+"");
                util_.saveData("user_signature",data.getFocus().get(i).getSignature()+"");
                util_.saveData("user_selfbackgroung",data.getFocus().get(i).getSelfBackgroung()+"");
                util_.saveData("user_focus",data.getFocus().get(i).getFocus()+"");
                util_.saveData("user_vip",data.getFocus().get(i).getVip()+"");
                util_.saveData("user_logday",data.getFocus().get(i).getLogDay()+"");
                util_.saveData("user_level",data.getFocus().get(i).getLevel()+"");
                startActivity(new Intent(getActivity(), UserActivity.class));
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        searchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CollectsSearchActivity.class));
            }
        });
    }

    private void init() {
        searchFriend= (ImageView) getView().findViewById(R.id.collects_main_search);

        userImage= (ImageView) getView().findViewById(R.id.collects_main_userImage);
        userName= (TextView) getView().findViewById(R.id.collects_main_userName);
        focus= (TextView) getView().findViewById(R.id.collects_main_userFocus);
        signature= (TextView) getView().findViewById(R.id.collects_main_userSignature);
        userSelf= (LinearLayout) getView().findViewById(R.id.collects_main_userSelf);
        selfClick();
        getData();
    }

    public void getData() {
        UserFocusService service=new UserFocusService();
        RequestParams params=new RequestParams();
        params.put("UserId",util.getData("user_id",""));
        service.post(getActivity(), "focus", params, new Listener() {
            @Override
            public void onSuccess(Object object) {
                data = (UserFocusModel) object;
                call.back();
            }

            @Override
            public void onError(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(),msg + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selfClick() {
        util = new DataUtil("user", getActivity());
        pictureUtil = new PictureUtil();
        Bitmap bitmap = pictureUtil.getPicture(Environment.getExternalStorageDirectory().getPath()
                +"/voiceshare", util.getData("user_name", "")+"_voiceShare");
        if (bitmap == null) {
            new photoAsyncTask(userImage).execute(util.getData("user_photo", ""));
        } else {
            BitmapDrawable bd= new BitmapDrawable(bitmap);
            userImage.setBackground(bd);
        }
        userName.setText(util.getData("user_name",""));
        focus.setText(util.getData("user_focus","")+" 人已关注");
        if("".equals(util.getData("user_signature",""))){
            signature.setText("暂无签名...");
        }else {
            signature.setText(util.getData("user_signature", ""));
        }

        userSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataUtil util_=new DataUtil("user_focus",getActivity());
                util_.clearData();
                util_.saveData("user_id",util.getData("user_id","")+"");
                util_.saveData("user_name",util.getData("user_name","")+"");
                util_.saveData("user_photo",util.getData("user_photo","")+"");
                util_.saveData("user_signature",util.getData("user_signature","")+"");
                util_.saveData("user_selfbackgroung",util.getData("user_selfbackgroung","")+"");
                util_.saveData("user_focus",util.getData("user_focus","")+"");
                util_.saveData("user_vip",util.getData("user_vip","")+"");
                util_.saveData("user_logday",util.getData("user_logday","")+"");
                util_.saveData("user_level",util.getData("user_level","")+"");
                startActivity(new Intent(getActivity(), UserActivity.class));
            }
        });
    }

    class photoAsyncTask extends AsyncTask<String,Void,Bitmap> {

        private ImageView imageView;
        private boolean key;
        public photoAsyncTask(ImageView imageView) {
            this.imageView=imageView;
            this.key=false;
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
            if(!file.exists()){
                try {
                    file.mkdirs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            pictureUtil.savePicture(bitmap,Environment.getExternalStorageDirectory().getPath()
                    +"/voiceshare",util.getData("user_name","")+"_voiceShare");
            if(key) {
                this.imageView.setImageBitmap(PhotoCut.toRoundBitmap(bitmap));
            }else{
                BitmapDrawable bd= new BitmapDrawable(bitmap);
                this.imageView.setBackground(bd);
            }
        }
    }
}
