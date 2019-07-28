package com.dmitriy_vusyk.pecode_test_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.dmitriy_vusyk.pecode_test_app.R;
import com.dmitriy_vusyk.pecode_test_app.constants.Constants;
import com.dmitriy_vusyk.pecode_test_app.interfaces.MainActivityContract;
import com.dmitriy_vusyk.pecode_test_app.presenter.MainPresenterImpl;

import static com.dmitriy_vusyk.pecode_test_app.constants.Constants.FRAGMENT_ID;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    public ViewPager pager;
    private MainActivityContract.Presenter presenter;
    private boolean isStopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.pager);
        MainPresenterImpl.getInstance(this);
        presenter.createFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isStopped) {
            animatePager(pager);
            isStopped = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStopped = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.IS_STOPPED, isStopped);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isStopped = savedInstanceState.getBoolean(Constants.IS_STOPPED, false);
    }

    public ViewPager getPager() {
        return pager;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        int id = intent.getIntExtra(FRAGMENT_ID, 0);
        presenter.setPagerCurrentItem(id);
    }

    @Override
    public void setPresenter(MainActivityContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void animatePager(ViewPager pager) {
        AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);
        animation.setDuration(400);
        animation.setStartOffset(50);
        animation.setFillAfter(true);
        pager.startAnimation(animation);
    }

}
