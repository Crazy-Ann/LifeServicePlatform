package com.service.customer.net.entity;

import android.os.Parcel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.base.net.model.BaseEntity;

import java.util.ArrayList;

public final class TaskInfos extends BaseEntity {

    private int pageIndex;
    private int pageCount;
    private int totalRecord;
    private ArrayList<TaskInfo> taskInfos;

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public ArrayList<TaskInfo> getTaskInfos() {
        return taskInfos;
    }

    public TaskInfos() {
    }

    public TaskInfos parse(JSONObject object) {
        if (object != null) {
            this.pageIndex = object.getIntValue(ResponseParameterKey.PAGE_INDEX);
            this.pageCount = object.getIntValue(ResponseParameterKey.PAGE_COUNT);
            this.totalRecord = object.getIntValue(ResponseParameterKey.TOTAL_RECORD);
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
                    "pageIndex='" + pageIndex + '\'' +
                    ", pageCount='" + pageCount + '\'' +
                    ", totalRecord='" + totalRecord + '\'' +
                    ", taskInfos='" + taskInfos + '\'' +
                    '}';
        } else {
            return super.toString();
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.pageIndex);
        dest.writeInt(this.pageCount);
        dest.writeInt(this.totalRecord);
        dest.writeList(this.taskInfos);
    }

    protected TaskInfos(Parcel in) {
        super(in);
        this.pageIndex = in.readInt();
        this.pageCount = in.readInt();
        this.totalRecord = in.readInt();
        this.taskInfos = new ArrayList<TaskInfo>();
        in.readList(this.taskInfos, TaskInfo.class.getClassLoader());
    }

    public static final Creator<TaskInfos> CREATOR = new Creator<TaskInfos>() {
        @Override
        public TaskInfos createFromParcel(Parcel source) {return new TaskInfos(source);}

        @Override
        public TaskInfos[] newArray(int size) {return new TaskInfos[size];}
    };
}
