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
                String[] item = new String[]{"????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????"};
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
                if (topic.equals("??????????????????(?????????????????????)") || neirong.equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Publishother.this).create();
                    alertDialog.setMessage("???????????????????????????");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "????????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                } else {
                    progressDialog = MaskUtil.showProgressDialog("?????????",Publishother.this);

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
                        //??????
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Looper.prepare();
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(Publishother.this).create();
                            alertDialog.setMessage("????????????????????????????????????");
                            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "????????????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alertDialog.show();
                            Looper.loop();
                        }

                        //??????
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            progressDialog.dismiss();
                             if (result.equals("1")) {
                                Looper.prepare();
                                AlertDialog alertDialog = new AlertDialog.Builder(Publishother.this).create();
                                alertDialog.setMessage("????????????!");
                                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "????????????", new DialogInterface.OnClickListener() {
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
                            // ???????????? ???????????????????????????
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(Publishother.this).externalPicturePreview(position, selectList);

                            break;
                        case 2:
                            // ????????????
                            PictureSelector.create(Publishother.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // ????????????
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
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// ????????????????????????

                images = PictureSelector.obtainMultipleResult(data);
                selectList.addAll(images);

                //selectList = PictureSelector.obtainMultipleResult(data);

                // ?????? LocalMedia ??????????????????path
                // 1.media.getPath(); ?????????path
                // 2.media.getCutPath();????????????path????????????media.isCut();?????????true
                // 3.media.getCompressPath();????????????path????????????media.isCompressed();?????????true
                // ????????????????????????????????????????????????????????????????????????????????????
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
            }
        }
    }
    private PublishAdapter.onAddPicClickListener onAddPicClickListener = new PublishAdapter.onAddPicClickListener() {
        @SuppressLint("CheckResult")
        @Override
        public void onAddPicClick() {
            //??????????????????
            RxPermissions rxPermission = new RxPermissions(Publishother.this);
            rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) {
                            if (permission.granted) {// ???????????????????????????
                                //??????????????????????????????????????????dialog
                                RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
                                int count=manager.getChildCount();
                                if(count==4){
                                    Toast.makeText(Publishother.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    String[] item = new String[]{"???????????????", "???????????????"};
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
                                //????????????????????????????????????????????? ????????????????????????
//                                showAlbum();
                            } else {
                                Toast.makeText(Publishother.this, "??????", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };
    
}