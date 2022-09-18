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

import net.sf.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MoshengAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==111){
                boolean b=(boolean)msg.obj;
                gzmei=b;
                if(gzmei==false)GZIMG.setImageResource(R.mipmap.guanzhu);
                else if(gzmei==true)GZIMG.setImageResource(R.mipmap.yiguanzhu);
            }
        }
    };
    public static final int TYPE_HEADER = 0;  //说明是带有Header的

    public static final int TYPE_NORMAL = 2;
    private LayoutInflater mInflater;
    private Context context;
    private List<MoShengBean> mainmoshenglist;
    private UserBean hebean;
    private String MYID;
    private String MYTP;
    private boolean gzmei=false;
    private View mHeaderView;
    private ImageView GZIMG;
    MoshengAdapter(Context context){
        this.context=context;
        mInflater=LayoutInflater.from(context);
    }
    public void setMainlmoshenglist(List<MoShengBean> list){
        mainmoshenglist=list;
    }
    public void sethebean(UserBean b){
        hebean=b;
    }
    public void setMYID(String id){MYID=id;}
    public void setMYTP(String tp){MYTP=tp;}
    class headerViewHolder extends MoshengAdapter.ViewHolder{
        headerViewHolder(View view){
            super(view);
        }
    }
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
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
        ImageView mstouxiang;
        TextView msnicheng;
        TextView mstime;
        TextView msneirong;
        TextView mstopic;
        RecyclerView msrv;
        ImageView msdzimg;
        TextView msdztext;
        ImageView msplimg;
        TextView mspltext;

        ImageView MSSIXIN;
        ImageView MSTOUXIANG;
        TextView MSNICHENG;
        ImageView MSSEX;
        TextView MSQIANMING;
        ViewHolder(View view){
            super(view);
            if (itemView == mHeaderView){
                MSTOUXIANG=view.findViewById(R.id.MStouxiang);
                MSNICHENG=view.findViewById(R.id.MSnicheng);
                MSSEX=view.findViewById(R.id.MSsex);
                MSQIANMING=view.findViewById(R.id.MSqianming);
                GZIMG=view.findViewById(R.id.MSguanzhu);
                MSSIXIN=view.findViewById(R.id.MSsixin);
                return;
            }
                mstouxiang = view.findViewById(R.id.msbitouxiang);
                msnicheng = view.findViewById(R.id.msbinicheng);
                mstime = view.findViewById(R.id.msbitime);
                msneirong = view.findViewById(R.id.msbineirong);
                mstopic = view.findViewById(R.id.msbitopic);
                msrv = view.findViewById(R.id.msbirv);
                msdzimg = view.findViewById(R.id.msbidianzanimg);
                msdztext = view.findViewById(R.id.msbidianzantext);
                msplimg = view.findViewById(R.id.msbipinglunimg);
                mspltext = view.findViewById(R.id.msbipingluntext);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ViewHolder(mHeaderView);
        }
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.moshengmainitem, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof ViewHolder) {
                byte[] imageBytes = Base64.decode(mainmoshenglist.get(position - 1).getTouxiang(), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ((ViewHolder) holder).mstouxiang.setImageBitmap(decodedImage);
                ((ViewHolder) holder).msnicheng.setText(mainmoshenglist.get(position - 1).getNicheng());
                ((ViewHolder) holder).mstime.setText(Timejisuan.computePastTime(mainmoshenglist.get(position - 1).getTime()));
                ((ViewHolder) holder).mstopic.setText(mainmoshenglist.get(position - 1).getHuati());
                ((ViewHolder) holder).msneirong.setText(mainmoshenglist.get(position - 1).getNeirong());
                BigImgAdapter bigImgAdapter = new BigImgAdapter(context);
                bigImgAdapter.setImglist(mainmoshenglist.get(position - 1).getImglist());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                ((ViewHolder) holder).msrv.setLayoutManager(linearLayoutManager);
                ((ViewHolder) holder).msrv.setAdapter(bigImgAdapter);
                ((ViewHolder) holder).msrv.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).msdztext.setText(mainmoshenglist.get(position - 1).getDianzan());
                ((ViewHolder) holder).mspltext.setText(mainmoshenglist.get(position - 1).getPinglun());
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
            guanzhulema();
            byte[] imageBytes = Base64.decode(hebean.getTupian(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            ((ViewHolder) holder).MSTOUXIANG.setImageBitmap(decodedImage);
            ((ViewHolder) holder).MSNICHENG.setText(hebean.getNicheng());
            if(hebean.getSex().equals("男")){
                ((ViewHolder) holder).MSSEX.setImageResource(R.mipmap.sexman);
            }
            else if(hebean.getSex().equals("女")){
                ((ViewHolder) holder).MSSEX.setImageResource(R.mipmap.sexwoman);
            }
            ((ViewHolder) holder).MSQIANMING.setText(hebean.getGexing());
            if(gzmei==false)GZIMG.setImageResource(R.mipmap.guanzhu);
            else GZIMG.setImageResource(R.mipmap.yiguanzhu);
            GZIMG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MYID.equals(hebean.getId())) {
                        Toast.makeText(context, "不能关注自己", Toast.LENGTH_SHORT).show();
                    } else {
                        if (gzmei == false) {
                            guanzhu();
                            gzmei = true;
                            GZIMG.setImageResource(R.mipmap.yiguanzhu);
                        } else if (gzmei == true) {
                            guanzhuquxiao();
                            gzmei = false;
                            GZIMG.setImageResource(R.mipmap.guanzhu);
                        }
                    }
                }
            });
            ((ViewHolder) holder).MSSIXIN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,MainChat.class);
                    intent.putExtra("HEID",hebean.getId());
                    intent.putExtra("HETP",hebean.getTupian());
                    intent.putExtra("HENICHENG",hebean.getNicheng());
                    intent.putExtra("MYID",MYID);
                    intent.putExtra("MYTP",MYTP);
                    context.startActivity(intent);
                }
            });
            return;
        }
    }
    @Override
    public int getItemCount() {
        if(mainmoshenglist==null)return 1;
        else return mainmoshenglist.size()+1;
    }
    private MoshengAdapter.OnItemClickListener mItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(MoshengAdapter.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
    public void guanzhulema(){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=guanzhulema";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=guanzhulema";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new JSONObject();
        try {
            jsob.put("MYID", MYID);
            jsob.put("HEID", hebean.getId());
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
                boolean setguanzhu=false;
                if(result.equals("0")){
                    setguanzhu=false;
                }
                else if(result.equals("1")){
                    setguanzhu=true;
                }
                Message mes=handler.obtainMessage();
                mes.what=111;
                mes.obj=setguanzhu;
                handler.sendMessage(mes);
            }
        });
    }
    public void guanzhu() {
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=guanzhu";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=guanzhu";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new JSONObject();
        try {
            jsob.put("MYID", MYID);
            jsob.put("HEID", hebean.getId());
            jsob.put("HENICHENG",hebean.getNicheng());
            jsob.put("HETOUXIANG", hebean.getTupian());
            jsob.put("HESEX",hebean.getSex());
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
                alertDialog.setMessage("网络或服务器原因关注失败");
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
                if(result.equals("1")){
                    Looper.prepare();
                    Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }
    public void guanzhuquxiao(){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=guanzhuquxiao";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=guanzhuquxiao";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new JSONObject();
        try {
            jsob.put("MYID", MYID);
            jsob.put("HEID", hebean.getId());
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
                alertDialog.setMessage("网络或服务器原因取消关注失败");
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
                if(result.equals("1")){
                    Looper.prepare();
                    Toast.makeText(context, "取消关注成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

}
