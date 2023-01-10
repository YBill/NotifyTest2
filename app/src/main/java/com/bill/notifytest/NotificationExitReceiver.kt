package com.bill.notifytest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class NotificationExitReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notifyId = intent?.getIntExtra(NotificationConstants.KEY_NOTIFY_ID, 0) ?: 0
        if (notifyId > 0 && context != null) {
            NotificationManagerCompat.from(context).cancel(notifyId)
        }
    }


}