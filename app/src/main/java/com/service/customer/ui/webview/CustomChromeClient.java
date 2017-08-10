package com.service.customer.ui.webview;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebView;

import com.service.customer.ui.webview.listener.OnReceivedTitleListener;
import com.yjt.bridge.InjectedChromeClient;
import com.yjt.bridge.JsCallJava;

public class CustomChromeClient extends InjectedChromeClient {

    private OnReceivedTitleListener onReceivedTitleListener;

    public CustomChromeClient(JsCallJava JsCallJava) {
        super(JsCallJava);
    }

    public CustomChromeClient(String injectedName, Class injectedCls) {
        super(injectedName, injectedCls);
    }

    public CustomChromeClient(String injectedName, Class injectedCls, OnReceivedTitleListener onReceivedTitleListener) {
        super(injectedName, injectedCls);
        this.onReceivedTitleListener = onReceivedTitleListener;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        onReceivedTitleListener.receivedTitle(view, title);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }
}
