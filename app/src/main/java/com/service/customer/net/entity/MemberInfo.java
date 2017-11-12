package com.service.customer.net.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.components.constant.Regex;
import com.service.customer.components.widget.sticky.listener.OnGroupListener;

import java.io.Serializable;


public class MemberInfo implements Serializable, Parcelable, OnGroupListener {

    private String accountId;
    private String realName;
    private String phone;
    private String townsname;
    private String accountAvatar;
    private double latitude;
    private double longitude;
    private String address;

    public String getAccountId() {
        return accountId;
    }

    public String getRealName() {
        return realName;
    }

    public String getPhone() {
        return phone;
    }

    public String getTownsname() {
        return townsname;
    }

    public String getAccountAvatar() {
        return accountAvatar;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public MemberInfo() {
    }


    public MemberInfo parse(JSONObject object) {
        if (object != null) {
            this.accountId = object.getString(ResponseParameterKey.ACCOUNT_ID);
            this.realName = object.getString(ResponseParameterKey.REAL_NAME);
            this.phone = object.getString(ResponseParameterKey.PHONE);
            this.townsname = object.getString(ResponseParameterKey.TOWNS_NAME);
            this.accountAvatar = object.getString(ResponseParameterKey.ACCOUNT_AVATAR).replaceAll(Regex.RIGHT_DOUBLE_SLASH.getRegext(), Regex.LEFT_SINGLE_SLASH.getRegext());
            this.latitude = object.getDoubleValue(ResponseParameterKey.LATITUDE);
            this.longitude = object.getDoubleValue(ResponseParameterKey.LONGITUDE);
            this.address = object.getString(ResponseParameterKey.ADDRESS);
            return this;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "TaskInfo{" +
                    "accountId:" + accountId + '\'' +
                    ", realName:" + realName + '\'' +
                    ", phone:" + phone + '\'' +
                    ", townsname:" + townsname + '\'' +
                    ", accountAvatar:" + accountAvatar + '\'' +
                    ", latitude:" + latitude + '\'' +
                    ", longitude:" + longitude + '\'' +
                    ", address:" + address + '\'' +
                    '}';
        } else {
            return super.toString();
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountId);
        dest.writeString(this.realName);
        dest.writeString(this.accountAvatar);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.address);
    }

    protected MemberInfo(Parcel in) {
        this.accountId = in.readString();
        this.realName = in.readString();
        this.accountAvatar = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.address = in.readString();
    }

    public static final Creator<MemberInfo> CREATOR = new Creator<MemberInfo>() {
        @Override
        public MemberInfo createFromParcel(Parcel source) {return new MemberInfo(source);}

        @Override
        public MemberInfo[] newArray(int size) {return new MemberInfo[size];}
    };

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public long getGroupId() {
        return 0;
    }
}
