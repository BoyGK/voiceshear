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
public class ForgetActivity2 extends Activity {

    private TextView hintCode,hintPass,hintPassword;
    private EditText checkCode,pass,passWord;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_activity_2);
        FinishListActivity.getInstance().addActivity(this);

        init();
        hintMessage();
        Login();
    }
    public void init(){
        hintCode= (TextView) findViewById(R.id.forget_2_hint_checkCode);
        hintPass= (TextView) findViewById(R.id.forget_2_hint_password);
        hintPassword= (TextView) findViewById(R.id.forget_2_hint_password2);

        checkCode= (EditText) findViewById(R.id.forget_2_checkCode);
        pass= (EditText) findViewById(R.id.forget_2_password);
        passWord= (EditText) findViewById(R.id.forget_2_password2);
        submit= (Button) findViewById(R.id.forget_submit);
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
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pass1=pass.getText().toString();
                if(pass1.length()>0){
                    hintPass.setText("密码");
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
                    hintPassword.setText("确认密码");
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
                if((pass.getText().toString()+"").equals((passWord.getText().toString()+""))){
                    RegisteService registeService = new RegisteService();
                    RequestParams params=new RequestParams();
                    params.put("PassWord",passWord.getText().toString());
                    params.put("CheckCode",checkCode.getText().toString());
                    registeService.post(getApplicationContext(), "forget_2", params, new Listener() {
                        @Override
                        public void onSuccess(Object object) {
                            startActivity(new Intent(ForgetActivity2.this,MainActivity.class));
                            FinishListActivity.getInstance().exit();
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(ForgetActivity2.this, msg+"", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(ForgetActivity2.this, "密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
