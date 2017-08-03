package com.service.customer.ui.dialog;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;

import com.service.customer.R;
import com.service.customer.base.constant.Temp;
import com.service.customer.base.dialog.BaseDialogFragment;
import com.service.customer.base.dialog.listener.OnDialogNegativeListener;
import com.service.customer.base.dialog.listener.OnDialogPositiveListener;
import com.service.customer.components.utils.BundleUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.components.widget.progressbar.DownloadProgressBar;
import com.service.customer.components.widget.progressbar.listener.OnProgressUpdateListener;
import com.service.customer.net.task.DownloadTask;
import com.service.customer.ui.dialog.builder.DownloadDialogBuilder;
import com.service.customer.ui.dialog.listener.OnDialogInstallListner;
import com.service.customer.ui.dialog.listener.OnDownloadListener;

import java.io.File;

public class DownloadDialog extends BaseDialogFragment implements OnDownloadListener, OnProgressUpdateListener, View.OnClickListener {

    private String url;
    private File derectory;
    private CharSequence positive;
    private CharSequence negative;

    private DownloadTask downloadTask;
    private DownloadProgressBar dpbProgress;

    private boolean isSuccess;
    private OnDialogInstallListner onDialogInstallListner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogInstallListner) {
            onDialogInstallListner = (OnDialogInstallListner) context;
        }
    }

    @Override
    protected Builder build(Builder builder) {
        CharSequence title = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_TITLE.getContent());
        CharSequence prompt = BundleUtil.getInstance().getStringData(getArguments(), Temp.DIALOG_PROMPT.getContent());
        url = BundleUtil.getInstance().getStringData(getArguments(), Temp.DIALOG_DOWNLOAD_URL.getContent());
        derectory = BundleUtil.getInstance().getSerializableData(getArguments(), Temp.DIALOG_DOWNLOAD_FILE.getContent());
        positive = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_POSITIVE.getContent());
        negative = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_NEGATIVE.getContent());
        View view = builder.getLayoutInflater().inflate(R.layout.dialog_download, null);
        dpbProgress = ViewUtil.getInstance().findView(view, R.id.dpbProgress);
        dpbProgress.setOnClickListener(this);
        builder.setView(view);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(prompt)) {
            builder.setMessage(prompt);
        }
        if (!TextUtils.isEmpty(url) && derectory != null) {
            downloadTask = new DownloadTask(url, derectory, this);
            downloadTask.execute();
        } else {
            onDownloadFailed();
        }
        if (!TextUtils.isEmpty(positive)) {
            builder.setPositiveButton(positive, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (OnDialogPositiveListener listener : getDialogListeners(OnDialogPositiveListener.class)) {
                        listener.onPositiveButtonClicked(requestCode);
                    }
                }
            });
        }
        if (!TextUtils.isEmpty(negative)) {
            builder.setNegativeButton(negative, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (OnDialogNegativeListener listener : getDialogListeners(OnDialogNegativeListener.class)) {
                        listener.onNegativeButtonClicked(requestCode);
                    }
                    if (downloadTask != null) {
                        downloadTask.cancel(true);
                        dpbProgress.abortDownload();
                    }
                    dismiss();
                }
            });
        }
        return builder;
    }

    public static DownloadDialogBuilder createBuilder(FragmentManager fragmentManager) {
        return new DownloadDialogBuilder(fragmentManager, DownloadDialog.class);
    }

    @Override
    public void onDownloadStart() {
        LogUtil.getInstance().print("onDownloadStart");
        dpbProgress.onManualProgressAnimation();
        dpbProgress.setOnProgressUpdateListener(this);
        dpbProgress.setEnabled(false);
        isSuccess = false;
    }

    @Override
    public void onDownloadProgress(float progress, long speed) {
        LogUtil.getInstance().print("onDownloadProgress");
        dpbProgress.setProgress(progress);
        dpbProgress.setEnabled(false);
        isSuccess = false;
    }

    @Override
    public void onDownloadFailed() {
        LogUtil.getInstance().print("onDownloadFailed");
        dpbProgress.setEnabled(true);
        dpbProgress.onFailed();
        isSuccess = false;
    }

    @Override
    public void onDownloadSuccess() {
        LogUtil.getInstance().print("onDownloadSuccess");
        dpbProgress.setEnabled(false);
        dpbProgress.onSuccess();
        isSuccess = true;
    }

    @Override
    public void onProgressUpdate(float degree) {
        LogUtil.getInstance().print(degree);
    }

    @Override
    public void onAnimationStarted() {
        LogUtil.getInstance().print("onAnimationStarted");
        dpbProgress.setEnabled(false);
    }

    @Override
    public void onAnimationEnded() {
        LogUtil.getInstance().print("onAnimationEnded");
        dpbProgress.setEnabled(false);
        if (isSuccess) {
            dismissAllowingStateLoss();
            onDialogInstallListner.onDialogInstall(derectory.getAbsolutePath());
        }
    }

    @Override
    public void onAnimationSuccess() {
        LogUtil.getInstance().print("onAnimationSuccess");
        dpbProgress.setEnabled(false);
    }

    @Override
    public void onAnimationFailed() {
        LogUtil.getInstance().print("onAnimationFailed");
        dpbProgress.setEnabled(true);
    }

    @Override
    public void onManualProgressStarted() {
        LogUtil.getInstance().print("onManualProgressStarted");
        dpbProgress.setEnabled(false);
    }

    @Override
    public void onManualProgressEnded() {
        LogUtil.getInstance().print("onManualProgressEnded");
        dpbProgress.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dpbProgress:
                if (TextUtils.isEmpty(url) && derectory != null) {
                    if (downloadTask == null) {
                        downloadTask = new DownloadTask(url, derectory, this);
                    }
                    LogUtil.getInstance().print(downloadTask.getStatus());
                    if (downloadTask.getStatus() == AsyncTask.Status.PENDING) {
                        downloadTask.execute();
                    } else {
                        dpbProgress.setEnabled(false);
                    }
                } else {
                    onDownloadFailed();
                }
                break;
            default:
                break;
        }
    }
}
