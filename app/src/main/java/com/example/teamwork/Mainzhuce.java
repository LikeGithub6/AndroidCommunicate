package com.example.teamwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Mainzhuce extends AppCompatActivity {
    private ProgressDialog progressDialog;
    String MYID;
    ImageView imag;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;
    private String mFilePath;
    private RadioGroup sexgroup;
    Toolbar bar;
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainzhuce);
        MYID=getIntent().getStringExtra("ThisID");
        imag=findViewById(R.id.mainZCtx);
        imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] item=new String[]{"拍照上传","从相册选择"};
                AlertDialog.Builder builder=new AlertDialog.Builder(Mainzhuce.this);
                builder.setItems(item, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if(which==0){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // 指定调用相机拍照后照片的储存路径
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
                            startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);

                        }
                        else if(which==1){
                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

                        }
                    }
                });
                builder.create().show();
            }
        });
        sexgroup=findViewById(R.id.mainzhucesexcheck);
        sexgroup.check(R.id.mainzcsexman);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                startPhotoZoom(Uri.fromFile(tempFile), 150);
                break;

            case PHOTO_REQUEST_GALLERY:
                if (data != null)
                    startPhotoZoom(data.getData(), 150);
                break;

            case PHOTO_REQUEST_CUT:
                if (data != null)
                    setPicToView(data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size*2);
        intent.putExtra("outputY", size*2);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            //  Drawable drawable = new BitmapDrawable(photo);
            //  imag.setImageDrawable(drawable);
            imag.setImageBitmap(photo);

        }
    }
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    public void mainzhuce(View view) {
        EditText zcnic = (EditText) findViewById(R.id.mainZCNi);
        String sex ="";
        if(sexgroup.getCheckedRadioButtonId()==R.id.mainzcsexman)sex="男";
        else if(sexgroup.getCheckedRadioButtonId()==R.id.mainzcsexwoman)sex="女";
        EditText zcbz = (EditText) findViewById(R.id.mainZCQM);
        String nicheng = zcnic.getText().toString();
        String bz = zcbz.getText().toString();
        if(bz.equals(""))bz="这个人很懒，还没个性签名";
        ImageView img = (ImageView) findViewById(R.id.mainZCtx);
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        if (panduan(nicheng)) {
            AlertDialog alertDialog = new AlertDialog.Builder(Mainzhuce.this).create();
            alertDialog.setMessage("昵称中含有非法字符");
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
        }
        else {
            byte[] imagebyte=baos.toByteArray();
            String imagestr= Base64.encodeToString(imagebyte,Base64.DEFAULT);
            //byte[] imageBytes = Base64.decode(imagestr, Base64.DEFAULT);
            progressDialog = MaskUtil.showProgressDialog("注册中",Mainzhuce.this);
            //String path = "http://192.168.43.117:8080/TestAnWe/LoginAction?method=zhuce";
            String path = "http://8.130.12.58:8080/Androidservices/services?method=zhuce";
            OkHttpClient client = new OkHttpClient();
            JSONObject jsob = new JSONObject();
            try {
                jsob.put("id", MYID);
                jsob.put("nicheng", nicheng);
                jsob.put("sex", sex);
                jsob.put("tupian", imagestr);
                jsob.put("gexing", bz);
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
                    AlertDialog alertDialog = new AlertDialog.Builder(Mainzhuce.this).create();
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
                    progressDialog.dismiss();
                    if (result.equals("1")) {
                        Intent intent = new Intent(Mainzhuce.this,User.class);
                        intent.putExtra("ThisID",MYID);
                        //intent.putExtra("MYTP",list1.get(0).getTupian());
                        //intent.putExtra("nicheng",list1.get(0).getNicheng());
                        startActivity(intent);
                        finish();
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        }
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