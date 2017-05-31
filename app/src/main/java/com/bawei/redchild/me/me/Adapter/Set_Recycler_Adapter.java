package com.bawei.redchild.me.me.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bawei.redchild.R;

import java.util.List;

/**
 * Created by chengqianlang on 2017/5/31.
 */

public class Set_Recycler_Adapter extends RecyclerView.Adapter<Set_Recycler_Adapter.ViewHolder> {
    private Context context;
    private List<String>list;

    public Set_Recycler_Adapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Set_Recycler_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.set_recyclerview_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.myview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//得到点击的条目下标
                int position = holder.getAdapterPosition();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Set_Recycler_Adapter.ViewHolder holder, int position) {
            holder.tv_function_set.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_function_set,tv_into_set;
        View myview;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_function_set= (TextView) itemView.findViewById(R.id.tv_function_set);
            tv_into_set= (TextView) itemView.findViewById(R.id.tv_into_set);
            myview=itemView;
        }
    }
}
