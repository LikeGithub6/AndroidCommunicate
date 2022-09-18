package com.example.teamwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog progressDialog;
    Button but;
    TextView ed1;
    List<UserBean> list=new ArrayList<>();
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            list=(List<UserBean>)msg.obj;
        }
    };
    private SqliteDB databaseHelper;   //用于创建帮助器对象
    private SQLiteDatabase db;   //用于创建数据库对象
    private static final String name = "JZ.db"; //数据库名称
    private static final int version = 1; //数据库版本
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        but=findViewById(R.id.dlbt);
        ed1=(TextView) findViewById(R.id.zced);
        but.setOnClickListener(this);
        ed1.setOnClickListener(this);
        databaseHelper = new SqliteDB(this, name, null, version);
        SqliteDB moh=new SqliteDB(this,"JZ.db", null, 1);
        SQLiteDatabase db = moh.getReadableDatabase(); // 以只读的方式打开数据库
        String sql = "select * from lastDL;";
        String id="";
        String cishu="";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex("id"));
            cishu=cursor.getString(cursor.getColumnIndex("cishu"));
        }
        cursor.close();
        if(!id.equals("")&&cishu.equals("1")){
            Intent intent = new Intent(MainActivity.this,User.class);
            intent.putExtra("ThisID",id);
            //intent.putExtra("MYTP",list1.get(0).getTupian());
            //intent.putExtra("nicheng",list1.get(0).getNicheng());
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dlbt:
                EditText EditDLid=(EditText)findViewById(R.id.DLID);
                String DLId= EditDLid.getText().toString();
                EditText EditDLPA=(EditText)findViewById(R.id.DLPA);
                String DLPA= EditDLPA.getText().toString();
                if(panduan(DLPA)){
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage("密码中含有特殊字符");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                }
                else if(DLId.length()!=6){
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage("账号输入有误");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                }
                else if(DLPA.length()<6){
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage("密码长度有误");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                }
                else denglu(v);
                break;
            case R.id.zced:
                Intent intent = new Intent(MainActivity.this,zhuce.class);
                startActivity(intent);
                break;

        }
    }
    public void denglu(View view){
        progressDialog = MaskUtil.showProgressDialog("登陆中",MainActivity.this);
        EditText EditDLid=(EditText)findViewById(R.id.DLID);
        String DLId= EditDLid.getText().toString();
        EditText EditDLPA=(EditText)findViewById(R.id.DLPA);
        String DLPA= EditDLPA.getText().toString();
        String path = "http://8.130.12.58:8080/Androidservices/services?method=denglu";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder requestBuild = new FormBody.Builder();
        //添加请求体
        RequestBody requestBody = requestBuild
                .add("id", DLId)
                .add("password", DLPA)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            //失败
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                progressDialog.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("网络或服务器原因登录失败");
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
                progressDialog.dismiss();
                if(result.equals("0")) {
                    Looper.prepare();
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage("该用户不存在");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                    Looper.loop();

                }
                else if(result.equals("1")){
                    Looper.prepare();
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage("密码错误");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                    Looper.loop();
                }
                else if(result.equals("2")){
                    //Looper.prepare();
                    //Intent intent = new Intent(MainActivity.this,User.class);
                    netweb();
                    //finish();
                    //intent.putExtra("ThisID",DLId);
                    //intent.putExtra("MYTP",list.get(0).getTupian());
                    //startActivity(intent);
                    //finish();
                    //Looper.loop();
                }
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }
    public void netweb(){
        EditText EditDLid=(EditText)findViewById(R.id.DLID);
        String DLId= EditDLid.getText().toString();
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=retme&id="+DLId;
        String path="http://8.130.12.58:8080/Androidservices/services?method=retme&id="+DLId;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
                List<UserBean> list1=new ArrayList<>();
                for (int i = 0; i <jsar.size(); i++) {
                    JSONObject jsob = jsar.getJSONObject(i);
                    String id = jsob.getString("id");
                    String pass = jsob.getString("password");
                    String nic=jsob.getString("nicheng");
                    String sex=jsob.getString("sex");
                    String tup=jsob.getString("tupian");
                    String gexi=jsob.getString("gexing");
                    list1.add(new UserBean(id,pass,nic,sex,tup,gexi));
                }
                Intent intent = new Intent(MainActivity.this,User.class);
                intent.putExtra("ThisID",DLId);
                //intent.putExtra("MYTP",list1.get(0).getTupian());
                //intent.putExtra("nicheng",list1.get(0).getNicheng());
                startActivity(intent);
                finish();
                //Looper.loop();
            }
        });
    }
    public static boolean panduan(String editText) {
        String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(speChat);
        Matcher matcher = pattern.matcher(editText);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }
}