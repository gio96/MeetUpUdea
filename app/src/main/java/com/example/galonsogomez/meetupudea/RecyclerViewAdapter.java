package com.example.galonsogomez.meetupudea;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{


    private Context mContext;
    private List<Group> mData;

    public RecyclerViewAdapter(Context mContext, List<Group> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_group,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_group_title.setText(mData.get(position).getTitle());
        holder.img_group_thumbnail.setImageResource(mData.get(position).getThumbnail());

        //Set Click listener
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_group_title;
        ImageView img_group_thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_group_title = (TextView) itemView.findViewById(R.id.text_Group);
            img_group_thumbnail = (ImageView) itemView.findViewById(R.id.img_Group);
        }
    }
}
