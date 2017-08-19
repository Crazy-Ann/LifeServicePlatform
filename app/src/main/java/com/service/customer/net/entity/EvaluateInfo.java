package com.service.customer.net.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.components.widget.sticky.listener.OnGroupListener;


public class EvaluateInfo implements Parcelable, OnGroupListener {

    private String realName;
    private String accountAvatar;
    private String billNo;

    public String getRealName() {
        return realName;
    }

    public String getAccountAvatar() {
        return accountAvatar;
    }

    public String getBillNo() {
        return billNo;
    }

    public EvaluateInfo() {
    }


    public EvaluateInfo parse(JSONObject object) {
        if (object != null) {
           this.realName = object.getString(ResponseParameterKey.REAL_NAME);
           this.accountAvatar = object.getString(ResponseParameterKey.ACCOUNT_AVATAR);
           this.billNo = object.getString(ResponseParameterKey.BILL_NO);
            return this;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "EvaluateInfo{" +
                    "realName:" + realName + '\'' +
                    ", accountAvatar:" + accountAvatar + '\'' +
                    ", billNo:" + billNo + '\'' +
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
        dest.writeString(this.accountAvatar);
        dest.writeString(this.billNo);
    }

    protected EvaluateInfo(Parcel in) {
        this.realName = in.readString();
        this.accountAvatar = in.readString();
        this.billNo = in.readString();
    }

    public static final Creator<EvaluateInfo> CREATOR = new Creator<EvaluateInfo>() {
        @Override
        public EvaluateInfo createFromParcel(Parcel source) {return new EvaluateInfo(source);}

        @Override
        public EvaluateInfo[] newArray(int size) {return new EvaluateInfo[size];}
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
