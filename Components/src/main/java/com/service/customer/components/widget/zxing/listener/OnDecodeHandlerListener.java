package com.service.customer.components.widget.zxing.listener;

import android.content.Intent;
import android.os.Handler;

import com.google.zxing.Result;
import com.service.customer.components.widget.zxing.view.ViewfinderView;


/**
 * 二维码解码处理监听
 */
public interface OnDecodeHandlerListener {

    void drawViewfinder();

    ViewfinderView getViewfinderView();

    Handler getHandler();

    void handleDecode(Result result);

    void returnScanResult(int resultCode, Intent data);

    void launchProductQuary(String url);
}
