package com.service.customer.ui.fragment.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.R;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.constant.Constant;
import com.service.customer.net.entity.NotificationAnnouncementInfos;
import com.service.customer.net.entity.ServiceInfos;
import com.service.customer.ui.contract.HomePageContract;
import com.service.customer.ui.contract.implement.BasePresenterImplement;

import java.io.IOException;

public class HomePagePresenter extends BasePresenterImplement implements HomePageContract.Presenter {

    private HomePageContract.View view;

    public HomePagePresenter(Context context, HomePageContract.View view) {
        super(context, view);
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public ServiceInfos generateServiceInfos() {
        view.showLoadingPromptDialog(R.string.get_service_infos, Constant.RequestCode.DIALOG_PROGRESS_GET_SERVICE_INFOS);
        try {
            return new ServiceInfos().parse(JSONObject.parseObject(IOUtil.getInstance().readString(context.getAssets().open("ServiceInfos.json"))));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public NotificationAnnouncementInfos generateNotificationAnnouncementInfos() {
        view.showLoadingPromptDialog(R.string.get_notification_announcement_infos, Constant.RequestCode.DIALOG_PROGRESS_GET_NOTIFICATION_ANNOUNCEMENT_INFOS);
        try {
            return new NotificationAnnouncementInfos().parse(JSONObject.parseObject(IOUtil.getInstance().readString(context.getAssets().open("NotificationAnnouncementInfos.json"))));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
