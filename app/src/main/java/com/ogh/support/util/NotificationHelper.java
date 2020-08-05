package com.ogh.support.util;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.ogh.support.R;
import com.ogh.support.receiver.NotificationClickReceiver;


/**
 * 下载通知栏适配(8.0用这个)
 */
public class NotificationHelper {

    private NotificationManager manager;
    private Context mContext;
    private static String CHANNEL_ID = "DownloadService";//8.0两者必须一致
    private static String CHANNEL_NAME = "下载更新";//会出现在(通知-类别）
    private static final int NOTIFICATION_ID = 1;//不能为0

    public NotificationHelper(Context context) {
        this.mContext = context.getApplicationContext();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription("下载服务");//设置描述,会出现在(通知-类别-详细描述）
            //mChannel.enableLights(true);//设置提示灯
            //mChannel.setLightColor(Color.RED);//设置提示灯颜色
            //mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
            mChannel.setShowBadge(true);//显示logo
            getManager().createNotificationChannel(mChannel);
        }
    }

    private Notification.Builder getNofity(String content) {
        Notification.Builder builder = new Notification.Builder(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            builder.setChannelId(CHANNEL_ID);
        builder.setContentTitle("应用下载")//设置标题
                .setContentText(content)//设置内容
                .setSmallIcon(R.mipmap.ic_launcher)//设置状态栏图标,一定要设置,否则报错(如果不设置它启动服务前台化不会报错,但是你会发现这个通知不会启动),如果是普通通知,不设置必然报错
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setWhen(System.currentTimeMillis())//设置创建时间
                // .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))//通知栏图片
                .setPriority(Notification.PRIORITY_HIGH);
        return builder;
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
     * 显示通知栏(8.0以下调用)
     */
    public void showNotification() {
        getManager().notify(NOTIFICATION_ID, getNofity("准备下载").build());
    }

    /**
     * 显示通知栏(8.0以上调用)
     */
    public Notification getNotification() {
        return getNofity("准备下载").build();
    }

    /**
     * 不断调用次方法通知通知栏更新进度条
     */
    public void updateProgress(int progress) {
        Notification.Builder builder = progress == -1 ? getNofity("下载失败") : getNofity(mContext.getString(R.string.update_download_progress, progress)).setProgress(100, progress, false);
        getManager().notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * 下载完成后，设置通知栏可以点击
     */
    public void downloadComplete(String fileName) {
        Intent intentClick = new Intent(mContext, NotificationClickReceiver.class);
        intentClick.setAction("NOTIFICATION_CLICKED");
        intentClick.putExtra("FILE_NAME", fileName);
        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(mContext, 0, intentClick, PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = getNofity("下载已完成").setContentIntent(pendingIntentClick);
        getManager().notify(NOTIFICATION_ID, builder.build());
    }
}
