package com.freshport.apn.react;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nova on 2017/7/6.
 */

public class APNPackage implements ReactPackage {

    private Application app;

    private static final String APP_ID = "2882303761517594326";
    private static final String APP_KEY = "5541759491326";
    private static final String TAG = "MI_PUSH";

    public APNPackage(Application app) {
        this.app = app;

        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {
            MiPushClient.registerPush(this.app.getApplicationContext(), APP_ID, APP_KEY);
        }

        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List list = new ArrayList();
        APNNativeModule apnNativeModule = new APNNativeModule(reactContext);
        list.add(apnNativeModule);
        return list;

    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) this.app.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = this.app.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
