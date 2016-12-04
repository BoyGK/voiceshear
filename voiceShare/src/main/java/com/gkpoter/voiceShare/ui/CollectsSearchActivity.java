package com.gkpoter.voiceShare.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.model.UserFocusModel;
import com.gkpoter.voiceShare.service.UserFocusService;
import com.gkpoter.voiceShare.ui.Adapter.CollectsAdapter;
import com.gkpoter.voiceShare.util.DataUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

/**
 * Created by dy on 2016/10/21.
 */
public class CollectsSearchActivity extends Activity {
    private Button back_left,search_;
    private EditText edit_;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserFocusModel data;
    private CollectsAdapter adapter;

    private interface CallBack{
        public void back();
    }

    public CallBack call=new CallBack() {
        @Override
        public void back() {
            adapter=new CollectsAdapter(data,getApplication());
            listView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        init();

        backMain();
        Onclick();
    }

    private void Onclick() {
        search_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"".equals(edit_.getText().toString()+"")) {
                    getData(edit_.getText().toString() + "");
                }else{
                    Toast.makeText(getApplicationContext(), "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edit_.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!"".equals(edit_.getText().toString()+"")) {
                    getData(edit_.getText().toString() + "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!"".equals(edit_.getText().toString()+"")) {
                    getData(edit_.getText().toString() + "");
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void getData(String s) {
        UserFocusService service=new UserFocusService();
        RequestParams params=new RequestParams();
        params.put("UserName",s);
        service.post(getApplication(), "s_people", params, new Listener() {
            @Override
            public void onSuccess(Object object) {
                data = (UserFocusModel) object;
                call.back();
            }

            @Override
            public void onError(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(),msg + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        back_left= (Button) findViewById(R.id.search_back);
        edit_= (EditText) findViewById(R.id.search_edit_data);
        listView= (ListView) findViewById(R.id.listView_main_search);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.listView_main_search_SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(0x19,0xb4,0xff));
        search_= (Button) findViewById(R.id.search_ok);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i--;
                DataUtil util_=new DataUtil("user_focus",getApplication());
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
                startActivity(new Intent(getApplication(), UserActivity.class));
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
}
