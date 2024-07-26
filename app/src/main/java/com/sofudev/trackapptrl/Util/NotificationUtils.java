package com.sofudev.trackapptrl.Util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.sofudev.trackapptrl.Data.NotificationVO;
import com.sofudev.trackapptrl.MainActivity;
import com.sofudev.trackapptrl.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URL;

import static com.sofudev.trackapptrl.App.AppController.TAG;

/**
 * Created by sholeh on 6/26/2019.
 */

public class NotificationUtils {
    private static final String NOTIFICATION_ID = "200";
    private static final String PUSH_NOTIFICATION = "pushNotification";
    private static final String CHANNEL_ID = "myChannel";
    private static final String URL = "url";
    private static final String ACTIVITY = "activity";
    private static final String CHANNEL_OREO = "channel_oreo";
    private static final String CHANNEL_DESC = "oreo";
    Map<String, Class> activityMap = new HashMap<>();
    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
        //Populate activity map
        activityMap.put("MainActivity", MainActivity.class);
    }

    public static boolean isAppIsInBackground(Context context) {
        Log.e(TAG, "isAppIsInBackground: "+"show" );
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                        processInfo.processName.equals(context.getPackageName())) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            assert componentInfo != null;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    /**
     * Displays notification based on parameters
     *
     * @param notificationVO
     * @param resultIntent
     */
