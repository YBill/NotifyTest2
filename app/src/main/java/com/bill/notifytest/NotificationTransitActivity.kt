package com.bill.notifytest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat

class NotificationTransitActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent == null) {
            gotoMainAct()
            return
        }

        // 关闭通知
        val notifyId = intent.getIntExtra(NotificationConstants.KEY_NOTIFY_ID, 0)
        if (notifyId > 0) {
            NotificationManagerCompat.from(applicationContext).cancel(notifyId)
        }

        val content = intent.getStringExtra(NotificationConstants.KEY_NOTIFY_CONTENT) ?: ""

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(NotificationConstants.KEY_NOTIFY_CONTENT, content)
        startActivity(intent)
        finish()
    }

    private fun gotoMainAct() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}