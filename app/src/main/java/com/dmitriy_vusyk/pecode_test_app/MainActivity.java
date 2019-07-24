package com.dmitriy_vusyk.pecode_test_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    SparseArray<MyFragment> pages;

    public ViewPager pager;
    public MyPagerAdapter myPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pages.put(0, MyFragment.getInstance(0));
        pager.setAdapter(myPagerAdapter);

    }

    public void replaceData() {
        SparseArray<MyFragment> pages = this.pages.clone();
        this.pages.clear();
        this.pages = pages.clone();
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            pages = new SparseArray<>();
        }


        @Override
        public Fragment getItem(int i) {
            Log.d("GET ITEM", "!!!!!!!!!!!!!!!!!!!");
            return pages.get(i);
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            MyFragment fragment = (MyFragment) super.instantiateItem(container, position);
            pages.put(position, fragment);
            Log.d("INSTANTIATE ITEM", "!!!!!!!!!!!!!!!!!!!");
            return fragment;
        }


        public MyFragment getRegisteredFragment(int position) {
            return pages.get(position);
        }


        @Override
        public int getItemPosition(@NonNull Object object) {
            Log.d("GET ITEM POSITION", "!!!!!!!!!!!!!!!!!!!");
            return POSITION_UNCHANGED;
        }

    }

}
