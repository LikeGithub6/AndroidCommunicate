package com.example.teamwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class User extends AppCompatActivity implements FragmentForth.CallBackValue{
    private ProgressDialog progressDialog;
    List<UserBean> list=new ArrayList<>();
    ImageView addbut;
    private RadioGroup mTabRadioGroup;
    private SparseArray<Fragment> mFragmentSparseArray;
    String MYID=null;
    String MYTP=null;
    String MYNICHENG=null;
    newfragment1 f1;
    FragmentFirst F1;
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                list=(List<UserBean>)msg.obj;
                MYTP=list.get(0).getTupian();
                MYNICHENG=list.get(0).getNicheng();
                initView();
            }
        }
    };



    private SqliteDB databaseHelper;   //用于创建帮助器对象
    private SQLiteDatabase db;   //用于创建数据库对象
    private static final String name = "JZ.db"; //数据库名称
    private static final int version = 1; //数据库版本



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent=getIntent();
        MYID=intent.getStringExtra("ThisID");
        //MYTP=intent.getStringExtra("MYTP");
        //MYNICHENG=intent.getStringExtra("nicheng");
        denglu();
        //initView();
        databaseHelper = new SqliteDB(this, name, null, version);
        SqliteDB moh=new SqliteDB(this,"JZ.db", null, 1);
        SQLiteDatabase db = moh.getReadableDatabase(); // 以只读的方式打开数据库
        db.execSQL("delete from LastDL");
        db.execSQL("insert into lastDL(id,cishu) values(?, ?)", new Object[]{MYID,"1"});
    }
    private void initView() {
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        mFragmentSparseArray = new SparseArray<>();
        //f1=new newfragment1(list.get(0));
        F1=new FragmentFirst(list.get(0));
        mFragmentSparseArray.append(R.id.today_tab,F1);
        /*
        mFragmentSparseArray.append(R.id.record_tab, new FragmentSecond());
        mFragmentSparseArray.append(R.id.contact_tab, new FragmentThird(MYID));
        mFragmentSparseArray.append(R.id.settings_tab, new firstFragment());

         */
        mTabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 具体的fragment切换逻辑可以根据应用调整，例如使用show()/hide()
               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                      //  mFragmentSparseArray.get(checkedId)).commit();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                //hidefrag(transaction);
                if(mFragmentSparseArray.get(checkedId)==null){
                    if(checkedId==R.id.record_tab){
                        mFragmentSparseArray.append(R.id.record_tab, new FragmentSecond(list.get(0)));
                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                                mFragmentSparseArray.get(R.id.record_tab)).commit();
                    }
                    else if(checkedId==R.id.contact_tab){
                        mFragmentSparseArray.append(R.id.contact_tab, new FragmentThird(MYID,MYTP,MYNICHENG));
                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                                mFragmentSparseArray.get(R.id.contact_tab)).commit();
                    }
                    else if(checkedId==R.id.settings_tab){

                        Fragment f=new FragmentForth(list.get(0));
                        mFragmentSparseArray.append(R.id.settings_tab, f);
                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                                mFragmentSparseArray.get(R.id.settings_tab)).commit();

                    }
                }
                hidefrag(transaction);
                transaction.show(mFragmentSparseArray.get(checkedId)).commit();

            }
        });
        // 默认显示第一个
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                mFragmentSparseArray.get(R.id.today_tab)).commit();
        addbut=(ImageView)findViewById(R.id.sign_iv);
        addbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User.this,Publishother.class);
                intent.putExtra("MYID",list.get(0).getId());
                intent.putExtra("MYTP",list.get(0).getTupian());
                intent.putExtra("MYNICHENG",list.get(0).getNicheng());
                startActivity(intent);
            }
        });
    }
    public void hidefrag(FragmentTransaction fra){
        if(mFragmentSparseArray.get(R.id.today_tab)!=null){
            fra.hide(mFragmentSparseArray.get(R.id.today_tab));
        }
        if(mFragmentSparseArray.get(R.id.record_tab)!=null){
            fra.hide(mFragmentSparseArray.get(R.id.record_tab));
        }
        if(mFragmentSparseArray.get(R.id.contact_tab)!=null){
            fra.hide(mFragmentSparseArray.get(R.id.contact_tab));
        }
        if(mFragmentSparseArray.get(R.id.settings_tab)!=null){
            fra.hide(mFragmentSparseArray.get(R.id.settings_tab));
        }
    }
    public void denglu(){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=retme&id="+MYID;
        String path = "http://8.130.12.58:8080/Androidservices/services?method=retme&id="+MYID;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();

                AlertDialog alertDialog = new AlertDialog.Builder(User.this).create();
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
                Message mes=handler.obtainMessage();
                mes.obj=list1;
                mes.what=0;
                handler.sendMessage(mes);
            }

        });
    }

    @Override
    public void SendMessageValue(UserBean bean) {
        list.set(0,bean);
       // f1.newfrag1setlist(bean);
        F1.setFragfirstbean(bean);

    }
}