package com.service.customer.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.service.customer.net.entity.LoginInfo;
import com.service.customer.ui.activity.LoginActivity;
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
    private TextView tvPhone;
    private TextView tvIdCard;
    private Button btnLogout;
    private MineHandler mineHandler;

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
        inToolbar = ViewUtil.getInstance().findView(rootView, R.id.inToolbar);
        ivHeadImage = ViewUtil.getInstance().findViewAttachOnclick(rootView, R.id.ivHeadImage, this);
        btnLogout = ViewUtil.getInstance().findViewAttachOnclick(rootView, R.id.btnLogout, this);
        tvRealName = ViewUtil.getInstance().findView(rootView, R.id.tvRealName);
        tvPhone = ViewUtil.getInstance().findView(rootView, R.id.tvPhone);
        tvIdCard = ViewUtil.getInstance().findView(rootView, R.id.tvIdCard);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initializeToolbar(R.color.color_1f90f0, android.R.color.white, false, getString(R.string.mine), null);
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
        tvPhone.setText(((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getPhone());
        tvIdCard.setText(((LoginInfo) BaseApplication.getInstance().getLoginInfo()).getIdCard());
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {
        if (InputUtil.getInstance().isDoubleClick()) {
            return;
        }
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
            case R.id.btnLogout:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    minePresenter.checkPermission(getActivity());
                } else {
                    minePresenter.logout();
                }
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
                    minePresenter.checkPermission(getActivity());
                }
                break;
            case Constant.RequestCode.REQUEST_CODE_PHOTOGRAPH:
                ThreadPoolUtil.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File file = IOUtil.getInstance().getExternalStoragePublicDirectory(BaseApplication.getInstance(), Constant.FILE_NAME, Regex.LEFT_SLASH.getRegext() + RequestParameterKey.SAVE_HEAD_IMAGE + Regex.IMAGE_JPG.getRegext());
                            if (file != null) {
                                mineHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_SUCCESS, file));
                            } else {
                                mineHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            mineHandler.sendMessage(MessageUtil.getMessage(Constant.Message.GET_IMAGE_FAILED));
                        }
                    }
                });
                break;
            case Constant.RequestCode.REQUEST_CODE_ALBUM:
                if (data != null) {
                    final Uri uri = data.getData();
                    showLoadingPromptDialog(R.string.get_image_prompt, Constant.RequestCode.DIALOG_PROGRESS_GET_IMAGE);
                    ThreadPoolUtil.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                File file = IOUtil.getInstance().getExternalStoragePublicDirectory(BaseApplication.getInstance(), Constant.FILE_NAME, Regex.LEFT_SLASH.getRegext() + RequestParameterKey.SAVE_HEAD_IMAGE + Regex.IMAGE_JPG.getRegext());
                                Bitmap photo = ImageUtil.getNarrowBitmap(BaseApplication.getInstance(), uri, 0.5f);
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
                LogUtil.getInstance().print("onNeutralButtonClicked_DIALOG_PROMPT_SELECT_IMAGE");
                try {
                    HashMap<String, Parcelable> map = new HashMap<>();
                    File file = IOUtil.getInstance().getExternalStoragePublicDirectory(BaseApplication.getInstance(), Constant.FILE_NAME, Regex.LEFT_SLASH.getRegext() + RequestParameterKey.SAVE_HEAD_IMAGE + Regex.IMAGE_JPG.getRegext());
                    map.put(MediaStore.EXTRA_OUTPUT, Uri.parse(file.getAbsolutePath()));
                    startActivityForResultWithParcelable(MediaStore.ACTION_IMAGE_CAPTURE, map, Constant.RequestCode.REQUEST_CODE_PHOTOGRAPH);
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
        GlideUtil.getInstance().with(BaseApplication.getInstance(), url, null, null, DiskCacheStrategy.NONE, ivHeadImage);
    }

    @Override
    public void startLoginActivity() {
        startActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        onFinish("startLoginActivity");
    }
}
