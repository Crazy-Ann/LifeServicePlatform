package com.service.customer.ui.widget.edittext;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.service.customer.R;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.ui.widget.edittext.listener.OnVoiceClickListener;

import java.lang.reflect.Field;


public class VoiceEdittext extends LinearLayout implements View.OnClickListener, TextWatcher {

    private EditText etContent;
    private Button btnVoice;
    private TextView tvTextCount;
    private OnVoiceClickListener onVoiceClickListener;

    private StringBuilder stringBuilder;

    public VoiceEdittext(Context context) {
        this(context, null);
    }

    public VoiceEdittext(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_voice_edittext, this, true);
        etContent = ViewUtil.getInstance().findView(view, R.id.etContent);
        btnVoice = ViewUtil.getInstance().findViewAttachOnclick(view, R.id.btnVoice, this);
        tvTextCount = ViewUtil.getInstance().findView(view, R.id.tvTextCount);
        etContent.addTextChangedListener(this);
        stringBuilder = new StringBuilder();
    }

    public VoiceEdittext(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnVoiceClickListener(OnVoiceClickListener onVoiceClickListener) {
        this.onVoiceClickListener = onVoiceClickListener;
    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
        if (view.getId() == R.id.btnVoice) {
            onVoiceClickListener.onVoiceClick();
        }
    }

    public EditText getEtContent() {
        return etContent;
    }

    public void setText(String content) {
        if (TextUtils.isEmpty(etContent.getText())) {
            etContent.setText(content);
            stringBuilder.append(content);
        } else {
            stringBuilder.append(etContent.getText().toString().trim()).append(content);
            etContent.setText(stringBuilder.toString());
        }
        etContent.setSelection(stringBuilder.length());
        stringBuilder.delete(0, stringBuilder.length());
    }

    public String getText() {
        return etContent.getText().toString().trim();
    }

    public void setHint(String content) {
        etContent.setHint(content);
    }

    public void setMaxLength(int maxLength) {
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public int getMaxLength() {
        try {
            InputFilter[] inputFilters = etContent.getFilters();
            for (InputFilter filter : inputFilters) {
                Class<?> clazz = filter.getClass();
                if (clazz.getName().equals("android.text.InputFilter$LengthFilter")) {
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        if (field.getName().equals("mMax")) {
                            field.setAccessible(true);
                            return (Integer) field.get(filter);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public void setTextCount(int length) {
        tvTextCount.setText(String.format("%s/%s", length, getMaxLength()));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        etContent.removeTextChangedListener(this);
        setTextCount(editable.length());
        etContent.addTextChangedListener(this);
    }
}
