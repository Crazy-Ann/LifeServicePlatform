package com.service.customer.ui.dialog.listener;

public interface OnDownloadListener {

    void onDownloadStart();

    void onDownloadProgress(float progress, long speed);

    void onDownloadFailed();

    void onDownloadSuccess();
}
