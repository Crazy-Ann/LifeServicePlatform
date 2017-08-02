package com.service.customer.components.permission.listener;

import android.support.annotation.NonNull;

public interface RationaleRequest extends Request<RationaleRequest> {

    @NonNull
    RationaleRequest rationale(RationaleListener listener);

}
