package com.service.customer.constant;

public enum Temp {

    CONFIG_INFO("config_info"),
    LOGIN_INFO("login_info");

    private String mContent;

    Temp(String content) {
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

}
