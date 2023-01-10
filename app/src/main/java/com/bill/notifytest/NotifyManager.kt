package com.bill.notifytest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat


object NotifyManager {

    const val CHANNEL_ID_URGENT = "channel_id_urgent"
    private const val CHANNEL_NAME_URGENT = "Notice"

    const val CHANNEL_ID_PERMANENT = "channel_id_permanent"
    private const val CHANNEL_NAME_PERMANENT = "Tools"

    fun createNotificationChannel(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return true
        }

        try {
            createNotificationChannel(
                context,
                CHANNEL_ID_URGENT,
                CHANNEL_NAME_URGENT,
                NotificationManager.IMPORTANCE_HIGH
            )
            createNotificationChannel(
                context,
                CHANNEL_ID_PERMANENT,
                CHANNEL_NAME_PERMANENT,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        importance: Int
    ): Boolean {
        val appContext = context.applicationContext
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.setSound(null, null)
        channel.enableLights(false)
        channel.enableVibration(false)
        channel.vibrationPattern = LongArray(0)
        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return true
    }

    /**
     * 检查当前渠道的通知是否可用，Android O及以上版本调用
     * <p>
     * 注：areNotificationsEnabled()返回false时，即当前App通知被关时，此方法仍可能返回true，
     *
     * @param channelId 渠道Id
     * @return false：不可用
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun areChannelsEnabled(context: Context, channelId: String): Boolean {
        val appContext = context.applicationContext
        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = notificationManager.getNotificationChannel(channelId);
        if (notificationChannel != null && notificationChannel.importance == NotificationManager.IMPORTANCE_NONE) {
            return false;
        }
        return true;
    }

    /**
     * 检查通知是否可用
     *
     * @return false：不可用
     */
    fun areNotificationsEnabled(context: Context): Boolean {
        val appContext = context.applicationContext
        val notificationManagerCompat = NotificationManagerCompat.from(appContext)
        return notificationManagerCompat.areNotificationsEnabled();
    }

    /**
     * 调转到渠道设置页
     *
     * @param channelId
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun gotoChannelSetting(context: Context, channelId: String) {
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
        context.startActivity(intent)
    }

    /**
     * 调转到通知设置页
     *
     */
    fun gotoNotificationSetting(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS;
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.applicationInfo.uid)
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
            context.startActivity(intent)
        } else {
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo.uid)
            context.startActivity(intent)
        }
    }
}