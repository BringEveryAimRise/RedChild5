package com.bawei.redchild.me.me.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.bawei.redchild.R;
import com.bawei.redchild.base.BaseFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.content.Context.MODE_PRIVATE;

/**
 * Effect :
 * <p>
 * Created by Administrator
 * Time by 2017/5/17 0017
 */

public class MeFragment extends BaseFragment implements View.OnClickListener{

    private CircleImageView mHeadicon;
    private TextView mName,mset;
    private SharedPreferences babyInfo;
    private Bitmap mBimap;
    private String mName1;
    private String mIconurl;
    private String mPicture;

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_loginsuccess;
    }

    /**
     * 初始化 view控件
     */
    @Override
    protected void initView() {
        mset= (TextView) getView().findViewById(R.id.tv_set_success);
        mset.setOnClickListener(this);
        mHeadicon = (CircleImageView) getView().findViewById(R.id.iv_head_success);
        mHeadicon.setOnClickListener(this);
        mName = (TextView) getView().findViewById(R.id.tv_name_success);
        babyInfo = getActivity().getSharedPreferences("babyInfo", MODE_PRIVATE);
        mName1 = babyInfo.getString("name", "aa");
        mIconurl = babyInfo.getString("icon", "");
        mPicture = babyInfo.getString("picture", "");
        mName.setText(mName1);
        if (!TextUtils.isEmpty(mPicture)){
            mHeadicon.setImageBitmap(convertStringToIcon(mPicture));
            return;
        } else if (!TextUtils.isEmpty(mIconurl)){
            Glide.with(getActivity()).load(mIconurl).bitmapTransform(new CropCircleTransformation(getActivity())).placeholder(R.mipmap.courier_default_icon) .into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource,
                                            GlideAnimation<? super GlideDrawable> glideAnimation) {
                    mHeadicon.setImageDrawable(resource);
                }
            });
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_set_success:
                Intent intent = new Intent(getActivity(), Set_act.class);
                startActivity(intent);
                break;
            case R.id.iv_head_success:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final String[] items = {"照相机", "本地图片", "取消"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                invokecamera();
                                dialog.dismiss();
                                break;
                            case 1:
                                invokeimgages();
                                dialog.dismiss();
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                builder.create().show();
                break;
        }
    }

    public void invokecamera() {
        //启动照像机组件
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory("android.intent.category.DEFAULT");
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            //得到像册中图片的地址
            Uri uri = data.getData();
//简单编辑
            crop(uri);
        }else if (requestCode == 9999) {
            //得到裁剪后的照片
            mBimap = data.getParcelableExtra("data");
            babyInfo.edit().putString("picture",convertIconToString(mBimap)).commit();
            mHeadicon.setImageBitmap(mBimap);
        }else if (requestCode == 100) {
            //得到照片
            mBimap = data.getParcelableExtra("data");
            babyInfo.edit().putString("picture",convertIconToString(mBimap)).commit();
            mHeadicon.setImageBitmap(mBimap);

            super.onActivityResult(requestCode, resultCode, data);
        }


    }
    public void invokeimgages() {
        //调用系统相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        //设置图片种类,去除视频类
        intent.setType("image/*");
        startActivityForResult(intent, 200);
    }


    //简单编辑的方法
    private void crop(Uri uri) {

        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //是否裁剪
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", false);// 取消人脸识别

        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 9999);
    }

    /**
     * 图片转成string
     */
    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }

    //将字符串转为bitmap
    public static Bitmap convertStringToIcon(String st)
    {
        // OutputStream out;
        Bitmap bitmap = null;
        try
        {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }


}
