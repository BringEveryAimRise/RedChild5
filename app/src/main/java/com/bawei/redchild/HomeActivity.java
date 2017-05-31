package com.bawei.redchild;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bawei.redchild.base.BaseActivity;
import com.bawei.redchild.classify.ClassifyFragment;
import com.bawei.redchild.groupon.GrouponFragment;
import com.bawei.redchild.home.HomeFragment;
import com.bawei.redchild.me.me.View.Login_act;
import com.bawei.redchild.me.me.View.MeFragment;
import com.bawei.redchild.shoppingCart.ShoppingCartFragment;

public class HomeActivity extends BaseActivity{


    private HomeFragment homeFragment;
    private ClassifyFragment classifyFragment;
    private ShoppingCartFragment shoppingCartFragment;
    private MeFragment meFragment;
    private GrouponFragment grouponFragment;
    private final int REQUEST_CODE=100;
    private boolean isLogin=false;

    private int tag;
    private SharedPreferences babyInfo;
    private RadioGroup rg;

    /**
     * 获取焦点
     * 判断 用户是否登录 两个来源
     *  1. 用户登录后保存在shared中
     *  2. shared中
     */
    @Override
    protected void onResume() {
        super.onResume();
        isLogin = babyInfo.getBoolean("isLogin", false);
    }

    /**
     * 初始化 Layout布局
     *
     */
    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_home;
    }

    /**
     * 初始化 View控件
     */
    @Override
    protected void initView() {
        //标记 设置值
        babyInfo = getSharedPreferences("babyInfo", MODE_PRIVATE);
        if(babyInfo.getBoolean("isTag",true)) {
            /**
             * 首次弹出 PopWindonws 红包
             */
            initTag();
        }

        rg = (RadioGroup) findViewById(R.id.rg_home_show);

        findViewById(R.id.iv_home_groupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //既然 图片被点击了  那么就将radiogroup设置为null的那个
                rg.check(R.id.rb_home_groupon_show);

                if(grouponFragment==null){
                    grouponFragment = new GrouponFragment();
                }
                HomeActivity.super.replaceFragment(R.id.rl_home_show_fragment,grouponFragment);
            }
        });

        //加载默认 Fragment页面
        homeFragment = new HomeFragment();
        HomeActivity.super.addFragment(R.id.rl_home_show_fragment, homeFragment);
        //标记 当前被选中
        tag= rg.getCheckedRadioButtonId();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                //为了防止重复点击
                //如果标记不等于被选中值，则进行页面改变
                if(tag!=checkedId){
                    //标记被改变值
                    tag=checkedId;
                    switch (checkedId){
                        case R.id.rb_home_home_show:
                            if(homeFragment==null){
                                homeFragment = new HomeFragment();
                            }
                            HomeActivity.super.replaceFragment(R.id.rl_home_show_fragment,homeFragment);
                            //提示
                            Toast.makeText(HomeActivity.this, "0", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_home_classify_show:
                            if(classifyFragment==null){
                                classifyFragment = new ClassifyFragment();
                            }
                            HomeActivity.super.replaceFragment(R.id.rl_home_show_fragment,classifyFragment);
                            //提示
                            Toast.makeText(HomeActivity.this, "1", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_home_shoppingcart_show:
                            if(shoppingCartFragment==null){
                                shoppingCartFragment = new ShoppingCartFragment();
                            }
                            HomeActivity.super.replaceFragment(R.id.rl_home_show_fragment,shoppingCartFragment);
                            //提示
                            Toast.makeText(HomeActivity.this, "2", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_home_me_show:
                            //判断 用户是否登录
                            if(isLogin){
                                if(meFragment==null){
                                    meFragment = new MeFragment();
                                }
                                HomeActivity.super.replaceFragment(R.id.rl_home_show_fragment,meFragment);

                            }else{
                                startActivityForResult(new Intent(HomeActivity.this,Login_act.class),REQUEST_CODE);
                            }
                            break;
                    }
                }
            }
        });

    }

    private void initTag() {
            Intent intent = getIntent();
            SharedPreferences.Editor edit = babyInfo.edit();
            //判断用户当前状态
            switch (intent.getIntExtra("state", -1)){
                case 0:
                    /**
                     * 当前状态：state
                     * 0:我在备孕
                     *  备孕：
                     *   无字段
                     */
                    edit.putBoolean("isTag",false).putString("state","我在备孕").commit();
                    break;
                case 1:
                    /**
                     * 当前状态：state
                     * 1:我怀孕了
                     * 怀孕：
                     *   预产期：dueDate 20170519
                     */
                    edit.putBoolean("isTag",false)
                            .putString("state","我怀孕了")
                            .putString("duDate",intent.getStringExtra("duDate"))
                            .commit();
                    break;
                case 2:
                    /**
                     * 当前状态：state
                     * 2:家有宝宝
                     * 宝宝：
                     *   出生日期：birthdate 20170519
                     *   性别：sex 男，女
                     *   名字：name
                     */
                    edit.putBoolean("isTag",false)
                            .putString("state","家有宝宝")
                            .putString("birthdate",intent.getStringExtra("birthdate"))
                            .putString("sex",intent.getStringExtra("sex"))
                            .putString("name",intent.getStringExtra("name"))
                            .commit();
                    break;
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            //如果 比对成功代表 是登录页面 判断当前的值
            isLogin = babyInfo.getBoolean("isLogin", false);
            if(isLogin){
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                rg.check(R.id.rb_home_me_show);
            }else {
                rg.check(R.id.rb_home_home_show);
            }
        }
    }
}
