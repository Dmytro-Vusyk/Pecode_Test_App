package com.dmitriy_vusyk.pecode_test_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    private String LOG_TAG = "LIFECYCLE:";
    public ViewPager pager;
    private MainActivityContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "MainActivity: onCreate");
        pager = (ViewPager) findViewById(R.id.pager);
        //        myPagerAdapter = new MyPagerAdapter(fm);
//        pages.add(0, MyFragment.getInstance(0));
//        pager.setAdapter(myPagerAdapter);
        MainPresenterImpl.getInstance(this);
        presenter.createFragment();
    }

    public ViewPager getPager() {
        return pager;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(LOG_TAG, "MainActivity: onNewIntent");
        int id = intent.getIntExtra(NotificationHandler.FRAGMENT_ID, 0);
        presenter.setPagerCurrentItem(id);
    }

    @Override
    public void setPresenter(MainActivityContract.Presenter presenter) {
        this.presenter = presenter;
    }

}
