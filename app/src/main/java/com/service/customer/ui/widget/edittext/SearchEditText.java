package com.service.customer.ui.widget.edittext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.service.customer.R;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.ViewUtil;

public class SearchEditText extends RelativeLayout implements View.OnClickListener {

    private EditText etLocation;
    private ImageButton ibLocationEmpty;
    private ImageView ivSearch;

    public EditText getEtLocation() {
        return etLocation;
    }

    public ImageButton getIbLocationEmpty() {
        return ibLocationEmpty;
    }

    public SearchEditText(Context context) {
        this(context, null);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_search_edittext, this, true);
        etLocation = ViewUtil.getInstance().findView(view, R.id.etLocation);
        ibLocationEmpty = ViewUtil.getInstance().findViewAttachOnclick(view, R.id.ibLocationEmpty, this);
        ivSearch = ViewUtil.getInstance().findView(view, R.id.ivSearch);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public String getText() {
        return etLocation.getText().toString().trim();
    }

    public void setText(String condition) {
        etLocation.setText(condition);
    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
        if (view.getId() == R.id.ibLocationEmpty) {
            etLocation.setText(null);
        }
    }
}
