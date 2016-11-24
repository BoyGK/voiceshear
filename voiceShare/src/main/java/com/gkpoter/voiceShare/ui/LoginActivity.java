package com.gkpoter.voiceShare.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.model.UserModel;
import com.gkpoter.voiceShare.service.LoginService;
import com.gkpoter.voiceShare.util.DataUtil;
import com.gkpoter.voiceShare.util.FinishListActivity;
import com.loopj.android.http.RequestParams;

/**
 * Created by dy on 2016/10/18.
 */
public class LoginActivity extends Activity{

    private LinearLayout linearLayout;
    private EditText editEmail,editPass;
    private TextView hintEmail,hintPass,forget,register;
    private Button login;
    float state = (float) 0.1;
    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        FinishListActivity.getInstance().addActivity(this);

        init();
        userHint();
        forgetAndRegister();
        Login();
    }

    public void init(){
        editEmail= (EditText) findViewById(R.id.login_userEmail);
        editPass= (EditText) findViewById(R.id.login_userPassword);
        hintEmail= (TextView) findViewById(R.id.login_hint_email);
        hintPass= (TextView) findViewById(R.id.login_hint_password);

        forget= (TextView) findViewById(R.id.forget_password);
        register= (TextView) findViewById(R.id.login_to_register);
        login= (Button) findViewById(R.id.login_submit);
        linearLayout= (LinearLayout) findViewById(R.id.login_linearLayout);
        new Thread(){
            @Override
            public void run() {
                try {
                    while(state <= 1) {
                        sleep(80);
                        linearLayout.setAlpha(state);
                        state += 0.05;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    public void userHint(){
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email=editEmail.getText().toString();
                if(email.length()>0){
                    hintEmail.setText("邮箱");
                }else{
                    hintEmail.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        editPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password=editPass.getText().toString();
                if(password.length()>0){
                    hintPass.setText("密码");
                }else{
                    hintPass.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public void forgetAndRegister(){
        forget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
        forget.getPaint().setAntiAlias(true);//抗锯齿
        register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
        register.getPaint().setAntiAlias(true);//抗锯齿
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 100;
                startActivity(new Intent(LoginActivity.this,ForgetActivity1.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 100;
                startActivity(new Intent(LoginActivity.this,RegisterActivity1.class));
            }
        });
    }

    private void Login() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"".equals(editEmail.getText().toString())&&!"".equals(editPass.getText().toString())) {
                    loginService = new LoginService();
                    RequestParams params = new RequestParams();
                    params.put("UserEmail", editEmail.getText().toString());
                    params.put("PassWord", editPass.getText().toString());
                    loginService.post(getApplicationContext(), "doLogin", params, new Listener() {
                        @Override
                        public void onSuccess(Object object) {
                            state = 100;
                            UserModel userModel = new UserModel();
                            userModel = (UserModel) object;
                            save(userModel);
                            saveUser(editEmail.getText().toString(), editPass.getText().toString());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            FinishListActivity.getInstance().exit();
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(LoginActivity.this, msg + "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "用户名密码不能为空！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void save(UserModel userModel) {
        DataUtil util=new DataUtil("user",getApplication());
        util.clearData();
        util.saveData("user_id",userModel.getUserId()+"");
        util.saveData("user_name",userModel.getUserName()+"");
        util.saveData("user_photo",userModel.getUserPhoto()+"");
        util.saveData("user_signature",userModel.getSignature()+"");
        util.saveData("user_selfbackgroung",userModel.getSelfBackgroung()+"");
        util.saveData("user_focus",userModel.getFocus()+"");
        util.saveData("user_vip",userModel.getVip()+"");
        util.saveData("user_logday",userModel.getLogDay()+"");
        util.saveData("user_level",userModel.getLevel()+"");
    }

    private void saveUser(String useremail, String password) {
        DataUtil util=new DataUtil("userlogin",getApplicationContext());
        util.clearData();
        util.saveData("useremail",useremail);
        util.saveData("password",password);
    }
}
