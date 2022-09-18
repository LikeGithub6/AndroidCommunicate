package com.example.teamwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MeguanzhuAdapter extends RecyclerView.Adapter<MeguanzhuAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private Context context;
    private List<MeguanzhuBean> mainlist=new ArrayList<>();
    MeguanzhuAdapter(Context context){
        this.context=context;
        mInflater=LayoutInflater.from(context);
    }
    @Override
    public MeguanzhuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.meguanzhuitem, parent, false);
        return new MeguanzhuAdapter.ViewHolder(view);
    }
    public void setMainlist(List<MeguanzhuBean> list){
        mainlist=list;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView touxiang;
        TextView nicheng;
        ImageView sex;
        ViewHolder(View view){
            super(view);
            touxiang=view.findViewById(R.id.meguanzhutouxiang);
            nicheng=view.findViewById(R.id.meguanzhunicheng);
            sex=view.findViewById(R.id.meguanzhusex);
        }
    }
    @Override
    public void onBindViewHolder(MeguanzhuAdapter.ViewHolder holder, int position) {
        byte[] imageBytes = Base64.decode(mainlist.get(position).getTouxiang(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.touxiang.setImageBitmap(decodedImage);
        holder.nicheng.setText(mainlist.get(position).getNicheng());
        if(mainlist.get(position).getSex().equals("男")){
            holder.sex.setImageResource(R.mipmap.sexman);
        }
        else if(mainlist.get(position).getSex().equals("女")){
            holder.sex.setImageResource(R.mipmap.sexwoman);
        }
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    mItemClickListener.onItemClick(adapterPosition, v);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mainlist==null)return 0;
        else return mainlist.size();
    }
    private MeguanzhuAdapter.OnItemClickListener mItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(MeguanzhuAdapter.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
