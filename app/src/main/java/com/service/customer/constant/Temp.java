package com.service.customer.constant;

public enum Temp {

    CONFIG_INFO("config_info"),
    LOGIN_INFO("login_info"),
    TITLE("title"),
    NEED_LOCATION("need_location"),
    URL("url"),
    TASK_INFO("task_info"),
    EVALUATE_INFO("evaluate_info"),
    TAB("tab");

    private String content;

    Temp(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
