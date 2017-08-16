package com.service.customer.net.entity;

import android.os.Parcel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.customer.BuildConfig;
import com.service.customer.base.constant.net.ResponseParameterKey;
import com.service.customer.base.net.model.BaseEntity;
import com.service.customer.components.widget.sticky.listener.OnGroupListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public final class ServiceInfos extends BaseEntity implements OnGroupListener {

    private ArrayList<ServiceInfo> serviceInfos;

    public ArrayList<ServiceInfo> getServiceInfos() {
        return serviceInfos;
    }

    public ServiceInfos() {
    }

    public ServiceInfos parse(JSONObject object) {
        if (object != null) {
            if (object.containsKey(ResponseParameterKey.SERVICE_INFOS)) {
                JSONArray array = object.getJSONArray(ResponseParameterKey.SERVICE_INFOS);
                this.serviceInfos = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    this.serviceInfos.add(new ServiceInfo().parse(array.getJSONObject(i)));
                }
                Collections.sort(this.serviceInfos, new Comparator<ServiceInfo>() {

                    @Override
                    public int compare(ServiceInfo o1, ServiceInfo o2) {
                        if (o1.getIndex() > o2.getIndex()) {
                            return 1;
                        }
                        if (o1.getIndex() < o2.getIndex()) {
                            return -1;
                        }
                        return 0;
                    }
                });
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
            return "ServiceInfos{" +
                    ", serviceInfos='" + serviceInfos + '\'' +
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
        dest.writeTypedList(this.serviceInfos);
    }

    protected ServiceInfos(Parcel in) {
        this.serviceInfos = in.createTypedArrayList(ServiceInfo.CREATOR);
    }

    public static final Creator<ServiceInfos> CREATOR = new Creator<ServiceInfos>() {
        @Override
        public ServiceInfos createFromParcel(Parcel source) {
            return new ServiceInfos(source);
        }

        @Override
        public ServiceInfos[] newArray(int size) {
            return new ServiceInfos[size];
        }
    };
}
