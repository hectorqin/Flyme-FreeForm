package com.sunshine.freeform.utils

import android.app.ActivityManager
import android.app.IActivityManager
import android.app.IActivityTaskManager
import android.content.Context
import android.hardware.display.DisplayManager
import android.hardware.input.IInputManager
import android.util.Log
import android.view.IWindowManager
import android.view.WindowManager
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper

/**
 * @date 2021/2/1
 * 服务相关工具类
 */
object ServiceUtils {
    private const val TAG = "ServiceUtils"
    
    lateinit var activityManager: IActivityManager
        private set
    lateinit var activityTaskManager: IActivityTaskManager
        private set
    lateinit var displayManager: DisplayManager
        private set
    lateinit var windowManager: WindowManager
        private set
    lateinit var iWindowManager: IWindowManager
        private set
    lateinit var inputManager: IInputManager
        private set

    // 标记服务是否已成功初始化
    private var isInitialized = false
    
    /**
     * 安全地初始化 Shizuku 系统服务
     * 
     * @param context 应用上下文
     * @return 初始化是否成功
     */
    fun initWithShizuku(context: Context): Boolean {
        if (!Shizuku.pingBinder()) {
            Log.e(TAG, "Shizuku binder is not available")
            return false
        }
        
        try {
            // 初始化服务
            displayManager = context.getSystemService(DisplayManager::class.java)
            windowManager = context.getSystemService(WindowManager::class.java)
            
            // 使用 Shizuku 初始化系统服务
            activityManager = IActivityManager.Stub.asInterface(
                ShizukuBinderWrapper(
                    SystemServiceHelper.getSystemService("activity")
                )
            )
            activityTaskManager = IActivityTaskManager.Stub.asInterface(
                ShizukuBinderWrapper(
                    SystemServiceHelper.getSystemService("activity_task")
                )
            )
            iWindowManager = IWindowManager.Stub.asInterface(
                ShizukuBinderWrapper(
                    SystemServiceHelper.getSystemService("window")
                )
            )
            inputManager = IInputManager.Stub.asInterface(
                ShizukuBinderWrapper(
                    SystemServiceHelper.getSystemService("input")
                )
            )
            
            isInitialized = true
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Shizuku services: ${e.message}")
            return false
        }
    }
    
    /**
     * 检查服务是否已初始化
     * 
     * @return 服务是否已初始化
     */
    fun isInitialized(): Boolean {
        return isInitialized
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    fun isServiceWork(mContext: Context, serviceName: String): Boolean {
        var isWork = false
        val myAM = mContext
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myList: List<ActivityManager.RunningServiceInfo> = myAM.getRunningServices(40)
        if (myList.isEmpty()) {
            return false
        }
        for (i in myList.indices) {
            val mName: String = myList[i].service.className
            myList[i].service.className
            if (mName == serviceName) {
                isWork = true
                break
            }
        }
        return isWork
    }
}