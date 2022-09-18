package com.example.teamwork;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class FragmentForth extends Fragment implements View.OnClickListener{

    UserBean mybean;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    LinearLayout lguanzhu;
    LinearLayout lshoucang;
    LinearLayout lchange;
    LinearLayout ltuichu;

    private SqliteDB databaseHelper;   //用于创建帮助器对象
    private SQLiteDatabase db;   //用于创建数据库对象
    private static final String name = "JZ.db"; //数据库名称
    private static final int version = 1; //数据库版本
    // TODO: Rename and change types of parameters
    ImageView mytouxiang;
    ImageView mysex;
    TextView mynicheng;
    TextView myqianming;
    CallBackValue callBackValue;
    public FragmentForth(UserBean bean) {
        // Required empty public constructor
        mybean=bean;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment FragmentForth.
     */
    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_forth, container, false);
        mytouxiang=view.findViewById(R.id.MYmytouxiang);
        byte[] imageBytes = Base64.decode(mybean.getTupian(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        mytouxiang.setImageBitmap(decodedImage);
        mysex=view.findViewById(R.id.MYmysex);
        if(mybean.getSex().equals("男")) {
            mysex.setImageResource(R.mipmap.sexman);
        }
        else mysex.setImageResource(R.mipmap.sexwoman);
        mynicheng=view.findViewById(R.id.MYmynicheng);
        mynicheng.setText(mybean.getNicheng());
        myqianming=view.findViewById(R.id.MYmyqianming);
        myqianming.setText(mybean.getGexing());
        lguanzhu=view.findViewById(R.id.MYLguanzhu);
        lguanzhu.setOnClickListener(this);
        lshoucang=view.findViewById(R.id.MYLshoucang);
        lshoucang.setOnClickListener(this);
        lchange=view.findViewById(R.id.MYLchange);
        lchange.setOnClickListener(this);
        ltuichu=view.findViewById(R.id.MYLtuichu);
        ltuichu.setOnClickListener(this);
        return view;
    }
    public void guanzhu(){
        Intent intent = new Intent(getActivity(),MeGuanZhu.class);
        intent.putExtra("MYID",mybean.getId());
        intent.putExtra("MYNICHENG",mybean.getNicheng());
        intent.putExtra("MYGX",mybean.getGexing());
        intent.putExtra("MYSEX",mybean.getSex());
        startActivity(intent);
    }
    public void shoucang(){
        Intent intent = new Intent(getActivity(),Meshoucang.class);
        intent.putExtra("MYID",mybean.getId());
        intent.putExtra("MYTP",mybean.getTupian());
        intent.putExtra("MYNICHENG",mybean.getNicheng());
        startActivity(intent);
    }
    public void changeinf(){
        Intent intent = new Intent(getActivity(),Mechangeinf.class);
        intent.putExtra("MYID",mybean.getId());
        intent.putExtra("MYNICHENG",mybean.getNicheng());
        intent.putExtra("MYGX",mybean.getGexing());
        intent.putExtra("MYSEX",mybean.getSex());
        startActivityForResult(intent,1);
    }
    public void tuichuidenglu(){
        Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
        databaseHelper = new SqliteDB(getContext(), name, null, version);
        SqliteDB moh=new SqliteDB(getContext(),"JZ.db", null, 1);
        SQLiteDatabase db = moh.getReadableDatabase(); // 以只读的方式打开数据库
        db.execSQL("update LastDL set cishu= ? where id=?",new Object[]{"2",mybean.getId()});
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.MYLguanzhu:guanzhu();
                break;
            case R.id.MYLshoucang:shoucang();
                break;
            case R.id.MYLchange:changeinf();
                break;
            case R.id.MYLtuichu:tuichuidenglu();
                break;
        }
    }
    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(CallBackValue) getActivity();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            mynicheng.setText(data.getStringExtra("newnicheng"));
            myqianming.setText(data.getStringExtra("newqianming"));
            String sex=data.getStringExtra("newsex");
            if(sex.equals("男"))mysex.setImageResource(R.mipmap.sexman);
            else mysex.setImageResource(R.mipmap.sexwoman);
            mybean.setNicheng(data.getStringExtra("newnicheng"));
            mybean.setGexing(data.getStringExtra("newqianming"));
            mybean.setSex(data.getStringExtra("newsex"));
        }
        callBackValue.SendMessageValue(mybean);
    }

    public interface CallBackValue{
        public void SendMessageValue(UserBean userBean);
    }
}