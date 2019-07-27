package com.dmitriy_vusyk.pecode_test_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private ImageButton ibCreateNotification;
    private ImageButton ibAddFragment;
    private ImageButton ibDeleteFragment;
    private TextView tvFragmentNumber;
    private ImageView ivOval;

    NotificationFactory factory = new NotificationFactory();

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
        Log.d("BUTTON ", "click");

        int id = v.getId();
        MainActivity activity = (MainActivity) getActivity();
        int itemIndex = activity.pager.getCurrentItem();
        MyFragment fragment = activity.myPagerAdapter.getRegisteredFragment(activity.pager.getCurrentItem());
        Log.d("itemIndex = ", String.valueOf(itemIndex));

        switch (id) {
            case R.id.btn_create_notification:
                factory.createNotification(getContext(), label);
                ids.add(factory.getNotificationId());
                factory.bindNotifications(fragment, ids);
                break;

            case R.id.btn_plus:
                int nextPageID = itemIndex + 1;
                MyFragment nextFragment = MyFragment.getInstance(nextPageID);
                if(activity.pages.get(itemIndex).equals(nextFragment)){
                    activity.pager.setCurrentItem(nextPageID);
                    break;
                }
                activity.pages.add(nextPageID, nextFragment);
                activity.myPagerAdapter.notifyDataSetChanged();
                activity.pager.setCurrentItem(activity.pager.getCurrentItem() + 1);
                activity.pager.setOffscreenPageLimit(activity.pages.size());
                //activity.myPagerAdapter.notifyDataSetChanged();
                Log.d("nextPageID = ", String.valueOf(nextPageID));
                break;

            case R.id.btn_minus:
                if (itemIndex > 0) {
                    activity.pages.remove(itemIndex);
                  //  activity.replaceData();
                    activity.myPagerAdapter.notifyDataSetChanged();
                    activity.pager.setCurrentItem(itemIndex - 1);
                    activity.pager.setOffscreenPageLimit(activity.pages.size());
                    activity.myPagerAdapter.notifyDataSetChanged();
                  //  activity.pager.setOffscreenPageLimit(activity.pages.size());
                    factory.deleteAllFragmentNotifications(getContext(), fragment);
                }
                break;
        }
    }

}
