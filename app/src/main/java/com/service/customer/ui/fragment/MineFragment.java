package com.service.customer.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.service.customer.R;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.constant.net.RequestParameterKey;
import com.service.customer.base.handler.FragmentHandler;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.BitmapUtil;
import com.service.customer.components.utils.GlideUtil;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.components.utils.ImageUtil;
import com.service.customer.components.utils.InputUtil;
import com.service.customer.components.utils.LogUtil;
import com.service.customer.components.utils.MessageUtil;
import com.service.customer.components.utils.ThreadPoolUtil;
import com.service.customer.components.utils.ViewUtil;
import com.service.customer.constant.Constant;
import com.service.customer.constant.Temp;
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.ui.activity.MapActivity;
import com.service.customer.ui.activity.SettingActivity;
import com.service.customer.ui.activity.WapActivity;
import com.service.customer.ui.activity.WorkSubmitActivity;
import com.service.customer.ui.contract.MineContract;
import com.service.customer.ui.contract.implement.FragmentViewImplement;
import com.service.customer.ui.dialog.PromptDialog;
import com.service.customer.ui.presenter.MinePresenter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MineFragment extends FragmentViewImplement<MineContract.Presenter> implements MineContract.View, View.OnClickListener {

    private MinePresenter minePresenter;
    private ImageView ivHeadImage;
    private TextView tvRealName;
    private LinearLayout llVolunteerContent;
    private RelativeLayout rlWorkLog;
    private RelativeLayout rlAddWorkLog;
    private RelativeLayout rlMapEvent;
    private RelativeLayout rlGreetingCard;
    private RelativeLayout rlSetting;
    private Button btnLogout;
    private MineHandler mineHandler;

    @Override
    public void startMainActivity(int tab) {

    }

    private class MineHandler extends FragmentHandler<MineFragment> {

        public MineHandler(MineFragment fragments) {
            super(fragments);
        }

        @Override
        protected void handleMessage(MineFragment fragments, Message msg) {
            switch (msg.what) {
                case Constant.Message.GET_IMAGE_SUCCESS:
                    hideLoadingPromptDialog();
                    minePresenter.saveHeadImage((File) msg.obj);
                    break;
                case Constant.Message.GET_IMAGE_FAILED:
                    hideLoadingPromptDialog();
                    showPromptDialog(R.string.dialog_prompt_get_image_error, Constant.RequestCode.DIALOG_PROMPT_GET_IMAGE_ERROR);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        findViewById();
        initialize(savedInstanceState);
        setListener();
        return rootView;
    }

    @Override
    protected void findViewById() {
        ivHeadImage = ViewUtil.getInstance().findViewAttachOnclick(rootView, R.id.ivHeadImage, this);
        llVolunteerContent = ViewUtil.getInstance().findView(rootView, R.id.llVolunteerContent);
        rlWorkLog = ViewUtil.getInstance().findViewAttachOnclick(rootView, R.id.rlWorkLog, this);
        rlAddWorkLog = ViewUtil.getInstance().findViewAttachOnclick(rootView, R.id.rlAddWorkLog, this);
        rlMapEvent = ViewUtil.getInstance().findViewAttachOnclick(rootView, R.id.rlMapEvent, this);
        rlGreetingCard = ViewUtil.getInstance().findViewAttachOnclick(rootView, R.id.rlGreetingCard, this);
        rlSetting = ViewUtil.getInstance().findViewAttachOnclick(rootView, R.id.rlSetting, this);
        btnLogout = ViewUtil.getInstance().findViewAttachOnclick(rootView, R.id.btnLogout, this);
        tvRealName = ViewUtil.getInstance().findView(rootView, R.id.tvRealName);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        mineHandler = new MineHandler(this);
        minePresenter = new MinePresenter(getActivity(), this);
        minePresenter.initialize();
        setBasePresenterImplement(minePresenter);
        getSavedInstanceState(savedInstanceState);

        setHeadImage(((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getAccountAvatar());
        tvRealName.setText(((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getRealName()
                                   + Regex.LEFT_PARENTHESIS.getRegext()
                                   + ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getAccountId()
                                   + Regex.RIGHT_PARENTHESIS.getRegext());
        if (TextUtils.equals(((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getMemberType(), "1")) {
            ViewUtil.getInstance().setViewGone(rlGreetingCard);
            ViewUtil.getInstance().setViewVisible(llVolunteerContent);
        } else {
            ViewUtil.getInstance().setViewGone(llVolunteerContent);
            ViewUtil.getInstance().setViewVisible(rlGreetingCard);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
        Bundle bundle;
        switch (view.getId()) {
            case R.id.ivHeadImage:
                PromptDialog.createBuilder(getActivity().getSupportFragmentManager())
                        .setTitle(getString(R.string.dialog_prompt))
                        .setPrompt(getString(R.string.select_image_prompt))
                        .setPositiveButtonText(getActivity(), R.string.album)
                        .setNegativeButtonText(getActivity(), R.string.photograph)
                        .setTargetFragment(this, Constant.RequestCode.DIALOG_PROMPT_SELECT_IMAGE)
                        .setCancelableOnTouchOutside(true)
                        .setCancelable(true)
                        .showAllowingStateLoss(getActivity());
                break;
            case R.id.rlWorkLog:
                bundle = new Bundle();
                bundle.putString(Temp.URL.getContent(), ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getWorkUrl());
                startActivity(WapActivity.class, bundle);
                break;
            case R.id.rlAddWorkLog:
                startActivity(WorkSubmitActivity.class);
                break;
            case R.id.rlMapEvent:
                startActivity(MapActivity.class);
                break;
            case R.id.rlGreetingCard:
                bundle = new Bundle();
                bundle.putString(Temp.URL.getContent(), ((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getCardUrl());
                //bundle.putString(Temp.URL.getContent(), Constant.ASSET_URL.GREETING_CARD_LIST);
                startActivity(WapActivity.class, bundle);
                break;
            case R.id.rlSetting:
                startActivity(SettingActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.RequestCode.NET_WORK_SETTING:
            case Constant.RequestCode.PREMISSION_SETTING:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    minePresenter.checkPermission(getActivity(), this);
                }
                break;
            case Constant.RequestCode.REQUEST_CODE_PHOTOGRAPH:
                showLoadingPromptDialog(R.string.get_image_prompt, Constant.RequestCode.DIALOG_PROGRESS_GET_IMAGE);
                ThreadPoolUtil.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File file = IOUtil.getInstance().getExternalFilesDir(getActivity(), Constant.FILE_NAME, RequestParameterKey.SAVE_HEAD_IMAGE + Regex.IMAGE_JPG.getRegext());
                            if (file != null && BitmapUtil.getInstance().saveBitmap(ImageUtil.getNarrowBitmap(getActivity(), IOUtil.getInstance().getFileUri(getActivity(), true, file), 0.5f), file.getAbsolutePath())) {
                                mineHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_SUCCESS, file));
                            } else {
                                mineHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                            }
                        } catch (IOException | InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            mineHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                        }
                    }
                });
                break;
            case Constant.RequestCode.REQUEST_CODE_ALBUM:
                if (data != null) {
                    showLoadingPromptDialog(R.string.get_image_prompt, Constant.RequestCode.DIALOG_PROGRESS_GET_IMAGE);
                    ThreadPoolUtil.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                File file = IOUtil.getInstance().getExternalFilesDir(getActivity(), Constant.FILE_NAME, RequestParameterKey.SAVE_HEAD_IMAGE + Regex.IMAGE_JPG.getRegext());
                                Bitmap photo = ImageUtil.getNarrowBitmap(getActivity(), data.getData(), 0.5f);
                                if (file != null && BitmapUtil.getInstance().saveBitmap(photo, file.getAbsolutePath())) {
                                    mineHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_SUCCESS, file));
                                } else {
                                    mineHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                                }
                            } catch (InterruptedException | ExecutionException | IOException e) {
                                e.printStackTrace();
                                mineHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        minePresenter.deleteFile();
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_PROMPT_SELECT_IMAGE:
                LogUtil.getInstance().print("onPositiveButtonClicked_DIALOG_PROMPT_SELECT_IMAGE");
                startActivityForResult(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Regex.IMAGE_PATH.getRegext(), Constant.RequestCode.REQUEST_CODE_ALBUM, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_PROMPT_SELECT_IMAGE:
                LogUtil.getInstance().print("onNegativeButtonClicked_DIALOG_PROMPT_SELECT_IMAGE");
                try {
                    HashMap<String, Parcelable> map = new HashMap<>();
                    map.put(MediaStore.EXTRA_OUTPUT, IOUtil.getInstance().getFileUri(getActivity(), true, IOUtil.getInstance().getExternalFilesDir(getActivity(), Constant.FILE_NAME, Regex.LEFT_SLASH.getRegext() + RequestParameterKey.SAVE_HEAD_IMAGE + Regex.IMAGE_JPG.getRegext()).getAbsolutePath()));
                    startActivityForResultWithParcelable(MediaStore.ACTION_IMAGE_CAPTURE, Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Intent.FLAG_GRANT_READ_URI_PERMISSION : com.service.customer.components.constant.Constant.FileProvider.DEFAULT_FLAG, map, Constant.RequestCode.REQUEST_CODE_PHOTOGRAPH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(int requestCode, @NonNull List<String> grantPermissions) {

    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setHeadImage(String url) {
        LogUtil.getInstance().print("headimage url:" + url);
        GlideUtil.getInstance().with(getActivity(), url, null, getResources().getDrawable(R.mipmap.ic_launcher_round), DiskCacheStrategy.NONE, ivHeadImage);
    }
}
