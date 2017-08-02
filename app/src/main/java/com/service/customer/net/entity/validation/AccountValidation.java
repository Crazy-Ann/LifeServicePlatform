package com.service.customer.net.entity.validation;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.service.customer.R;
import com.service.customer.components.utils.ToastUtil;
import com.service.customer.components.validation.ValidationExecutor;


public class AccountValidation extends ValidationExecutor {

    @Override
    public boolean doValidate(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            ToastUtil.getInstance().showToast(context, context.getString(R.string.account_prompt), Toast.LENGTH_SHORT);
            return false;
        } else {
            return true;
        }
    }
}
