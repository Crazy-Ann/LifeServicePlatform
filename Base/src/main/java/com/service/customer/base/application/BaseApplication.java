package com.service.customer.base.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.google.common.collect.Lists;
import com.iflytek.cloud.SpeechUtility;
import com.service.customer.base.BuildConfig;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.constant.Constant;
import com.service.customer.components.http.Configuration;
import com.service.customer.components.http.CustomHttpClient;
import com.service.customer.components.http.model.Parameter;
import com.service.customer.components.mta.MTACrashModule;
import com.service.customer.components.tts.TTSUtil;
import com.service.customer.components.utils.ActivityUtil;
import com.service.customer.components.utils.AnimationUtil;
import com.service.customer.components.utils.ApplicationUtil;
import com.service.customer.components.utils.BundleUtil;
import com.service.customer.components.utils.DeviceUtil;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.NetworkUtil;
import com.service.customer.components.utils.ReflectUtil;
import com.service.customer.components.utils.SecurityUtil;
import com.service.customer.components.utils.SharedPreferenceUtil;
import com.service.customer.components.utils.SnackBarUtil;
import com.service.customer.components.utils.StrictModeUtil;
import com.service.customer.components.utils.ToastUtil;
import com.service.customer.components.utils.TypefaceUtil;
import com.service.customer.components.utils.ViewUtil;
import com.tencent.mta.track.StatisticsDataAPI;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatCrashCallback;
import com.tencent.stat.StatCrashReporter;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;

import okhttp3.Headers;
import okhttp3.Interceptor;

public class BaseApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private static BaseApplication application;
    private String clientId;
    private BaseEntity configInfo;
    private BaseEntity loginInfo;

    public String getClientId() {
        if(TextUtils.isEmpty(clientId)){
            return "000000";
        }
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public BaseEntity getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(BaseEntity configInfo) {
        this.configInfo = configInfo;
    }

    public BaseEntity getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(BaseEntity loginInfo) {
        this.loginInfo = loginInfo;
    }

    public static BaseApplication getInstance() {
        return application;
    }

    public void releaseInstance() {
        CustomHttpClient.releaseInstance();
        CrashHandler.releaseInstance();
        AnimationUtil.releaseInstance();
        BundleUtil.releaseInstance();
        DeviceUtil.releaseInstance();
        InputUtil.releaseInstance();
        LogUtil.releaseInstance();
        ToastUtil.releaseInstance();
        NetworkUtil.releaseInstance();
        ReflectUtil.releaseInstance();
        SecurityUtil.releaseInstance();
        SharedPreferenceUtil.releaseInstance();
        SnackBarUtil.releaseInstance();
        StrictModeUtil.releaseInstance();
        NetworkUtil.releaseInstance();
        TypefaceUtil.releaseInstance();
        ViewUtil.releaseInstance();
        TTSUtil.getInstance().destroy();
        ApplicationUtil.releaseInstance();
        application = null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onCreate() invoked!!");
        LogUtil.getInstance().print("SHA1:" + SecurityUtil.getInstance().getSha1(this));
        application = this;
        registerActivityLifecycleCallbacks(this);
        SpeechUtility.createUtility(this, Constant.TTS.APP_ID);
        CustomHttpClient.getInstance().initialize(new Configuration.Builder()
                                                          .setParameters(Lists.<Parameter>newArrayList())
                                                          .setHeaders(new Headers.Builder().build())
                                                          .setTimeout(Constant.HttpTask.REQUEST_TIME_OUT_PERIOD)
                                                          .setInterceptors(Lists.<Interceptor>newArrayList())
                                                          .setDebug(true).build());
        StatConfig.setAppKey(this, "A4DY1MVHH29F");
        StatConfig.setInstallChannel("");
        StatisticsDataAPI.instance(this);
        StatService.setContext(this);
        StatConfig.setTLinkStatus(true);
        if (BuildConfig.DEBUG) {
            StatConfig.setDebugEnable(true);
            StatConfig.setEnableSmartReporting(false);
            StatConfig.setAutoExceptionCaught(false);
        } else {
            StatConfig.setDebugEnable(false);
            StatConfig.initNativeCrashReport(this, null);
            StatConfig.setAutoExceptionCaught(true);
            StatConfig.setEnableSmartReporting(true);
//            StatConfig.setStatSendStrategy(StatReportStrategy.BATCH);
            StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
            StatConfig.setSendPeriodMinutes(10);
        }
        StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJavaCrashHandlerStatus(true);
        StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJniNativeCrashStatus(true);
        StatCrashReporter.getStatCrashReporter(getApplicationContext()).addCrashCallback(new StatCrashCallback() {

            @Override
            public void onJniNativeCrash(String tombstoneMsg) {
                LogUtil.getInstance().print("Native crash happened, tombstone message:" + tombstoneMsg);
            }

            @Override
            public void onJavaCrash(Thread thread, Throwable throwable) {
                LogUtil.getInstance().print("Java crash happened, thread: " + thread + ",Throwable:" + throwable.toString());
            }
        });
        StatService.registerActivityLifecycleCallbacks(this);
        MTACrashModule.initMtaCrashModule(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        StatService.onLowMemory(this);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onLowMemory() invoked!!");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onTrimMemory() invoked!!" + level);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtil.getInstance().print(activity.getClass().getSimpleName() + " onActivityCreated() invoked!!");
        LogUtil.getInstance().print(activity.getClass().getSimpleName() + " taskId:" + activity.getTaskId());
        ActivityUtil.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LogUtil.getInstance().print(activity.getClass().getSimpleName() + " onActivityStarted() invoked!!");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        StatService.onResume(activity);
        LogUtil.getInstance().print(activity.getClass().getSimpleName() + " onActivityResumed() invoked!!");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        StatService.onPause(activity);
        LogUtil.getInstance().print(activity.getClass().getSimpleName() + " onActivityPaused() invoked!!");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        StatService.onStop(activity);
        LogUtil.getInstance().print(activity.getClass().getSimpleName() + " onActivityStopped() invoked!!");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LogUtil.getInstance().print(activity.getClass().getSimpleName() + " onActivitySaveInstanceState() invoked!!");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.getInstance().print(activity.getClass().getSimpleName() + " onActivityDestroyed() invoked!!");
        Glide.get(this).clearMemory();
    }
}
