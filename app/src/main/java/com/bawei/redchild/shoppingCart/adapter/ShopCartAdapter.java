package com.bawei.redchild.shoppingCart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawei.redchild.R;
import com.bawei.redchild.shoppingCart.bean.ChildBean;
import com.bawei.redchild.shoppingCart.bean.ShopCartBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * date: 2017/5/28
 * author:侯亮亮候亮亮
 */

public class ShopCartAdapter extends BaseExpandableListAdapter {
    private ArrayList<ShopCartBean> mShopCartBeen;
    private Context mContext;
    private  CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    public ShopCartAdapter(ArrayList<ShopCartBean> shopCartBeen, Context context) {
        mShopCartBeen=shopCartBeen;
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return mShopCartBeen.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mShopCartBeen.get(groupPosition).getChildList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mShopCartBeen.get(groupPosition).getGroupName();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mShopCartBeen.get(groupPosition).getChildList().get(childPosition).getName();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

       GroupHolder gh;
        if (convertView==null){
            gh=new GroupHolder();
            convertView = View.inflate(mContext, R.layout.item_shopcart_group, null);
            gh.shopcart_fragment_group_item_cb= (CheckBox) convertView.findViewById(R.id.shopcart_fragment_group_item_cb);
            gh.shopcart_fragment_group_item_tv= (TextView) convertView.findViewById(R.id.shopcart_fragment_group_item_tv);
            convertView.setTag(gh);
        }else {
            gh =(GroupHolder) convertView.getTag();

        }

        final ShopCartBean shopCartBean = mShopCartBeen.get(groupPosition);
        if (shopCartBean!=null){
        //这只所有的标题是否全选
        gh.shopcart_fragment_group_item_cb.setChecked(shopCartBean.isChecked());
        gh.shopcart_fragment_group_item_tv.setText(shopCartBean.getGroupName());
            gh.shopcart_fragment_group_item_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopCartBean.setChecked(((CheckBox )v).isChecked());
                    checkInterface.checkGroup(groupPosition,((CheckBox )v).isChecked());
                }
            });

        }
        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
       final ChileHolder ch;
        if (convertView==null){
            convertView=convertView.inflate(mContext,R.layout.item_shopcart_chile,null);
            ch=new ChileHolder();
            //条目的checkBox控件
            ch.shopcart_fragment_item_chile_cb= (CheckBox) convertView.findViewById(R.id.shopcart_fragment_item_chile_cb);
            //条目的图片框架
            ch.shopcart_fragment_item_chile_im= (ImageView) convertView.findViewById(R.id.shopcart_fragment_item_chile_im);
            //条目的商品名称
            ch.shopcart_fragment_item_chile_title= (TextView) convertView.findViewById(R.id.shopcart_fragment_item_chile_title);
            //条目的商品价格
            ch.shopcart_fragment_item_chile_price= (TextView) convertView.findViewById(R.id.shopcart_fragment_item_chile_price);
            //条目的数目加
            ch.shopcart_fragment_item_chile_add= (TextView) convertView.findViewById(R.id.shopcart_fragment_item_chile_add);
            //条目的减
            ch.shopcart_fragment_item_chile_sub= (TextView) convertView.findViewById(R.id.shopcart_fragment_item_chile_sub);
            //条目的数字显示
            ch.shopcart_fragment_item_chile_num= (TextView) convertView.findViewById(R.id.shopcart_fragment_item_chile_num);
            convertView.setTag(ch);
        }else {
            ch =(ChileHolder) convertView.getTag();
        }
        //获取集合里的状态值在单击全选和全不选的时候我们把状态值给存里起来  所以在全选和不全选的时候获取值刷新就行
        final ChildBean childBean = mShopCartBeen.get(groupPosition).getChildList().get(childPosition);
        if (childBean!=null) {
            //加载图片
            Glide.with(mContext).load(childBean.getImageUrl()).into(ch.shopcart_fragment_item_chile_im);
            //获取状态值来这是否选中
            ch.shopcart_fragment_item_chile_cb.setChecked(childBean.isChoesed());
            //设置商品的名字
            ch.shopcart_fragment_item_chile_title.setText(childBean.getName());
            //设置商品的数量
            ch.shopcart_fragment_item_chile_num.setText(childBean.getConut()+"");
            //设置商品的价格
            ch.shopcart_fragment_item_chile_price.setText(childBean.getPrice()+"");

            //子条目checkBox的监听
            ch.shopcart_fragment_item_chile_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childBean.setChoesed(((CheckBox)v).isChecked());
                    ch.shopcart_fragment_item_chile_cb.setChecked(((CheckBox)v).isChecked());
                    checkInterface.checkChild(groupPosition,childPosition,((CheckBox) v).isChecked());
                }
            });

            //子条目的加号监听
            ch.shopcart_fragment_item_chile_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition,childPosition,ch.shopcart_fragment_item_chile_num,ch.shopcart_fragment_item_chile_cb.isChecked());
                }
            });


            //子条目的减号监听

            ch.shopcart_fragment_item_chile_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition,childPosition,ch.shopcart_fragment_item_chile_num,ch.shopcart_fragment_item_chile_cb.isChecked());
                }
            });
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



    private class GroupHolder
    {
        CheckBox shopcart_fragment_group_item_cb;
        TextView shopcart_fragment_group_item_tv;
    }

    private class ChileHolder{
        CheckBox shopcart_fragment_item_chile_cb;
        ImageView shopcart_fragment_item_chile_im;
        TextView shopcart_fragment_item_chile_price;
        TextView shopcart_fragment_item_chile_add;
        TextView shopcart_fragment_item_chile_sub;
        TextView shopcart_fragment_item_chile_num;
        TextView shopcart_fragment_item_chile_title;

    }


    public interface CheckInterface
    {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition
         *            组元素位置
         * @param isChecked
         *            组元素选中与否
         */
        public void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition
         *            组元素位置
         * @param childPosition
         *            子元素位置
         * @param isChecked
         *            子元素选中与否
         */
        public void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    public void setCheckInterface(CheckInterface checkInterface)
    {
        this.checkInterface = checkInterface;
    }




    /**
     * 改变数量的接口
     *
     *
     */
    public interface ModifyCountInterface
    {
        /**
         * 增加操作
         *
         * @param groupPosition
         *            组元素位置
         * @param childPosition
         *            子元素位置
         * @param showCountView
         *            用于展示变化后数量的View
         * @param isChecked
         *            子元素选中与否
         */
        public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition
         *            组元素位置
         * @param childPosition
         *            子元素位置
         * @param showCountView
         *            用于展示变化后数量的View
         * @param isChecked
         *            子元素选中与否
         */
        public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
    }


    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface)
    {
        this.modifyCountInterface = modifyCountInterface;
    }

}
