package com.gkpoter.voiceShare.ui.self;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.Listener;
import com.gkpoter.voiceShare.listener.RefashListener;
import com.gkpoter.voiceShare.service.Service;
import com.gkpoter.voiceShare.ui.Fragment.SelfFragment;
import com.gkpoter.voiceShare.util.DataUtil;
import com.loopj.android.http.RequestParams;

/**
 * Created by dy on 2016/11/19.
 */
public class SignatureActivity extends Activity {

    public static RefashListener refashListener;
    private EditText signature;
    private Button button;
    private ImageView image;
    private TextView userName,text_size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signature_input);
        init();
    }

    private void init() {
        final DataUtil util = new DataUtil("user", getApplication());
        userName= (TextView) findViewById(R.id.sian_input_username);
        userName.setText(util.getData("user_name","")+".");
        text_size= (TextView) findViewById(R.id.sian_input_textsize);
        image= (ImageView) findViewById(R.id.signature_back);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        signature= (EditText) findViewById(R.id.sian_input);
        signature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int s=20-(signature.getText().toString()+"").length();
                text_size.setText(s+" 字剩余");
                if(!"".equals(signature.getText().toString()+"")){
                    button.setClickable(true);
                    button.setBackgroundResource(R.drawable.button_log);
                }else{
                    button.setClickable(false);
                    button.setBackgroundResource(R.drawable.blue_button_bg);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        button= (Button) findViewById(R.id.sian_input_up);
        button.setClickable(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!((signature.getText().toString()+"").equals(""))){
                    Service service=new Service();
                    RequestParams params=new RequestParams();
                    params.put("UserId",util.getData("user_id",""));
                    params.put("Signature",signature.getText().toString());
                    service.post(getApplicationContext(), "c_Signature", params, new Listener() {
                        @Override
                        public void onSuccess(Object object) {
                            util.saveData("user_signature",signature.getText().toString());
                            refashListener.back();
                            Toast.makeText(SignatureActivity.this, "编辑成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(SignatureActivity.this, msg + "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    
}
