package com.service.customer.base.application;

import android.content.Context;

import com.service.customer.components.constant.Constant;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.ActivityUtil;
import com.service.customer.components.utils.ApplicationUtil;
import com.service.customer.components.utils.DateUtil;
import com.service.customer.components.utils.DeviceUtil;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.components.utils.LogUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

public class CrashHandler implements UncaughtExceptionHandler {

    private static CrashHandler crashHandler;
    private UncaughtExceptionHandler uncaughtExceptionHandler;
    private Context context;

    private CrashHandler() {
        // cannot be instantiated
    }

    public static synchronized CrashHandler getInstance() {
        if (crashHandler == null) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    public static void releaseInstance() {
        if (crashHandler != null) {
            crashHandler = null;
        }
    }

    public void initialize(Context ctx) {
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = ctx;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleExcetion(ex) && uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, ex);
        } else {
            ActivityUtil.removeAll();
        }
    }

    private boolean handleExcetion(Throwable ex) {
        if (ex != null) {
            LogUtil.getInstance().print(formatCrashMessage(ex));
            IOUtil.getInstance().writeFile(context, formatCrashMessage(ex), true);
            return true;
        }
        return false;
    }

    private String formatCrashMessage(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        StringBuilder builder = new StringBuilder();
        builder.append(DateUtil.getCurrentTime());
        builder.append(System.getProperty(Regex.LINE_SEPARATOR.getRegext()));
        builder.append(Constant.Device.DEVICE_VERSION).append(Regex.COLON.getRegext()).append(DeviceUtil.getInstance().getSystemVersion());
        builder.append(System.getProperty(Regex.LINE_SEPARATOR.getRegext()));
        builder.append(Constant.Device.DEVICE_VERSION_NAME).append(Regex.COLON.getRegext()).append(DeviceUtil.getInstance().getSystemName());
        builder.append(System.getProperty(Regex.LINE_SEPARATOR.getRegext()));
        builder.append(Constant.Device.DEVICE_ID).append(Regex.COLON.getRegext()).append(DeviceUtil.getInstance().getDeviceId(context));
        builder.append(System.getProperty(Regex.LINE_SEPARATOR.getRegext()));
        builder.append(Constant.Device.DEVICE_NAME).append(Regex.COLON.getRegext()).append(DeviceUtil.getInstance().getDeviceModel());
        builder.append(System.getProperty(Regex.LINE_SEPARATOR.getRegext()));
        builder.append("app_version:").append(ApplicationUtil.getInstance().getVersionName(context));
        builder.append(System.getProperty(Regex.LINE_SEPARATOR.getRegext()));
        builder.append("exception_message:").append(ex.toString());
        builder.append(System.getProperty(Regex.LINE_SEPARATOR.getRegext()));
        builder.append("dump_message:").append(writer.toString());
        builder.append(System.getProperty(Regex.LINE_SEPARATOR.getRegext()));
        printWriter.close();
        return builder.toString();
    }
}
