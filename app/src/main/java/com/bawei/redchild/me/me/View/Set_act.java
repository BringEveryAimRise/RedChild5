package com.bawei.redchild.me.me.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.bawei.redchild.R;
import com.bawei.redchild.me.me.Adapter.Set_Recycler_Adapter;

import java.util.ArrayList;

public class Set_act extends AppCompatActivity implements View.OnClickListener {

    private Toolbar tb_set;
    private RecyclerView rv_set;
    private Button but_exit_set;
    private SharedPreferences babyInfo;
    private ArrayList<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_act);
        initData();
        initView();
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add("消息提醒设置");
        mList.add("清除图片缓存");
        mList.add("开启省流量模式");
        mList.add("关于红孩子母婴");
        mList.add("意见反馈");
        mList.add("苏宁家族");
    }

    private void initView() {
        tb_set = (Toolbar) findViewById(R.id.tb_set);
        tb_set.setNavigationIcon(R.mipmap.btn_back);
        tb_set.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rv_set = (RecyclerView) findViewById(R.id.rv_set);
        rv_set.setAdapter(new Set_Recycler_Adapter(this,mList));
        rv_set.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_set.setLayoutManager(linearLayoutManager);
        but_exit_set = (Button) findViewById(R.id.but_exit_set);
        but_exit_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_exit_set:
                babyInfo = getSharedPreferences("babyInfo", MODE_PRIVATE);
                babyInfo.edit().clear().commit();
                Intent intent = new Intent(Set_act.this, Login_act.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