//    public void displayNotification(NotificationVO notificationVO, Intent resultIntent) {
//        {
//            String message = notificationVO.getMessage();
//            String title = notificationVO.getTitle();
//            String iconUrl = notificationVO.getIconUrl();
//            String action = notificationVO.getAction();
//            String destination = notificationVO.getActionDestination();
//            Bitmap iconBitMap = null;
//            if (iconUrl != null) {
//                iconBitMap = getBitmapFromURL(iconUrl);
//            }
//            final int icon = R.mipmap.ic_launcher;
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//            PendingIntent resultPendingIntent;
//
//            if (!NotificationUtils.isAppIsInBackground(mContext)) {
//                // app is in foreground, broadcast the push message
//                Log.e(TAG, "title: "+ title);
//                Log.e(TAG, "message: "+ message);
//
//                if (URL.equals(action))
//                {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));
//                    intent.putExtra("message", message);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//                }
//                else if (ACTIVITY.equals(action) && activityMap.containsKey(destination)) {
//                    resultIntent = new Intent(mContext, activityMap.get(destination));
//
//                    resultPendingIntent =
//                            PendingIntent.getActivity(
//                                    mContext,
//                                    0,
//                                    resultIntent,
//                                    PendingIntent.FLAG_CANCEL_CURRENT
//                            );
//                }
//                else
//                {
//                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    resultPendingIntent =
//                            PendingIntent.getActivity(
//                                    mContext,
//                                    0,
//                                    resultIntent,
//                                    PendingIntent.FLAG_CANCEL_CURRENT
//                            );
//                }
//
//                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
//
//                Notification notification;
//
//                if (iconBitMap == null) {
//                    //When Inbox Style is applied, user can expand the notification
//                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//
//                    inboxStyle.addLine(message);
//                    notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                            .setAutoCancel(true)
//                            .setContentTitle(title)
//                            .setContentText(message)
//                            .setStyle(inboxStyle)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                            .setContentIntent(resultPendingIntent)
//                            .setSound(defaultSoundUri)
//                            .setVibrate(new long[]{0, 1000, 500, 1000})
//                            .build();
//
//                }
//                else {
//                    //If Bitmap is created from URL, show big icon
//                    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
//                    bigPictureStyle.setBigContentTitle(title);
//                    bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
//                    bigPictureStyle.bigPicture(iconBitMap);
//                    notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                            .setAutoCancel(true)
//                            .setContentTitle(title)
//                            .setContentText(message)
//                            .setStyle(bigPictureStyle)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                            .setContentIntent(resultPendingIntent)
//                            .setSound(defaultSoundUri)
//                            .setVibrate(new long[]{0, 1000, 500, 1000})
//                            .build();
//                }
//
//                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(NOTIFICATION_ID, notification);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                    /* Create or update. */
//                    NotificationChannel channel = new NotificationChannel(CHANNEL_OREO, CHANNEL_DESC, NotificationManager.IMPORTANCE_DEFAULT);
//
//                    mBuilder.setChannelId(CHANNEL_OREO);
//
//                    AudioAttributes att = new AudioAttributes.Builder()
//                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                            .build();
//                    channel.setSound(defaultSoundUri, att);
//                    channel.enableLights(true);
//                    channel.setLightColor(Color.BLUE);
//                    channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                    channel.enableVibration(true);
//                    channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//                    notificationManager.createNotificationChannel(channel);
//
//                    if (iconBitMap != null)
//                    {
//                        mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(iconBitMap).setSummaryText(message));
//                    }
//                    notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//                }
//            }
//            else
//            {
//                if (URL.equals(action)) {
//                    Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));
//
//                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
//                }
//                else if (ACTIVITY.equals(action) && activityMap.containsKey(destination)) {
//                    resultIntent = new Intent(mContext, activityMap.get(destination));
//
//                    resultPendingIntent =
//                            PendingIntent.getActivity(
//                                    mContext,
//                                    0,
//                                    resultIntent,
//                                    PendingIntent.FLAG_CANCEL_CURRENT
//                            );
//                }
//                else {
//                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    resultPendingIntent =
//                            PendingIntent.getActivity(
//                                    mContext,
//                                    0,
//                                    resultIntent,
//                                    PendingIntent.FLAG_CANCEL_CURRENT
//                            );
//                }
//
//                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
//
//                Notification notification;
//
//                if (iconBitMap == null) {
//                    //When Inbox Style is applied, user can expand the notification
//                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//
//                    inboxStyle.addLine(message);
//                    notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                            .setAutoCancel(true)
//                            .setContentTitle(title)
//                            .setContentIntent(resultPendingIntent)
//                            .setStyle(inboxStyle)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                            .setSound(defaultSoundUri)
//                            .setVibrate(new long[]{0, 1000, 500, 1000})
//                            .setContentText(message)
//                            .build();
//
//                } else {
//                    //If Bitmap is created from URL, show big icon
//                    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
//                    bigPictureStyle.setBigContentTitle(title);
//                    bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
//                    bigPictureStyle.bigPicture(iconBitMap);
//                    notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                            .setAutoCancel(true)
//                            .setContentTitle(title)
//                            .setContentIntent(resultPendingIntent)
//                            .setStyle(bigPictureStyle)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                            .setSound(defaultSoundUri)
//                            .setVibrate(new long[]{0, 1000, 500, 1000})
//                            .setContentText(message)
//                            .build();
//                }
//
//
//                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(NOTIFICATION_ID, notification);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                    /* Create or update. */
//                    NotificationChannel channel = new NotificationChannel(CHANNEL_OREO, CHANNEL_DESC, NotificationManager.IMPORTANCE_DEFAULT);
//
//                    mBuilder.setChannelId(CHANNEL_OREO);
//
//                    AudioAttributes att = new AudioAttributes.Builder()
//                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                            .build();
//                    channel.setSound(defaultSoundUri, att);
//                    channel.enableLights(true);
//                    channel.setLightColor(Color.BLUE);
//                    channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                    channel.enableVibration(true);
//                    channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//                    notificationManager.createNotificationChannel(channel);
//
//                    if (iconBitMap != null)
//                    {
//                        mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(iconBitMap).setSummaryText(message));
//                    }
//                    notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//                }
//            }
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void displayNotification(NotificationVO notificationVO, Intent resultIntent)
    {
        String message = notificationVO.getMessage();
        String title = notificationVO.getTitle();
        String iconUrl = notificationVO.getIconUrl();
        String action = notificationVO.getAction();
        String destination = notificationVO.getActionDestination();
        Bitmap iconBitMap = null;
        if (iconUrl != null) {
            iconBitMap = getBitmapFromURL(iconUrl);
        }
        final int icon = R.mipmap.ic_launcher;

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();

        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        if (!powerManager.isInteractive())
        {
            @SuppressLint("InvalidWakeLockTag")
            PowerManager.WakeLock wl = powerManager.newWakeLock( PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "SCREENLOCK");
            wl.acquire(10000);
        }

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,0);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            //Configure Notification Channel
            notificationChannel.setDescription(message);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setAllowBubbles(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.setSound(defaultSound, att);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (iconBitMap == null)
        {
            inboxStyle.addLine(message);
        }
        else
        {
            bigPictureStyle.setBigContentTitle(title);
            bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
            bigPictureStyle.bigPicture(iconBitMap);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setStyle(iconBitMap == null ? inboxStyle : bigPictureStyle)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon));

        notificationManager.notify(1, notificationBuilder.build());
    }

    /**
     * Downloads push notification image before displaying it in
     * the notification tray
     *
     * @param strURL : URL of the notification Image
     * @return : BitMap representation of notification Image
     */
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Playing notification sound
     */
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
