package com.example.teamwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;

import net.sf.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Mainxiangxi extends AppCompatActivity implements View.OnClickListener{
    private ProgressDialog progressDialog;
    SwipeRefreshLayout comfre;
    String MYID;
    String MYTP;
    String MYNICHENG;
    String senderid;
    String sendtime;
    String pinglun;
    private int maxSelectNum = 3;
    private List<LocalMedia> selectList = new ArrayList<>();
    private PublishAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView comrecv;
    TextView comment;
    private Toolbar mabar;
    EditText comedit;
    private PopupWindow pop;
    Button button;
    View popview;
    List<CommentBean> comlist=new ArrayList<>();
    CommentBean adbean;
    private CommentBean addbean;
    CommentAdapter comadapter;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==111){
                recyclerView.setLayoutManager(new LinearLayoutManager(Mainxiangxi.this));
                comlist.addAll((List<CommentBean>) msg.obj);
                comadapter = new CommentAdapter(Mainxiangxi.this);
                comadapter.setCommentbeanlist(comlist);
                comadapter.setMYID(MYID);
                comadapter.setMYTP(MYTP);
                comadapter.SETMYNICHENG(MYNICHENG);
                comadapter.setpinglun(pinglun);
                setHeaderView(recyclerView);
                comadapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent intent = new Intent(Mainxiangxi.this,Moshengren.class);
                        intent.putExtra("MYID",MYID);
                        intent.putExtra("MYNICHENG",MYNICHENG);
                        intent.putExtra("MYTP",MYTP);
                        intent.putExtra("HEID",comlist.get(position).getId());
                        intent.putExtra("HENICHENG",comlist.get(position).getNicheng());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(comadapter);
            }
            else if(msg.what==222){
                comadapter.pltext.setText(Integer.parseInt(pinglun)+1+"");
                pinglun=Integer.parseInt(pinglun)+1+"";
                addbean=(CommentBean)msg.obj;
                comlist.add(1,addbean);
                comadapter.notifyItemInserted(1);
                recyclerView.getLayoutManager().scrollToPosition(1);
            }
            else if(msg.what==333){
                comlist=null;
                comlist=new ArrayList<>();
                comlist.addAll((List<CommentBean>)msg.obj);
                comadapter.setCommentbeanlist(comlist);
                comadapter.notifyDataSetChanged();
                comfre.setRefreshing(false);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainxiangxi);
        MYID=getIntent().getStringExtra("MYID");
        MYTP=getIntent().getStringExtra("MYTP");
        MYNICHENG=getIntent().getStringExtra("MYNICHENG");
        senderid=getIntent().getStringExtra("senderid");
        sendtime=getIntent().getStringExtra("sendtime");
        pinglun=getIntent().getStringExtra("pinglun");
        //String sendernicheng=getIntent().getStringExtra("sendernicheng");
        //String sendertouxiang=getIntent().getStringExtra("sendertouxiang");
        //String senderneirong=getIntent().getStringExtra("senderneirong");
        //String senderpictsize=getIntent().getStringExtra("senderpictsize");
        //List<String> senderlist=(List<String>)getIntent().getStringArrayListExtra("imgl");
        //comlist.add(new CommentBean(senderid,sendernicheng,sendertouxiang,senderneirong,sendtime,senderpictsize,senderlist));
        //Toast.makeText(Mainxiangxi.this, "发布id:"+getIntent().getStringExtra("senderid"), Toast.LENGTH_SHORT).show();
        recyclerView=findViewById(R.id.commentrv);
        comment=findViewById(R.id.comtext);
        comment.setOnClickListener(this);
        mabar=findViewById(R.id.xiangxibar);
        popview = View.inflate(Mainxiangxi.this, R.layout.compopwin, null);
        comedit=popview.findViewById(R.id.compopedit);
        comrecv=popview.findViewById(R.id.compoprv);
        button=popview.findViewById(R.id.send);
        comfre=findViewById(R.id.xiangxirefresh);
        comfre.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getcomment1();
            }
        });
        pop = new PopupWindow(popview, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mabar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getcomment();
    }
    private void setHeaderView(RecyclerView view){
        View header = LayoutInflater.from(this).inflate(R.layout.commentheader, view, false);
        comadapter.setHeaderView(header);
    }
    public void popWindows(){

        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        /*
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

         */
        PublishAdapterup manager = new PublishAdapterup(popview.getContext(), 3, GridLayoutManager.VERTICAL, false);
        comrecv.setLayoutManager(manager);
        adapter = new PublishAdapter(this,onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        comrecv.setAdapter(adapter);
        adapter.setOnItemClickListener(new PublishAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(Mainxiangxi.this).externalPicturePreview(position, selectList);

                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(Mainxiangxi.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(Mainxiangxi.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
        popview.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int measuredHeight = popview.getMeasuredHeight();
        int[] location = new int[2];
        comment.getLocationOnScreen(location);
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(comment, Gravity.NO_GRAVITY, 0, location[1] - measuredHeight);
        Button btn=popview.findViewById(R.id.send);
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.send:netweb();
                    int lisize=selectList.size();
                    for(int i=0;i<lisize;i++){
                        selectList.remove(0);
                    }
                    comedit.setText("");
                    pop.dismiss();
                    break;
                }
            }
        };
       // comaddimg();
        btn.setOnClickListener(clickListener);

    }


    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.comtext:popWindows();
            break;

        }
    }
    public void getcomment(){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=getcomment&id="+senderid+"&time="+sendtime;
        String path = "http://8.130.12.58:8080/Androidservices/services?method=getcomment&id="+senderid+"&time="+sendtime;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                AlertDialog alertDialog = new AlertDialog.Builder(Mainxiangxi.this).create();
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
                List<CommentBean> list1=new ArrayList<>();
                CommentBean bean=null;
                for (int i = 0; i <jsar.size(); i++) {
                    net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                    String id = jsob.getString("id");
                    String touxiang=jsob.getString("touxiang");
                    String nicheng=jsob.getString("nicheng");
                    String neirong=jsob.getString("neirong");
                    String imgsize=jsob.getString("size");
                    String time=jsob.getString("time");
                    List<String> imgl=new ArrayList<>();
                    if(imgsize.equals("1")){
                        String img1=jsob.getString("pi1");
                        imgl.add(img1);
                    }
                    if(imgsize.equals("2")) {
                        String img1=jsob.getString("pi1");
                        String img2=jsob.getString("pi2");
                        imgl.add(img1);
                        imgl.add(img2);
                    }
                    if(imgsize.equals("3")) {
                        String img1=jsob.getString("pi1");
                        String img2=jsob.getString("pi2");
                        String img3=jsob.getString("pi3");
                        imgl.add(img1);
                        imgl.add(img2);
                        imgl.add(img3);
                    }
                    String dianzan=jsob.getString("dianzan");
                    bean=new CommentBean(id,nicheng,touxiang,neirong,time,imgsize,imgl,dianzan);
                    list1.add(bean);
                }
                Message mes=handler.obtainMessage();
                mes.obj=list1;
                mes.what=111;
                handler.sendMessage(mes);
                //Looper.loop();
            }
        });
    }
    public void getcomment1(){
        //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=getcomment&id="+senderid+"&time="+sendtime;
        String path = "http://8.130.12.58:8080/Androidservices/services?method=getcomment&id="+senderid+"&time="+sendtime;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                AlertDialog alertDialog = new AlertDialog.Builder(Mainxiangxi.this).create();
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
                List<CommentBean> list1=new ArrayList<>();
                CommentBean bean=null;
                for (int i = 0; i <jsar.size(); i++) {
                    net.sf.json.JSONObject jsob = jsar.getJSONObject(i);
                    String id = jsob.getString("id");
                    String touxiang=jsob.getString("touxiang");
                    String nicheng=jsob.getString("nicheng");
                    String neirong=jsob.getString("neirong");
                    String imgsize=jsob.getString("size");
                    String time=jsob.getString("time");
                    List<String> imgl=new ArrayList<>();
                    if(imgsize.equals("1")){
                        String img1=jsob.getString("pi1");
                        imgl.add(img1);
                    }
                    if(imgsize.equals("2")) {
                        String img1=jsob.getString("pi1");
                        String img2=jsob.getString("pi2");
                        imgl.add(img1);
                        imgl.add(img2);
                    }
                    if(imgsize.equals("3")) {
                        String img1=jsob.getString("pi1");
                        String img2=jsob.getString("pi2");
                        String img3=jsob.getString("pi3");
                        imgl.add(img1);
                        imgl.add(img2);
                        imgl.add(img3);
                    }
                    String dianzan=jsob.getString("dianzan");
                    bean=new CommentBean(id,nicheng,touxiang,neirong,time,imgsize,imgl,dianzan);
                    list1.add(bean);
                }
                Message mes=handler.obtainMessage();
                mes.obj=list1;
                mes.what=333;
                handler.sendMessage(mes);
                //Looper.loop();
            }
        });
    }
    public void netweb(){
        progressDialog = MaskUtil.showProgressDialog("评论中",Mainxiangxi.this);
        String path = "http://8.130.12.58:8080/Androidservices/services?method=comment";
        OkHttpClient client = new OkHttpClient();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String neirong=comedit.getText().toString();
        JSONObject jsob = new JSONObject();

        List<String> adimg=new ArrayList<>();
        try {
            jsob.put("senderid",senderid);
            jsob.put("sendtime",sendtime);
            jsob.put("id", MYID);
            jsob.put("nicheng",MYNICHENG);
            jsob.put("touxiang",MYTP);
            jsob.put("neirong",neirong);
            jsob.put("time",formatter.format(date));
            RecyclerView.LayoutManager manager = comrecv.getLayoutManager();
            int count=adapter.getLocalListsize();
            jsob.put("pictsize",count);
            if(count!=0) {
                for (int i = 0; i < count; i++) {
                    View view = manager.getChildAt(i);
                    PublishAdapter.ViewHolder ada = (PublishAdapter.ViewHolder) comrecv.getChildViewHolder(view);
                    Bitmap bitmap = ada.retimg();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] imagebyte = baos.toByteArray();
                    String imagestr = Base64.encodeToString(imagebyte, Base64.DEFAULT);
                    adimg.add(imagestr);
                    jsob.put("pict" + i, imagestr);
                }
            }
            adbean=new CommentBean(MYID,MYNICHENG,MYTP,neirong,formatter.format(date),count+"",adimg,"0");
        }catch (JSONException e) {
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
                progressDialog.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(Mainxiangxi.this).create();
                alertDialog.setMessage("网络或服务器原因评论失败");
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
                if (result.equals("1")) {
                    Looper.prepare();
                    AlertDialog alertDialog = new AlertDialog.Builder(Mainxiangxi.this).create();
                    alertDialog.setMessage("评论成功!");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                    Message mes=handler.obtainMessage();
                    mes.obj=adbean;
                    mes.what=222;
                    handler.sendMessage(mes);
                    Looper.loop();
                }
                if (response.body() != null) {
                    response.body().close();
                }

            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调

                images = PictureSelector.obtainMultipleResult(data);
                selectList.addAll(images);

                //selectList = PictureSelector.obtainMultipleResult(data);

                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
            }
        }
    }
    private PublishAdapter.onAddPicClickListener onAddPicClickListener = new PublishAdapter.onAddPicClickListener() {

        @SuppressLint("CheckResult")
        @Override
        public void onAddPicClick() {
            //获取写的权限
            RxPermissions rxPermission = new RxPermissions(Mainxiangxi.this);
            rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) {
                            if (permission.granted) {// 用户已经同意该权限
                                //第一种方式，弹出选择和拍照的dialog
                                RecyclerView.LayoutManager manager = comrecv.getLayoutManager();
                                int count=adapter.getLocalListsize();
                                if(count==3){
                                    Toast.makeText(Mainxiangxi.this, "已经选择三张不可再选", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    String[] item = new String[]{"相册选一选", "现在拍一张"};
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Mainxiangxi.this);
                                    builder.setItems(item, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Looper.prepare();
                                            if (which == 0 && count != 3) {
                                                PictureSelector.create(Mainxiangxi.this)
                                                        .openGallery(PictureMimeType.ofImage())
                                                        .maxSelectNum(maxSelectNum - count)
                                                        .minSelectNum(1)
                                                        .imageSpanCount(3)
                                                        .selectionMode(PictureConfig.MULTIPLE)
                                                        .forResult(PictureConfig.CHOOSE_REQUEST);
                                            }
                                            if (which == 1 && count != 3) {
                                                PictureSelector.create(Mainxiangxi.this)
                                                        .openCamera(PictureMimeType.ofImage())
                                                        .forResult(PictureConfig.CHOOSE_REQUEST);
                                            }

                                            // Looper.loop();
                                        }
                                    });
                                    builder.create().show();
                                }
                                //第二种方式，直接进入相册，但是 是有拍照得按钮的
//                                showAlbum();
                            } else {
                                Toast.makeText(Mainxiangxi.this, "拒绝", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };
}