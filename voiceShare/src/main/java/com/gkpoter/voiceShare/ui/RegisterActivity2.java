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
import com.gkpoter.voiceShare.service.RegisteService;
import com.gkpoter.voiceShare.util.FinishListActivity;
import com.loopj.android.http.RequestParams;

/**
 * Created by dy on 2016/10/18.
 */
public class RegisterActivity2 extends Activity {
    private TextView hintCode,hintPass,hintPassword;
    private EditText checkCode,userName,passWord;
    private Button submit;
    private RegisteService registeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity2);
        FinishListActivity.getInstance().addActivity(this);

        init();
        hintMessage();
        Login();
    }
    public void init(){
        hintCode= (TextView) findViewById(R.id.register_2_hint_checkCode);
        hintPass= (TextView) findViewById(R.id.register_2_hint_password);
        hintPassword= (TextView) findViewById(R.id.register_2_hint_password2);

        checkCode= (EditText) findViewById(R.id.register_2_checkCode);
        userName= (EditText) findViewById(R.id.register_2_userName);
        passWord= (EditText) findViewById(R.id.register_2_password);
        submit= (Button) findViewById(R.id.register_submit);
    }

    public void hintMessage(){
        checkCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String code=checkCode.getText().toString();
                if(code.length()>0){
                    hintCode.setText("验证码");
                }else{
                    hintCode.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pass1=userName.getText().toString();
                if(pass1.length()>0){
                    hintPass.setText("用户名");
                }else{
                    hintPass.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pass2=passWord.getText().toString();
                if(pass2.length()>0){
                    hintPassword.setText("密码");
                }else{
                    hintPassword.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
    public void Login(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registeService = new RegisteService();
                RequestParams params=new RequestParams();
                params.put("UserName",userName.getText().toString());
                params.put("PassWord",passWord.getText().toString());
                params.put("CheckCode",checkCode.getText().toString());
                registeService.post(getApplicationContext(), "register_2", params, new Listener() {
                    @Override
                    public void onSuccess(Object object) {
                        startActivity(new Intent(RegisterActivity2.this,MainActivity.class));
                        FinishListActivity.getInstance().exit();
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(RegisterActivity2.this, msg+"", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
