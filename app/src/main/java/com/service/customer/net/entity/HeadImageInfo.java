package com.service.customer.net.entity;

import android.os.Parcel;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.base.net.model.BaseEntity;


public class HeadImageInfo extends BaseEntity {

    private String accountavatar;

    public HeadImageInfo() {
    }

    public String getAccountavatar() {
        return accountavatar;
    }

    public HeadImageInfo parse(JSONObject object) {
        if (object != null) {
            this.accountavatar = object.getString(ResponseParameterKey.ACCOUNT_AVATAR);
            return this;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "HeadImageInfo{" +
                    "accountavatar:" + accountavatar + '\'' +
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
        dest.writeString(this.accountavatar);
    }

    protected HeadImageInfo(Parcel in) {
        super(in);
        this.accountavatar = in.readString();
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
