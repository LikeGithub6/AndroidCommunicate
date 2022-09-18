package com.example.teamwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Mechangeinf extends AppCompatActivity {
    String MYID="";
    String MYNICHENG="";
    String MYQIANMING="";
    String MYSEX="";
    Toolbar bar;
    EditText nicheng;
    EditText qianming;
    RadioGroup group;
    LinearLayout OL;
    String Onicheng="";
    String Oqm="";
    String Osex="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechangeinf);
        MYID=getIntent().getStringExtra("MYID");
        MYNICHENG=getIntent().getStringExtra("MYNICHENG");
        MYQIANMING=getIntent().getStringExtra("MYGX");
        MYSEX=getIntent().getStringExtra("MYSEX");
        bar=findViewById(R.id.MEchangebar);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("newnicheng",MYNICHENG);
                intent.putExtra("newqianming",MYQIANMING);
                String sex=MYSEX;
                intent.putExtra("newsex",sex);
                setResult(1,intent);
                finish();
            }
        });
        nicheng=findViewById(R.id.MECnicheng);
        nicheng.setText(MYNICHENG);
        qianming=findViewById(R.id.MECqianming);
        qianming.setText(MYQIANMING);
        group=findViewById(R.id.MECGroup);
        if(MYSEX.equals("男"))group.check(R.id.MECSEXMAN);
        else group.check(R.id.MECSEXWOMAN);
        OL=findViewById(R.id.MECL);
        OL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("newnicheng",nicheng.getText().toString());
                intent.putExtra("newqianming",qianming.getText().toString());
                String sex;
                if(group.getCheckedRadioButtonId()==R.id.MECSEXMAN)sex="男";
                else sex="女";
                intent.putExtra("newsex",sex);
                setResult(1,intent);
                changeinf();
                //finish();
            }
        });
    }
    public void changeinf(){
        Onicheng=nicheng.getText().toString();
        Oqm=qianming.getText().toString();
        if(group.getCheckedRadioButtonId()==R.id.MECSEXMAN)Osex="男";
        else Osex="女";
        if(!MYNICHENG.equals(Onicheng)||!MYQIANMING.equals(Oqm)||!MYSEX.equals(Osex)){
            //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=changemyinf";
            String path = "http://8.130.12.58:8080/Androidservices/services?method=changemyinf";
            OkHttpClient client = new OkHttpClient();
            JSONObject jsob = new JSONObject();
            try {
                jsob.put("id",MYID);
                jsob.put("nicheng",Onicheng);
                jsob.put("qianming",Oqm);
                jsob.put("sex", Osex);
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

                    AlertDialog alertDialog = new AlertDialog.Builder(Mechangeinf.this).create();
                    alertDialog.setMessage("网络或服务器原因注册失败");
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

                    if (result.equals("0")) {
                        Looper.prepare();
                        AlertDialog alertDialog = new AlertDialog.Builder(Mechangeinf.this).create();
                        alertDialog.setMessage("修改失败");
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                        Looper.loop();

                    } else if (result.equals("1")) {
                        Looper.prepare();


                        Looper.loop();
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        }
        finish();
    }
}