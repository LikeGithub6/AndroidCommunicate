package com.example.teamwork;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BigImgAdapter extends RecyclerView.Adapter<BigImgAdapter.ViewHolder>{
    private Context mContext;
    DisplayMetrics dm;
    List<String> imglist=new ArrayList<>();
    public void setImglist(List<String> l){
        imglist=l;
    }
    public BigImgAdapter(Context context){
        mContext=context;
        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }
    class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView imag;
        public ViewHolder(View view){
            super(view);
            imag=view.findViewById(R.id.myrvimg);
        }
    }
    @Override
    public BigImgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mainimgitem, parent, false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((dm.widthPixels - dip2px(20)) / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder( BigImgAdapter.ViewHolder holder, int position) {
        byte[] imageBytes = Base64.decode(imglist.get(position), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.imag.setImageBitmap(decodedImage);
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
        if(imglist==null)return 0;
        else return imglist.size();
    }
    private OnItemClickListener mItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
    int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
