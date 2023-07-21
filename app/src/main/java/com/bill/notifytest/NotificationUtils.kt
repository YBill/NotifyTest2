package com.bill.notifytest

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

object NotificationUtils {

    fun showNotification(context: Context) {
        val appContext = context.applicationContext

        val notifyId = NotificationConstants.NOTIFICATION_FUNCTION
        val contentView = getRemoteView(appContext, notifyId)
        val builder = NotificationCompat.Builder(appContext, NotifyManager.CHANNEL_ID_URGENT)
            .setSmallIcon(R.drawable.ic_notification_small)
            .setCustomContentView(contentView)
            .setCustomBigContentView(contentView)
            .setCustomHeadsUpContentView(contentView)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(getLockScreenIntent(context), true)

        // 设置这个桌面icon出角标（貌似设一个就行，还不能为""）
        builder.setContentTitle("1").setContentText("1")

        val notification = builder.build()

        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        notificationManager?.notify(notifyId, notification)
    }

    private fun getRemoteView(context: Context, notifyId: Int): RemoteViews {
        val remoteView = RemoteViews(context.packageName, R.layout.notification_layout)
        remoteView.setOnClickPendingIntent(
            R.id.btn_nc_cancel, getCancelIntent(context, notifyId)
        )
        remoteView.setOnClickPendingIntent(
            R.id.btn_nc_confirm,
            getFunctionIntent(context, notifyId)
        )
        return remoteView
    }

    private fun getFunctionIntent(context: Context, notifyId: Int): PendingIntent {
        val intent = Intent(context, NotificationTransitActivity::class.java)
        intent.putExtra(NotificationConstants.KEY_NOTIFY_ID, notifyId)
        intent.putExtra(NotificationConstants.KEY_NOTIFY_CONTENT, "Urgent Clean")
        return PendingIntent.getActivity(
            context,
            NotificationConstants.NOTIFICATION_REQUEST_CODE_FUNCTION,
            intent,
            TargetSSupport.pendingIntentFlagImmutable(PendingIntent.FLAG_UPDATE_CURRENT)
        )
    }

    private fun getLockScreenIntent(context: Context): PendingIntent {
        val intent = LockScreenNotificationActivity.enter(context, "Lock Clean")
        return PendingIntent.getActivity(
            context,
            NotificationConstants.NOTIFICATION_REQUEST_CODE_TO_OUT_SCREEN_ACTIVITY,
            intent,
            TargetSSupport.pendingIntentFlagImmutable()
        )
    }

    private fun getCancelIntent(context: Context, notifyId: Int): PendingIntent {
        val exitIntent = Intent(context, NotificationExitReceiver::class.java)
        exitIntent.putExtra(NotificationConstants.KEY_NOTIFY_ID, notifyId)
        return PendingIntent.getBroadcast(
            context,
            NotificationConstants.NOTIFICATION_REQUEST_CODE_EXIT,
            exitIntent,
            TargetSSupport.pendingIntentFlagImmutable()
        )
    }

}