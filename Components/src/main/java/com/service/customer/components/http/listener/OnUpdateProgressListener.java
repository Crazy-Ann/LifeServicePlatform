package com.service.customer.components.http.listener;

public interface OnUpdateProgressListener {

    void updateProgress(int progress, long speed, boolean isDone);
}
