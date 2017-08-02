package com.service.customer.net.entity;

import android.os.Parcel;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.net.model.BaseEntity;


public class ConfigInfo extends BaseEntity {

    private String serverUrl;
    private int clientVersion;
    private String versionName;
    private String downloadUrl;
    private int lowestClientVersion;
    private String updateMessage;
    private String key;

    public ConfigInfo() {
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public int getClientVersion() {
        return clientVersion;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public int getLowestClientVersion() {
        return lowestClientVersion;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public String getKey() {
        return key;
    }

    public ConfigInfo parse(JSONObject object) {
        if (object != null) {
//            this.clientVersion = object.getIntValue(ParameterKey.Version.CLIENT_VER);
//            this.lowestClientVersion = object.getIntValue(ParameterKey.Version.LOWEST_CLIENT_VER);
//            this.versionName = object.getString(ParameterKey.Version.VER_NAME);
//            this.updateMessage = object.getString(ParameterKey.Version.UPDATA_MSG);
//            this.downloadUrl = object.getString(ParameterKey.Version.DOWNLOAD_URL);
//            this.key = object.getString(ParameterKey.Version.KEY);
            return this;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "ConfigInfo{" +
                    "clientVersion:" + clientVersion + '\'' +
                    ", serverUrl:" + serverUrl + '\'' +
                    ", lowestClientVersion:" + lowestClientVersion + '\'' +
                    ", versionName:" + versionName + '\'' +
                    ", updateMessage:" + updateMessage + '\'' +
                    ", downloadUrl:" + downloadUrl + '\'' +
                    ", key:" + key + '\'' +
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
        dest.writeString(this.serverUrl);
        dest.writeInt(this.clientVersion);
        dest.writeString(this.versionName);
        dest.writeString(this.downloadUrl);
        dest.writeInt(this.lowestClientVersion);
        dest.writeString(this.updateMessage);
        dest.writeString(this.key);
    }

    protected ConfigInfo(Parcel in) {
        super(in);
        this.serverUrl = in.readString();
        this.clientVersion = in.readInt();
        this.versionName = in.readString();
        this.downloadUrl = in.readString();
        this.lowestClientVersion = in.readInt();
        this.updateMessage = in.readString();
        this.key = in.readString();
    }

    public static final Creator<ConfigInfo> CREATOR = new Creator<ConfigInfo>() {
        @Override
        public ConfigInfo createFromParcel(Parcel source) {
            return new ConfigInfo(source);
        }

        @Override
        public ConfigInfo[] newArray(int size) {
            return new ConfigInfo[size];
        }
    };
}
