package com.example.teamwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;

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

public class Publishother extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog progressDialog;
    String MYID;
    String MYTP;
    String MYNICHENG;
    TextView choicetop;
    ImageView sendtop;
    EditText maintalk;
    EditText biaoqian;
    Toolbar quxiao;
    private List<LocalMedia> selectList = new ArrayList<>();
    private PublishAdapter adapter;
    private RecyclerView mRecyclerView;
    private int maxSelectNum = 4;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str=(String)msg.obj;
            choicetop.setText(str);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishother);
        MYID=getIntent().getStringExtra("MYID");
        MYTP=getIntent().getStringExtra("MYTP");
        MYNICHENG=getIntent().getStringExtra("MYNICHENG");
        choicetop=findViewById(R.id.choicetopic);
        sendtop=(ImageView)findViewById(R.id.sendtopic);
        maintalk=findViewById(R.id.maintalk);
        quxiao=(Toolbar) findViewById(R.id.qxfb);
        mRecyclerView=findViewById(R.id.mRecycli);
        quxiao.setOnClickListener(this);
        choicetop.setOnClickListener(this);
        sendtop.setOnClickListener(this);
        initWidget();
        quxiao.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choicetopic: {
                String[] item = new String[]{"生活提问", "日常趣事", "游戏开黑", "一起自习", "失物招领", "健康运动", "难题求解", "吐槽一下", "广场话题"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Publishother.this);
                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Looper.prepare();
                        Message mes = handler.obtainMessage();
                        mes.obj = item[which];
                        handler.sendMessage(mes);
                        // Looper.loop();
                    }
                });
                builder.create().show();
            }
            break;
            case R.id.sendtopic: {

                String topic = choicetop.getText().toString();
                String neirong = maintalk.getText().toString();
                //String biaoqianstr= biaoqian.getText().toString();
                if (topic.equals("选择一个话题(点击飞机放飞它)") || neirong.equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Publishother.this).create();
                    alertDialog.setMessage("你的话题内容不全哦");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                } else {
                    progressDialog = MaskUtil.showProgressDialog("发布中",Publishother.this);

                    SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = new Date(System.currentTimeMillis());
                    String tablename=MYID+formatter.format(date);
                    //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=addtopic";
                    String path = "http://8.130.12.58:8080/Androidservices/services?method=addtopic";
                    OkHttpClient client = new OkHttpClient();
                    JSONObject jsob = new JSONObject();
                    try {
                        jsob.put("id", MYID);
                        jsob.put("nicheng",MYNICHENG);
                        jsob.put("tupian",MYTP);
                        jsob.put("topic", topic);
                        //jsob.put("biaoqian", biaoqianstr);
                        jsob.put("neirong", neirong);
                        jsob.put("time",formatter.format(date));
                        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
                        int count=manager.getChildCount();
                        jsob.put("geshu",count-1);

                        if(count!=1){
                            for(int i=0;i<count-1;i++){
                                View view=manager.getChildAt(i);
                                PublishAdapter.ViewHolder ada=(PublishAdapter.ViewHolder)mRecyclerView.getChildViewHolder(view);
                                Bitmap bitmap=ada.retimg();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] imagebyte=baos.toByteArray();
                                String imagestr= Base64.encodeToString(imagebyte,Base64.DEFAULT);
                                jsob.put("pict"+i,imagestr);
                            }
                        }
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
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(Publishother.this).create();
                            alertDialog.setMessage("网络或服务器原因发布失败");
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
                                AlertDialog alertDialog = new AlertDialog.Builder(Publishother.this).create();
                                alertDialog.setMessage("发布成功!");
                                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                alertDialog.show();
                                Looper.loop();
                            }
                            if (response.body() != null) {
                                response.body().close();
                            }
                        }
                    });

                }
            }
            break;
        }
    }
    public void method(){
        PictureSelector.create(Publishother.this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(maxSelectNum)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .forResult(PictureConfig.CHOOSE_REQUEST);

    }

    private void initWidget() {
        PublishAdapterup manager = new PublishAdapterup(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new PublishAdapter(this,onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(adapter);
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
                            PictureSelector.create(Publishother.this).externalPicturePreview(position, selectList);

                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(Publishother.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(Publishother.this).externalPictureAudio(media.getPath());
                            break;
                    }
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
            RxPermissions rxPermission = new RxPermissions(Publishother.this);
            rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) {
                            if (permission.granted) {// 用户已经同意该权限
                                //第一种方式，弹出选择和拍照的dialog
                                RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
                                int count=manager.getChildCount();
                                if(count==4){
                                    Toast.makeText(Publishother.this, "已经选择三张不可再选", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    String[] item = new String[]{"相册选一选", "现在拍一张"};
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Publishother.this);
                                    builder.setItems(item, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Looper.prepare();
                                            if (which == 0 && count != 4) {
                                                PictureSelector.create(Publishother.this)
                                                        .openGallery(PictureMimeType.ofImage())
                                                        .maxSelectNum(maxSelectNum - count)
                                                        .minSelectNum(1)
                                                        .imageSpanCount(4)
                                                        .selectionMode(PictureConfig.MULTIPLE)
                                                        .forResult(PictureConfig.CHOOSE_REQUEST);
                                            }
                                            if (which == 1 && count != 4) {
                                                PictureSelector.create(Publishother.this)
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
                                Toast.makeText(Publishother.this, "拒绝", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };
    
}