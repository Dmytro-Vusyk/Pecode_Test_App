package com.dmitriy_vusyk.pecode_test_app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private ImageButton ibCreateNotification;
    private ImageButton ibAddFragment;
    private ImageButton ibDeleteFragment;
    private TextView tvFragmentNumber;
    private ImageView ivOval;
    private NotificationFactory factory = new NotificationFactory();
    private static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private static final String ARGUMENT_PAGE_LABEL = "arg_page_label";
    private int fragmentId;
    private int label;
    private ArrayList<Integer> ids = new ArrayList<>();

    public static MyFragment getInstance(int id) {
        MyFragment fragment = new MyFragment();
        Bundle arguments = new Bundle();
        int pageLabel = id + 1;
        arguments.putInt(ARGUMENT_PAGE_NUMBER, id);
        arguments.putInt(ARGUMENT_PAGE_LABEL, pageLabel);
        fragment.setArguments(arguments);
        fragment.fragmentId = id;
        fragment.label = pageLabel;
        return fragment;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        fragmentId = getArguments().getInt(ARGUMENT_PAGE_NUMBER, 0);
        label = getArguments().getInt(ARGUMENT_PAGE_LABEL, 0);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        ivOval = (ImageView) view.findViewById(R.id.image_rect_oval);
        ibCreateNotification = (ImageButton) view.findViewById(R.id.btn_create_notification);
        ibCreateNotification.setOnClickListener(this);
        ibAddFragment = (ImageButton) view.findViewById(R.id.btn_plus);
        ibAddFragment.bringToFront();
        ibAddFragment.setOnClickListener(this);
        ibDeleteFragment = (ImageButton) view.findViewById(R.id.btn_minus);
        ibDeleteFragment.bringToFront();
        ibDeleteFragment.setOnClickListener(this);
        tvFragmentNumber = (TextView) view.findViewById(R.id.fragment_number);
        tvFragmentNumber.setText(String.valueOf(label));
        tvFragmentNumber.setContentDescription(String.valueOf(label));

        if (fragmentId == 0) {
            ibDeleteFragment.setVisibility(View.INVISIBLE);
        } else {
            ibDeleteFragment.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        MainActivity activity = (MainActivity) getActivity();
        int itemPosition = activity.pager.getCurrentItem();
        MyFragment currentFragment = activity.myPagerAdapter.getRegisteredFragment(itemPosition);

        switch (id) {
            case R.id.btn_create_notification:
                factory.createNotification(getContext(), label);
                ids.add(factory.getNotificationId());
                factory.bindNotifications(currentFragment, ids);
                break;

            case R.id.btn_plus:
                int nextPagePosition = itemPosition + 1;
                MyFragment nextFragment = MyFragment.getInstance(currentFragment.fragmentId + 1);
                if (nextPagePosition < activity.pages.size() &&
                        nextFragment.fragmentId == activity.myPagerAdapter.getRegisteredFragment(nextPagePosition).fragmentId) {
                    activity.pager.setCurrentItem(nextPagePosition);
                } else if (nextPagePosition == activity.pages.size()) {
                    activity.pages.add(nextFragment);
                    activity.myPagerAdapter.notifyDataSetChanged();
                    activity.pager.setAdapter(activity.myPagerAdapter);
                    activity.pager.setCurrentItem(itemPosition + 1);
                } else {
                    activity.pages.add(nextPagePosition, nextFragment);
                    activity.myPagerAdapter.notifyDataSetChanged();
                    activity.pager.setAdapter(activity.myPagerAdapter);
                    activity.pager.setCurrentItem(itemPosition + 1);
                }
                break;

            case R.id.btn_minus:
                if (itemPosition > 0) {
                    factory.deleteAllFragmentNotifications(getContext(), currentFragment);
                    activity.pages.remove(itemPosition);
                    activity.myPagerAdapter.notifyDataSetChanged();
                    activity.pager.setAdapter(activity.myPagerAdapter);
                    activity.pager.setCurrentItem(itemPosition - 1);
                }
                break;

            //Uncomment this for simple button plus realisation
            // case R.id.btn_plus:
            //     MyFragment lastFragment = activity.myPagerAdapter.getRegisteredFragment(activity.pages.size()-1);
            //     MyFragment nextFragment = MyFragment.getInstance(lastFragment.fragmentId+1);
            //     activity.pages.add(nextFragment);
            //     activity.myPagerAdapter.notifyDataSetChanged();
            //     activity.pager.setAdapter(activity.myPagerAdapter);
            //     activity.pager.setCurrentItem(itemPosition);
            //     break;
        }
    }
}
