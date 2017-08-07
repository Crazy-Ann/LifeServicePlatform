package com.service.customer.net.entity;

import android.os.Parcel;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.base.net.model.BaseEntity;


public class HeadImageInfo extends BaseEntity {

    private String accountAvatar;

    public HeadImageInfo() {
    }

    public String getAccountAvatar() {
        return accountAvatar;
    }

    public HeadImageInfo parse(JSONObject object) {
        if (object != null) {
            this.accountAvatar = object.getString(ResponseParameterKey.ACCOUNT_AVATAR);
            return this;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "HeadImageInfo{" +
                    "accountAvatar:" + accountAvatar + '\'' +
                    '}';
        } else {
            return super.toString();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.accountAvatar);
    }

    protected HeadImageInfo(Parcel in) {
        super(in);
        this.accountAvatar = in.readString();
    }

    public static final Creator<HeadImageInfo> CREATOR = new Creator<HeadImageInfo>() {
        @Override
        public HeadImageInfo createFromParcel(Parcel source) {
            return new HeadImageInfo(source);
        }

        @Override
        public HeadImageInfo[] newArray(int size) {
            return new HeadImageInfo[size];
        }
    };
}
