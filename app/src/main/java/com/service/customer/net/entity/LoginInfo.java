package com.service.customer.net.entity;

import android.os.Parcel;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.application.BaseApplication;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.utils.IOUtil;
import com.service.customer.constant.Constant;

import java.io.IOException;

public final class LoginInfo extends BaseEntity {

    private String accountId;
    private String memberType;
    private String accountAvatar;
    private String phone;
    private String idCard;
    private String realName;
    private String token;
    private String indexUrl;
    private String taskUrl;
    private String cardUrl;
    private String workUrl;
    private String helperUrl;
    private String helpSeekerUrl;

    public LoginInfo() {}

    public String getAccountId() {
        return accountId;
    }

    public String getMemberType() {
        return memberType;
    }

    public String getAccountAvatar() {
        return accountAvatar;
    }

    public void setAccountAvatar(String accountAvatar) {
        this.accountAvatar = accountAvatar;
    }

    public String getPhone() {
        return phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getRealName() {
        return realName;
    }

    public String getToken() {
        return token;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public String getCardUrl() {
        return cardUrl;
    }

    public String getWorkUrl() {
        return workUrl;
    }

    public String getHelperUrl() {
        return helperUrl;
    }

    public String getHelpSeekerUrl() {
        return helpSeekerUrl;
    }

    public LoginInfo parse(JSONObject object) {
        try {
            if (object != null) {
                this.indexUrl = object.getString(ResponseParameterKey.INDEX_URL);
                this.taskUrl = object.getString(ResponseParameterKey.TASK_URL);
                this.cardUrl = object.getString(ResponseParameterKey.CARD_URL);
                this.workUrl = object.getString(ResponseParameterKey.WORK_URL);
                this.helperUrl = object.getString(ResponseParameterKey.ASIST_URL);
                this.helpSeekerUrl = object.getString(ResponseParameterKey.ACCOUNT_URL);
                this.token = object.getString(ResponseParameterKey.TOKEN);
                if (object.containsKey(ResponseParameterKey.USER_INFO)) {
                    JSONObject userInfo = object.getJSONObject(ResponseParameterKey.USER_INFO);
                    this.accountId = userInfo.getString(ResponseParameterKey.ACCOUNT_ID);
                    this.memberType = userInfo.getString(ResponseParameterKey.MEMBER_TYPE);
                    this.accountAvatar = userInfo.getString(ResponseParameterKey.ACCOUNT_AVATAR);
                    this.phone = userInfo.getString(ResponseParameterKey.PHONE);
                    this.idCard = userInfo.getString(ResponseParameterKey.ID_CARD);
                    this.realName = userInfo.getString(ResponseParameterKey.REAL_NAME);
                    String directory = BaseApplication.getInstance().getCacheDir().getAbsolutePath() + Constant.Cache.LOGIN_INFO_CACHE_PATH;
                    delete(directory);
                    IOUtil.getInstance().forceMkdir(directory);
                    write(this, directory + Regex.LEFT_SLASH.getRegext() + getClass().getSimpleName());
                }
                return this;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "LoginInfo{" +
                    "accountId='" + accountId + '\'' +
                    "memberType='" + memberType + '\'' +
                    ", accountAvatar='" + accountAvatar + '\'' +
                    ", phone='" + phone + '\'' +
                    ", idCard='" + idCard + '\'' +
                    ", realName='" + realName + '\'' +
                    ", indexUrl='" + indexUrl + '\'' +
                    ", taskUrl='" + taskUrl + '\'' +
                    ", cardUrl='" + cardUrl + '\'' +
                    ", workUrl='" + workUrl + '\'' +
                    ", helperUrl='" + helperUrl + '\'' +
                    ", helpSeekerUrl='" + helpSeekerUrl + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        } else {
            return super.toString();
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.accountId);
        dest.writeString(this.memberType);
        dest.writeString(this.accountAvatar);
        dest.writeString(this.phone);
        dest.writeString(this.idCard);
        dest.writeString(this.realName);
        dest.writeString(this.token);
        dest.writeString(this.indexUrl);
        dest.writeString(this.taskUrl);
        dest.writeString(this.cardUrl);
        dest.writeString(this.workUrl);
        dest.writeString(this.helperUrl);
        dest.writeString(this.helpSeekerUrl);
    }

    protected LoginInfo(Parcel in) {
        super(in);
        this.accountId = in.readString();
        this.memberType = in.readString();
        this.accountAvatar = in.readString();
        this.phone = in.readString();
        this.idCard = in.readString();
        this.realName = in.readString();
        this.token = in.readString();
        this.indexUrl = in.readString();
        this.taskUrl = in.readString();
        this.cardUrl = in.readString();
        this.workUrl = in.readString();
        this.helperUrl = in.readString();
        this.helpSeekerUrl = in.readString();
    }

    public static final Creator<LoginInfo> CREATOR = new Creator<LoginInfo>() {
        @Override
        public LoginInfo createFromParcel(Parcel source) {return new LoginInfo(source);}

        @Override
        public LoginInfo[] newArray(int size) {return new LoginInfo[size];}
    };
}
