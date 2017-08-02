package com.service.customer.components.widget.lock.listener.implement;


import com.service.customer.components.constant.Constant;
import com.service.customer.components.widget.lock.listener.OnPointSelectedListener;
import com.service.customer.components.widget.lock.view.NodeDrawable;

public class NoneHighLightMode implements OnPointSelectedListener {

    @Override
    public int setOnPointSelectedListener(NodeDrawable node, int patternIndex, int patternLength, int nodeX, int nodeY, int gridLength) {
//        LogUtil.getInstance().print("NoneHighLightMode:" + nodeX + "," + nodeY);
        return Constant.Lock.STATE_SELECTED;
    }

}
