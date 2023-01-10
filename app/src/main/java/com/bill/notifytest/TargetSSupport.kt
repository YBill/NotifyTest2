package com.bill.notifytest

import android.app.PendingIntent
import android.os.Build

object TargetSSupport {

    fun pendingIntentFlagImmutable(flags: Int = PendingIntent.FLAG_UPDATE_CURRENT): Int {
        return flags or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else 0
    }

    fun pendingIntentFlagMutable(flags: Int = PendingIntent.FLAG_UPDATE_CURRENT): Int {
        return flags or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
    }
}