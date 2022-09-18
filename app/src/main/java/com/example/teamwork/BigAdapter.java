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

public class BigAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
            }
            else if(msg.what==REF_remen){
                List<MainTopicBean> lb=(List<MainTopicBean>)msg.obj;
                if(lb.size()>0) {
                    int count = maintopicbeanlist.size();
                    maintopicbeanlist.add(lb.get(0));
                    notifyItemInserted(count);
                }
            }
            else if(msg.what==REF_zuixin){
                List<MainTopicBean> lb=(List<MainTopicBean>)msg.obj;
                if(lb.size()>0) {
                    int count = maintopicbeanlist.size();
                    maintopicbeanlist.add(lb.get(0));
                    notifyItemInserted(count);
                }
            }
            else if(msg.what==REF_zuixin){
                List<MainTopicBean> lb=(List<MainTopicBean>)msg.obj;
                if(lb.size()>0) {
                    int count = maintopicbeanlist.size();
                    maintopicbeanlist.add(lb.get(0));
                    notifyItemInserted(count);
                }
            }
        }
    };
    private String listtype="";
    private static  final String STR_remen="remen";
    private static  final String STR_zuixin="zuixin";
    private static  final String STR_guanzhu="guanzhu";
    private static final int REF_remen=222;
    private static  final int REF_zuixin=333;
    private static  final int REF_guanzhu=444;
    public static final int TYPE_Normal = 0;
    public static final int TYPE_Footer = 1;
    private UserBean mybean;
    private LayoutInflater mInflater;
    private Context context;
    private List<MainTopicBean> maintopicbeanlist;
    public BigAdapter(Context context){
        this.context=context;
        mInflater=LayoutInflater.from(context);
    }
    public void setMaintopicbeanlist(List<MainTopicBean> maintopicbeanlist) {
        this.maintopicbeanlist = maintopicbeanlist;
    }
    public void setMybean(UserBean userBean){
        mybean=userBean;
    }
    public void setlisttype(String s){
        listtype=s;
    }
    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1)return TYPE_Footer;
        else return TYPE_Normal;
    }
    @Override
    public BigAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_Normal) {
            View view = mInflater.inflate(R.layout.mainitem, parent, false);
            return new ViewHolder(view);
        }
        else if(viewType==TYPE_Footer){
            View view=mInflater.inflate(R.layout.loadingfooter,parent,false);
            return new ViewHolder(view);
        }
        else return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_Normal) {
            byte[] imageBytes = Base64.decode(maintopicbeanlist.get(position).getTouxiang(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            ((ViewHolder) holder).bigtouxiang.setImageBitmap(decodedImage);
            ((ViewHolder) holder).bignicheng.setText(maintopicbeanlist.get(position).getNicheng());
            ((ViewHolder) holder).bigneirong.setText(maintopicbeanlist.get(position).getNeirong());
            ((ViewHolder) holder).bigbiaoqian.setText(maintopicbeanlist.get(position).getTopic());
            BigImgAdapter bigImgAdapter = new BigImgAdapter(context);
            bigImgAdapter.setOnItemClickListener(new BigImgAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position1, View v) {
                    Intent intent = new Intent(context, PictBig.class);
                    intent.putExtra("TUPIAN", maintopicbeanlist.get(position).getImglist().get(position1));
                    context.startActivity(intent);
                }
            });
            bigImgAdapter.setImglist(maintopicbeanlist.get(position).getImglist());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ((ViewHolder) holder).imgrecyclerView.setLayoutManager(linearLayoutManager);
            ((ViewHolder) holder).imgrecyclerView.setAdapter(bigImgAdapter);
            ((ViewHolder) holder).imgrecyclerView.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).bigshanchu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setMessage("确认不想显示这一条内容吗");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我再想想", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "我不想看见", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            maintopicbeanlist.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, maintopicbeanlist.size());
                        }
                    });
                    alertDialog.show();
                }
            });
            ((ViewHolder) holder).bigdztext.setText(maintopicbeanlist.get(position).getDianzan());
            ((ViewHolder) holder).bigpltext.setText(maintopicbeanlist.get(position).getPinglun());
            ((ViewHolder) holder).bigdianzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (maintopicbeanlist.get(position).isDianmei() == false) {
                        ((ViewHolder) holder).bigdianzan.setImageResource(R.mipmap.dianzanyes);
                        maintopicbeanlist.get(position).setDianmei(true);
                        ((ViewHolder) holder).bigdztext.setText(Integer.parseInt(maintopicbeanlist.get(position).getDianzan()) + 1 + "");
                        maintopicbeanlist.get(position).setDianzan(Integer.parseInt(maintopicbeanlist.get(position).getDianzan()) + 1 + "");
                        addianzan(position, 1);
                        //holder.bigdztext.setText("点赞了");
                    } else if (maintopicbeanlist.get(position).isDianmei() == true) {
                        ((ViewHolder) holder).bigdianzan.setImageResource(R.mipmap.dianzanno);
                        maintopicbeanlist.get(position).setDianmei(false);
                        ((ViewHolder) holder).bigdztext.setText(Integer.parseInt(maintopicbeanlist.get(position).getDianzan()) - 1 + "");
                        maintopicbeanlist.get(position).setDianzan(Integer.parseInt(maintopicbeanlist.get(position).getDianzan()) - 1 + "");
                        addianzan(position, 2);
                        //holder.bigdztext.setText("没点赞");
                    }
                }
            });
            ((ViewHolder) holder).bigpltext.setText(maintopicbeanlist.get(position).getPinglun());
            ((ViewHolder) holder).bigtouxiang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Moshengren.class);
                    intent.putExtra("MYID", mybean.getId());
                    intent.putExtra("MYNICHENG", mybean.getNicheng());
                    intent.putExtra("MYTP", mybean.getTupian());
                    intent.putExtra("HEID", maintopicbeanlist.get(position).getId());
                    intent.putExtra("HENICHENG", maintopicbeanlist.get(position).getNicheng());
                    context.startActivity(intent);
                }
            });
        }
        else if(getItemViewType(position)==TYPE_Footer){
            if(listtype.equals(STR_remen)){
                remenref();
            }
            else if(listtype.equals(STR_zuixin)){
                zuixinref();
            }
            else if(listtype.equals(STR_guanzhu)){

            }
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
        if(maintopicbeanlist==null)return 0;
        else return maintopicbeanlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView bigtouxiang;
        TextView bignicheng;
        ImageView bigshanchu;
        TextView bigneirong;
        TextView bigbiaoqian;
        RecyclerView imgrecyclerView;
        ImageView bigdianzan;
        TextView bigdztext;
        ImageView bigpl;
        TextView bigpltext;
        ViewHolder(View view){
            super(view);
            bigtouxiang=view.findViewById(R.id.maidtouxiang);
            bignicheng=view.findViewById(R.id.maidnicheng);
            bigshanchu=view.findViewById(R.id.maidshanchu);
            bigneirong=view.findViewById(R.id.maidneirong);
            bigbiaoqian=view.findViewById(R.id.maidbiaoqian);
            imgrecyclerView=view.findViewById(R.id.imgrecycleview);
            bigdianzan=view.findViewById(R.id.bigdianzanimg);
            bigdianzan=view.findViewById(R.id.bigdianzanimg);
            bigdztext=view.findViewById(R.id.bigdianzantext);
            bigpl=view.findViewById(R.id.bigpinglunimg);
            bigpltext=view.findViewById(R.id.bigpingluntext);
        }
    }
    private OnItemClickListener mItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
    public void addianzan(int position,int biaoji){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=adddianzan";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=adddianzan";
        OkHttpClient client = new OkHttpClient();
        JSONObject jsob = new JSONObject();
        try {
            jsob.put("senderid",maintopicbeanlist.get(position).getId());
            jsob.put("time",maintopicbeanlist.get(position).getTime());
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
    public void remenref(){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=getremen";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=getremenref";
        OkHttpClient client = new OkHttpClient();
        JSONObject jsob = new JSONObject();
        try {
            jsob.put("lsize",getItemCount());
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
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setMessage("网络或服务器原因连接失败");
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
                //progressDialog.dismiss();
                //Looper.prepare();
                String result = response.body().string();
                JSONArray jsar = JSONArray.fromObject(result);
                List<MainTopicBean> list1=new ArrayList<>();
                MainTopicBean bean=null;
                for (int i = 0; i <jsar.size(); i++) {
                    net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                    String id = jsob.getString("id");
                    String touxiang=jsob.getString("touxiang");
                    String nicheng=jsob.getString("nicheng");
                    String neirong=jsob.getString("neirong");
                    String topic=jsob.getString("topic");
                    String imgsize=jsob.getString("size");
                    String time=jsob.getString("time");
                    List<String> imgl=new ArrayList<>();
                    if(imgsize.equals("1")){
                        String img1=jsob.getString("pi1");
                        imgl.add(img1);
                    }
                    if(imgsize.equals("2")) {
                        String img1=jsob.getString("pi2");
                        String img2=jsob.getString("pi3");
                        imgl.add(img1);
                        imgl.add(img2);
                    }
                    if(imgsize.equals("3")) {
                        String img1=jsob.getString("pi1");
                        String img2=jsob.getString("pi2");
                        String img3=jsob.getString("pi3");
                        imgl.add(img1);
                        imgl.add(img2);
                        imgl.add(img3);
                    }
                    String dianzan=jsob.getString("dianzan");
                    String pinglun=jsob.getString("pinglun");
                    bean=new MainTopicBean(id,touxiang,nicheng,neirong,topic,imgsize,time,imgl,dianzan,pinglun);
                    list1.add(bean);
                }
                Message mes=handler.obtainMessage();
                mes.obj=list1;
                mes.what=REF_remen;
                handler.sendMessage(mes);
                //Looper.loop();
            }
        });
    }
    public void zuixinref(){
        String path = "http://8.130.12.58:8080/Androidservices/services?method=getzuixinref";
        OkHttpClient client = new OkHttpClient();
        JSONObject jsob = new JSONObject();
        try {
            jsob.put("lsize",getItemCount());
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
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setMessage("网络或服务器原因连接失败");
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
                //progressDialog.dismiss();
                //Looper.prepare();
                String result = response.body().string();
                JSONArray jsar = JSONArray.fromObject(result);
                List<MainTopicBean> list1=new ArrayList<>();
                MainTopicBean bean=null;
                for (int i = 0; i <jsar.size(); i++) {
                    net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                    String id = jsob.getString("id");
                    String touxiang=jsob.getString("touxiang");
                    String nicheng=jsob.getString("nicheng");
                    String neirong=jsob.getString("neirong");
                    String topic=jsob.getString("topic");
                    String imgsize=jsob.getString("size");
                    String time=jsob.getString("time");
                    List<String> imgl=new ArrayList<>();
                    if(imgsize.equals("1")){
                        String img1=jsob.getString("pi1");
                        imgl.add(img1);
                    }
                    if(imgsize.equals("2")) {
                        String img1=jsob.getString("pi2");
                        String img2=jsob.getString("pi3");
                        imgl.add(img1);
                        imgl.add(img2);
                    }
                    if(imgsize.equals("3")) {
                        String img1=jsob.getString("pi1");
                        String img2=jsob.getString("pi2");
                        String img3=jsob.getString("pi3");
                        imgl.add(img1);
                        imgl.add(img2);
                        imgl.add(img3);
                    }
                    String dianzan=jsob.getString("dianzan");
                    String pinglun=jsob.getString("pinglun");
                    bean=new MainTopicBean(id,touxiang,nicheng,neirong,topic,imgsize,time,imgl,dianzan,pinglun);
                    list1.add(bean);
                }
                Message mes=handler.obtainMessage();
                mes.obj=list1;
                mes.what=REF_zuixin;
                handler.sendMessage(mes);
                //Looper.loop();
            }
        });
    }
    public void guanzhuref(){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=getremen";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=getguanzhutopicref";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new org.json.JSONObject();
        try {
            jsob.put("lsize",getItemCount());
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
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setMessage("网络或服务器原因连接失败");
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
                //progressDialog.dismiss();
                //Looper.prepare();
                String result = response.body().string();
                JSONArray jsar = JSONArray.fromObject(result);
                List<MainTopicBean> list1=new ArrayList<>();
                MainTopicBean bean=null;
                for (int i = 0; i <jsar.size(); i++) {
                    net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                    String id = jsob.getString("id");
                    String touxiang=jsob.getString("touxiang");
                    String nicheng=jsob.getString("nicheng");
                    String neirong=jsob.getString("neirong");
                    String topic=jsob.getString("topic");
                    String imgsize=jsob.getString("size");
                    String time=jsob.getString("time");
                    List<String> imgl=new ArrayList<>();
                    if(imgsize.equals("1")){
                        String img1=jsob.getString("pi1");
                        imgl.add(img1);
                    }
                    if(imgsize.equals("2")) {
                        String img1=jsob.getString("pi2");
                        String img2=jsob.getString("pi3");
                        imgl.add(img1);
                        imgl.add(img2);
                    }
                    if(imgsize.equals("3")) {
                        String img1=jsob.getString("pi1");
                        String img2=jsob.getString("pi2");
                        String img3=jsob.getString("pi3");
                        imgl.add(img1);
                        imgl.add(img2);
                        imgl.add(img3);
                    }
                    String dianzan=jsob.getString("dianzan");
                    String pinglun=jsob.getString("pinglun");
                    bean=new MainTopicBean(id,touxiang,nicheng,neirong,topic,imgsize,time,imgl,dianzan,pinglun);
                    list1.add(bean);
                }
                Message mes=handler.obtainMessage();
                mes.obj=list1;
                mes.what=REF_guanzhu;
                handler.sendMessage(mes);
                //Looper.loop();
            }
        });
    }
}
