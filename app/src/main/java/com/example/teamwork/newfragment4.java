package com.example.teamwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class newfragment4 extends Fragment {
    private boolean IS_LOADED = false;
    private RecyclerView tbrv;
    private LuntanAdapter adapter;
    private List<LuntanBean> ltlist;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 111) {
                if (!IS_LOADED) {
                    IS_LOADED = true;
                    gettieba();
                }
            }
            else if(msg.what == 222){
                ltlist=(List<LuntanBean>)msg.obj;
                tbrv.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter=new LuntanAdapter(getActivity());
                adapter.setLuntanlist(ltlist);
                adapter.setOnItemClickListener(new LuntanAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getContext(), tiebashow.class);
                        intent.putExtra("tiebaid", ltlist.get(position).getId());
                        startActivity(intent);
                    }
                });
                HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
                //stringIntegerHashMap.put(Itemjianju.TOP_DECORATION,50);//top间距

                stringIntegerHashMap.put(Itemjianju.BOTTOM_DECORATION, 50);//底部间距

                stringIntegerHashMap.put(Itemjianju.LEFT_DECORATION, 20);//左间距

                stringIntegerHashMap.put(Itemjianju.RIGHT_DECORATION, 20);//右
                tbrv.setAdapter(adapter);
                tbrv.addItemDecoration(new Itemjianju(stringIntegerHashMap));

            }
        }
    };
    public newfragment4() {

    }
    public void sendMessage(){
        Message message = handler.obtainMessage();
        message.what=111;
        handler.sendMessage(message);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_newfragment4, container, false);
        tbrv=view.findViewById(R.id.tiebarv);
        return view;
    }
    public void gettieba(){
        String path = "http://8.130.12.58:8080/Androidservices/services?method=tiebaall";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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
                String result = response.body().string();
                JSONArray jsar = JSONArray.fromObject(result);
                List<LuntanBean> list=new ArrayList<>();
                LuntanBean bean=null;
                for (int i = 0; i <jsar.size(); i++) {
                    JSONObject jsob = jsar.getJSONObject(i);
                    String id=jsob.getString("id");
                    String title=jsob.getString("title");
                    String zuozhe=jsob.getString("zuozhe");
                    String time=jsob.getString("time");
                    String looknum=jsob.getString("looknum");
                    bean=new LuntanBean(id,title,zuozhe,time,looknum);
                    list.add(bean);
                }
                Message mes=handler.obtainMessage();
                mes.obj=list;
                mes.what=222;
                handler.sendMessage(mes);
            }
        });
    }

}