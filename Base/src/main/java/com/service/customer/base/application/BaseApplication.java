package com.service.customer.base.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.google.common.collect.Lists;
import com.iflytek.cloud.SpeechUtility;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.constant.Constant;
import com.service.customer.components.http.Configuration;
import com.service.customer.components.http.CustomHttpClient;
import com.service.customer.components.http.model.Parameter;
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
import com.service.customer.components.tts.TTSUtil;
import com.service.customer.components.utils.ToastUtil;
import com.service.customer.components.utils.TypefaceUtil;
import com.service.customer.components.utils.ViewUtil;

import okhttp3.Headers;
import okhttp3.Interceptor;

public class BaseApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private static BaseApplication application;
    private BaseEntity configInfo;
    private BaseEntity loginInfo;

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
        TTSUtil.getInstance(null).destroy();
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
//        StatService.setContext(this);
//        if (BuildConfig.DEBUG) {
//            // 查看MTA日志及上报数据内容
//            StatConfig.setDebugEnable(true);
//            // 禁用MTA对app未处理异常的捕获，方便开发者调试时，及时获知详细错误信息。
//            StatConfig.setAutoExceptionCaught(false);
//        } else { // 发布时，建议设置的开关状态，请确保以下开关是否设置合理
//            // 禁止MTA打印日志
//            StatConfig.setDebugEnable(false);
//            // 根据情况，决定是否开启MTA对app未处理异常的捕获
//            StatConfig.setAutoExceptionCaught(true);
//            // 选择默认的上报策略
//            StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
//            // 10分钟上报一次的周期
//            StatConfig.setSendPeriodMinutes(10);
//        }
//        // 注册Activity生命周期监控，自动统计时长
//        StatService.registerActivityLifecycleCallbacks(this);
//        // 初始化MTA的Crash模块，可监控java、native的Crash，以及Crash后的回调
//        MTACrashModule.initMtaCrashModule(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
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
        LogUtil.getInstance().print(activity.getClass().getSimpleName() + " onActivityResumed() invoked!!");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LogUtil.getInstance().print(activity.getClass().getSimpleName() + " onActivityPaused() invoked!!");
    }

    @Override
    public void onActivityStopped(Activity activity) {
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
        ActivityUtil.remove(activity);
    }
}
