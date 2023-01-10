package com.bill.notifytest

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LockScreenNotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_pop_layout)
        val attributes = window.attributes
        attributes.width = getScreenWidth()
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window.attributes = attributes
        initView()
    }

    private fun getScreenWidth(): Int {
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val point = Point()
        wm.defaultDisplay.getRealSize(point)
        return point.x
    }

    private fun initView() {
        val content = intent.getStringExtra(NotificationConstants.KEY_NOTIFY_CONTENT) ?: ""

        val notifyTypeTv = findViewById<TextView>(R.id.notify_type)
        notifyTypeTv.text = "Lock"

        val btnConfirm = findViewById<TextView>(R.id.btn_nc_confirm)
        val tvExit = findViewById<TextView>(R.id.btn_nc_cancel)
        btnConfirm.setOnClickListener {
            goToTransferPage(content)
        }
        tvExit.setOnClickListener {
            this.finish()
            overridePendingTransition(0, 0)
        }
    }

    private fun goToTransferPage(content: String) {
        val intent = Intent(this, NotificationTransitActivity::class.java)
        intent.putExtra(NotificationConstants.KEY_NOTIFY_CONTENT, content)
        startActivity(intent)
        this.finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                return false
            }
        }
        return true
    }

    companion object {
        fun enter(context: Context, content: String): Intent {
            val intent = Intent(context, LockScreenNotificationActivity::class.java)
            intent.putExtra(NotificationConstants.KEY_NOTIFY_CONTENT, content)
            return intent
        }
    }
}