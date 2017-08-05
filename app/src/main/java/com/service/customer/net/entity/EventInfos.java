package com.service.customer.net.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.components.cache.listener.implement.CacheableImplement;
import com.service.customer.components.widget.sticky.listener.OnGroupListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public final class EventInfos extends CacheableImplement implements Parcelable, OnGroupListener {

    private ArrayList<EventInfo> eventInfos;

    public ArrayList<EventInfo> getEventInfos() {
        return eventInfos;
    }

    public EventInfos() {
    }

    public EventInfos parse(JSONObject object) {
        if (object != null) {
            if (object.containsKey(ResponseParameterKey.EVENT_INFOS)) {
                JSONArray array = object.getJSONArray(ResponseParameterKey.EVENT_INFOS);
                this.eventInfos = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    this.eventInfos.add(new EventInfo().parse(array.getJSONObject(i)));
                }
            } else {
                return null;
            }
            return this;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "EventInfos{" +
                    ", eventInfos='" + eventInfos + '\'' +
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.eventInfos);
    }

    protected EventInfos(Parcel in) {
        this.eventInfos = in.createTypedArrayList(EventInfo.CREATOR);
    }

    public static final Creator<EventInfos> CREATOR = new Creator<EventInfos>() {
        @Override
        public EventInfos createFromParcel(Parcel source) {
            return new EventInfos(source);
        }

        @Override
        public EventInfos[] newArray(int size) {
            return new EventInfos[size];
        }
    };
}
