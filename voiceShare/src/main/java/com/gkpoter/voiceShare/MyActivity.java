package com.gkpoter.voiceShare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.model.UserModel;
import com.gkpoter.voiceShare.service.LoginService;
import com.gkpoter.voiceShare.ui.LoginActivity;
import com.gkpoter.voiceShare.ui.MainActivity;
import com.gkpoter.voiceShare.util.DataUtil;
import com.gkpoter.voiceShare.util.FinishListActivity;
import com.loopj.android.http.RequestParams;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private boolean statekey=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FinishListActivity.getInstance().addActivity(this);
        init();
        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(statekey) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        }.start();
    }

    public void init(){
        LoginService loginService = new LoginService();
        RequestParams params=new RequestParams();

        DataUtil util=new DataUtil("userlogin",getApplication());

        if(!(util.getData("useremail","").equals(""))){
            params.put("UserEmail",util.getData("useremail",""));
            params.put("PassWord",util.getData("password",""));
            loginService.post(getApplicationContext(), "doLogin", params, new Listener() {
                @Override
                public void onSuccess(Object object) {
                    statekey=false;
                    save((UserModel) object);
                    startActivity(new Intent(MyActivity.this,MainActivity.class));
                    FinishListActivity.getInstance().exit();
                }
                @Override
                public void onError(String msg) {
                    Toast.makeText(MyActivity.this, msg+"", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

}
