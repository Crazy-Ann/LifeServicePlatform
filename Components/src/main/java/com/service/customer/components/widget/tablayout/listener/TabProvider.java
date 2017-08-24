package com.service.customer.components.widget.tablayout.listener;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public interface TabProvider {

    /**
     * @return Return the View of {@code position} for the Tabs
     */
    View createTabView(ViewGroup container, int position, PagerAdapter adapter);

}
