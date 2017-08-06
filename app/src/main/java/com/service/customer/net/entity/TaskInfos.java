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

public final class TaskInfos extends CacheableImplement implements Parcelable, OnGroupListener {

    private ArrayList<TaskInfo> taskInfos;

    public ArrayList<TaskInfo> getTaskInfos() {
        return taskInfos;
    }

    public TaskInfos() {
    }

    public TaskInfos parse(JSONObject object) {
        if (object != null) {
            if (object.containsKey(ResponseParameterKey.TASK_INFOS)) {
                JSONArray array = object.getJSONArray(ResponseParameterKey.TASK_INFOS);
                this.taskInfos = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    this.taskInfos.add(new TaskInfo().parse(array.getJSONObject(i)));
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
            return "TaskInfos{" +
                    ", taskInfos='" + taskInfos + '\'' +
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
        dest.writeTypedList(this.taskInfos);
    }

    protected TaskInfos(Parcel in) {
        this.taskInfos = in.createTypedArrayList(TaskInfo.CREATOR);
    }

    public static final Creator<TaskInfos> CREATOR = new Creator<TaskInfos>() {
        @Override
        public TaskInfos createFromParcel(Parcel source) {
            return new TaskInfos(source);
        }

        @Override
        public TaskInfos[] newArray(int size) {
            return new TaskInfos[size];
        }
    };
}
