package com.service.customer.constant;

public enum Temp {

    CONFIG_INFO("config_info"),
    LOGIN_INFO("login_info"),
    TITLE("title"),
    NEED_LOCATION("need_location");

    private String content;

    Temp(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
