package com.service.customer.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.service.customer.R;
import com.service.customer.base.handler.FragmentHandler;
import com.service.customer.base.sticky.adapter.FixedStickyViewAdapter;
import com.service.customer.components.permission.listener.PermissionCallback;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.MessageUtil;
import com.service.customer.components.utils.ThreadPoolUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.components.widget.sticky.decoration.LinearLayoutDividerItemDecoration;
import com.service.customer.constant.Temp;
import com.service.customer.net.entity.NotificationAnnouncementInfo;
import com.service.customer.net.entity.NotificationAnnouncementInfos;
import com.service.customer.net.entity.ServiceInfo;
import com.service.customer.net.entity.ServiceInfos;
import com.service.customer.ui.activity.TaskMapActivity;
import com.service.customer.ui.activity.TaskSubmitActivity;
import com.service.customer.ui.adapter.NotificationAnnouncementAdapter;
import com.service.customer.ui.adapter.ServiceAdapter;
import com.service.customer.ui.binder.NotificationAnnouncementBinder;
import com.service.customer.ui.binder.ServiceBinder;
import com.service.customer.ui.contract.HomePageContract2;
import com.service.customer.ui.contract.implement.FragmentViewImplement;
import com.service.customer.ui.presenter.HomePagePresenter2;

import java.util.List;

public class HomePageFragment2 extends FragmentViewImplement<HomePageContract2.Presenter> implements HomePageContract2.View {

    private HomePagePresenter2 homePagePresenter2;
    private RecyclerView rvService;
    private RecyclerView rvNotificationAnnouncement;
    private ServiceAdapter serviceAdapter;
    private List<ServiceInfo> serviceInfos;
    private NotificationAnnouncementAdapter notificationAnnouncementAdapter;
    private List<NotificationAnnouncementInfo> notificationAnnouncementInfos;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private HomePageHandler homePageHandler;

    private static class HomePageHandler extends FragmentHandler<HomePageFragment2> {

        public HomePageHandler(HomePageFragment2 fragments) {
            super(fragments);
        }

        @Override
        protected void handleMessage(final HomePageFragment2 fragments, Message msg) {
            switch (msg.what) {
                case com.service.customer.constant.Constant.Message.GET_SERVICE_INFOS_SUCCESS:
                    fragments.hideLoadingPromptDialog();
                    fragments.serviceInfos = ((ServiceInfos) msg.obj).getServiceInfos();
                    fragments.serviceAdapter.setData(fragments.serviceInfos);
                    ThreadPoolUtil.execute(new Runnable() {
                        @Override
                        public void run() {
                            NotificationAnnouncementInfos notificationAnnouncementInfos = fragments.homePagePresenter2.generateNotificationAnnouncementInfos();
                            if (notificationAnnouncementInfos != null) {
                                sendMessage(MessageUtil.getMessage(com.service.customer.constant.Constant.Message.GET_NOTIFICATION_ANNOUNCEMENT_INFOS_SUCCESS, notificationAnnouncementInfos));
                            } else {
                                sendMessage(MessageUtil.getMessage(com.service.customer.constant.Constant.Message.GET_NOTIFICATION_ANNOUNCEMENT_INFOS_FAILED));
                            }
                        }
                    });
                    break;
                case com.service.customer.constant.Constant.Message.GET_SERVICE_INFOS_FAILED:
                    fragments.hideLoadingPromptDialog();
                    break;
                case com.service.customer.constant.Constant.Message.GET_NOTIFICATION_ANNOUNCEMENT_INFOS_SUCCESS:
                    fragments.hideLoadingPromptDialog();
                    fragments.notificationAnnouncementInfos = ((NotificationAnnouncementInfos) msg.obj).getNotificationAnnouncementInfos();
                    fragments.notificationAnnouncementAdapter.setData(fragments.notificationAnnouncementInfos);
                    break;
                case com.service.customer.constant.Constant.Message.GET_NOTIFICATION_ANNOUNCEMENT_INFOS_FAILED:
                    fragments.hideLoadingPromptDialog();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_page2, container, false);
        findViewById();
        initialize(savedInstanceState);
        setListener();
        return rootView;
    }

