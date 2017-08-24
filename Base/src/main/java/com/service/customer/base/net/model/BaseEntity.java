package com.service.customer.base.net.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.components.cache.listener.implement.CacheableImplement;

public class BaseEntity extends CacheableImplement implements Parcelable {

    private String returnResult;
    private String returnCode;
    private String returnMessage;

    public BaseEntity() { }

    public String getReturnResult() {
        return returnResult;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public BaseEntity parse(JSONObject object) {
        if (object != null) {
            this.returnResult = object.getString(ResponseParameterKey.RESULT);
            this.returnCode = object.getString(ResponseParameterKey.CODE);
            this.returnMessage = object.getString(ResponseParameterKey.MESSAGE);
        }
        return this;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "returnResult='" + returnResult + '\'' +
                "returnCode='" + returnCode + '\'' +
                ", returnMessage='" + returnMessage + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.returnCode);
        dest.writeString(this.returnCode);
        dest.writeString(this.returnMessage);
    }

    protected BaseEntity(Parcel in) {
        this.returnCode = in.readString();
        this.returnCode = in.readString();
        this.returnMessage = in.readString();
    }
    
    public static final Creator<BaseEntity> CREATOR = new Creator<BaseEntity>() {
        @Override
        public BaseEntity createFromParcel(Parcel in) {
            return new BaseEntity(in);
        }

        @Override
        public BaseEntity[] newArray(int size) {
            return new BaseEntity[size];
        }
    };
}
