package com.muumlover.antforest

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.PendingIntent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ListeningService : AccessibilityService() {
    private val TAG = javaClass.name

    /**
     * 辅助功能是否启动
     */
    companion object {
        var mIsStart = false
        var mService: ListeningService? = null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate——无障碍物服务创建成功")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected——无障碍服务启动成功")
        mIsStart = true
        mService = this
    }

    override fun onInterrupt() {
        Log.d(TAG, "onInterrupt——无障碍服务被中断")
        mIsStart = false
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy——无障碍服务已关闭")
        mIsStart = false
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val eventType = event?.eventType
        when (eventType) {
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                Log.v(TAG, "TYPE_NOTIFICATION_STATE_CHANGED")
                handleNotification(event)
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                Log.v(TAG, "TYPE_WINDOW_CONTENT_CHANGED")
                Log.v(TAG, "TYPE_WINDOW_STATE_CHANGED")
                val className = event.className.toString()
                Log.v(TAG, className)
                when (className) {
                    "com.alipay.mobile.quinox.SchemeLauncherActivity" -> {
                    }
                    "com.alipay.mobile.quinox.LauncherActivity" -> {
                    }
                    "com.eg.android.AlipayGphone.AlipayLogin" -> {
                    }
                    "com.alipay.mobile.nebulacore.ui.H5Activity" -> {
                        Log.d(TAG, "收到 TYPE_WINDOW_STATE_CHANGED 事件，类名称 com.alipay.mobile.nebulacore.ui.H5Activity")
                        Global.ant.loadWebView(event)
                        handleViewUpdate(event)
                    }
                }
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                Log.v(TAG, "TYPE_WINDOW_STATE_CHANGED")
                val className = event.className.toString()
                Log.v(TAG, className)
                when (className) {
                    "com.alipay.mobile.quinox.SchemeLauncherActivity" -> {
                    }
                    "com.alipay.mobile.quinox.LauncherActivity" -> {
                    }
                    "com.eg.android.AlipayGphone.AlipayLogin" -> {
                    }
                    "com.alipay.mobile.nebulacore.ui.H5Activity" -> {
                        Log.d(TAG, "收到 TYPE_WINDOW_STATE_CHANGED 事件，类名称 com.alipay.mobile.nebulacore.ui.H5Activity")
                        Global.ant.loadWebView(event)
                        handleViewUpdate(event)
                    }
                }
            }
            AccessibilityEvent.TYPE_ANNOUNCEMENT -> Log.v(TAG, "TYPE_ANNOUNCEMENT")
            AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT -> Log.v(TAG, "TYPE_ASSIST_READING_CONTEXT")
            AccessibilityEvent.TYPE_GESTURE_DETECTION_END -> Log.v(TAG, "TYPE_GESTURE_DETECTION_END")
            AccessibilityEvent.TYPE_GESTURE_DETECTION_START -> Log.v(TAG, "TYPE_GESTURE_DETECTION_START")
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END -> Log.v(TAG, "TYPE_TOUCH_EXPLORATION_GESTURE_END")
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START -> Log.v(
                TAG,
                "TYPE_TOUCH_EXPLORATION_GESTURE_START"
            )
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_END -> Log.v(TAG, "TYPE_TOUCH_INTERACTION_END")
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_START -> Log.v(TAG, "TYPE_TOUCH_INTERACTION_START")
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED -> Log.v(TAG, "TYPE_VIEW_ACCESSIBILITY_FOCUSED")
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED -> Log.v(
                TAG,
                "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED"
            )
            AccessibilityEvent.TYPE_VIEW_CLICKED -> Log.v(TAG, "TYPE_VIEW_CLICKED")
            AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED -> Log.v(TAG, "TYPE_VIEW_CONTEXT_CLICKED")
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> Log.v(TAG, "TYPE_VIEW_FOCUSED")
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> Log.v(TAG, "TYPE_VIEW_HOVER_ENTER")
            AccessibilityEvent.TYPE_VIEW_HOVER_EXIT -> Log.v(TAG, "TYPE_VIEW_HOVER_EXIT")
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> Log.v(TAG, "TYPE_VIEW_LONG_CLICKED")
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> Log.v(TAG, "TYPE_VIEW_SCROLLED")
            AccessibilityEvent.TYPE_VIEW_SELECTED -> Log.v(TAG, "TYPE_VIEW_SELECTED")
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> Log.v(TAG, "TYPE_VIEW_TEXT_CHANGED")
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> Log.v(TAG, "TYPE_VIEW_TEXT_SELECTION_CHANGED")
            AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY -> Log.v(
                TAG,
                "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY"
            )
            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> {
                Log.v(TAG, "TYPE_WINDOWS_CHANGED")
            }
        }
    }

    /**
     * 处理通知栏信息
     *
     *
     * 如果是微信红包的提示信息,则模拟点击
     *
     * @param event
     */
    private fun handleNotification(event: AccessibilityEvent) {
        val texts = event.text
        if (!texts.isEmpty()) {
            for (text in texts) {
                val content = text.toString()
                //如果微信红包的提示信息,则模拟点击进入相应的聊天窗口
                if (content.contains("能量")) {
                    if (event.parcelableData != null && event.parcelableData is Notification) {
                        val notification = event.parcelableData as Notification
                        val pendingIntent = notification.contentIntent
                        try {
                            pendingIntent.send()
                        } catch (e: PendingIntent.CanceledException) {
                            e.printStackTrace()
                        }

                    }
                }
            }
        }
    }

    private fun handleViewUpdate(event: AccessibilityEvent) {
        Log.d(TAG, "handleViewUpdate——支付宝H5更新")
        //        if(event.getClassName().toString().equals("com.uc.webkit.be"))
        //        if (Global.ant.onPageMain()) {
        //        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        //            List<AccessibilityNodeInfo> nodeInfoList = nodeInfo.findAccessibilityNodeInfosByViewId(AntForest.panelId);
        //        List<AccessibilityNodeInfo> nodeInfoList = nodeInfo.findAccessibilityNodeInfosByText("地图");
        val allBall = Global.ant.getAllBall()
        Log.i(TAG, "找到" + allBall.size + "个能量球")
        General.showToast(this, "找到" + allBall.size + "个能量球")
        if (allBall.isNotEmpty()) {
            for (ball in allBall) {
                ball.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        //        }
    }

}