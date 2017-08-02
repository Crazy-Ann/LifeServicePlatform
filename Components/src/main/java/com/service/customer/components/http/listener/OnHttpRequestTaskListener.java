package com.service.customer.components.http.listener;

import android.content.Context;

public interface OnHttpRequestTaskListener {

    String getHttpTaskKey();

    Context getHttpTasContext();
}
