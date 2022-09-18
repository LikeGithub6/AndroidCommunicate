package com.example.teamwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatneirongAdapter  extends RecyclerView.Adapter<ChatneirongAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private Context context;
    private List<ChatneirongBean> chatlist;
    private Bitmap HETP;
    private Bitmap MYTP;
    private String HEID;
    private String MYID;
    public ChatneirongAdapter(Context context,Bitmap hetp,Bitmap mytp,String heid,String myid){
        this.context=context;
        mInflater=LayoutInflater.from(context);
        HETP=hetp;
        MYTP=mytp;
        HEID=heid;
        MYID=myid;
    }
    public void setChatlist(List<ChatneirongBean> l){
        chatlist=l;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.chatneirongitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(chatlist.get(position).getSendid().equals(HEID)){
            holder.leftimg.setImageBitmap(HETP);
            holder.rightimg.setImageBitmap(null);
            holder.linearLayout.setGravity(Gravity.LEFT);
        }
        else if(chatlist.get(position).getSendid().equals(MYID)){
            holder.rightimg.setImageBitmap(MYTP);
            holder.leftimg.setImageBitmap(null);
            holder.linearLayout.setGravity(Gravity.RIGHT);
        }
        holder.time.setText(Timejisuan.computePastTime(chatlist.get(position).getTime()));
        holder.neirong.setText(chatlist.get(position).getNeirong());
    }

    @Override
    public int getItemCount() {
        if(chatlist==null)return 0;
        else return chatlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        ImageView leftimg;
        ImageView rightimg;
        LinearLayout linearLayout;
        TextView neirong;
        ViewHolder(View view) {
            super(view);
            time=view.findViewById(R.id.chattime);
            leftimg=view.findViewById(R.id.chatimgleft);
            rightimg=view.findViewById(R.id.chatimgright);
            linearLayout=view.findViewById(R.id.chatlinear);
            neirong=view.findViewById(R.id.chatneirong);
        }
    }
}
