package com.bill.notifytest

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

class PermanentService : Service() {

    companion object {
        private const val TAG = "PermanentService"
    }

    override fun onCreate() {
        super.onCreate()
        startForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun startForeground() {
        val notification = getNotification(this)
        startForeground(NotificationConstants.NOTIFICATION_RESIDENT, notification)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.notify(NotificationConstants.NOTIFICATION_RESIDENT, notification)
    }

    private fun getPendingIntent(
        context: Context,
        content: String,
        requestCode: Int
    ): PendingIntent {
        val intent = Intent(context, NotificationTransitActivity::class.java)
        intent.putExtra(NotificationConstants.KEY_NOTIFY_CONTENT, content)
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            TargetSSupport.pendingIntentFlagImmutable()
        )
    }

    private fun getNotification(context: Context): Notification {
        val remoteView = RemoteViews(context.packageName, R.layout.notification_permanent_layout)
        remoteView.setOnClickPendingIntent(
            R.id.ll_pn_clean,
            getPendingIntent(context, "Permanent Clean", NotificationConstants.REQUEST_CODE_CLEAN)
        )
        remoteView.setOnClickPendingIntent(
            R.id.ll_pn_booster,
            getPendingIntent(context, "Permanent Boost", NotificationConstants.REQUEST_CODE_PHONE_BOOST)
        )
        remoteView.setOnClickPendingIntent(
            R.id.ll_pn_app_security,
            getPendingIntent(context, "Permanent Security", NotificationConstants.REQUEST_CODE_APP_SECURITY)
        )
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, NotifyManager.CHANNEL_ID_PERMANENT)
                .setSmallIcon(R.drawable.ic_notification_small)
                .setOngoing(true)
                .setContent(remoteView)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.priority = NotificationCompat.PRIORITY_MAX
        }
        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}