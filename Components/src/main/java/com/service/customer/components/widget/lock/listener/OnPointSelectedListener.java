package com.service.customer.components.widget.lock.listener;


import com.service.customer.components.widget.lock.view.NodeDrawable;

/**
 * 手势密码point选择监听
 *
 * @author yjt
 */
public interface OnPointSelectedListener {

    int setOnPointSelectedListener(NodeDrawable node, int patternIndex,
                                   int patternLength, int nodeX, int nodeY, int gridLength);
}
    