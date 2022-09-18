package com.example.teamwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainChat extends AppCompatActivity {
    private Bitmap hetp;
    private Bitmap mytp;
    private String HEID;
    private String MYID;
    private String HETP;
    private String MYTP;
    private String HENICHENG;
    private Timer timer;
    private Toolbar bar;
    private RecyclerView chatneirongrecycle;
    private EditText editText;
    private Button sentbut;
    private ChatneirongAdapter adapter;
    private List<ChatneirongBean> chatnrlist;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 111) {
                chatnrlist=(List<ChatneirongBean>)msg.obj;
                chatneirongrecycle.setLayoutManager(new LinearLayoutManager(MainChat.this));
                adapter=new ChatneirongAdapter(MainChat.this,hetp,mytp,HEID,MYID);
                adapter.setChatlist(chatnrlist);
                chatneirongrecycle.setAdapter(adapter);
                HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
                //stringIntegerHashMap.put(Itemjianju.TOP_DECORATION,50);//top间距

                stringIntegerHashMap.put(Itemjianju.BOTTOM_DECORATION, 50);//底部间距

                stringIntegerHashMap.put(Itemjianju.LEFT_DECORATION, 20);//左间距

                stringIntegerHashMap.put(Itemjianju.RIGHT_DECORATION, 20);//右间距
                chatneirongrecycle.addItemDecoration(new Itemjianju(stringIntegerHashMap));

                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // (1) 使用handler发送消息
                        Message message=new Message();
                        message.what=333;
                        handler.sendMessage(message);
                    }
                },0,1000);
            }
            else if(msg.what==222){
                ChatneirongBean chatb=(ChatneirongBean)msg.obj;
                chatnrlist.add(chatb);
                adapter.notifyItemInserted(chatnrlist.size());
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // (1) 使用handler发送消息
                        Message message=new Message();
                        message.what=333;
                        handler.sendMessage(message);
                    }
                },0,1000);
            }
            else if(msg.what==333){
                neirongref();
            }
            else if(msg.what==444){
                List<ChatneirongBean> l=(List<ChatneirongBean>)msg.obj;
                int count=chatnrlist.size();
                //chatnrlist.addAll(l);
                //adapter.notifyItemRangeInserted(count,l.size());
                chatnrlist.add(l.get(0));
                adapter.notifyItemInserted(count);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        String he=getIntent().getStringExtra("HETP");
        String my=getIntent().getStringExtra("MYTP");
        HETP=he;
        MYTP=my;
        HEID=getIntent().getStringExtra("HEID");
        MYID=getIntent().getStringExtra("MYID");
        HENICHENG=getIntent().getStringExtra("HENICHENG");
        byte[] imageBytes1 = Base64.decode(he, Base64.DEFAULT);
        hetp= BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
        byte[] imageBytes2 = Base64.decode(my, Base64.DEFAULT);
        mytp=BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.length);
        bar=findViewById(R.id.chatbar);
        bar.setTitle(HENICHENG);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chatneirongrecycle=findViewById(R.id.chatnrrecycle);
        editText=findViewById(R.id.chatedit);
        sentbut=findViewById(R.id.chatsend);
        sentbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("")){
                    Toast.makeText(MainChat.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    chatwithpeople();
                    editText.setText("");
                }
            }
        });
        getchatneironglist();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    public void getchatneironglist(){
        String path = "http://8.130.12.58:8080/Androidservices/services?method=chatneironglist";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new JSONObject();
        try {
            jsob.put("MYID", MYID);
            jsob.put("HEID", HEID);
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
                AlertDialog alertDialog = new AlertDialog.Builder(MainChat.this).create();
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
                List<ChatneirongBean> l=new ArrayList<>();
                ChatneirongBean b=null;
                for (int i = 0; i <jsar.size(); i++) {
                    net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                    String sendid = jsob.getString("sendid");
                    String time=jsob.getString("time");
                    String neirong=jsob.getString("neirong");
                    b=new ChatneirongBean(sendid,time,neirong);
                    l.add(b);
                }
                Message mes=handler.obtainMessage();
                mes.obj=l;
                mes.what=111;
                handler.sendMessage(mes);
            }
        });
    }
    public void chatwithpeople(){
        timer.cancel();
        String path = "http://8.130.12.58:8080/Androidservices/services?method=chatwithpeople";
        OkHttpClient client = new OkHttpClient();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        org.json.JSONObject jsob = new JSONObject();
        try {
            jsob.put("MYID", MYID);
            jsob.put("HEID", HEID);
            jsob.put("neirong",editText.getText().toString());
            jsob.put("time",formatter.format(date));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ChatneirongBean b=new ChatneirongBean(MYID,formatter.format(date),editText.getText().toString());
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
                AlertDialog alertDialog = new AlertDialog.Builder(MainChat.this).create();
                alertDialog.setMessage("网络或服务器原因发送失败");
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
                Message mes=handler.obtainMessage();
                mes.obj=b;
                mes.what=222;
                handler.sendMessage(mes);
            }
        });
    }
    public void neirongref(){
        String path = "http://8.130.12.58:8080/Androidservices/services?method=chatneirongref";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new JSONObject();
        try {
            jsob.put("MYID", MYID);
            jsob.put("HEID", HEID);
            jsob.put("COUNT",chatnrlist.size()+"");
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

            }

            //成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.equals("1"));
                else {
                    JSONArray jsar = JSONArray.fromObject(result);
                    List<ChatneirongBean> l=new ArrayList<>();
                    ChatneirongBean b=null;
                    for (int i = 0; i <jsar.size(); i++) {
                        net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                        String sendid = jsob.getString("sendid");
                        String time=jsob.getString("time");
                        String neirong=jsob.getString("neirong");
                        b=new ChatneirongBean(sendid,time,neirong);
                        l.add(b);
                    }
                    Message mes=handler.obtainMessage();
                    mes.obj=l;
                    mes.what=444;
                    handler.sendMessage(mes);
                }
            }
        });
    }
}