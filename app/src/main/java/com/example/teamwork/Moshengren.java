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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import net.sf.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Moshengren extends AppCompatActivity {
    String MYID;
    String MYTP;
    String MYNICHENG;
    String HEID;
    UserBean hebean;
    private Toolbar toolbar;
    private RecyclerView mobigrv;
    private MoshengAdapter adapter;
    List<MoShengBean> mainlist=new ArrayList<>();
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==111){
                List<UserBean> list=(List<UserBean>)msg.obj;
                hebean=list.get(0);
                toolbar.setTitle(hebean.getNicheng());
                netweb();
            }
            else if(msg.what==222){
                mainlist = (List<MoShengBean>) msg.obj;
                mobigrv.setLayoutManager(new LinearLayoutManager(Moshengren.this));
                adapter = new MoshengAdapter(Moshengren.this);
                adapter.setMainlmoshenglist(mainlist);
                adapter.sethebean(hebean);
                adapter.setMYID(MYID);
                adapter.setMYTP(MYTP);
                HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
                //stringIntegerHashMap.put(Itemjianju.TOP_DECORATION,50);//top间距

                stringIntegerHashMap.put(Itemjianju.BOTTOM_DECORATION,50);//底部间距

                stringIntegerHashMap.put(Itemjianju.LEFT_DECORATION,20);//左间距

                stringIntegerHashMap.put(Itemjianju.RIGHT_DECORATION,20);//右间距
                adapter.setOnItemClickListener(new MoshengAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(Moshengren.this,Mainxiangxi.class);
                        intent.putExtra("senderid",hebean.getId());
                        intent.putExtra("MYID",MYID);
                        intent.putExtra("MYTP",MYTP);
                        intent.putExtra("MYNICHENG",MYNICHENG);
                        intent.putExtra("sendtime",mainlist.get(position-1).getTime());
                        intent.putExtra("pinglun",mainlist.get(position-1).getPinglun());
                        startActivity(intent);
                    }
                });
                mobigrv.setAdapter(adapter);
                setHeaderView(mobigrv);
                mobigrv.addItemDecoration(new Itemjianju(stringIntegerHashMap));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moshengren);
        MYID=getIntent().getStringExtra("MYID");
        MYTP=getIntent().getStringExtra("MYTP");
        MYNICHENG=getIntent().getStringExtra("MYNICHENG");
        HEID=getIntent().getStringExtra("HEID");
        toolbar=findViewById(R.id.MSINFBAR);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mobigrv=findViewById(R.id.moshengrecycleview);
        getHE();

    }
    private void setHeaderView(RecyclerView view){
        View header = LayoutInflater.from(this).inflate(R.layout.moshengheader, view, false);
        adapter.setHeaderView(header);
    }
    public void netweb() {
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=getmoshengren";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=getmoshengren";
        OkHttpClient client = new OkHttpClient();
        JSONObject jsob = new JSONObject();
        try {
            jsob.put("heid", HEID);
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
                AlertDialog alertDialog = new AlertDialog.Builder(Moshengren.this).create();
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
                JSONArray jsar = JSONArray.fromObject(result);
                List<MoShengBean> list = new ArrayList<>();
                MoShengBean bean = null;
                for (int i = 0; i < jsar.size(); i++) {
                    net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                    String id=jsob.getString("id");
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
                    bean=new MoShengBean(id,touxiang,nicheng,neirong,topic,imgsize,time,imgl,dianzan,pinglun);
                    list.add(bean);
                }
                Message mes=handler.obtainMessage();
                mes.what=222;
                mes.obj=list;
                handler.sendMessage(mes);

            }
        });
    }
    public void getHE() {
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=gethe";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=gethe";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new org.json.JSONObject();
        try {
            jsob.put("heid",HEID);
        }catch (JSONException e) {
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
                    AlertDialog alertDialog = new AlertDialog.Builder(Moshengren.this).create();
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
                    List<UserBean> list1 = new ArrayList<>();
                    for (int i = 0; i < jsar.size(); i++) {
                        net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                        String id = jsob.getString("id");
                        String pass = jsob.getString("password");
                        String nic = jsob.getString("nicheng");
                        String sex = jsob.getString("sex");
                        String tup = jsob.getString("tupian");
                        String gexi = jsob.getString("gexing");
                        list1.add(new UserBean(id, pass, nic, sex, tup, gexi));
                    }
                    Message mes=handler.obtainMessage();
                    mes.obj=list1;
                    mes.what=111;
                    handler.sendMessage(mes);
                    //Looper.loop();
                }
            });
    }
}