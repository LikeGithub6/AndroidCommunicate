package com.example.teamwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

public class PictBig extends AppCompatActivity {
    ImageView bigimage;
    Toolbar picbigbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pict_big);
        String TUPIAN=getIntent().getStringExtra("TUPIAN");
        byte[] imageBytes = Base64.decode(TUPIAN, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        bigimage=findViewById(R.id.pibigimg);
        bigimage.setImageBitmap(decodedImage);
        picbigbar=findViewById(R.id.picBigbar);
        picbigbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}