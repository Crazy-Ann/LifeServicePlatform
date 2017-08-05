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

public final class NotificationAnnouncementInfos extends CacheableImplement implements Parcelable, OnGroupListener {

    private ArrayList<NotificationAnnouncementInfo> NotificationAnnouncementInfos;

    public ArrayList<NotificationAnnouncementInfo> getNotificationAnnouncementInfos() {
        return NotificationAnnouncementInfos;
    }

    public NotificationAnnouncementInfos() {
    }

    public NotificationAnnouncementInfos parse(JSONObject object) {
        if (object != null) {
            if (object.containsKey(ResponseParameterKey.NOTIFICATION_ANNOUNCEMENT_INFOS)) {
                JSONArray array = object.getJSONArray(ResponseParameterKey.NOTIFICATION_ANNOUNCEMENT_INFOS);
                this.NotificationAnnouncementInfos = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    this.NotificationAnnouncementInfos.add(new NotificationAnnouncementInfo().parse(array.getJSONObject(i)));
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
            return "NotificationAnnouncementInfos{" +
                    ", NotificationAnnouncementInfos='" + NotificationAnnouncementInfos + '\'' +
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
        dest.writeTypedList(this.NotificationAnnouncementInfos);
    }

    protected NotificationAnnouncementInfos(Parcel in) {
        this.NotificationAnnouncementInfos = in.createTypedArrayList(NotificationAnnouncementInfo.CREATOR);
    }

    public static final Creator<NotificationAnnouncementInfos> CREATOR = new Creator<NotificationAnnouncementInfos>() {
        @Override
        public NotificationAnnouncementInfos createFromParcel(Parcel source) {
            return new NotificationAnnouncementInfos(source);
        }

        @Override
        public NotificationAnnouncementInfos[] newArray(int size) {
            return new NotificationAnnouncementInfos[size];
        }
    };
}
