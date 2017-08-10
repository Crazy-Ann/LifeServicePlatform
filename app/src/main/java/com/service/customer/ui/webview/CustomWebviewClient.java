package com.service.customer.ui.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebView;

import com.yjt.bridge.InjectedWebviewClient;

public class CustomWebviewClient extends InjectedWebviewClient {

    public CustomWebviewClient(Activity activity) {
        super(activity);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }
}
