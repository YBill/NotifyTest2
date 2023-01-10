package com.bill.notifytest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val content = intent.getStringExtra(NotificationConstants.KEY_NOTIFY_CONTENT) ?: ""
        titleTv.text = content
    }
}