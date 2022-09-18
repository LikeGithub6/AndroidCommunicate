package com.example.teamwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LtplAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int TYPE_Normal = 0;
    public static final int TYPE_Footer = 1;
    private LayoutInflater mInflater;
    private Context context;
    private List<LtplBean> ltlist=new ArrayList<>();
    private
    Handler handler= new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==222){
                byte[] tupian=(byte[])msg.obj;
                Bitmap bitmap = BitmapFactory.decodeByteArray(tupian, 0, tupian.length);
                ltlist.get(msg.arg1).setTupianbit(bitmap);
                ltlist.get(msg.arg1).setIsload(true);
                notifyItemChanged(msg.arg1);
            }
        }
    };
    public LtplAdapter(Context context){
        this.context=context;
        mInflater=LayoutInflater.from(context);
    }
    public void setLtlist(List<LtplBean> l){
        ltlist=l;
    }
    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1)return TYPE_Footer;
        else return TYPE_Normal;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_Normal) {
            View view = mInflater.inflate(R.layout.tiebaallitem, parent, false);
            return new LtplAdapter.ViewHolder(view);
        }
        else if(viewType==TYPE_Footer){
            View view=mInflater.inflate(R.layout.loadingfooter,parent,false);
            return new LtplAdapter.ViewHolder(view);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_Normal) {
            if(ltlist.get(position).getIsload()==false) {
                showtupian(position, ltlist.get(position).getImgurl());
            }



            ((ViewHolder)holder).xxnicheng.setText(ltlist.get(position).getNicheng());
            ((ViewHolder)holder).xxneirong.setText(ltlist.get(position).getNeiorng());
            ((ViewHolder)holder).xximg.setImageBitmap(ltlist.get(position).getTupianbit());
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
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView xximg;
        TextView xxnicheng;
        TextView xxneirong;
        ViewHolder(View view){
            super(view);
            xximg=view.findViewById(R.id.tbxxtouxiang);
            xxnicheng=view.findViewById(R.id.tbxxnicheng);
            xxneirong=view.findViewById(R.id.tbxxneirong);
        }
    }
    @Override
    public int getItemCount() {
        if(ltlist==null)return 0;
        else return ltlist.size();
    }
    private OnItemClickListener mItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
    public void showtupian(int poi,String url){
        String Path = "https:"+url;
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
        Request request = new Request.Builder()
                .url(Path)
                .build();
        //3.创建一个Call对象，参数是request对象，发送请求
        Call call = okHttpClient.newCall(request);
        //4.异步请求，请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到从网上获取资源，转换成我们想要的类型
                byte[] Picture_bt = response.body().bytes();
                //通过handler更新UI
                Message message = handler.obtainMessage();
                message.obj = Picture_bt;
                message.what=222;
                message.arg1=poi;
                handler.sendMessage(message);
            }
        });
    }
}
