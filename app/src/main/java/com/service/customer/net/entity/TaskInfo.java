package com.service.customer.net.entity;

import android.os.Parcel;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.base.net.model.BaseEntity;


public class TaskInfo extends BaseEntity {

    private String accountAvatar;
    private String realName;
    private String title;
    private String descreption;
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAccountAvatar() {
        return accountAvatar;
    }

    public String getRealName() {
        return realName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescreption() {
        return descreption;
    }

    public TaskInfo() {
    }


    public TaskInfo parse(JSONObject object) {
        if (object != null) {
            this.accountAvatar = object.getString(ResponseParameterKey.ACCOUNT_AVATAR);
            this.realName = object.getString(ResponseParameterKey.REAL_NAME);
            this.title = object.getString(ResponseParameterKey.TITLE);
            this.descreption = object.getString(ResponseParameterKey.DESCREPTION);
            this.latitude = object.getDouble(ResponseParameterKey.LATITUDE);
            this.longitude = object.getDouble(ResponseParameterKey.LONGITUDE);
            return this;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "TaskInfo{" +
                    "accountAvatar:" + accountAvatar + '\'' +
                    ", realName:" + realName + '\'' +
                    ", title:" + title + '\'' +
                    ", descreption:" + descreption + '\'' +
                    ", latitude:" + latitude + '\'' +
                    ", longitude:" + longitude + '\'' +
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
        dest.writeString(this.realName);
        dest.writeString(this.title);
        dest.writeString(this.descreption);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected TaskInfo(Parcel in) {
        super(in);
        this.accountAvatar = in.readString();
        this.realName = in.readString();
        this.title = in.readString();
        this.descreption = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<TaskInfo> CREATOR = new Creator<TaskInfo>() {
        @Override
        public TaskInfo createFromParcel(Parcel source) {
            return new TaskInfo(source);
        }

        @Override
        public TaskInfo[] newArray(int size) {
            return new TaskInfo[size];
        }
    };
}
