package com.muumlover.antforest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log

class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val softState = ListeningService.mIsStart
        val sysState = isAccessibilitySettingsOn(applicationContext, ListeningService::class.java.canonicalName)

        if (softState && sysState) {
            Log.d(TAG, "无障碍服务运行中")
            Global.ant.open(applicationContext)
        } else if (!softState && sysState) {
            Log.e(TAG, "无障碍服务异常，软件服务未启动，尝试启动")
            try {
                this.startActivity(Intent(this@MainActivity, ListeningService::class.java))
                Log.e(TAG, "软件服务启动成功")
//              this.stopService(Intent(this@MainActivity, ListeningService::class.java))
            } catch (e: Exception) {
                Log.e(TAG, "软件服务启动失败")
                General.showToast(this, "无障碍服务异常\r\n软件服务启动失败")
                e.printStackTrace()
            }
        } else if (softState && !sysState) {
            Log.e(TAG, "无障碍服务异常，系统服务未启动")
            General.showToast(this, "无障碍服务异常\r\n系统服务未启动，请手动开启")
            try {
                this.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            } catch (e: Exception) {
                this.startActivity(Intent(Settings.ACTION_SETTINGS))
                e.printStackTrace()
            }
        } else {
            Log.d(TAG, "无障碍服务已停止")
            General.showToast(this, "AntForest\r\n无障碍服务已关闭，请手动开启")
            try {
                this.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            } catch (e: Exception) {
                this.startActivity(Intent(Settings.ACTION_SETTINGS))
                e.printStackTrace()
            }
        }
    }

    /**
     * 检测辅助功能是否开启
     *
     * @param mContext
     * @return boolean
     */
    private fun isAccessibilitySettingsOn(mContext: Context, serviceName: String?): Boolean {
        var accessibilityEnabled = 0
        // 对应的服务
        val service = "$packageName/$serviceName"
        //Log.i(TAG, "service:" + service);
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.applicationContext.contentResolver,
                android.provider.Settings.Secure.ACCESSIBILITY_ENABLED
            )
            Log.v(TAG, "accessibilityEnabled = $accessibilityEnabled")
        } catch (e: Settings.SettingNotFoundException) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.message)
        }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------")
            val settingValue = Settings.Secure.getString(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()

                    Log.v(
                        TAG,
                        "-------------- > accessibilityService :: $accessibilityService $service"
                    )
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!")
                        return true
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***")
        }
        return false
    }
}
