package com.service.customer.net.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.components.widget.sticky.listener.OnGroupListener;

import java.io.Serializable;

public final class ServiceInfo implements Serializable, Parcelable, OnGroupListener {

    private String name;
    private String action;
    private int index;
    private String iconUrl;

    public ServiceInfo() {}

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    public int getIndex() {
        return index;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public ServiceInfo parse(JSONObject object) {
        if (object != null) {
            this.name = object.getString(ResponseParameterKey.NAME);
            this.action = object.getString(ResponseParameterKey.ACTION);
            this.index = object.getIntValue(ResponseParameterKey.INDEX);
            this.iconUrl = object.getString(ResponseParameterKey.ICON_URL);
            return this;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "ServiceInfo{" +
                    "name='" + name + '\'' +
                    "action='" + action + '\'' +
                    "index='" + index + '\'' +
                    ", iconUrl='" + iconUrl + '\'' +
                    '}';
        } else {
            return super.toString();
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.action);
        dest.writeInt(this.index);
        dest.writeString(this.iconUrl);
    }

    protected ServiceInfo(Parcel in) {
        this.name = in.readString();
        this.action = in.readString();
        this.index = in.readInt();
        this.iconUrl = in.readString();
    }

    public static final Creator<ServiceInfo> CREATOR = new Creator<ServiceInfo>() {
        @Override
        public ServiceInfo createFromParcel(Parcel source) {return new ServiceInfo(source);}

        @Override
        public ServiceInfo[] newArray(int size) {return new ServiceInfo[size];}
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
