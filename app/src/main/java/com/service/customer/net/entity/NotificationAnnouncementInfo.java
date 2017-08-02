package com.service.customer.net.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.components.widget.sticky.listener.OnGroupListener;

import java.io.Serializable;

public final class NotificationAnnouncementInfo implements Serializable, Parcelable, OnGroupListener {

    private String title;
    private String descreption;
    private String url;

    public NotificationAnnouncementInfo() {}

    public String getTitle() {
        return title;
    }

    public String getDescreption() {
        return descreption;
    }

    public String getUrl() {
        return url;
    }

    public NotificationAnnouncementInfo parse(JSONObject object) {
        if (object != null) {
            this.title = object.getString(ResponseParameterKey.TITLE);
            this.descreption = object.getString(ResponseParameterKey.DESCREPTION);
            this.url = object.getString(ResponseParameterKey.URL);
            return this;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "ServiceInfo{" +
                    "title='" + title + '\'' +
                    "descreption='" + descreption + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        } else {
            return super.toString();
        }
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public long getGroupId() {
        return 0;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.descreption);
        dest.writeString(this.url);
    }

    protected NotificationAnnouncementInfo(Parcel in) {
        this.title = in.readString();
        this.descreption = in.readString();
        this.url = in.readString();
    }

    public static final Creator<NotificationAnnouncementInfo> CREATOR = new Creator<NotificationAnnouncementInfo>() {
        @Override
        public NotificationAnnouncementInfo createFromParcel(Parcel source) {return new NotificationAnnouncementInfo(source);}

        @Override
        public NotificationAnnouncementInfo[] newArray(int size) {return new NotificationAnnouncementInfo[size];}
    };
}
