package com.service.customer.components.utils;

import com.service.customer.components.constant.Regex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String getDayTime() {
        return getCurrentTime(new Date(System.currentTimeMillis()), Regex.DATE_FORMAT_DAY.getRegext());
    }

    public static String getCurrentTime() {
        return getCurrentTime(new Date(System.currentTimeMillis()), Regex.DATE_FORMAT_ALL.getRegext());
    }

    public static String getCurrentTime(Date date, String regex) {
        return new SimpleDateFormat(regex, Locale.getDefault()).format(date);
    }
}
