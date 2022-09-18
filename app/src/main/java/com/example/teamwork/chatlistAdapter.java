package com.example.teamwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

public class chatlistAdapter extends RecyclerView.Adapter<chatlistAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private Context context;
    private List<reschatBean> chatlist;
    public List<reschatBean> getChatlist(){
        return chatlist;
    }
    String str="";
    public chatlistAdapter(Context context){
        this.context=context;
        mInflater=LayoutInflater.from(context);
    }
    public void setChatlist(List<reschatBean> l){
        chatlist=l;
    }
    public void listchangeset(int i){
        chatlist.get(i).setNosee("0");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.chatlistitem, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        byte[] imageBytes = Base64.decode(chatlist.get(position).getHetp(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.listtouxiang.setImageBitmap(decodedImage);
        holder.listnicheng.setText(chatlist.get(position).getHenicheng());
        if(chatlist.get(position).getType().equals("recive")){
            str=chatlist.get(position).getHenicheng()+":"+chatlist.get(position).getNeirong();
        }
        else str=chatlist.get(position).getNeirong();
        if(str.length()>=20) {
            holder.listneirong.setText(str.substring(0, 20));
        }
        else holder.listneirong.setText(str);
        holder.listtime.setText(Timejisuan.computePastTime(chatlist.get(position).getTime()));
        if(chatlist.get(position).getNosee().equals("0")){
            holder.listnosee.setText("");
            holder.listnosee.setBackground(null);
        }
        else {
            Drawable d=context.getResources().getDrawable(R.drawable.chatweidu);
            holder.listnosee.setText(chatlist.get(position).getNosee());
            holder.listnosee.setBackground(d);
        }
        //holder.listnosee.setText(chatlist.get(position).getNosee()+position);
        //holder.listnosee.setTextColor(Color.BLACK);
        /*
        if(!(chatlist.get(position).getNosee().equals("0"))) {
            Drawable d=context.getResources().getDrawable(R.drawable.chatweidu);
            holder.listnosee.setText(chatlist.get(position).getNosee()+position);
            holder.listnosee.setTextColor(Color.BLACK);
            //holder.listnosee.setBackground(d);
        }


         */
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
        if(chatlist==null)return 0;
        else return chatlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView listtouxiang;
        TextView listnicheng;
        TextView listneirong;
        TextView listtime;
        TextView listnosee;
        ViewHolder(View view) {
            super(view);
            listtouxiang=view.findViewById(R.id.chatlisttouxiang);
            listnicheng=view.findViewById(R.id.chatlistnicheng);
            listneirong=view.findViewById(R.id.chatlistneirong);
            listtime=view.findViewById(R.id.chatlisttime);
            listnosee=view.findViewById(R.id.chatlistnosee);
        }
    }
    private OnItemClickListener mItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
