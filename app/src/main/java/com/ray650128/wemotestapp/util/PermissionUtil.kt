package com.ray650128.wemotestapp.util

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


/**
 * 權限工具
 * 用法：
 *     1.在 Activity 中定義所需的權限陣列
 *     2.呼叫 PermissionUtil.checkPermissions()
 *     3.在 PermissionResultCallback 中處理權限授與或拒絕的處裡
 */
object PermissionUtil {
    /**
     * 當權限拒絕時，是否顯示系統的應用程式設定頁面
     */
    var showSystemSetting: Boolean = true

    private const val mRequestCode: Int = 100
    private var mPermissionResult: PermissionResultCallback? = null
    private var mPermissionDialog: AlertDialog? = null

    /**
     * 檢查權限
     * @param context  Activity 對象
     * @param permissions  要求的權限陣列
     * @param permissionResult  權限授與/拒絕的回呼
     */
    fun checkPermission(
        context: Activity,
        permissions: Array<String>,
        @NonNull permissionResultCallback: PermissionResultCallback
    ) {
        mPermissionResult = permissionResultCallback

        if (Build.VERSION.SDK_INT < 23) {   // 如果系統低於 Android 6.0，不使用動態申請
            permissionResultCallback.onGrant()
            return
        }

        // 遍歷整個 permissions 陣列，檢查有哪些權限未允許
        val mPermissionList = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 將沒有允許的權限加入 mPermissionList
                mPermissionList.add(permission)
            }
        }

        // 申請權限
        if (mPermissionList.size > 0) { // mPermissionList 中有權限沒有被允許
            ActivityCompat.requestPermissions(context, permissions, mRequestCode)
        } else {
            // 全部的權限都已被允許
            permissionResultCallback.onGrant()
            return
        }
    }

    /**
     * onRequestPermissionsResult，
     * 請務必在 Activity 的 onRequestPermissionsResult 呼叫此方法
     * @param context  Activity 對象
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    fun onRequestPermissionsResult(
        context: Activity,
        requestCode: Int,
        @NonNull permissions: Array<out String>,
        @NonNull grantResults: IntArray
    ) {
        // 檢查是否有權限被拒
        var hasPermissionDenied = false

        // 被拒絕的權限
        val denyList = ArrayList<String>()

        if (mRequestCode == requestCode) {
            for (i in grantResults.indices) {
                if (grantResults[i] == -1) {
                    hasPermissionDenied = true
                    denyList.add(permissions[i])
                }
            }
        }

        // 如果有權限被拒絕時
        if (hasPermissionDenied) {
            if (showSystemSetting) {
                showSystemPermissionSettingDialog(context, denyList)
            } else {
                mPermissionResult?.onDeny(denyList)
            }
        } else {
            // 全部的權限都已被允許
            mPermissionResult?.onGrant()
        }
    }

    /**
     * 顯示系統設定對話框
     * @param context
     * @param denies  被拒絕的權限
     */
    private fun showSystemPermissionSettingDialog(context: Activity, denies: ArrayList<String>?) {
        val mPackageName = context.packageName
        if (mPermissionDialog == null) {
            mPermissionDialog = AlertDialog.Builder(context)
                .setMessage("已禁用權限，請手動授與")
                .setPositiveButton("前往設定") { _, _ ->
                    cancelPermissionDialog()

                    val packageUri = Uri.parse("package:$mPackageName")
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri)
                    context.startActivity(intent)
                    context.finish()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    // 關閉對話框
                    cancelPermissionDialog()

                    mPermissionResult?.onDeny(denies)
                }
                .create()
        }
        mPermissionDialog?.show()
    }

    /**
     * 關閉對話框
     */
    private fun cancelPermissionDialog() {
        if (mPermissionDialog != null) {
            mPermissionDialog?.cancel()
            mPermissionDialog = null
        }
    }

    /**
     * 動態申請權限的狀態回呼
     */
    interface PermissionResultCallback {
        /**
         * 已授與
         */
        fun onGrant()

        /**
         * 已拒絕
         * @param denies  被拒絕的權限
         */
        fun onDeny(denies: ArrayList<String>?)
    }
}