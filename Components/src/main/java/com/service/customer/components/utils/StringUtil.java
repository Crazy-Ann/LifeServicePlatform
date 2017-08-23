package com.service.customer.components.utils;

import com.service.customer.components.constant.Regex;

import java.util.List;

public class StringUtil {

    private StringUtil() {
        // cannot be instantiated
    }

    public static String append(boolean isSeparate, String... content) {
        StringBuffer buffer = new StringBuffer();
        if (isSeparate) {
            for (int i = 0; i < content.length; i++) {
                if (i != content.length - 1) {
                    buffer.append(content[i]).append(Regex.COMMA.getRegext());
                } else {
                    buffer.append(content[i]);
                }
            }
        } else {
            for (String str : content) {
                buffer.append(str);
            }
        }
        LogUtil.getInstance().print(buffer.toString());
        return buffer.toString();
    }

    public static String append(boolean isSeparate, List<String> contents) {
        StringBuffer buffer = new StringBuffer();
        if (isSeparate) {
            for (int i = 0; i < contents.size(); i++) {
                if (i != contents.size() - 1) {
                    buffer.append(contents.get(i)).append(Regex.COMMA.getRegext());
                } else {
                    buffer.append(contents.get(i));
                }
            }
        } else {
            for (int i = 0; i < contents.size(); i++) {
                buffer.append(contents.get(i));
            }
        }
        LogUtil.getInstance().print(buffer.toString());
        return buffer.toString();
    }
}
