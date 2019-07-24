package com.dmitriy_vusyk.pecode_test_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    ImageButton ibCreateNotification;
    ImageButton ibAddFragment;
    ImageButton ibDeleteFragment;
    TextView tvFragmentNumber;
    ImageView ivOval;

    private static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private static final String ARGUMENT_PAGE_LABEL = "arg_page_label";

    private static int fragmentIndex;

    private static  int NOTIFICATION_ID = fragmentIndex;
    private static  int PENDING_INTENT_ID = fragmentIndex;
    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel";


    private int label = 1 + fragmentIndex;

    private Map<MyFragment, ArrayList<Integer>> notificationsBuffer = new HashMap<>();
    private ArrayList<Integer> ids = new ArrayList<>();

    public static MyFragment getInstance(int page) {
        MyFragment fragment = new MyFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        fragmentIndex = getArguments().getInt(ARGUMENT_PAGE_NUMBER, 0);

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


        if (fragmentIndex == 0) {
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


    public void createNotification() {

        NotificationManager notificationManager = (NotificationManager)
                getContext().getSystemService(NOTIFICATION_SERVICE);

    //    //  notification channel for Android O devices
    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    //        NotificationChannel mChannel = new NotificationChannel(
    //                NOTIFICATION_CHANNEL_ID,
    //                getContext().getString(R.string.main_notification_channel_name),
    //                NotificationManager.IMPORTANCE_HIGH);
    //        notificationManager.createNotificationChannel(mChannel);
    //    }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                .setSmallIcon(R.drawable.oval_copy)
                .setLargeIcon(largeIcon(getContext()))
                .setContentTitle(getContext().getString(R.string.notification_title))
                .setContentText(getContext().getString(R.string.notification_body) + " " + label)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        getContext().getString(R.string. notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(getContext()))
                .setAutoCancel(true);

        // If the build version is greater than or equal to JELLY_BEAN and less than OREO,
        // set the notification's priority to PRIORITY_HIGH.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        // Trigger the notification by calling notify on the NotificationManager.
        // Pass in a unique ID of your choosing for the notification and notificationBuilder.build()
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MyFragment.class);

        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Create a helper method called largeIcon which takes in a Context as a parameter and
    // returns a Bitmap. This method is necessary to decode a bitmap needed for the notification.
    private static Bitmap largeIcon(Context context) {
        // Get a Resources object from the context.
        Resources res = context.getResources();
        // Create and return a bitmap using BitmapFactory.decodeResource, passing in the
        // resources object and R.drawable //TODO create drawable for notification
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.oval_blue);
        return largeIcon;
    }

    private void deleteIntent(int id){
        NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);

    }

    private void incrementIDs(){
        NOTIFICATION_ID++;
        PENDING_INTENT_ID++;
    }

    private void bindNotifications(MyFragment f, ArrayList<Integer> id){
        notificationsBuffer.put(f,id);
    }

    private void deleteAllFragmentNotifications(MyFragment fragment){
        ArrayList<Integer> values = notificationsBuffer.get(fragment);
        for(Integer id : values){
            deleteIntent(id);
        }

        notificationsBuffer.remove(fragment);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        MainActivity activity = (MainActivity) getActivity();
        MyFragment fragment = activity.myPagerAdapter.getRegisteredFragment(activity.pager.getCurrentItem());

        switch (id) {
            case R.id.btn_create_notification:
                    createNotification();
                    ids.add(NOTIFICATION_ID);
                    bindNotifications(fragment,ids);
                    incrementIDs();
                break;

            case R.id.btn_plus:

                int index = ++fragmentIndex;
                activity.pages.put(index, getInstance(index));
                activity.replaceData();
                activity.myPagerAdapter.notifyDataSetChanged();
                activity.pager.setCurrentItem(activity.pager.getCurrentItem() + 1);
                break;

            case R.id.btn_minus:
                int itemIndex = activity.pager.getCurrentItem();
                if (itemIndex > 0) {
                    activity.pages.remove(itemIndex);
                    activity.replaceData();
                    activity.myPagerAdapter.notifyDataSetChanged();
                    activity.pager.setCurrentItem(itemIndex - 1);
                    deleteAllFragmentNotifications(fragment);
                }
                break;

        }


    }
}
