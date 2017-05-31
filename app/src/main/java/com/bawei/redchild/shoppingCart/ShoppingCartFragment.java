package com.bawei.redchild.shoppingCart;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bawei.redchild.R;
import com.bawei.redchild.RedChildApplication;
import com.bawei.redchild.base.BaseFragment;
import com.bawei.redchild.shoppingCart.adapter.ShopCartAdapter;
import com.bawei.redchild.shoppingCart.bean.ChildBean;
import com.bawei.redchild.shoppingCart.bean.ShopCartBean;
import com.greendao.bean.Data;
import com.greendao.dao.DataDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Effect :
 * <p>
 * Created by Administrator
 * Time by 2017/5/17 0017
 */

public class ShoppingCartFragment extends BaseFragment {


    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    //标题的集合
    private CheckBox mShopcart_fragment_cb_all;
    private ArrayList<ShopCartBean> mShopCartBeen;
    private ShopCartAdapter mShopCartAdapter;
    private TextView mShopcart_fragment_tv_tatal;
    private TextView mShopcart_fragment_tv_close;
    private TextView mShopcart_fragment_compile;
    private boolean mFlag=false;
    private ArrayList<ChildBean> mChildList2;
    private ShopCartBean mShopCartBean;

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_shoppingcart;
    }

    /**
     * 初始化 view控件
     */
    @Override
    protected void initView() {
        //初始化数据，模拟假数据

        initShopCartData();

        //全选的CheckBox
        mShopcart_fragment_cb_all = (CheckBox) getView().findViewById(R.id.shopcart_fragment_cb_all);
        //合计金额
        mShopcart_fragment_tv_tatal = (TextView) getView().findViewById(R.id.shopcart_fragment_tv_tatal);
        //结算
        mShopcart_fragment_tv_close = (TextView) getView().findViewById(R.id.shopcart_fragment_tv_close);
        //数据的二级展示
        ExpandableListView shopcart_fragment_exand= (ExpandableListView) getView().findViewById(R.id.shopcart_fragment_exand);
         //删除控件
        mShopcart_fragment_compile = (TextView) getView().findViewById(R.id.shopcart_fragment_compile);
        //删除监听
        mShopcart_fragment_compile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doDelete();

            }
        });





        //适配数据
        mShopCartAdapter = new ShopCartAdapter(mShopCartBeen, getActivity());
        shopcart_fragment_exand.setAdapter(mShopCartAdapter);
        for (int i = 0; i < mShopCartAdapter.getGroupCount() ; i++)
        {
            shopcart_fragment_exand.expandGroup(i);// 初始化时，将ExpandableListView以展开的方式呈现
        }
           calculate();

         //自定义的接口回调来监听checkbox
        mShopCartAdapter.setCheckInterface(new ShopCartAdapter.CheckInterface() {
            @Override
            public void checkGroup(int groupPosition, boolean isChecked) {
                ShopCartBean shopCartBean = mShopCartBeen.get(groupPosition);
                ArrayList<ChildBean> childList = shopCartBean.getChildList();
                  for (int i=0;i<childList.size();i++){
                   childList.get(i).setChoesed(isChecked);

                          }

                calculate();
                //判断所谓全选的checked是否是选中状态
                select();
            }




            @Override
            public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
                boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
                ArrayList<ChildBean> childList = mShopCartBeen.get(groupPosition).getChildList();
                for (int i = 0; i <childList.size() ; i++) {
                    if (childList.get(i).isChoesed()!=isChecked){
                        allChildSameState=false;
                        break;
                    }
                }
                if (allChildSameState){
                   mShopCartBeen.get(groupPosition).setChecked(isChecked);
                }else {
                    mShopCartBeen.get(groupPosition).setChecked(false);
                }
                calculate();
                //判断所谓全选的checked是否是选中状态
                select();
            }
        });


        //自己定的接口回调来监听加号减号
        mShopCartAdapter.setModifyCountInterface(new ShopCartAdapter.ModifyCountInterface() {
            //加号的监听
            @Override
            public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
                ChildBean childBean = mShopCartBeen.get(groupPosition).getChildList().get(childPosition);
                int conut = childBean.getConut();
                conut+=1;
                ((TextView)showCountView).setText(conut+"");
                childBean.setConut(conut);
                calculate();
                mShopCartAdapter.notifyDataSetChanged();

            }
            //减号的监听
            @Override
            public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
                ChildBean childBean = mShopCartBeen.get(groupPosition).getChildList().get(childPosition);
                int conut = childBean.getConut();
                if (conut==1)
                    return;
                    conut--;
                    ((TextView) showCountView).setText(conut + "");
                    childBean.setConut(conut);
                    calculate();

                mShopCartAdapter.notifyDataSetChanged();
            }
        });

         //全选的监听
        mShopcart_fragment_cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置全选或全部选
                doCheckall();

                //全选状态时的价格和个数的计算
                boolean checked = mShopcart_fragment_cb_all.isChecked();
                if (checked){
                    calculate();
                }else {
                    mShopcart_fragment_tv_tatal.setText("￥" + 0);
                    mShopcart_fragment_tv_close.setText("去结算(" + 0 + ")");
                    mShopcart_fragment_tv_close.setBackgroundColor(Color.parseColor("#f8f8f8"));

                }
                //设置完刷新一下适配器
              mShopCartAdapter.notifyDataSetChanged();
            }


        });


        //结算监听
        mShopcart_fragment_tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderFormActivity.class);
                intent.putExtra("zongjia",mShopcart_fragment_tv_tatal.getText().toString());
                startActivity(intent);
            }
        });



    }
    //操作数据库的两个类
    private DataDao getNoteDao() {
        // 通过 BaseApplication 类提供的 getDaoSession() 获取具体 Dao
        return ((RedChildApplication) getActivity().getApplicationContext()).getDaoSession().getDataDao();
    }

    private SQLiteDatabase getDb() {
        // 通过 BaseApplication 类提供的 getDb() 获取具体 db
        return ((RedChildApplication) getActivity().getApplicationContext()).getDb();
    }

   //判断每组额checkbox的选中状态
    private boolean isAllCheck()
    {

        for (ShopCartBean group : mShopCartBeen)
        {
            if (!group.isChecked())
                return false;

        }

        return true;
    }
