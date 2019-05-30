package com.frame.support.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.frame.support.R;
import com.frame.support.receiver.NotificationClickReceiver;


/**
 * 下载通知栏适配
 */
public class NotificationHelper {

    private NotificationManager manager;
    private Context mContext;
    private static String CHANNEL_ID = "redpacket";
    private static String CHANNEL_NAME = "下载更新";
    private static final int NOTIFICATION_ID = 1;
    private static String NotificationTitle = "应用下载";//通知栏要显示的标题

    public NotificationHelper(Context context) {
        this.mContext = context.getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription("应用有新版本");
            //   mChannel.enableLights(true); //是否在桌面icon右上角展示小红点(默认为true)
            mChannel.setShowBadge(true);
            getManager().createNotificationChannel(mChannel);
        }
    }

    private NotificationCompat.Builder getNofity(String title, String content) {
        return new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }

    private NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    /**
     * 取消通知栏
     */
    public void cancel() {
        getManager().cancel(NOTIFICATION_ID);
    }

    /**
     * 显示通知栏
     */
    public void showNotification(String title) {
        if (!TextUtils.isEmpty(title))
            NotificationTitle = title;
        NotificationCompat.Builder builder = getNofity(NotificationTitle, "");
        getManager().notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * 不断调用次方法通知通知栏更新进度条
     */
    public void updateProgress(int progress) {
        String text = "";
        if (progress == -1)
            text = "下载失败";
        else
            text = mContext.getString(R.string.update_download_progress, progress);
        PendingIntent pendingintent = PendingIntent.getActivity(mContext, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = getNofity(NotificationTitle, text)
                .setProgress(100, progress, false)
                .setContentIntent(pendingintent);
        getManager().notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * 下载完成后，设置通知栏可以点击
     *
     * @param fileName 需要安装的文件名
     */
    public void downloadComplete(String fileName) {
        String text = mContext.getString(R.string.update_download_progress, 100);
        Intent intentClick = new Intent(mContext, NotificationClickReceiver.class);
        intentClick.setAction("NOTIFICATION_CLICKED");
        intentClick.putExtra("FILE_NAME", fileName);
        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(mContext, 0, intentClick, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = getNofity(NotificationTitle, text).setProgress(100, 100, false).setContentIntent(pendingIntentClick);
        getManager().notify(NOTIFICATION_ID, builder.build());
    }


}
