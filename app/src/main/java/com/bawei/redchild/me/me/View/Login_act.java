package com.bawei.redchild.me.me.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.redchild.HomeActivity;
import com.bawei.redchild.R;
import com.bawei.redchild.me.me.Utils.SlideSwitch;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

public class Login_act extends AppCompatActivity implements View.OnClickListener {

    private Toolbar tb_login;
    private TextView tv_name_login;
    private EditText et_name_login;
    private TextView textView;
    private EditText et_pass_login;
    private ImageView iv_name_login;
    private ImageView iv_pass_login;
    private SlideSwitch st_showpass_login;
    private Button but_login_login;
    private TextView tv_forgetpass_login;
    private Button but_register_login;
    private TextView tv_qqlogin_login;
    private ImageButton iv_xllogin_login;
    private TextView tv_xllogin_login;
    private TextView tv_otherlogin_login;
    private ImageButton ib_qqlog_login;
    private SharedPreferences babyInfo;
    private UMAuthListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_act);
        initView();
        shezhi();
        mListener = new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
                String name = map.get("name");
                String iconurl = map.get("iconurl");
                Log.e("zzz",name+iconurl);
                babyInfo.edit().putString("name",name).commit();
                babyInfo.edit().putString("icon",iconurl).commit();
                babyInfo.edit().putBoolean("isLogin",true).commit();
                Intent intent = new Intent(Login_act.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        };
    }

    private void initView() {

        babyInfo = getSharedPreferences("babyInfo", MODE_PRIVATE);
        tb_login = (Toolbar) findViewById(R.id.tb_login);
        tb_login.setNavigationIcon(R.mipmap.btn_back);
        tb_login.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_name_login = (TextView) findViewById(R.id.tv_name_login);
        et_name_login = (EditText) findViewById(R.id.et_name_login);
        et_name_login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_name_login.getText().toString().trim())){
                    iv_name_login.setVisibility(View.INVISIBLE);
                }else{
                    iv_name_login.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        textView = (TextView) findViewById(R.id.textView);
        et_pass_login = (EditText) findViewById(R.id.et_pass_login);
        et_pass_login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_pass_login.getText().toString().trim())){
                    iv_pass_login.setVisibility(View.INVISIBLE);
                }else{
                    iv_pass_login.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        iv_name_login = (ImageView) findViewById(R.id.iv_name_login);
        iv_pass_login = (ImageView) findViewById(R.id.iv_pass_login);
        st_showpass_login = (SlideSwitch) findViewById(R.id.st_showpass_login);
        st_showpass_login.setOnStateChangedListener(new SlideSwitch.OnStateChangedListener() {
            @Override
            public void onStateChanged(boolean state) {
                if (state){
                    et_pass_login.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    et_pass_login.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        but_login_login = (Button) findViewById(R.id.but_login_login);
        tv_forgetpass_login = (TextView) findViewById(R.id.tv_forgetpass_login);
        but_register_login = (Button) findViewById(R.id.but_register_login);
        tv_qqlogin_login = (TextView) findViewById(R.id.tv_qqlogin_login);
        iv_xllogin_login = (ImageButton) findViewById(R.id.iv_xllogin_login);
        tv_xllogin_login = (TextView) findViewById(R.id.tv_xllogin_login);
        tv_otherlogin_login = (TextView) findViewById(R.id.tv_otherlogin_login);
        ib_qqlog_login = (ImageButton) findViewById(R.id.ib_qqlog_login);

        but_login_login.setOnClickListener(this);
        but_register_login.setOnClickListener(this);
        iv_xllogin_login.setOnClickListener(this);
        ib_qqlog_login.setOnClickListener(this);
        iv_name_login.setOnClickListener(this);
        iv_pass_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_login_login:
                String name = et_name_login.getText().toString().trim();
                String pass = et_pass_login.getText().toString().trim();
                if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pass)){
                    Toast.makeText(Login_act.this,"请补全信息",Toast.LENGTH_SHORT).show();
                }else{
                    babyInfo.edit().putBoolean("isLogin",true).commit();
                    babyInfo.edit().putString("name",name).commit();
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.but_register_login:
                Intent intent = new Intent(Login_act.this, Register_act.class);
                startActivity(intent);
                break;
            case R.id.iv_xllogin_login:
                UMShareAPI.get(Login_act.this).getPlatformInfo(Login_act.this,SHARE_MEDIA.SINA ,mListener);
                break;
            case R.id.ib_qqlog_login:
                UMShareAPI.get(Login_act.this).getPlatformInfo(Login_act.this,SHARE_MEDIA.QQ ,mListener);
                break;
            case R.id.tv_forgetpass_login:
                break;
            case R.id.iv_name_login:
                et_name_login.setText("");
                break;
            case R.id.iv_pass_login:
                et_pass_login.setText("");
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    private void shezhi(){
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        config.isOpenShareEditActivity(true);
        config.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
        config.setFacebookAuthType(UMShareConfig.AUTH_TYPE_SSO);
        config.setShareToLinkedInFriendScope(UMShareConfig.LINKED_IN_FRIEND_SCOPE_ANYONE);
        UMShareAPI.get(this).setShareConfig(config);
    }
}
