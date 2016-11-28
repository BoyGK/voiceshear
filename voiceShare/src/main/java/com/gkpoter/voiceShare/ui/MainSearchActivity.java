package com.gkpoter.voiceShare.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.model.MainVideoModel;
import com.gkpoter.voiceShare.service.MainVideoService;
import com.gkpoter.voiceShare.ui.Adapter.MainAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

/**
 * Created by dy on 2016/10/20.
 */
public class MainSearchActivity extends Activity {

    private Button back_left;
    private Button search_bt;
    private EditText search_edit;

    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainAdapter adapter;
    private MainVideoModel data;
    private boolean FrishKey=true;

    private interface AsyncBack{
        public void onBack();
    }

    private AsyncBack call=new AsyncBack() {
        @Override
        public void onBack() {
            if(FrishKey) {
                adapter = new MainAdapter(data, MainSearchActivity.this,listView);
                listView.setAdapter(adapter);
                FrishKey=false;
            }else{
                adapter.setData(data);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        init();

        backMain();
    }

    private void init() {
        listView= (ListView) findViewById(R.id.listView_main_search);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.listView_main_search_SwipeRefreshLayout);
        back_left= (Button) findViewById(R.id.search_back);
        search_edit= (EditText) findViewById(R.id.search_edit_data);
        search_bt= (Button) findViewById(R.id.search_ok);
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"".equals(search_edit.getText().toString()+"")) {
                    getData(search_edit.getText().toString() + "");
                }else{
                    Toast.makeText(MainSearchActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!"".equals(search_edit.getText().toString()+"")) {
                    getData(search_edit.getText().toString() + "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if("".equals(search_edit.getText().toString() + "")){
                    swipeRefreshLayout.setRefreshing(false);
                }else{
                    getData(search_edit.getText().toString() + "");
                }
            }
        });
    }

    private void backMain() {
        back_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getData(String search_data) {
        MainVideoService service=new MainVideoService();
        RequestParams params=new RequestParams();
        params.put("SearchData",search_data);
        service.get(getApplicationContext(), "search", params, new Listener() {
            @Override
            public void onSuccess(Object object) {
                data= (MainVideoModel) object;
                call.onBack();
            }

            @Override
            public void onError(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), msg+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
