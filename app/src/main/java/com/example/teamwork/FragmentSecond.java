package com.example.teamwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class FragmentSecond extends Fragment implements View.OnClickListener{
    UserBean mybean;
    LinearLayout ltiwen;
    LinearLayout lqushi;
    LinearLayout lnanti;
    LinearLayout lgame;
    LinearLayout lyundong;
    LinearLayout lzhaoling;
    LinearLayout lzixi;
    LinearLayout ltucao;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    public FragmentSecond(UserBean bean){
        mybean=bean;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getinformation();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        ltiwen=view.findViewById(R.id.lintiwen);
        lqushi=view.findViewById(R.id.linqushi);
        lnanti=view.findViewById(R.id.linnanti);
        lgame=view.findViewById(R.id.lingame);
        lyundong=view.findViewById(R.id.linyundong);
        lzhaoling=view.findViewById(R.id.linzhaoling);
        lzixi=view.findViewById(R.id.linzixi);
        ltucao=view.findViewById(R.id.lintucao);
        ltiwen.setOnClickListener(this);
        lqushi.setOnClickListener(this);
        lnanti.setOnClickListener(this);
        lgame.setOnClickListener(this);
        lyundong.setOnClickListener(this);
        lzhaoling.setOnClickListener(this);
        lzixi.setOnClickListener(this);
        ltucao.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        String topic="";
        switch (v.getId()){
            case R.id.lintiwen:topic="生活提问";break;
            case R.id.linqushi:topic="日常趣事";break;
            case R.id.linnanti:topic="难题求解";break;
            case R.id.lingame:topic="游戏开黑";break;
            case R.id.linyundong:topic="健康运动";break;
            case R.id.linzhaoling:topic="失物招领";break;
            case R.id.linzixi:topic="一起自习";break;
            case R.id.lintucao:topic="吐槽一下";break;
        }
        Intent intent = new Intent(getContext(),MainTopicList.class);
        intent.putExtra("MYID",mybean.getId());
        intent.putExtra("MYTP",mybean.getTupian());
        intent.putExtra("MYNICHENG",mybean.getNicheng());
        intent.putExtra("huati",topic);
        startActivity(intent);

    }
}