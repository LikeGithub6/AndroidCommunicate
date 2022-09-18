package com.example.teamwork;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class FragmentThird extends Fragment {


    String MYID;
    String MYTP;
    String MYNICHENG;
    List<reschatBean> mainchatlist;
    RecyclerView chatlistrecycle;
    chatlistAdapter adapter;
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 111) {
                mainchatlist=(List<reschatBean>)msg.obj;
                chatlistrecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter=new chatlistAdapter(getActivity());
                adapter.setChatlist(mainchatlist);
                adapter.setOnItemClickListener(new chatlistAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(getContext(), MainChat.class);
                        intent.putExtra("HEID",mainchatlist.get(position).getHeid());
                        intent.putExtra("HETP",mainchatlist.get(position).getHetp());
                        intent.putExtra("HENICHENG",mainchatlist.get(position).getHenicheng());
                        intent.putExtra("MYID",MYID);
                        intent.putExtra("MYTP",MYTP);
                        mainchatlist.get(position).setNosee("0");
                        //adapter.listchangeset(position);
                        clearnosee(mainchatlist.get(position).getHeid());
                        adapter.notifyItemChanged(position);
                        startActivity(intent);
                    }
                });
                chatlistrecycle.setAdapter(adapter);

                HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
                //stringIntegerHashMap.put(Itemjianju.TOP_DECORATION,50);//top间距

                stringIntegerHashMap.put(Itemjianju.BOTTOM_DECORATION, 50);//底部间距

                stringIntegerHashMap.put(Itemjianju.LEFT_DECORATION, 20);//左间距

                stringIntegerHashMap.put(Itemjianju.RIGHT_DECORATION, 20);//右间距
                chatlistrecycle.addItemDecoration(new Itemjianju(stringIntegerHashMap));
            }
        }
    };
    public FragmentThird(String id, String tp, String nicheng) {
        // Required empty public constructor
        MYID = id;
        MYTP = tp;
        MYNICHENG = nicheng;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentThird.
     */
    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        chatlistrecycle=view.findViewById(R.id.chatlistrv);
        getchatlist();
        return view;
    }
    public void getchatlist(){
        String path = "http://8.130.12.58:8080/Androidservices/services?method=chatpeoplelist";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new JSONObject();
        try {
            jsob.put("myid", MYID);
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
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setMessage("网络或服务器原因查看失败");
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
                List<reschatBean> list=new ArrayList<>();
                reschatBean bean=null;
                for (int i = 0; i <jsar.size(); i++) {
                    net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                    String heid=jsob.getString("heid");
                    String henicheng=jsob.getString("henicheng");
                    String tupian=jsob.getString("tupian");
                    String type=jsob.getString("type");
                    String neirong=jsob.getString("neirong");
                    String time=jsob.getString("time");
                    String nosee=jsob.getString("nosee");
                    bean=new reschatBean(heid,henicheng,tupian,type,neirong,time,nosee);
                    list.add(bean);
                }
                Message mes=handler.obtainMessage();
                mes.obj=list;
                mes.what=111;
                handler.sendMessage(mes);
            }
        });
    }
    public void clearnosee(String heid){
        String path = "http://8.130.12.58:8080/Androidservices/services?method=chatclearnosee";
        OkHttpClient client = new OkHttpClient();
        org.json.JSONObject jsob = new JSONObject();
        try {
            jsob.put("myid", MYID);
            jsob.put("heid",heid);
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
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setMessage("网络或服务器原因打开失败");
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
            }
        });
    }

}
