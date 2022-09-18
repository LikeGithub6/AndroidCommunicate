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
import android.widget.Toast;

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

public class MeGuanZhu extends AppCompatActivity {
    String MYID="";
    String MYTP;
    String MYNICHENG;
    RecyclerView recyclerView;
    MeguanzhuAdapter adapter;
    List<MeguanzhuBean> Mainlist=new ArrayList<>();
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==111){
                Mainlist=(List<MeguanzhuBean>)msg.obj;
                //Toast.makeText(MeGuanZhu.this, "话题:"+Mainlist.size(), Toast.LENGTH_SHORT).show();
                recyclerView.setLayoutManager(new LinearLayoutManager(MeGuanZhu.this));
                adapter=new MeguanzhuAdapter(MeGuanZhu.this);
                if(Mainlist!=null)adapter.setMainlist(Mainlist);
                if(Mainlist!=null)recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new MeguanzhuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(MeGuanZhu.this,Moshengren.class);
                        intent.putExtra("MYID",MYID);
                        intent.putExtra("MYNICHENG",MYNICHENG);
                        intent.putExtra("MYTP",MYTP);
                        intent.putExtra("HEID",Mainlist.get(position).getId());
                        intent.putExtra("HENICHENG",Mainlist.get(position).getNicheng());
                        startActivity(intent);
                    }
                });
            }
        }
    };
    private Toolbar guanzhubar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_guan_zhu);
        MYID=getIntent().getStringExtra("MYID");
        MYTP=getIntent().getStringExtra("MYTP");
        MYNICHENG=getIntent().getStringExtra("MYNICHENG");
        getmeguanzhu();
        recyclerView=findViewById(R.id.meguanzhurv);
        guanzhubar=findViewById(R.id.Meguanzhubar);
        guanzhubar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void getmeguanzhu(){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=getmeguanzhu";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=getmeguanzhu";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new JSONObject();
        try {
            jsob.put("MYID", MYID);
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
                AlertDialog alertDialog = new AlertDialog.Builder(MeGuanZhu.this).create();
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
                List<MeguanzhuBean> list1=new ArrayList<>();
                MeguanzhuBean bean=null;
                for (int i = 0; i <jsar.size(); i++) {
                    net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                    String id=jsob.getString("heid");
                    String touxiang = jsob.getString("hetouxiang");
                    String nicheng = jsob.getString("henicheng");
                    String sex=jsob.getString("hesex");
                    bean=new MeguanzhuBean(id,touxiang,nicheng,sex);
                    list1.add(bean);
                }
                Message mes=handler.obtainMessage();
                mes.what=111;
                mes.obj=list1;
                handler.sendMessage(mes);
            }
        });
    }
}