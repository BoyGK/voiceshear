package com.gkpoter.voiceShare.ui.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.model.MainVideoModel;
import com.gkpoter.voiceShare.service.MainVideoService;
import com.gkpoter.voiceShare.ui.Adapter.TopRightAdapter;
import com.gkpoter.voiceShare.ui.MainVideoActivity;
import com.gkpoter.voiceShare.util.DataUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dy on 2016/10/20.
 */
public class TopFragmentRight extends Fragment {
    private PullToRefreshListView listView;
    private MainVideoModel data;
    private TopRightAdapter adapter;
    private boolean FrishKey=true;

    private interface CallBack{
        public void back();
    }
    private CallBack call=new CallBack() {
        @Override
        public void back() {
            if(FrishKey) {
                adapter=new TopRightAdapter(data,getActivity(),listView);
                listView.setAdapter(adapter);
                listView.onRefreshComplete();
                FrishKey=false;
            }else{
                listView.onRefreshComplete();
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_right, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        viewOnClick();
    }

    private void viewOnClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("information", "onItemClick "+i);
                i--;
                DataUtil util=new DataUtil("video_data",getActivity());
                util.clearData();
                util.saveData("video_path",data.getVideoData().get(i).getVideoPath());
                util.saveData("video_id",data.getVideoData().get(i).getVideoId()+"");
                util.saveData("user_id",data.getUserData().get(i).getUserId()+"");
                util.saveData("user_image",data.getUserData().get(i).getUserPhoto());
                util.saveData("user_name",data.getUserData().get(i).getUserName());
                startActivity(new Intent(getActivity(), MainVideoActivity.class));
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

    }

    private void init() {
        listView= (PullToRefreshListView) getView().findViewById(R.id.top_rightList);
        getData();
    }

    private void getData() {
        RequestParams params=new RequestParams();
        params.put("VideoTop","month");
        MainVideoService service=new MainVideoService();
        service.get(getActivity(), "top_video", params, new Listener() {
            @Override
            public void onSuccess(Object object) {
                data = (MainVideoModel) object;
                call.back();
            }

            @Override
            public void onError(String msg) {
                listView.onRefreshComplete();
                Toast.makeText(getActivity(), msg+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
