package com.service.customer.net.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.service.customer.BuildConfig;
import com.service.customer.components.widget.sticky.listener.OnGroupListener;

import java.io.File;
import java.io.Serializable;

public final class TaskImageInfo implements Serializable, Parcelable, OnGroupListener {

    private File file;
    private int resourceId;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public TaskImageInfo() {}

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "TaskImageInfo{" +
                    "file='" + file + '\'' +
                    ", resourceId='" + resourceId + '\'' +
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
        dest.writeSerializable(this.file);
        dest.writeInt(this.resourceId);
    }

    protected TaskImageInfo(Parcel in) {
        this.file = (File) in.readSerializable();
        this.resourceId = in.readInt();
    }

    public static final Creator<TaskImageInfo> CREATOR = new Creator<TaskImageInfo>() {
        @Override
        public TaskImageInfo createFromParcel(Parcel source) {return new TaskImageInfo(source);}

        @Override
        public TaskImageInfo[] newArray(int size) {return new TaskImageInfo[size];}
    };
}
