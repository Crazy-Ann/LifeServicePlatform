package com.service.customer.constant;

public enum Temp {

    CONFIG_INFO("config_info"),
    LOGIN_INFO("login_info"),
    TITLE("title"),
    URL("url"),
    BILL_NO("bill_no"),
    TASK_TYPE("task_type"),
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
