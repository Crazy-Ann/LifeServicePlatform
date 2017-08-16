package com.service.customer.net.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.components.widget.sticky.listener.OnGroupListener;

import java.io.Serializable;


public class TaskInfo implements Serializable, Parcelable, OnGroupListener {

    private String realName;
    private String tasNote;
    private double latitude;
    private double longitude;
    private double address;
    private String accountAvatar;
    private String billNo;
    private int status;

    public String getRealName() {
        return realName;
    }

    public String getTasNote() {
        return tasNote;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAddress() {
        return address;
    }

    public String getAccountAvatar() {
        return accountAvatar;
    }

    public String getBillNo() {
        return billNo;
    }

    public int getStatus() {
        return status;
    }

    public TaskInfo() {
    }


    public TaskInfo parse(JSONObject object) {
        if (object != null) {
            this.realName = object.getString(ResponseParameterKey.REAL_NAME);
            this.tasNote = object.getString(ResponseParameterKey.DESCREPTION);
            this.latitude = object.getDoubleValue(ResponseParameterKey.LATITUDE);
            this.longitude = object.getDoubleValue(ResponseParameterKey.LONGITUDE);
            this.address = object.getDoubleValue(ResponseParameterKey.ADDRESS);
            this.accountAvatar = object.getString(ResponseParameterKey.ACCOUNT_AVATAR);
            this.billNo = object.getString(ResponseParameterKey.TITLE);
            this.status = object.getIntValue(ResponseParameterKey.STATUS);
            return this;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "TaskInfo{" +
                    "realName:" + realName + '\'' +
                    ", tasNote:" + tasNote + '\'' +
                    ", latitude:" + latitude + '\'' +
                    ", longitude:" + longitude + '\'' +
                    ", address:" + address + '\'' +
                    ", accountAvatar:" + accountAvatar + '\'' +
                    ", billNo:" + billNo + '\'' +
                    ", status:" + status + '\'' +
                    '}';
        } else {
            return super.toString();
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.realName);
        dest.writeString(this.tasNote);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.address);
        dest.writeString(this.accountAvatar);
        dest.writeString(this.billNo);
        dest.writeInt(this.status);
    }

    protected TaskInfo(Parcel in) {
        this.realName = in.readString();
        this.tasNote = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.address = in.readDouble();
        this.accountAvatar = in.readString();
        this.billNo = in.readString();
        this.status = in.readInt();
    }

    public static final Creator<TaskInfo> CREATOR = new Creator<TaskInfo>() {
        @Override
        public TaskInfo createFromParcel(Parcel source) {return new TaskInfo(source);}

        @Override
        public TaskInfo[] newArray(int size) {return new TaskInfo[size];}
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
