package com.muumlover.antforest

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.graphics.Path
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.util.*


class AntForest {
    private val TAG = "AntForest"

    var panelId = "J_barrier_free"
    var moreFirend = "查看更多好友"
    var className: String? = null
    private var webkitRoot: AccessibilityNodeInfo? = null

    fun open(mContext: Context) {
        if (openAntForest(mContext)) {
            General.showToast(mContext, "打开蚂蚁森林成功，等待加载中。")
        } else {
            General.showToast(mContext, "未能打开蚂蚁森林，请手动打开。")
        }
    }

    private fun openAntForest(context: Context): Boolean {
        try {
            //final String alipayqr = "alipayqr://platformapi/startapp?clientVersion=3.7.0.0718";
            val alipayqr = "alipayqr://platformapi/startapp?saId=60000002&clientVersion=3.7.0.0718"
            General.openUri(context, alipayqr)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    private fun mainPage() {
        val panelId = "J_barrier_free"
        val moreFirend = "查看更多好友"
    }

    fun analysisFrameLayout(event: AccessibilityEvent) {
        val nodeFrameLayout = event.source
        // nodeFrameLayout: android.widget.FrameLayout
        for (i in 0 until nodeFrameLayout.childCount) {
            val child = nodeFrameLayout.getChild(i)
            if (child.className == "android.widget.TextView") {
                Log.d(TAG, "当前页面标题为：${child.text}")
                when (child.text) {
                    "蚂蚁森林" -> {
                        Log.d(TAG, "当前是 蚂蚁森林 首页")
                    }
                }
            }
        }
        setWebkitRoot(event)
    }


    fun setWebkitRoot(event: AccessibilityEvent) {
        var source: AccessibilityNodeInfo = event.source ?: return
        while (source.childCount > 0) {
            source = source.getChild(0)
            Log.d(TAG, "Class: ${source.className}")
            if (source.className == "android.webkit.WebView") {
                Log.d(TAG, "设置 WebkitRoot 节点: $source")
                this.webkitRoot = source
//                if (this.webkitRoot == null) {
//                    Log.d(TAG, "WebkitRoot 节点已更新")
//                    this.webkitRoot = source
//                } else {
//                    Log.d(TAG, "WebkitRoot 节点未更新")
//                }
                break
            }
        }
    }

    fun clearWebkitRoot() {
        this.webkitRoot = null
    }

    private fun getChildById(node: AccessibilityNodeInfo, childName: String): AccessibilityNodeInfo? {
        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            Log.d(TAG, "查找 $childName: index $i of ${node.childCount} is $child")
            if (child == null) continue
            if (child.viewIdResourceName == childName)
                return child
        }
        return node
    }

    private fun getJAppOutter(): AccessibilityNodeInfo? {
        val webkitRoot = this.webkitRoot
        Log.d(TAG, "找到 WebkitRoot: $webkitRoot")
        return if (webkitRoot == null) null
        else getChildById(webkitRoot, "J_app_outter")
    }

    private fun getJAfHome(): AccessibilityNodeInfo? {
        val jAppOutter = this.getJAppOutter()
        Log.d(TAG, "找到 J_app_outter: $jAppOutter")
        return if (jAppOutter == null) null
        else getChildById(jAppOutter, "J_af_home")
    }

    private fun getJHomePanel(): AccessibilityNodeInfo? {
        val jAfHome = this.getJAfHome()
        Log.d(TAG, "找到 J_af_home: $jAfHome")
        return if (jAfHome == null) null
        else getChildById(jAfHome, "J_home_panel")
    }

    private fun getJBarrierFree(): AccessibilityNodeInfo? {
        val jAfHome = this.getJAfHome()
        Log.d(TAG, "找到 J_af_home: $jAfHome")
        return if (jAfHome == null) null
        else getChildById(jAfHome, "J_barrier_free")
    }

    private fun getMoreFirendNode(): AccessibilityNodeInfo? {
        val webkitRoot = this.webkitRoot ?: return null
        Log.d(TAG, "找到 WebkitRoot: $webkitRoot")
        for (i in 0 until webkitRoot.childCount) {
            val root = webkitRoot.getChild(i) ?: return null
            if (root.viewIdResourceName == null) {
                for (j in 0 until webkitRoot.childCount) {
                    val node = webkitRoot.getChild(i)
                    if (node.contentDescription == "查看更多好友")
                        return node
                }
            }
        }
        return null
    }

    fun goMoreFriendPage() {
        val node = getMoreFirendNode() ?: return
        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }

    private val descList = listOf("地图", "成就", "通知", "背包", "任务", "攻略", "发消息", "弹幕", "浇水")
    fun getAllBall(): List<AccessibilityNodeInfo> {
        Log.d(TAG, "Func: getAllBall 查找能量球")
        val allBall = ArrayList<AccessibilityNodeInfo>()
        val jBarrierFree = getJBarrierFree()
        Log.d(TAG, "找到 J_barrier_free: $jBarrierFree")
        if (jBarrierFree != null)
            for (i in 0 until jBarrierFree.childCount) {
                val child = jBarrierFree.getChild(i) ?: continue
                Log.d(TAG, "检查是否为能量球：$child")
                if (child.contentDescription in descList) continue
                if (child.contentDescription == null) continue
                if (child.contentDescription == " " ||
                    (child.contentDescription.length >= 2 && child.contentDescription.subSequence(0, 2) == "收集")
                ) {
                    allBall.add(jBarrierFree.getChild(i))
                }
            }
        return allBall
    }

//    fun clickNode(node: AccessibilityNodeInfo) {
//        node.b
//        val x = 0
//        val y = 0
//        val order = arrayOf("input", "tap", " ", x.toString() + "", y.toString() + "")
//        try {
//            ProcessBuilder(*order).start()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//    }


    fun collageOwn(allBall: List<AccessibilityNodeInfo>) {
        if (allBall.isNotEmpty()) {
            for (ball in allBall) {
//                ball.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                useGestureClick(ball, ListeningService())
            }
        }
    }


//    void waitForest(){
//        AccessibilityNodeInfo nodeInfo = getRootIn
//        List<AccessibilityNodeInfo> list = nodeInfo.find
//    }

    fun useGestureClick(info: AccessibilityNodeInfo?, accessibilityService: AccessibilityService) {
        if (info == null) return
        val rect = Rect()
        info.getBoundsInScreen(rect)
        val builder = GestureDescription.Builder()
        val path = Path()
        path.moveTo(rect.centerX().toFloat(), rect.centerY().toFloat())
        val gestureDescription = builder
            .addStroke(GestureDescription.StrokeDescription(path, 100, 50))
            .build()
        accessibilityService.dispatchGesture(gestureDescription, object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
            }
        }, null)
    }
}

