package com.muumlover.antforest

import android.content.Context
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

    fun loadWebView(event: AccessibilityEvent) {
        var source = event.source
        while (source.childCount > 0) {
            source = source.getChild(0)
            Log.d(TAG, "Class: " + source.className)
            if (source.className == "android.webkit.WebView") {
                setWebkitRoot(source)
                break
            }
        }
    }

    private fun setWebkitRoot(source: AccessibilityNodeInfo) {
        this.webkitRoot = source
    }

    private fun getChildByName(node: AccessibilityNodeInfo, childName: String): AccessibilityNodeInfo? {
        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            Log.d(TAG, "index $i of ${node.childCount} is $child")
            if (child == null)
                continue
            if (child.viewIdResourceName == childName)
                return child
        }
        return node
    }

    private fun getJAppOutter(): AccessibilityNodeInfo? {
        val webkitRoot = this.webkitRoot
        Log.d(TAG, "webkitRoot:")
        Log.d(TAG, webkitRoot.toString())
        if (webkitRoot == null)
            return null
        return getChildByName(webkitRoot, "J_app_outter")
    }

    private fun getJAfHome(): AccessibilityNodeInfo? {
        val jAppOutter = this.getJAppOutter()
        Log.d(TAG, "J_app_outter:")
        Log.d(TAG, jAppOutter.toString())
        if (jAppOutter == null)
            return null
        return getChildByName(jAppOutter, "J_af_home")
    }

    private fun getJHomePanel(): AccessibilityNodeInfo? {
        val jAfHome = this.getJAfHome()
        Log.d(TAG, "J_af_home:")
        Log.d(TAG, jAfHome.toString())
        if (jAfHome == null)
            return null
        return getChildByName(jAfHome, "J_home_panel")
    }

    private fun getJBarrierFree(): AccessibilityNodeInfo? {
        val jAfHome = this.getJAfHome()
        Log.d(TAG, "J_af_home:")
        Log.d(TAG, jAfHome.toString())
        if (jAfHome == null)
            return null
        return getChildByName(jAfHome, "J_barrier_free")
    }

    fun getAllBall(): List<AccessibilityNodeInfo> {
        Log.d(TAG, "func:getAllBall")
        val allBall = ArrayList<AccessibilityNodeInfo>()
        val J_barrier_free = getJBarrierFree()
        Log.d(TAG, "J_barrier_free:")
        Log.d(TAG, J_barrier_free.toString())
        if (J_barrier_free != null)
            for (i in 0 until J_barrier_free.childCount - 6) {
                allBall.add(J_barrier_free.getChild(i))
            }
        return allBall
    }


//    void waitForest(){
//        AccessibilityNodeInfo nodeInfo = getRootIn
//        List<AccessibilityNodeInfo> list = nodeInfo.find
//    }
}