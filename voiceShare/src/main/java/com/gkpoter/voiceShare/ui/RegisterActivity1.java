package com.gkpoter.voiceShare.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.model.Model;
import com.gkpoter.voiceShare.service.RegisteService;
import com.gkpoter.voiceShare.util.FinishListActivity;
import com.loopj.android.http.RequestParams;

/**
 * Created by dy on 2016/10/18.
 */
public class RegisterActivity1 extends Activity {
    private TextView hintEmail;
    private EditText editEmail;
    private Button next;
    private RegisteService registeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity1);
        FinishListActivity.getInstance().addActivity(this);

        init();
        nextSubmit();
    }

    public void init(){
        hintEmail= (TextView) findViewById(R.id.register_1_hint_email);
        editEmail= (EditText) findViewById(R.id.register_1_email);
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = editEmail.getText().toString();
                if(email.length()>0){
                    hintEmail.setText("邮箱");
                }else{
                    hintEmail.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        next= (Button) findViewById(R.id.register_1_next);
    }

    public void nextSubmit(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registeService = new RegisteService();
                RequestParams params=new RequestParams();
                params.put("UserEmail",editEmail.getText().toString());
                registeService.post(getApplicationContext(), "register_1", params, new Listener() {
                    @Override
                    public void onSuccess(Object object) {
                        Toast.makeText(RegisterActivity1.this, ((Model)object).getMsg(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity1.this,RegisterActivity2.class));
                    }
                    @Override
                    public void onError(String msg) {
                        Toast.makeText(RegisterActivity1.this, msg+"", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
