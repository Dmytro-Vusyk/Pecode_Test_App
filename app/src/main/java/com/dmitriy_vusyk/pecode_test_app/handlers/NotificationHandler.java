package com.dmitriy_vusyk.pecode_test_app.handlers;

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

import com.dmitriy_vusyk.pecode_test_app.R;
import com.dmitriy_vusyk.pecode_test_app.activity.MainActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.dmitriy_vusyk.pecode_test_app.constants.Constants.FRAGMENT_ID;
import static com.dmitriy_vusyk.pecode_test_app.constants.Constants.NOTIFICATION_CHANNEL_ID;

public class NotificationHandler {

    private Map<Integer, ArrayList<Integer>> notificationsBuffer = new HashMap<>();
    private int notificationId = 0;
    private int pendingIntentId = 0;

    public NotificationHandler() {
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void createNotification(Context context, int label) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);

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
                        .setSmallIcon(R.drawable.circle_blue)
                        .setLargeIcon(largeIcon(context))
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(context.getString(R.string.notification_body) + " " + label)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(contentIntent(context, label - 1))
                        .setAutoCancel(true)
                        .setShowWhen(false);

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

    /**
     * This method is necessary to decode a bitmap needed for the notification.
     *
     * @param context application context
     * @return Bitmap for LargeIcon in notification
     */
    private Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.drawable.circle_blue);
    }

    private void incrementIDs() {
        notificationId++;
        pendingIntentId++;
    }

}
