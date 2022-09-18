package com.example.teamwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class newfragment2 extends Fragment {
    SwipeRefreshLayout relay2;
    private UserBean bean;
    private RecyclerView recyclerView2;
    private BigAdapter bigAdapter2;
    List<MainTopicBean> mainlis2=new ArrayList<>();



    public static final String ARG_PAGE = "ARG_PAGE";
    private boolean IS_LOADED = false;
    private static int mSerial=0;
    private int mTabPos=0;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==111){
                if(!IS_LOADED){
                    netweb();
                    IS_LOADED=true;
                }
            }
            else if (msg.what == 222) {
                mainlis2= (List<MainTopicBean>) msg.obj;
                recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                bigAdapter2 = new BigAdapter(getActivity());
                bigAdapter2.setMaintopicbeanlist(mainlis2);
                bigAdapter2.setMybean(bean);
                bigAdapter2.setlisttype("guanzhu");
                HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
                //stringIntegerHashMap.put(Itemjianju.TOP_DECORATION,50);//top间距

                stringIntegerHashMap.put(Itemjianju.BOTTOM_DECORATION, 50);//底部间距

                stringIntegerHashMap.put(Itemjianju.LEFT_DECORATION, 20);//左间距

                stringIntegerHashMap.put(Itemjianju.RIGHT_DECORATION, 20);//右间距
                bigAdapter2.setOnItemClickListener(new BigAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getContext(), Mainxiangxi.class);
                        intent.putExtra("senderid", mainlis2.get(position).getId());
                        intent.putExtra("MYID", bean.getId());
                        intent.putExtra("MYTP", bean.getTupian());
                        intent.putExtra("MYNICHENG", bean.getNicheng());
                        intent.putExtra("sendtime", mainlis2.get(position).getTime());
                        intent.putExtra("pinglun", mainlis2.get(position).getPinglun());
                        startActivity(intent);
                    }
                });
                recyclerView2.setAdapter(bigAdapter2);
                recyclerView2.addItemDecoration(new Itemjianju(stringIntegerHashMap));
            }
            else if(msg.what==333){
                mainlis2=null;
                mainlis2=new ArrayList<>();
                mainlis2.addAll((List<MainTopicBean>)msg.obj);
                bigAdapter2.setMaintopicbeanlist(mainlis2);
                bigAdapter2.notifyDataSetChanged();
                relay2.setRefreshing(false);
            }

        };
    };


    public void sendMessage(){
        Message message = handler.obtainMessage();
        message.what=111;
        handler.sendMessage(message);
    }
    public newfragment2(UserBean u) {
        bean=u;

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_newfragment2,container,false);
        recyclerView2=view.findViewById(R.id.newfrag2rv);
        relay2=view.findViewById(R.id.new2refresh);
        relay2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                netwebfre();
            }
        });
        return view;
    }
    public void netweb(){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=getremen";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=getguanzhutopic";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new org.json.JSONObject();
        try {
            jsob.put("MYID",bean.getId());
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
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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
                    JSONObject jsob = jsar.getJSONObject(i);
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
                mes.what=222;
                handler.sendMessage(mes);
                //Looper.loop();
            }
        });
    }
    public void netwebfre(){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=getremen";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=getguanzhutopic";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new org.json.JSONObject();
        try {
            jsob.put("MYID",bean.getId());
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
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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
                    JSONObject jsob = jsar.getJSONObject(i);
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
                mes.what=333;
                handler.sendMessage(mes);
                //Looper.loop();
            }
        });
    }
}