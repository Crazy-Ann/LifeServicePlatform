package com.service.customer.ui.presenter.listener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.service.customer.components.utils.LogUtil;

public class CustomPhoneStateListener extends PhoneStateListener {

    private static CustomPhoneStateListener customPhoneStateListener;
    private String phoneNumber;
    private OnSaveTaskInfoListener onSaveTaskInfoListener;
    private boolean hasHangUp;

    private CustomPhoneStateListener() {
        // cannot be instantiated
    }

    public static synchronized CustomPhoneStateListener getInstance() {
        if (customPhoneStateListener == null) {
            customPhoneStateListener = new CustomPhoneStateListener();
        }
        return customPhoneStateListener;
    }

    public static void releaseInstance() {
        if (customPhoneStateListener != null) {
            customPhoneStateListener = null;
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setOnSaveTaskInfoListener(OnSaveTaskInfoListener onSaveTaskInfoListener) {
        this.onSaveTaskInfoListener = onSaveTaskInfoListener;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                LogUtil.getInstance().print("CALL_STATE_IDLE");
                if (hasHangUp) {
                    onSaveTaskInfoListener.onSaveTaskInfo(phoneNumber);
                }
                hasHangUp = false;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                LogUtil.getInstance().print("CALL_STATE_OFFHOOK");
                hasHangUp = true;
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                LogUtil.getInstance().print("CALL_STATE_RINGING");
                hasHangUp = false;
                break;
            default:
                break;
        }
    }
}
