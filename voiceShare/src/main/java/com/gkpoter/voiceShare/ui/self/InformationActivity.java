package com.gkpoter.voiceShare.ui.self;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.ui.Adapter.InformationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dy on 2016/10/22.
 */
public class InformationActivity extends Activity {

    private ListView listView;
    private List<String> data;
    private InformationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.information_activity);

        init();
    }

    private void init() {
        findViewById(R.id.information_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView= (ListView) findViewById(R.id.information_listView);
        data=new ArrayList<>();
        for(int i=0;i<1;i++) data.add(i+"");
        adapter=new InformationAdapter(data,getApplicationContext());
        listView.setAdapter(adapter);
    }
}
