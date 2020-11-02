package com.org.zapayapp.notification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.org.zapayapp.R;
import com.org.zapayapp.activity.BorrowSummaryActivity;
import com.org.zapayapp.activity.HomeActivity;
import com.org.zapayapp.activity.LendingSummaryActivity;
import com.org.zapayapp.utils.CommonMethods;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.e("remoteMessage", "remoteMessage====" + remoteMessage.getData().toString());
        CommonMethods.showLogs(TAG, "From: " + remoteMessage.getFrom() + " " + remoteMessage + " " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {
            CommonMethods.showLogs(TAG, "Message data payload: " + remoteMessage.getData());
            if (isAppBackground()) {
                CommonMethods.showLogs(TAG, "isAppForeground : App is background");
            } else {
                CommonMethods.showLogs(TAG, "isAppForeground : App is foreground");

            }
            sendNotification(remoteMessage.getData());
        }

    }
    // [END receive_message]

    private void sendNotification(Map<String, String> data) {
        Intent intent = null;
        try {
            String notification_type = data.get("notification_type");
            String status = data.get("status");
            String title = data.get("title");
            String transaction_request_id = data.get("transaction_request_id");
            String message = data.get("message");
            String request_by = data.get("request_by");

            if (notification_type != null) {
                if (isAppBackground()){
                    intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("notification_type", notification_type);
                    intent.putExtra("request_by", request_by);
                }else {
                    if (request_by != null && request_by.equals("1")) {
                        intent = new Intent(this, LendingSummaryActivity.class);
                    } else if (request_by != null && request_by.equals("2")) {
                        intent = new Intent(this, BorrowSummaryActivity.class);
                    }
                }

                if (notification_type.equalsIgnoreCase("NEW_TRANSACTION_REQUEST")) {
                    intent.putExtra("moveFrom", getString(R.string.transaction));
                    intent.putExtra("transactionId", transaction_request_id);

                } else if (notification_type.equalsIgnoreCase("REQUEST_ACCEPTED")) {
                    intent.putExtra("moveFrom", getString(R.string.accepted));
                    intent.putExtra("transactionId", transaction_request_id);
                } else if (notification_type.equalsIgnoreCase("REQUEST_DECLINED")) {
                    intent.putExtra("moveFrom", getString(R.string.decline));
                    intent.putExtra("transactionId", transaction_request_id);
                } else if (notification_type.equalsIgnoreCase("REQUEST_NEGOTIATE")) {
                    intent.putExtra("moveFrom", getString(R.string.negotiation));
                    intent.putExtra("transactionId", transaction_request_id);
                }

               // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis()/* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                boolean flag = false;
                createImageBuilder(title, message, pendingIntent, flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isAppBackground() {
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        return (myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND);
    }

    private void createImageBuilder(String title, String message, PendingIntent pendingIntent, boolean flag) {
        try {
            Context mContext = this;
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Random myRandom = new Random();
            int value = myRandom.nextInt(100);
            String channelId = title + " " + value;

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            // Since android Oreo notification channel is needed.

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(mContext, channelId)
                            .setSmallIcon(R.mipmap.notification_logo)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher_foreground))
                            //.setColor(getResources().getColor(R.color.colorPrimaryDark))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setCategory(title)
                            .setColorized(true)
                            .setSound(defaultSoundUri)
                            .setChannelId(channelId)
                            .setContentIntent(pendingIntent)
                            .setStyle(new NotificationCompat.DecoratedCustomViewStyle());
            if (!flag) {
                notificationBuilder =
                        new NotificationCompat.Builder(mContext, channelId)
                                .setSmallIcon(R.mipmap.notification_logo)
                                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher_foreground))
                                // .setColor(getResources().getColor(R.color.colorPrimaryDark))
                                .setContentTitle(title)
                                .setContentText(message)
                                .setAutoCancel(true)
                                .setCategory(title)
                                .setColorized(true)
                                .setSound(defaultSoundUri)
                                .setChannelId(channelId)
                                .setContentIntent(pendingIntent)
                                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());
            }


            notificationBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
                NotificationChannel channel = new NotificationChannel(channelId,
                        title,
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                if (notificationManager != null)
                    notificationManager.createNotificationChannel(channel);

            } else {
                notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
            }

            if (notificationManager != null)
                notificationManager.notify((int) System.currentTimeMillis() + value, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
