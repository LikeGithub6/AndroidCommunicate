package com.example.teamwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import net.sf.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

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

public class tiebashow extends AppCompatActivity {
    private String id;
    RecyclerView showrv;
    List<LtplBean> showlist=new ArrayList<>();
    LtplAdapter adapter;
    Handler handler =new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==111){
                showlist=(List<LtplBean>)msg.obj;
                showrv.setLayoutManager(new LinearLayoutManager(tiebashow.this));
                adapter=new LtplAdapter(tiebashow.this);
                adapter.setLtlist(showlist);
                HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
                //stringIntegerHashMap.put(Itemjianju.TOP_DECORATION,50);//top间距

                stringIntegerHashMap.put(Itemjianju.BOTTOM_DECORATION, 50);//底部间距

                stringIntegerHashMap.put(Itemjianju.LEFT_DECORATION, 20);//左间距

                stringIntegerHashMap.put(Itemjianju.RIGHT_DECORATION, 20);//右间距
                showrv.setAdapter(adapter);
                showrv.addItemDecoration(new Itemjianju(stringIntegerHashMap));
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiebashow);
        id=getIntent().getStringExtra("tiebaid");
        showrv=findViewById(R.id.tiebashowrv);
        gettiebaall();
    }
    public void gettiebaall(){
        String path = "http://8.130.12.58:8080/Androidservices/services?method=tiebashow";
        OkHttpClient client = new OkHttpClient();
        JSONObject jsob = new JSONObject();
        try {
            jsob.put("tiebaid",id);
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

                AlertDialog alertDialog = new AlertDialog.Builder(tiebashow.this).create();
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
                String result = response.body().string();
                JSONArray jsar = JSONArray.fromObject(result);
                List<LtplBean> list=new ArrayList<>();
                LtplBean bean=null;
                Resources res = getResources();
                Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
                for (int i = 0; i <jsar.size(); i++) {
                    net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                    String url=jsob.getString("imgurl");
                    String nicheng=jsob.getString("nicheng");
                    String neirong=jsob.getString("neirong");
                    bean=new LtplBean(id,url,nicheng,neirong,bmp);
                    list.add(bean);
                }
                Message mes=handler.obtainMessage();
                mes.obj=list;
                mes.what=111;
                handler.sendMessage(mes);
            }
        });
    }
}