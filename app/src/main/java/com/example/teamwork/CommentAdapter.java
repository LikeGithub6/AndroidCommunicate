package com.example.teamwork;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==111){
               scmei=(boolean)msg.obj;
               if(scmei==false)shoucangimg.setImageResource(R.mipmap.shoucangno);
               else if(scmei==true) {
                   shoucangimg.setImageResource(R.mipmap.myshoucang);
                   shoucangtext.setText("已收藏");
               }
            }
        }
    };
    //str==0:收藏了吗    str==1:收藏     str==2:取消收藏
    private LayoutInflater mInflater;
    private Context context;
    private List<CommentBean> commentbeanlist;
    private View mHeaderView;
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_NORMAL = 2;
    private String MYID;
    private String MYTP;
    private String MYNICHENG;
    private String pinglun;
    ImageView dianzanimg;
    TextView dianzantext;
    boolean dzmei=false;
    TextView pltext;
    ImageView shoucangimg;
    TextView shoucangtext;
    boolean scmei=false;
    public CommentAdapter(Context context){
        this.context=context;
        mInflater=LayoutInflater.from(context);
    }
    public void setCommentbeanlist(List<CommentBean> list){
        commentbeanlist=list;
    }
    public void commentlistadd(int i,CommentBean c){
        commentbeanlist.add(i,c);
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public void setMYID(String str){
        MYID=str;
    }
    public void setMYTP(String tp){
        MYTP=tp;
    }
    public void SETMYNICHENG(String nicheng){
        MYNICHENG=nicheng;
    }
    public void setpinglun(String pl){
        pinglun=pl;
    }
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null ){
            return TYPE_NORMAL;
        }
        if (position == 0){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView comtx;
        TextView comnicheng;
        TextView comtime;
        TextView comneirong;
        RecyclerView comrv;




        ImageView headtouxiang;
        TextView headnicheng;
        TextView headtime;
        TextView headneirong;
        RecyclerView headrc;
        ImageView headpltp;
        ViewHolder(View view){
            super(view);
            if (itemView == mHeaderView) {
                headtouxiang=view.findViewById(R.id.comheadtouxiang);
                headnicheng=view.findViewById(R.id.comheadnicheng);
                headtime=view.findViewById(R.id.comheadtime);
                headneirong=view.findViewById(R.id.comheadneirong);
                headrc=view.findViewById(R.id.comheadrv);
                dianzanimg=view.findViewById(R.id.comheaddzimg);
                dianzantext=view.findViewById(R.id.comheaddztext);
                headpltp=view.findViewById(R.id.comheadplimg);
                pltext=view.findViewById(R.id.comheadpltext);
                shoucangimg=view.findViewById(R.id.comheadscimg);
                shoucangtext=view.findViewById(R.id.comheadsctext);
            }
            comtx=view.findViewById(R.id.commentertx);
            comnicheng=view.findViewById(R.id.commenternicheng);
            comtime=view.findViewById(R.id.commenttime);
            comneirong=view.findViewById(R.id.commentneirong);
            comrv=view.findViewById(R.id.commentimgrv);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new CommentAdapter.ViewHolder(mHeaderView);
        }
        View view = mInflater.inflate(R.layout.commentlist, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL) {
            if (holder instanceof ViewHolder) {
                byte[] imageBytes = Base64.decode(commentbeanlist.get(position).getTouxiang(), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ((ViewHolder) holder).comtx.setImageBitmap(decodedImage);
                if(commentbeanlist.get(position).getId().equals(commentbeanlist.get(0).getId())){
                    ((ViewHolder) holder).comnicheng.setText("楼主");
                    ((ViewHolder) holder).comnicheng.setTextColor(android.graphics.Color.parseColor("#0BE6D1"));
                }
                else ((ViewHolder) holder).comnicheng.setText(commentbeanlist.get(position).getNicheng());
                ((ViewHolder) holder).comtime.setText(Timejisuan.computePastTime(commentbeanlist.get(position).getTime()));
                ((ViewHolder) holder).comneirong.setText(commentbeanlist.get(position).getNeirong());
                BigImgAdapter bigImgAdapter = new BigImgAdapter(context);
                bigImgAdapter.setOnItemClickListener(new BigImgAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position1, View v) {
                        Intent intent = new Intent(context,PictBig.class);
                        intent.putExtra("TUPIAN",commentbeanlist.get(position).getImglist().get(position1));
                        context.startActivity(intent);
                    }
                });
                bigImgAdapter.setImglist(commentbeanlist.get(position).getImglist());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                ((ViewHolder) holder).comrv.setLayoutManager(linearLayoutManager);
                ((ViewHolder) holder).comrv.setAdapter(bigImgAdapter);
                ((ViewHolder) holder).comrv.setVisibility(View.VISIBLE);
                if (mItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int adapterPosition = holder.getAdapterPosition();
                            mItemClickListener.onItemClick(adapterPosition, v);
                        }
                    });
                }
                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER) {
            shoucang("0");
            byte[] imageBytes = Base64.decode(commentbeanlist.get(0).getTouxiang(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            ((ViewHolder) holder).headtouxiang.setImageBitmap(decodedImage);
            ((ViewHolder) holder).headtime.setText(Timejisuan.computePastTime(commentbeanlist.get(0).getTime()));
            ((ViewHolder) holder).headneirong.setText(commentbeanlist.get(0).getNeirong());
            BigImgAdapter bigImgAdapter = new BigImgAdapter(context);
            bigImgAdapter.setImglist(commentbeanlist.get(position).getImglist());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ((ViewHolder) holder).headrc.setLayoutManager(linearLayoutManager);
            ((ViewHolder) holder).headrc.setAdapter(bigImgAdapter);
            ((ViewHolder) holder).headrc.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).headtouxiang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,Moshengren.class);
                    intent.putExtra("MYID",MYID);
                    intent.putExtra("MYNICHENG",MYNICHENG);
                    intent.putExtra("MYTP",MYTP);
                    intent.putExtra("HEID",commentbeanlist.get(0).getId());
                    intent.putExtra("HENICHENG",commentbeanlist.get(0).getNicheng());
                    context.startActivity(intent);
                }
            });
            dianzanimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dzmei==false){
                        dianzanimg.setImageResource(R.mipmap.dianzanyes);
                        dianzantext.setText(Integer.parseInt(commentbeanlist.get(0).getDianzan())+1+"");
                        commentbeanlist.get(0).setDianzan(Integer.parseInt(commentbeanlist.get(0).getDianzan())+1+"");
                        dzmei=true;
                        addianzan(1);
                    }
                    else if(dzmei==true){
                        dianzanimg.setImageResource(R.mipmap.dianzanno);
                        dianzantext.setText(Integer.parseInt(commentbeanlist.get(0).getDianzan())-1+"");
                        commentbeanlist.get(0).setDianzan(Integer.parseInt(commentbeanlist.get(0).getDianzan())-1+"");
                        dzmei=false;
                        addianzan(2);
                    }
                }
            });
            dianzantext.setText(commentbeanlist.get(0).getDianzan());
            pltext.setText(commentbeanlist.size()-1+"");
            shoucangimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(scmei==false){
                        shoucang("1");
                        scmei=true;
                        shoucangimg.setImageResource(R.mipmap.myshoucang);
                        shoucangtext.setText("已收藏");
                    }
                    else if(scmei==true){
                        shoucang("2");
                        scmei=false;
                        shoucangimg.setImageResource(R.mipmap.shoucangno);
                        shoucangtext.setText("收藏");
                    }
                }
            });
            pltext.setText(commentbeanlist.size()-1+"");
        }
    }

    private CommentAdapter.OnItemClickListener mItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(CommentAdapter.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
    @Override
    public int getItemCount() {
        if(commentbeanlist==null)return 0;
        else return commentbeanlist.size();
    }
    public void shoucang(String str){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=shoucang";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=shoucang";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new JSONObject();
        try {
            jsob.put("MYID", MYID);
            jsob.put("HEID", commentbeanlist.get(0).getId());
            jsob.put("HETIME",commentbeanlist.get(0).getTime());
            jsob.put("STR",str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType type1 = MediaType.parse("appliaction/json;charset=utf-8");
        RequestBody body1 = RequestBody.create(type1, jsob + "");
        Request request = new Request.Builder()
                .url(path)
                .post(body1)
                .build();
        client.newCall(request).enqueue(new Callback() {
            //失败
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setMessage("网络或服务器原因加载失败");
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                Looper.loop();
            }

            //成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(str.equals("0")) {
                    boolean setshoucang = false;
                    if (result.equals("0")) {
                        setshoucang = false;
                    } else if (result.equals("1")) {
                        setshoucang = true;
                    }
                    Message mes = handler.obtainMessage();
                    mes.what = 111;
                    mes.obj = setshoucang;
                    handler.sendMessage(mes);
                }
                else if(str.equals("1")){
                    if (result.equals("1")) {

                    }
                }
                else if(str.equals("2")){
                    if (result.equals("1")) {

                    }
                }
            }
        });
    }
    public void addianzan(int biaoji){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=adddianzan";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=adddianzan";
        OkHttpClient client = new OkHttpClient();
        JSONObject jsob = new JSONObject();
        try {
            jsob.put("senderid",commentbeanlist.get(0).getId());
            jsob.put("time",commentbeanlist.get(0).getTime());
            jsob.put("biaoji",biaoji);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType type1 = MediaType.parse("appliaction/json;charset=utf-8");
        RequestBody body1 = RequestBody.create(type1, jsob + "");
        Request request = new Request.Builder()
                .url(path)
                .post(body1)
                .build();
        client.newCall(request).enqueue(new Callback() {
            //失败
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setMessage("网络或服务器原因点赞失败");
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                Looper.loop();
            }
            //成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }
    public void addcommentdianzan(int position,String biaoji){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=addcommentdianzan";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=addcommentdianzan";
        OkHttpClient client = new OkHttpClient();
        JSONObject jsob = new JSONObject();
        try {
            jsob.put("senderid",commentbeanlist.get(0).getId());
            jsob.put("time",commentbeanlist.get(0).getTime());
            jsob.put("commenterid",commentbeanlist.get(position).getId());
            jsob.put("commentertime",commentbeanlist.get(position).getTime());
            jsob.put("biaoji",biaoji);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType type1 = MediaType.parse("appliaction/json;charset=utf-8");
        RequestBody body1 = RequestBody.create(type1, jsob + "");
        Request request = new Request.Builder()
                .url(path)
                .post(body1)
                .build();
        client.newCall(request).enqueue(new Callback() {
            //失败
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setMessage("网络或服务器原因点赞失败");
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                Looper.loop();
            }
            //成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }
}
