package com.bill.notifytest

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showTargetSdkVersion()
        initView()
    }

    override fun onResume() {
        super.onResume()
        Log.i("Bill", "onResume")
        showMessage()
    }

    private fun showTargetSdkVersion() {
        val build = StringBuilder()
        build.append("targetSdkVersion = ")
        build.append(applicationInfo.targetSdkVersion)

        val bar = supportActionBar
        bar?.title = build.toString()
    }

    private fun showMessage() {
        val notificationSwitch = if (NotifyManager.areNotificationsEnabled(this)) "on" else "off"
        val urgentChannelSwitch =
            if (areChannelsEnabled(NotifyManager.CHANNEL_ID_URGENT)) "on" else "off"
        val permanentChannelSwitch =
            if (areChannelsEnabled(NotifyManager.CHANNEL_ID_PERMANENT)) "on" else "off"


        val build = StringBuilder()
        build.append("总通知开关：")
        build.append(notificationSwitch)
        build.append("\n")
        build.append("紧急通知渠道开关：")
        build.append(urgentChannelSwitch)
        build.append("\n")
        build.append("常驻通知渠道开关：")
        build.append(permanentChannelSwitch)
        tv_message.text = build.toString()
    }

    private fun initView() {
        btn_create_channel.setOnClickListener(this)
        btn_show_permanent.setOnClickListener(this)
        btn_show_urgent.setOnClickListener(this)
        btn_notification_setting.setOnClickListener(this)
        btn_urgent_setting.setOnClickListener(this)
        btn_permanent_setting.setOnClickListener(this)
        btn_apply_notification.setOnClickListener(this)
    }

    private fun areChannelsEnabled(channelId: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return true
        }
        return NotifyManager.areChannelsEnabled(this, channelId)
    }

    private fun gotoChannelSetting(channelId: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        NotifyManager.gotoChannelSetting(this, channelId)
    }

    private fun startPermanentService(context: Context) {
        val intent = Intent(this, PermanentService::class.java)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            context.startService(intent)
        } else {
            context.startForegroundService(intent)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_create_channel -> {
                val result = NotifyManager.createNotificationChannel(this)
                Log.i("Bill", "createChannel is $result")
            }
            R.id.btn_show_permanent -> {
                startPermanentService(applicationContext)
            }
            R.id.btn_show_urgent -> {
                NotificationUtils.showNotification(this)
            }
            R.id.btn_notification_setting -> {
                NotifyManager.gotoNotificationSetting(applicationContext)
            }
            R.id.btn_urgent_setting -> {
                gotoChannelSetting(NotifyManager.CHANNEL_ID_URGENT)
            }
            R.id.btn_permanent_setting -> {
                gotoChannelSetting(NotifyManager.CHANNEL_ID_PERMANENT)
            }
            R.id.btn_apply_notification -> {
                requestNotificationPermission(this)
            }
        }
    }

    private fun requestNotificationPermission(activity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= 33) {
            val postNotifications = "android.permission.POST_NOTIFICATIONS"
            if (ActivityCompat.checkSelfPermission(activity, postNotifications)
                != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("Bill", "没有 POST_NOTIFICATIONS 权限")
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, postNotifications)
                ) {
                    Log.d("Bill", "POST_NOTIFICATIONS 权限被拒绝不再弹出了，去设置页")
                    NotifyManager.gotoNotificationSetting(this)
                } else {
                    Log.d("Bill", "开始申请 POST_NOTIFICATIONS 权限")
                }
                ActivityCompat.requestPermissions(activity, arrayOf(postNotifications), 100)
            } else {
                Log.d("Bill", "已经有 POST_NOTIFICATIONS 权限了")
            }
        } else {
            Log.d("Bill", "${Build.VERSION.SDK_INT} < 33，去设置页")
            NotifyManager.gotoNotificationSetting(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("Bill", "requestCode = $requestCode")
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Bill", "POST_NOTIFICATIONS 权限申请成功")
            } else {
                Log.d("Bill", "POST_NOTIFICATIONS 权限被拒绝")
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    }

}