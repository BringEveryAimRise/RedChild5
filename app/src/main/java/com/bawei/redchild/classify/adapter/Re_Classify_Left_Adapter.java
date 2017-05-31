package com.bawei.redchild.classify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bawei.redchild.R;
import com.bawei.redchild.classify.dao.Classify;

import java.util.List;

/**
 * Created by lichaohui on 2017/5/17.
 * ------------------------------
 */

public class Re_Classify_Left_Adapter   extends RecyclerView.Adapter<Re_Classify_Left_Adapter.ViewHolder> {

    private Context context;
    List<Classify.RsBean> rs;
    private MyItemClickListener mItemClickListener;
    public Re_Classify_Left_Adapter(Context context,   List<Classify.RsBean> rs) {
        this.context = context;
        this.rs= rs;

    }
    @Override
    public Re_Classify_Left_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.rv_classify_item_left, parent,false);
        ViewHolder viewHolder = new ViewHolder(inflate,mItemClickListener);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(Re_Classify_Left_Adapter.ViewHolder holder, int position) {
        holder.setData(rs.get(position).getDirName());
    }
    @Override
    public int getItemCount() {
        if (rs!=null){
            return  rs.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private final TextView tv_classify_item;
        MyItemClickListener mListener;
        private final View view;

        public ViewHolder(View itemView,MyItemClickListener mItemClickListener) {
            super(itemView);
            view = itemView;
            tv_classify_item = (TextView) itemView.findViewById(R.id.classif_item_text);
            tv_classify_item.setOnClickListener(this);
            this.mListener = mItemClickListener;
        }
        public  void  setData(String name){
            tv_classify_item.setText(name);
        }
        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.onItemClick(view ,getPosition());
            }
        }
    }
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
