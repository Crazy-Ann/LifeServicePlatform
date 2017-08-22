package com.service.customer.components.tts;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.service.customer.components.constant.Constant;
import com.service.customer.components.tts.listener.OnDictationListener;
import com.service.customer.components.tts.listener.OnIntializeListener;
import com.service.customer.components.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class TTSUtil implements SynthesizerListener, InitListener, RecognizerDialogListener {

    public static TTSUtil ttsUtil;
    private SpeechSynthesizer speechSynthesizer;
    private SpeechRecognizer speechRecognizer;
    private RecognizerDialog recognizerDialog;
    private OnDictationListener onDictationListener;
    private OnIntializeListener onIntializeListener;

    private TTSUtil() {
        // cannot be instantiated
    }

    public static synchronized TTSUtil getInstance() {
        if (ttsUtil == null) {
            ttsUtil = new TTSUtil();
        }
        return ttsUtil;
    }

    public static void releaseInstance() {
        if (ttsUtil != null) {
            ttsUtil = null;
        }
    }

    public void setOnDictationListener(OnDictationListener onDictationListener) {
        this.onDictationListener = onDictationListener;
    }

    public void setOnIntializeListener(OnIntializeListener onIntializeListener) {
        this.onIntializeListener = onIntializeListener;
    }

    public void initializeSpeechSynthesizer(Context context) {
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(context, this);
        setSpeechSynthesizerParameter();
    }

    public void initializeSpeechRecognizer(Context context) {
        speechRecognizer = SpeechRecognizer.createRecognizer(context, this);
        LogUtil.getInstance().print("SpeechConstant:" + speechRecognizer);
        setSpeechRecognizerParameter();
    }

    public synchronized void startPlaying(Context context, String content) {
        if (speechSynthesizer == null) {
            initializeSpeechSynthesizer(context);
        }
        speechSynthesizer.startSpeaking(content, this);
    }

    public synchronized void stopPlaying() {
        if (speechSynthesizer != null) {
            speechSynthesizer.stopSpeaking();
        }
    }

    public synchronized void startListening(Activity activity) {
        if (speechRecognizer == null) {
            initializeSpeechRecognizer(activity);
        }
        recognizerDialog = new RecognizerDialog(activity, this);
        recognizerDialog.setListener(this);
        recognizerDialog.show();
//        speechRecognizer.startListening(this);
    }

    public synchronized void stopListening() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
    }

    public synchronized void destroy() {
        if (speechSynthesizer != null) {
            speechSynthesizer.stopSpeaking();
            speechSynthesizer.destroy();
        }
        if (speechRecognizer != null) {
            speechRecognizer.cancel();
            speechRecognizer.destroy();
        }
        releaseInstance();
    }

    private void setSpeechSynthesizerParameter() {
        speechSynthesizer.setParameter(SpeechConstant.PARAMS, null);
        speechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        speechSynthesizer.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_MSC);
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, Constant.TTS.TTS_ROLE);
        speechSynthesizer.setParameter(SpeechConstant.SPEED, Constant.TTS.TTS_SPEED);
        speechSynthesizer.setParameter(SpeechConstant.VOLUME, Constant.TTS.TTS_VOLUME);
        speechSynthesizer.setParameter(SpeechConstant.PITCH, Constant.TTS.TTS_PITCH);
        speechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, Constant.TTS.STREAM_TYPE);
        speechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, Constant.TTS.KEY_REQUEST_FOCUS);
        speechSynthesizer.setParameter(SpeechConstant.AUDIO_FORMAT, Constant.TTS.AUDIO_FORMAT);
//        speechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + Constant.Map.TTS_AUDIO_PATH);
    }

    private void setSpeechRecognizerParameter() {
        speechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        speechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        speechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, Constant.TTS.JSON);
        speechRecognizer.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_MSC);
        speechRecognizer.setParameter(SpeechConstant.LANGUAGE, Constant.TTS.LANGUAGE);
        speechRecognizer.setParameter(SpeechConstant.ACCENT, Constant.TTS.LANGUAGE_TYPE);
        speechRecognizer.setParameter(SpeechConstant.VAD_BOS, Constant.TTS.START_TIME_LINE);
        speechRecognizer.setParameter(SpeechConstant.VAD_EOS, Constant.TTS.END_TIME_LINE);
        speechRecognizer.setParameter(SpeechConstant.ASR_PTT, Constant.TTS.PUNCTUATION);
        speechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, Constant.TTS.AUDIO_FORMAT);
//        speechRecognizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + Constant.Map.TTS_AUDIO_PATH);
    }

    @Override
    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
        LogUtil.getInstance().print("TTS onBufferProgress");
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
        LogUtil.getInstance().print("TTS onEvent");
    }

    @Override
    public void onSpeakBegin() {
        LogUtil.getInstance().print("TTS onSpeakBegin");
    }

    @Override
    public void onSpeakPaused() {
        LogUtil.getInstance().print("TTS onSpeakPaused");
    }

    @Override
    public void onSpeakProgress(int arg0, int arg1, int arg2) {
        LogUtil.getInstance().print("TTS onSpeakProgress");
    }

    @Override
    public void onCompleted(SpeechError speechError) {
        LogUtil.getInstance().print("TTS onCompleted");
    }

    @Override
    public void onSpeakResumed() {
        LogUtil.getInstance().print("TTS onSpeakResumed");
    }

    @Override
    public void onInit(int resultCode) {
        LogUtil.getInstance().print("TTS onInit");
        onIntializeListener.onIntialize(resultCode);
    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean islast) {
        LogUtil.getInstance().print("TTS onResult");
        try {
            StringBuilder builder = new StringBuilder();
            JSONArray jsonArray = new JSONObject(new JSONTokener(recognizerResult.getResultString())).getJSONArray(Constant.TTS.WS);
            for (int i = 0; i < jsonArray.length(); i++) {
                builder.append(jsonArray.getJSONObject(i).getJSONArray(Constant.TTS.CW).getJSONObject(0).getString(Constant.TTS.W));
            }
            if (!TextUtils.isEmpty(builder.toString())) {
                onDictationListener.onDictation(builder.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(SpeechError speechError) {
        LogUtil.getInstance().print("TTS onError");
        recognizerDialog.dismiss();
    }
}