//模拟的假数据
    private void initShopCartData() {

//        Data data1 = new Data();
//        data1.setShopName("老熊之家1");
//        data1.setPrice(200);
//        data1.setName("商品");
//        data1.setNumber("2");
//        getNoteDao().insert(data1);
        //测试 时删除数据库的操作
//     getNoteDao().deleteAll();

        mShopCartBeen = new ArrayList<>();

        QueryBuilder<Data> dataQueryBuilder = getNoteDao().queryBuilder();
        List<Data> list = dataQueryBuilder.list();
        Collections.sort(list, new CityComparator());


        ArrayList<ChildBean> childList1 = new ArrayList<ChildBean>();
        for (int i = 0; i <list.size() ; i++) {
            Data data = list.get(i);
            //                ArrayList<ChildBean> childList = new ArrayList<ChildBean>();
            if (i!=0) {
                mShopCartBean = new ShopCartBean();
                if (!data.getShopName().equals(list.get(i - 1).getShopName())) {
                    mShopCartBean.setGroupName(data.getShopName());
                    mChildList2 = new ArrayList<ChildBean>();
                    ChildBean childBean = new ChildBean();
                    childBean.setName(data.getName());
                    childBean.setImageUrl(data.getImage());
                    childBean.setConut(Integer.valueOf(data.getNumber()));
                    childBean.setPrice(data.getPrice());
                    mChildList2.add(childBean);
                    mShopCartBean.setChildList(mChildList2);
                    mShopCartBeen.add(mShopCartBean);
                    mFlag = true;

                } else {

                    if (mFlag){
                        ChildBean childBean = new ChildBean();
                        childBean.setName(data.getName());
                        childBean.setImageUrl(data.getImage());
                        childBean.setConut(Integer.valueOf(data.getNumber()));
                        childBean.setPrice(data.getPrice());
                        mChildList2.add(childBean);
                        mShopCartBean.setChildList(mChildList2);
                    }else {
                        ChildBean childBean = new ChildBean();
                        childBean.setName(data.getName());
                        childBean.setImageUrl(data.getImage());
                        childBean.setConut(Integer.valueOf(data.getNumber()));
                        childBean.setPrice(data.getPrice());
                        childList1.add(childBean);
                        mShopCartBean.setChildList(childList1);
                    }
                }
            }
            else {
                ShopCartBean shopCartBean1 = new ShopCartBean();
                shopCartBean1.setGroupName(data.getShopName());

                    ChildBean childBean = new ChildBean();
                    childBean.setName(data.getName());
                    childBean.setImageUrl(data.getImage());
                    childBean.setConut(Integer.valueOf(data.getNumber()));
                    childBean.setPrice(data.getPrice());
                    childList1.add(childBean);
              shopCartBean1.setChildList(childList1);
                mShopCartBeen.add(shopCartBean1);
            }
        }


//        for (int i  = 0; i < 6; i++) {
//
//            String groupName="组名称";
//
//            ArrayList<ChildBean> childBeans = new ArrayList<>();
//            for (int j = 0; j < 6; j++) {
//                childBeans.add(new ChildBean("商品"+j,"",110*(j+1),"",1));
//            }
//
//            ShopCartBean groupBean = new ShopCartBean(groupName + i, childBeans);
//            mShopCartBeen.add(groupBean);
//        }



    }
    //根据判断每组是否是选中状态来判断全选checkbox是否是选中状态
    private void select() {
        if (isAllCheck()){
            mShopcart_fragment_cb_all.setChecked(true);

        }else {
            mShopcart_fragment_cb_all.setChecked(false);

        }
        mShopCartAdapter.notifyDataSetChanged();
    }

    //全选和全不选
    private void doCheckall() {
        for (int i = 0; i <mShopCartBeen.size() ; i++) {
            mShopCartBeen.get(i).setChecked(mShopcart_fragment_cb_all.isChecked());
            ShopCartBean shopCartBean = mShopCartBeen.get(i);
            ArrayList<ChildBean> childList = shopCartBean.getChildList();
            for (int j = 0; j <childList.size() ; j++) {
                childList.get(j).setChoesed(mShopcart_fragment_cb_all.isChecked());
            }
        }


    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate()
    {
        totalCount = 0;
        totalPrice = 0.00;

        for (int i = 0; i < mShopCartBeen.size(); i++)
        {
            ShopCartBean shopCartBean = mShopCartBeen.get(i);
            ArrayList<ChildBean> childList = shopCartBean.getChildList();
            for (int j = 0; j < childList.size(); j++)
            {
                ChildBean childBean = childList.get(j);
                if (childBean.isChoesed())
                {
                    totalCount+=childBean.getConut();
                    totalPrice += childBean.getPrice() * childBean.getConut();
                }
            }
        }
       mShopcart_fragment_tv_tatal.setText("￥" + totalPrice);
        mShopcart_fragment_tv_close.setText("去结算(" + totalCount + ")");
    }




    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     */
    protected void doDelete()
    {
        List<ShopCartBean> toBeDeleteGroups = new ArrayList<ShopCartBean>();// 待删除的组元素列表
        for (int i = 0; i < mShopCartBeen.size(); i++)
        {
            ShopCartBean shopCartBean = mShopCartBeen.get(i);
            if (shopCartBean.isChecked())
            {

                toBeDeleteGroups.add(shopCartBean);
            }
            List<ChildBean> toBeDeleteProducts = new ArrayList<ChildBean>();// 待删除的子元素列表
            ArrayList<ChildBean> childList = mShopCartBeen.get(i).getChildList();
            for (int j = 0; j < childList.size(); j++)
            {
                if (childList.get(j).isChoesed())
                {
                    toBeDeleteProducts.add(childList.get(j));
                }
            }

            childList.removeAll(toBeDeleteProducts);


        }
        mShopCartBeen.removeAll(toBeDeleteGroups);
        mShopCartAdapter.notifyDataSetChanged();
        calculate();
    }


    private class CityComparator implements Comparator<Data> {


        @Override
        public int compare(Data o1, Data o2) {

                String a = o1.getShopName().substring(0, 1);
                String b = o2.getShopName().substring(0, 1);
                return a.compareTo(b);

        }
    }
}