package com.example.teamwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class firstFragment extends Fragment {

    private ProgressDialog progressDialog;
    private String mContentText;
    ImageView img;
    TextView text;
    String str;
    Bitmap bitmap;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] bye=(byte[])msg.obj;
            bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String str1="";
            for(int i=0;i<30;i++){
                str1=str1+baos.toByteArray()[i]+"A"+i+"a";
            }
            text.setText(str1);
            bye=baos.toByteArray();
            //progressDialog = MaskUtil.firshowProgressDialog("拼命加载中",firstFragment.this);
            bitmap=BitmapFactory.decodeByteArray(bye, 0, bye.length);
            //text.setText(baos.toByteArray()[0]+"a"+baos.toByteArray()[1]+"b");
            img.setImageBitmap(bitmap);
            //bitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.dibu);
            //contentTv.setText("size:"+L.size());
            //img.setImageBitmap(bitmap);
           // progressDialog.dismiss();
        }
    };
    public firstFragment(){

    }


    //联网方法
    public void netmethod(){
        progressDialog = MaskUtil.firshowProgressDialog("拼命加载中",firstFragment.this);
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=testpict";
        String path = "http://8.130.12.58:8080/Androidservices/services?method=testpict";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                progressDialog.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setMessage("网络或服务器原因连接失败");
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //progressDialog.dismiss();
                //Looper.prepare();
                //String result = response.body().string();
                byte[] piby=response.body().bytes();

                Message mes=handler.obtainMessage();
                mes.obj=piby;
                handler.sendMessage(mes);
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getinformation();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_first_fragment, container, false);
        img=(ImageView)rootView.findViewById(R.id.firimg);
        text=rootView.findViewById(R.id.textView2);
        //byte[] bytt=
        //Bitmap bip=BitmapFactory.decodeByteArray(list.get(0).getTupian(), 0, list.get(0).getTupian().length);


        netmethod();

        //contentTv.setText("信息："+L.size());
        return rootView;
    }
}