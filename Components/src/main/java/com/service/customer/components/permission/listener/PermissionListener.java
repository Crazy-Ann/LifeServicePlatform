package com.service.customer.components.permission.listener;

import android.support.annotation.NonNull;

public interface PermissionListener {

    void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults);
}
