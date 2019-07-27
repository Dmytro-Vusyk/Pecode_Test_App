package com.dmitriy_vusyk.pecode_test_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<MyFragment> pages;

    private String LOG_TAG = "LIFECYCLE:";
    private String PAGES_SAVE_TAG = "pages:";

    public ViewPager pager;
    public MyPagerAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "MainActivity: onCreate");
        pager = (ViewPager) findViewById(R.id.pager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pages.add(0, MyFragment.getInstance(0));
        pager.setAdapter(myPagerAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(LOG_TAG, "MainActivity: onNewIntent");
        int id = intent.getIntExtra(NotificationFactory.FRAGMENT_ID, 0);
        for (int i = 0; i < pages.size(); i++) {
            if (id == pages.get(i).getFragmentId()) {
                pager.setCurrentItem(i);
            }
        }
    }

  // @Override
  // protected void onSaveInstanceState(Bundle outState) {
  //     super.onSaveInstanceState(outState);
  //     Log.d(LOG_TAG, "MainActivity:  onSaveInstanceState()");
  // }

  // @Override
  // protected void onRestoreInstanceState(Bundle savedInstanceState) {
  //     super.onRestoreInstanceState(savedInstanceState);
  //     Log.d(LOG_TAG, "MainActivity:  onRestoreInstanceState()");

  // }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        FragmentManager fm;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
            pages = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            Log.d("GET ITEM", String.valueOf(i));
            return pages.get(i);
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            MyFragment instantiatedFragment = (MyFragment) super.instantiateItem(container, position);
            Log.d("INSTANTIATE ITEM_1", String.valueOf(position));
            MyFragment fragment = pages.get(position);
            Log.d("INSTANTIATE ITEM_2", String.valueOf(position));
            return fragment;
        }

        public MyFragment getRegisteredFragment(int position) {
            return pages.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            int index = ((MyFragment) object).getFragmentId();
            Log.d("GET ITEM POSITION", String.valueOf(index));

            if (index == -1) {
                return POSITION_NONE;
            }
            return index;
        }
    }

}
