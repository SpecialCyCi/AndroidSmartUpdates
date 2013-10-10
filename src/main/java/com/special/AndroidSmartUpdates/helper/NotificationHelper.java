package com.special.AndroidSmartUpdates.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.special.AndroidSmartUpdates.Constant;
import com.special.AndroidSmartUpdates.R;

/**
 * User: special
 * Date: 13-9-17
 * Time: 下午2:52
 * Mail: specialcyci@gmail.com
 */
public class NotificationHelper {

    private final Context context;
    private NotificationManager notificationManager;
    private Notification notification;

    public NotificationHelper(Context context){
        this.context = context;
        setUpNotificationRelative();
        setNotificationView(0);
    }

    public void showNotification(){
        notificationManager.notify(Constant.NOTIFICATION_ID,notification);
    }

    public void updateProgress(int progess){
        setNotificationView(progess);
        notificationManager.notify(Constant.NOTIFICATION_ID, notification);
    }

    public void dismissNotification(){
        notificationManager.cancel(Constant.NOTIFICATION_ID);
    }

    private void setUpNotificationRelative(){
        notification = new Notification();
        notification.when = System.currentTimeMillis();
        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.icon = android.R.drawable.stat_sys_download;
        notification.contentIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void setNotificationView(int progress){
        String tips = context.getString(R.string.notification_downloading_progress) + progress + "%";
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_);
        remoteViews.setTextViewText(R.id.tv_process, tips);
        remoteViews.setProgressBar(R.id.pb_download, 100, progress, false);
        notification.contentView = remoteViews;
    }
}
