package com.bawei.redchild.shoppingCart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.redchild.R;
import com.bawei.redchild.shoppingCart.alipay.AlipayAPI;
import com.bawei.redchild.shoppingCart.alipay.PayResult;

public class OrderFormActivity extends AppCompatActivity {
    public static final int SDK_PAY_FLAG = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);
        initview();
    }

    private void initview() {
        //商品总价控件
        TextView orderform_tv_jine1= (TextView) findViewById(R.id.orderform_tv_jine1);
        //提交到支付宝控件
        TextView orderform_tv_tijiao= (TextView) findViewById(R.id.orderform_tv_tijiao);
        //实付价格控件
        TextView orderform_zongjia= (TextView) findViewById(R.id.orderform_zongjia);
        //back返回控件
       ImageView orderform_im_back= (ImageView) findViewById(R.id.orderform_im_back);
        //点击返回键，把页面销毁
        orderform_im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        orderform_tv_tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AliPayThread().start();
            }
        });
         //获取传过来的总价格
        Intent intent = getIntent();
        String zongjia = intent.getStringExtra("zongjia");
        //价格复制
        orderform_tv_jine1.setText(zongjia);
        orderform_zongjia.setText(zongjia);
    }
     //支付宝支付回调
    private Handler mHandler=new Handler()
    {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OrderFormActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderFormActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderFormActivity.this,
                                    "支付失败" + resultStatus, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    break;
                }
            }
        };
    };
    /**
     * 支付宝支付异步任务
     *
     * @author Simon
     */
    private class AliPayThread extends Thread {
        @Override
        public void run() {
            String result = AlipayAPI.pay(OrderFormActivity.this, "测试的商品",
                    "测试商品的详细描述", "0.01");
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    }

}
