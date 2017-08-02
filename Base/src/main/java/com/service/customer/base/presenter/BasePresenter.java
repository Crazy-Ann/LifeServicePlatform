package com.service.customer.base.presenter;

import android.content.Context;

public interface BasePresenter {

    void initialize();

    void checkPermission(Context context, String... permissions);
}
