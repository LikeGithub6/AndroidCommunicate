package com.example.teamwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainTopicList extends AppCompatActivity {
    String MYID="";
    String MYTP="";
    String MYNICHENG;
    String huati="";
    private Toolbar mabar;
    private RecyclerView recyclerView;
    private BigAdapter bigAdapter;
    List<MainTopicBean> mainlis=new ArrayList<>();
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mainlis=(List<MainTopicBean>)msg.obj;
            recyclerView.setLayoutManager(new LinearLayoutManager(MainTopicList.this));
            bigAdapter=new BigAdapter(MainTopicList.this);
            bigAdapter.setMybean(new UserBean(MYID,"pass",MYNICHENG,"sex",MYTP,"gexing"));
            bigAdapter.setMaintopicbeanlist(mainlis);
            HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
            //stringIntegerHashMap.put(Itemjianju.TOP_DECORATION,50);//top间距

            stringIntegerHashMap.put(Itemjianju.BOTTOM_DECORATION,50);//底部间距

            stringIntegerHashMap.put(Itemjianju.LEFT_DECORATION,20);//左间距

            stringIntegerHashMap.put(Itemjianju.RIGHT_DECORATION,20);//右间距
            bigAdapter.setOnItemClickListener(new BigAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent intent = new Intent(MainTopicList.this,Mainxiangxi.class);
                    intent.putExtra("senderid",mainlis.get(position).getId());
                    intent.putExtra("MYID",MYID);
                    intent.putExtra("MYTP",MYTP);
                    intent.putExtra("MYNICHENG",MYNICHENG);
                    intent.putExtra("sendtime",mainlis.get(position).getTime());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(bigAdapter);
            recyclerView.addItemDecoration(new Itemjianju(stringIntegerHashMap));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topic_list);
        MYID=getIntent().getStringExtra("MYID");
        MYTP=getIntent().getStringExtra("MYTP");
        MYNICHENG=getIntent().getStringExtra("MYNICHENG");
        huati=getIntent().getStringExtra("huati");
        recyclerView=findViewById(R.id.mianrecycleview);
        mabar=findViewById(R.id.mainbar);
        mabar.setTitle(huati);
        mabar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        netweb();
        //Toast.makeText(MainTopicList.this, "话题:"+huati, Toast.LENGTH_SHORT).show();
    }
    public void netweb(){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=gettopiclist&huati="+huati;
        String path = "http://8.130.12.58:8080/Androidservices/services?method=gettopiclist&huati="+huati;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                AlertDialog alertDialog = new AlertDialog.Builder(MainTopicList.this).create();
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
                handler.sendMessage(mes);
                //Looper.loop();
            }
        });
    }
}