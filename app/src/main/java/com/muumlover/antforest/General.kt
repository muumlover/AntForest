package com.muumlover.antforest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class General {
    companion object {
        /**
         * 发送 Intent
         */
        @JvmStatic
        fun openUri(context: Context, s: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(s))
            context.startActivity(intent)
        }

        /**
         * 显示 Toast
         */
        @JvmStatic
        fun showToast(context: Context, s: String) {
            val toast = Toast.makeText(context, s, Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}