package com.gkpoter.voiceShare.ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.bean.Video;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.model.MainVideoModel;
import com.gkpoter.voiceShare.service.MainVideoService;
import com.gkpoter.voiceShare.ui.Adapter.MainAdapter;
import com.gkpoter.voiceShare.ui.MainSearchActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

import java.util.List;

/**
 * Created by dy on 2016/10/19.
 */
public class MainFragment extends Fragment {

    private PullToRefreshListView listView;
    private MainAdapter adapter;
    private MainVideoModel data;
    private EditText search;
    private boolean FrishKey=true;
    private Integer PAGE = 1;
    private interface AsyncBack{
        public void onFrishBack();
        public void onPullBack();
    }
    private AsyncBack call=new AsyncBack() {
        @Override
        public void onFrishBack() {
            if(FrishKey) {
                listView.onRefreshComplete();
                adapter = new MainAdapter(data, getActivity(),listView);
                listView.setAdapter(adapter);
                FrishKey=false;
            }else{
                listView.onRefreshComplete();
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onPullBack() {
            adapter.setData(data);
            adapter.notifyDataSetChanged();
            listView.onRefreshComplete();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        ListViewClick();
    }

    private void init() {
        getData();
        listView= (PullToRefreshListView) getView().findViewById(R.id.listView_main);

        search= (EditText) getView().findViewById(R.id.fragment_main_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainSearchActivity.class));
            }
        });
    }

    private void getData() {
        MainVideoService service=new MainVideoService();
        RequestParams params=new RequestParams();
        params.put("Page","0");
        service.get(getActivity(), "getvideo", params, new Listener() {
            @Override
            public void onSuccess(Object object) {
                data= (MainVideoModel) object;
                call.onFrishBack();
            }

            @Override
            public void onError(String msg) {
                listView.onRefreshComplete();
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), msg+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void PullData() {
        MainVideoService service=new MainVideoService();
        RequestParams params=new RequestParams();
        params.put("Page",PAGE);
        service.get(getActivity(), "getvideo", params, new Listener() {
            @Override
            public void onSuccess(Object object) {
                data.getVideoData().addAll(((MainVideoModel)object).getVideoData());
                data.getUserData().addAll(((MainVideoModel)object).getUserData());
                call.onPullBack();
            }

            @Override
            public void onError(String msg) {
                listView.onRefreshComplete();
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), msg+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ListViewClick(){
        //刷新加载
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!isNetworkConnected(getActivity())){
                    Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
                    listView.onRefreshComplete();
                }
                getData();
                PAGE=1;
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!isNetworkConnected(getActivity())){
                    Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
                    listView.onRefreshComplete();
                }
                PullData();
                PAGE++;
            }
        });
        //点击回调
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("i",i+"");
            }
        });
    }

    /**
     * 联网判断
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
