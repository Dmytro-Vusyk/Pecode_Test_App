package com.dmitriy_vusyk.pecode_test_app.presenter;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.dmitriy_vusyk.pecode_test_app.App;
import com.dmitriy_vusyk.pecode_test_app.handlers.NotificationHandler;
import com.dmitriy_vusyk.pecode_test_app.activity.MainActivity;
import com.dmitriy_vusyk.pecode_test_app.interfaces.MainActivityContract;
import com.dmitriy_vusyk.pecode_test_app.view.FragmentPagerAdapter;
import com.dmitriy_vusyk.pecode_test_app.view.PageFragment;

import java.util.ArrayList;
import static com.dmitriy_vusyk.pecode_test_app.constants.Constants.ARGUMENT_PAGE_LABEL;
import static com.dmitriy_vusyk.pecode_test_app.constants.Constants.ARGUMENT_PAGE_NUMBER;

public class MainPresenterImpl implements MainActivityContract.Presenter {

    private static MainPresenterImpl instance;

    public static MainPresenterImpl getInstance(MainActivity activity) {
        if (instance == null) {
            instance = new MainPresenterImpl(activity);
        }
        return instance;
    }

    private ViewPager pager;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ArrayList<Integer> notificationIds;
    private ArrayList<PageFragment> attachedFragments;
    private FragmentManager fragmentManager;
    private NotificationHandler notificationHandler;

    private MainPresenterImpl(MainActivity activity) {
        notificationIds = new ArrayList<>();
        fragmentManager = activity.getSupportFragmentManager();
        attachedFragments = new ArrayList<>();
        fragmentPagerAdapter = new FragmentPagerAdapter(fragmentManager, attachedFragments);
        pager = activity.getPager();
        pager.setAdapter(fragmentPagerAdapter);
        activity.setPresenter(this);
        notificationHandler = new NotificationHandler();
    }

    @Override
    public PageFragment createFragment() {
        int itemPosition = pager.getCurrentItem();
        PageFragment currentFragment = fragmentPagerAdapter.getRegisteredFragment(itemPosition);
        PageFragment newFragment;
        if (currentFragment == null) {
            newFragment = createNewFragment(0);
            pager.setCurrentItem(0);
        } else {
            newFragment = createNewFragment(incrementLabel());
            pager.setAdapter(new FragmentPagerAdapter(fragmentManager, attachedFragments));
            pager.setCurrentItem(itemPosition);
        }
        return newFragment;
    }

    @Override
    public void removeFragment() {
        int itemPosition = pager.getCurrentItem();
        if (itemPosition > 0) {
            removeAllNotificationsForFragment(itemPosition);
            fragmentPagerAdapter.removeFragment(itemPosition);
            pager.setAdapter(new FragmentPagerAdapter(fragmentManager, attachedFragments));
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
        int id = fragmentPagerAdapter.getRegisteredFragment(pager.getCurrentItem()).getLabel() + 1;
        notificationHandler.createNotification(App.getInstance().getApplicationContext(), id);
        notificationIds.add(notificationHandler.getNotificationId());
        notificationHandler.bindNotifications(fragmentPagerAdapter.getRegisteredFragment(pager.getCurrentItem()).getId(), notificationIds);
    }

    @Override
    public void removeNotification() {

    }

    @Override
    public void removeAllNotificationsForFragment(int fragmentPosition) {
        notificationHandler.deleteAllFragmentNotifications(App.getInstance().getApplicationContext(), fragmentPosition);
    }

    private PageFragment createNewFragment(int id) {
        PageFragment fragment = new PageFragment();
        Bundle arguments = new Bundle();
        int pageLabel = id;
        arguments.putInt(ARGUMENT_PAGE_NUMBER, id);
        arguments.putInt(ARGUMENT_PAGE_LABEL, id);
        fragment.setArguments(arguments);
        fragment.setFragmentId(id);
        fragment.setLabel(pageLabel);
        fragment.setPresenter(this);
        attachedFragments.add(fragment);
        fragmentPagerAdapter.notifyDataSetChanged();
        return fragment;
    }

    private int incrementLabel() {
        return fragmentPagerAdapter.getFragmentList().get(fragmentPagerAdapter.getFragmentList().size() - 1).getLabel() + 1;
    }
}