    @Override
    protected void findViewById() {
        inToolbar = ViewUtil.getInstance().findView(rootView, R.id.inToolbar);
        rvService = ViewUtil.getInstance().findView(rootView, R.id.rvService);
        rvNotificationAnnouncement = ViewUtil.getInstance().findView(rootView, R.id.rvNotificationAnnouncement);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_015293, android.R.color.white, false, getString(R.string.home_page), null);
        homePageHandler = new HomePageHandler(this);
        homePagePresenter2 = new HomePagePresenter2(getActivity(), this);
        homePagePresenter2.initialize();
        setBasePresenterImplement(homePagePresenter2);
        getSavedInstanceState(savedInstanceState);

        serviceAdapter = new ServiceAdapter(getActivity(), new ServiceBinder(getActivity(), rvService), true);
        gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        rvService.setLayoutManager(gridLayoutManager);
        rvService.setAdapter(serviceAdapter);

        notificationAnnouncementAdapter = new NotificationAnnouncementAdapter(getActivity(), new NotificationAnnouncementBinder(getActivity(), rvNotificationAnnouncement), true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvNotificationAnnouncement.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvNotificationAnnouncement.setLayoutManager(linearLayoutManager);
        rvNotificationAnnouncement.addItemDecoration(new LinearLayoutDividerItemDecoration(getResources().getColor(R.color.color_e4e4e4), 2, LinearLayoutManager.HORIZONTAL));
        rvNotificationAnnouncement.setAdapter(notificationAnnouncementAdapter);
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                ServiceInfos serviceInfos = homePagePresenter2.generateServiceInfos();
                if (serviceInfos != null) {
                    homePageHandler.sendMessage(MessageUtil.getMessage(com.service.customer.constant.Constant.Message.GET_SERVICE_INFOS_SUCCESS, serviceInfos));
                } else {
                    homePageHandler.sendMessage(MessageUtil.getMessage(com.service.customer.constant.Constant.Message.GET_SERVICE_INFOS_FAILED));
                }
            }
        });
    }

    @Override
    protected void setListener() {
        serviceAdapter.setOnItemClickListener(new FixedStickyViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle;
                switch (serviceInfos.get(position).getAction()) {
                    case com.service.customer.constant.Constant.ServiceAction.EMERGENCY_CALL_FOR_HELP:
                        bundle = new Bundle();
                        bundle.putString(Temp.TITLE.getContent(), serviceInfos.get(position).getName());
                        startActivity(TaskSubmitActivity.class, bundle);
                        break;
                    case com.service.customer.constant.Constant.ServiceAction.APPLIANCE_MAINTENANCE:
                        bundle = new Bundle();
                        bundle.putString(Temp.TITLE.getContent(), serviceInfos.get(position).getName());
                        startActivity(TaskSubmitActivity.class, bundle);
                        break;
                    case com.service.customer.constant.Constant.ServiceAction.LIVING_FACILITIES_MAINTENANCE:
                        break;
                    case com.service.customer.constant.Constant.ServiceAction.OTHER_LIFE_EVENTS:
                        break;
                    case com.service.customer.constant.Constant.ServiceAction.PSYCHOLOGICAL_COUNSELING:
                        break;
                    case com.service.customer.constant.Constant.ServiceAction.DOCTOR_MEDICINE:
                        break;
                    case com.service.customer.constant.Constant.ServiceAction.OTHER:
                        break;
                    case com.service.customer.constant.Constant.ServiceAction.POLICIES_REGULATIONS:
                        break;
                    case com.service.customer.constant.Constant.ServiceAction.QUERY_ANALYSIS:
                        break;
                    case com.service.customer.constant.Constant.ServiceAction.INFORMATION_MANAGEMENT:
                        break;
                    case com.service.customer.constant.Constant.ServiceAction.EVENT_QUERY:
                        break;
                    case com.service.customer.constant.Constant.ServiceAction.MAP_QUERY:
                        bundle = new Bundle();
                        bundle.putString(Temp.TITLE.getContent(), serviceInfos.get(position).getName());
                        startActivity(TaskMapActivity.class, bundle);
                        break;
                    default:
                        break;
                }
            }
        });
        notificationAnnouncementAdapter.setOnItemClickListener(new FixedStickyViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                LogUtil.getInstance().print(notificationAnnouncementInfos.get(position).getUrl());
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case com.service.customer.constant.Constant.RequestCode.NET_WORK_SETTING:
            case com.service.customer.constant.Constant.RequestCode.PREMISSION_SETTING:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    homePagePresenter2.checkPermission(getActivity(), new PermissionCallback() {
                        @Override
                        public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {

                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                            showPermissionPromptDialog();
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {

    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showLocationPromptDialog(String prompt, int requestCode) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }
}
