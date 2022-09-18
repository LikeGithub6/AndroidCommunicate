package com.example.teamwork;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class FragmentFirst extends Fragment implements FragmentForth.CallBackValue{

    private UserBean bean;
    View myview;
    TabLayout tab;
    ViewPager myviewpager;
    private List<Fragment> fragmentList=new ArrayList<>();
    private RadioGroup viewpagergroup;
    private MyFragmentPagerAdapter mAdapter;
    private newfragment1 newf1;
    private newfragment2 newf2=null;
    private newfragment1 newf3=null;
    public FragmentFirst(UserBean b) {
        bean=b;
    }
    public void setFragfirstbean(UserBean b){
        bean=b;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview=inflater.inflate(R.layout.fragment_first, container, false);
        tab=myview.findViewById(R.id.tablay);
        myviewpager=myview.findViewById(R.id.firstviewpager);
        //viewpagergroup=myview.findViewById(R.id.firstgroup);
        initview();
        return myview;
    }
    public void initview(){
        fragmentList.add(new newfragment1(bean));
        fragmentList.add(new newfragment4());
        fragmentList.add(new newfragment3(bean));
        fragmentList.add(new newfragment2(bean));


        mAdapter=new MyFragmentPagerAdapter(getFragmentManager(),fragmentList);
        myviewpager.setAdapter(mAdapter);
        //fragmentList.add(new newfragment2());
        //mAdapter.setlist(fragmentList);
       // myviewpager.setAdapter(mAdapter);
        myviewpager.addOnPageChangeListener(mPageChangeListener);
        //viewpagergroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        List<String> l=new ArrayList<>();
        l.add("热门");
        l.add("贴吧");
        l.add("最新");
        l.add("关注");
        mAdapter.setTitle(l);
        myviewpager.setAdapter(mAdapter);
        tab.setupWithViewPager(myviewpager);


    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position==0)mAdapter.getItem(0);
            else if(position==1)((newfragment4)mAdapter.getItem(1)).sendMessage();
            else if(position==2)((newfragment3)mAdapter.getItem(2)).sendMessage();
            else if(position==3)((newfragment2)mAdapter.getItem(3)).sendMessage();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mList;

        private List<String> title=new ArrayList<>();
        public void setTitle(List<String> l){
            title=l;
        }

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }



        @Override
        public Fragment getItem(int position) {
            return this.mList == null ? null : this.mList.get(position);
        }


        /*
        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }
         */
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //如果注释这行，那么不管怎么切换，page都不会被销毁
            //super.destroyItem(container, position, object);
        }
        @Override
        public CharSequence getPageTitle(int posotion){
            return title.get(posotion);
        }
    }

    @Override
    public void SendMessageValue(UserBean userBean) {
        bean=userBean;
        if(newf1!=null)newf1.newfrag1setlist(userBean);
    }

}