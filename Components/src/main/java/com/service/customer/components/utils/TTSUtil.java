package com.service.customer.components.utils;

import android.content.Context;
import android.os.Bundle;

import com.service.customer.components.constant.Constant;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

public class TTSUtil implements SynthesizerListener {
    private Context mContext;
    public static TTSUtil mTTSUtil;
    private SpeechSynthesizer mSpeechSynthesizer;

    private TTSUtil() {
        // cannot be instantiated
    }

    public static synchronized TTSUtil getInstance() {
        if (mTTSUtil == null) {
            mTTSUtil = new TTSUtil();
        }
        return mTTSUtil;
    }

    public static void releaseInstance() {
        if (mTTSUtil != null) {
            mTTSUtil = null;
        }
    }

    public void initializeSpeechSynthesizer(Context context) {
        mContext = context;
        SpeechUtility.createUtility(context, SpeechConstant.APPID + Constant.TTS.APPID);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(context, null);
        setSpeechSynthesizerParameter();
    }

    public synchronized void startPlaying(Context context, String content) {
        if (mSpeechSynthesizer == null) {
            initializeSpeechSynthesizer(context);
        }
        mSpeechSynthesizer.startSpeaking(content, this);
    }

    public synchronized void stopPlaying() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
        }
    }


    public synchronized void destroy() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
            mSpeechSynthesizer.destroy();
        }
        releaseInstance();
    }

    private void setSpeechSynthesizerParameter() {
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, Constant.TTS.TTS_ROLE);
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, Constant.TTS.TTS_SPEED);//语速
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, Constant.TTS.TTS_VOLUME);//音量
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
    }


    @Override
    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
        LogUtil.getInstance().print("onBufferProgress");
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
        LogUtil.getInstance().print("onEvent");
    }

    @Override
    public void onSpeakBegin() {
        LogUtil.getInstance().print("onSpeakBegin");
    }

    @Override
    public void onSpeakPaused() {
        LogUtil.getInstance().print("onSpeakPaused");
    }

    @Override
    public void onSpeakProgress(int arg0, int arg1, int arg2) {
        LogUtil.getInstance().print("onSpeakProgress");
    }

    @Override
    public void onCompleted(SpeechError speechError) {
        LogUtil.getInstance().print("onCompleted");
    }

    @Override
    public void onSpeakResumed() {
        LogUtil.getInstance().print("onSpeakResumed");
    }

}
