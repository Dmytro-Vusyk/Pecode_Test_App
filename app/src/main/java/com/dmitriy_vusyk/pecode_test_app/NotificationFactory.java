package com.dmitriy_vusyk.pecode_test_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationFactory {

    private Map<Integer, ArrayList<Integer>> notificationsBuffer = new HashMap<>();
    private int notificationId = 0;
    private int pendingIntentId = 0;
    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel";
    public static final String FRAGMENT_ID = "fragment_ID";

    public NotificationFactory() {
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void createNotification(Context context, int label) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);

        //  notification channel for Android O devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setColor(Color.BLUE)
                        .setSmallIcon(R.drawable.circle)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(context.getString(R.string.notification_body) + " " + label)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(
                                context.getString(R.string.notification_body)))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(contentIntent(context, label - 1))
                        .setAutoCancel(true)
                        .setShowWhen(false);

        // If the build version is greater than or equal to JELLY_BEAN and less than OREO,
        // set the notification's priority to PRIORITY_HIGH.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        // Trigger the notification by calling notify on the NotificationManager.
        // Pass in a unique ID of your choosing for the notification and notificationBuilder.build()
        notificationManager.notify(notificationId, notificationBuilder.build());
        incrementIDs();
    }

    private PendingIntent contentIntent(Context context, int id) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        startActivityIntent.putExtra(FRAGMENT_ID, id);

        return PendingIntent.getActivity(
                context,
                pendingIntentId,
                startActivityIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void bindNotifications(Integer f, ArrayList<Integer> ids) {
        notificationsBuffer.put(f, ids);
    }

    public void deleteAllFragmentNotifications(Context context, Integer id) {
        ArrayList<Integer> values = notificationsBuffer.get(id);
        if (values != null) {
            for (Integer i : values) {
                deleteIntent(context, i);
            }
            notificationsBuffer.remove(id);
        }
    }

    private void deleteIntent(Context context, int id) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);

    }

    // Create a helper method called largeIcon which takes in a Context as a parameter and
    // returns a Bitmap. This method is necessary to decode a bitmap needed for the notification.
    private Bitmap largeIcon(Context context) {
        // Get a Resources object from the context.
        Resources res = context.getResources();
        // Create and return a bitmap using BitmapFactory.decodeResource, passing in the
        // resources object and R.drawable //TODO create drawable for notification
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.oval_blue);
        return largeIcon;
    }

    private void incrementIDs() {
        notificationId++;
        pendingIntentId++;
    }

}
