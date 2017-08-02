package com.service.customer.net.listener;

import com.service.customer.base.net.model.BaseEntity;

public interface ApiListener {

    void success(BaseEntity entity);

    void failed(BaseEntity entity, String errorCode, String errorMessage);
}
