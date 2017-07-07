package com.freshport.apn.react;


import android.content.Context;
import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * Created by Nova on 2017/7/6.
 */

public class APNNativeModule extends ReactContextBaseJavaModule {

    private static ReactApplicationContext rac;
    private static final String RECEIVE_NOTIFICATION = "receive_notification";
    private static final String OPEN_NOTIFICATION = "open_notification";

    public APNNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.rac = reactContext;
    }

    @Override
    public String getName() {
        return "APN";
    }

    @ReactMethod
    public void setAlias(String alias) {
        MiPushClient.setAlias(this.rac, alias, null);
    }

    @ReactMethod
    public void unAlias(String alias) {
        MiPushClient.unsetAlias(this.rac, alias, null);
    }


    public static class APNMessageReceiver extends PushMessageReceiver {

        private String mRegId;
        private String mTopic;
        private String mAlias;
        private String mAccount;
        private String mStartTime;
        private String mEndTime;

        @Override
        public void onReceivePassThroughMessage(Context context, MiPushMessage message) {

            if (!TextUtils.isEmpty(message.getTopic())) {
                mTopic = message.getTopic();
            } else if (!TextUtils.isEmpty(message.getAlias())) {
                mAlias = message.getAlias();
            }

        }

        @Override
        public void onNotificationMessageClicked(Context context, MiPushMessage message) {

            WritableMap params = Arguments.createMap();
            rac.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(OPEN_NOTIFICATION, params);

            if (!TextUtils.isEmpty(message.getTopic())) {
                mTopic = message.getTopic();
            } else if (!TextUtils.isEmpty(message.getAlias())) {
                mAlias = message.getAlias();
            }

        }

        @Override
        public void onNotificationMessageArrived(Context context, MiPushMessage message) {

            WritableMap params = Arguments.createMap();
            rac.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(RECEIVE_NOTIFICATION, params);

            if (!TextUtils.isEmpty(message.getTopic())) {
                mTopic = message.getTopic();
            } else if (!TextUtils.isEmpty(message.getAlias())) {
                mAlias = message.getAlias();
            }
        }

        @Override
        public void onCommandResult(Context context, MiPushCommandMessage message) {

            String command = message.getCommand();
            List<String> arguments = message.getCommandArguments();
            String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
            String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
            String log;
            if (MiPushClient.COMMAND_REGISTER.equals(command)) {
                if (message.getResultCode() == ErrorCode.SUCCESS) {
                    mRegId = cmdArg1;

                }
            } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
                if (message.getResultCode() == ErrorCode.SUCCESS) {
                    mAlias = cmdArg1;
                }
            } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
                if (message.getResultCode() == ErrorCode.SUCCESS) {
                    mAlias = cmdArg1;
                }
            } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
                if (message.getResultCode() == ErrorCode.SUCCESS) {
                    mAccount = cmdArg1;
                }
            } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
                if (message.getResultCode() == ErrorCode.SUCCESS) {
                    mAccount = cmdArg1;
                }
            } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
                if (message.getResultCode() == ErrorCode.SUCCESS) {
                    mTopic = cmdArg1;
                }
            } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
                if (message.getResultCode() == ErrorCode.SUCCESS) {
                    mTopic = cmdArg1;
                }
            } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
                if (message.getResultCode() == ErrorCode.SUCCESS) {
                    mStartTime = cmdArg1;
                    mEndTime = cmdArg2;
                }
            }
        }

        @Override
        public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {

            String command = message.getCommand();
            List<String> arguments = message.getCommandArguments();
            String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
            String log;
            if (MiPushClient.COMMAND_REGISTER.equals(command)) {
                if (message.getResultCode() == ErrorCode.SUCCESS) {
                    mRegId = cmdArg1;
                }
            } else {
                log = message.getReason();
            }
        }
    }
}
