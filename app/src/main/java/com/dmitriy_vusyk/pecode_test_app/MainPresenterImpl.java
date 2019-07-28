package com.dmitriy_vusyk.pecode_test_app;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import static com.dmitriy_vusyk.pecode_test_app.Constants.ARGUMENT_PAGE_LABEL;
import static com.dmitriy_vusyk.pecode_test_app.Constants.ARGUMENT_PAGE_NUMBER;

class MainPresenterImpl implements MainActivityContract.Presenter {

    private static MainPresenterImpl instance;

    public static MainPresenterImpl getInstance(MainActivity activity) {
        if (instance == null) {
            instance = new MainPresenterImpl(activity);
        }
        return instance;
    }

    private ViewPager pager;
    private MyPagerAdapter pagerAdapter;
    private ArrayList<Integer> notificationIds;
    private ArrayList<MyFragment> attachedFragments;
    private FragmentManager fragmentManager;
    private NotificationHandler notificationHandler;

    private MainPresenterImpl(MainActivity activity) {
        notificationIds = new ArrayList<>();
        fragmentManager = activity.getSupportFragmentManager();
        attachedFragments = new ArrayList<>();
        pagerAdapter = new MyPagerAdapter(fragmentManager, attachedFragments);
        pager = activity.getPager();
        pager.setAdapter(pagerAdapter);
        activity.setPresenter(this);
        notificationHandler = new NotificationHandler();
    }

    @Override
    public MyFragment createFragment() {
        int itemPosition = pager.getCurrentItem();
        MyFragment currentFragment = pagerAdapter.getRegisteredFragment(itemPosition);
        MyFragment newFragment;
        if (currentFragment == null) {
            newFragment = createNewFragment(0);
            pager.setCurrentItem(0);
        } else {
            newFragment = createNewFragment(incrementLabel());
            pager.setAdapter(new MyPagerAdapter(fragmentManager, attachedFragments));
            pager.setCurrentItem(itemPosition);
        }
        return newFragment;
    }

    @Override
    public void removeFragment() {
        int itemPosition = pager.getCurrentItem();
        if (itemPosition > 0) {
            removeAllNotificationsForFragment(itemPosition);
            pagerAdapter.removeFragment(itemPosition);
            pager.setAdapter(new MyPagerAdapter(fragmentManager, attachedFragments));
            pager.setCurrentItem(itemPosition - 1);
        }
    }

    @Override
    public void setPagerCurrentItem(int fragmentId) {
        for (int i = 0; i < attachedFragments.size(); i++) {
            if (fragmentId == attachedFragments.get(i).getFragmentId()) {
                pager.setCurrentItem(i);
            }
        }
    }

    @Override
    public void createNotification() {
        int id = pagerAdapter.getRegisteredFragment(pager.getCurrentItem()).getLabel() + 1;
        notificationHandler.createNotification(App.getInstance().getApplicationContext(), id);
        notificationIds.add(notificationHandler.getNotificationId());
        notificationHandler.bindNotifications(pagerAdapter.getRegisteredFragment(pager.getCurrentItem()).getId(), notificationIds);
    }

    @Override
    public void removeNotification() {

    }

    @Override
    public void removeAllNotificationsForFragment(int fragmentPosition) {
        notificationHandler.deleteAllFragmentNotifications(App.getInstance().getApplicationContext(), fragmentPosition);
    }

    private MyFragment createNewFragment(int id) {
        MyFragment fragment = new MyFragment();
        Bundle arguments = new Bundle();
        int pageLabel = id;
        arguments.putInt(ARGUMENT_PAGE_NUMBER, id);
        arguments.putInt(ARGUMENT_PAGE_LABEL, id);
        fragment.setArguments(arguments);
        fragment.setFragmentId(id);
        fragment.setLabel(pageLabel);
        fragment.setPresenter(this);
        attachedFragments.add(fragment);
        pagerAdapter.notifyDataSetChanged();
        return fragment;
    }

    private int incrementLabel() {
        return pagerAdapter.getFragmentList().get(pagerAdapter.getFragmentList().size() - 1).getLabel() + 1;
    }

    public ArrayList<MyFragment> getAttachedFragments() {
        return attachedFragments;
    }
}
