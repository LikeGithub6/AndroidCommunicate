package com.example.teamwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class newfragment3 extends Fragment {


    private boolean IS_LOADED = false;
    private static int mSerial=0;
    private int mTabPos=0;


    private UserBean bean;
    String MYID="";
    String MYTP="";
    String MYNICHENG;
    private RecyclerView recyclerView3;
    private BigAdapter bigAdapter3;
    List<MainTopicBean> mainl3=new ArrayList<>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 111) {
                if (!IS_LOADED) {
                    IS_LOADED = true;
                    getzuixin();
                }
            }
            else if (msg.what == 222) {
                mainl3= (List<MainTopicBean>) msg.obj;
                recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
                bigAdapter3 = new BigAdapter(getActivity());
                bigAdapter3.setMaintopicbeanlist(mainl3);
                bigAdapter3.setMybean(bean);
                bigAdapter3.setlisttype("zuixin");
                HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
                //stringIntegerHashMap.put(Itemjianju.TOP_DECORATION,50);//top间距

                stringIntegerHashMap.put(Itemjianju.BOTTOM_DECORATION, 50);//底部间距

                stringIntegerHashMap.put(Itemjianju.LEFT_DECORATION, 20);//左间距

                stringIntegerHashMap.put(Itemjianju.RIGHT_DECORATION, 20);//右间距
                bigAdapter3.setOnItemClickListener(new BigAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getContext(), Mainxiangxi.class);
                        intent.putExtra("senderid", mainl3.get(position).getId());
                        intent.putExtra("MYID", bean.getId());
                        intent.putExtra("MYTP", bean.getTupian());
                        intent.putExtra("MYNICHENG", bean.getNicheng());
                        intent.putExtra("sendtime", mainl3.get(position).getTime());
                        intent.putExtra("pinglun", mainl3.get(position).getPinglun());
                        startActivity(intent);
                    }
                });
                recyclerView3.setAdapter(bigAdapter3);
                recyclerView3.addItemDecoration(new Itemjianju(stringIntegerHashMap));
            }
        }
    };

    public newfragment3(UserBean u) {
        bean=u;
    }
    public void sendMessage(){
        Message message = handler.obtainMessage();
        message.what=111;
        handler.sendMessage(message);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MYID=bean.getId();
        MYTP=bean.getTupian();
        MYNICHENG=bean.getNicheng();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_newfragment3, container, false);
        recyclerView3=view.findViewById(R.id.newfrag3rv);
        return view;
    }
    public void getzuixin(){
        String path = "http://8.130.12.58:8080/Androidservices/services?method=getzuixin";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
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
}