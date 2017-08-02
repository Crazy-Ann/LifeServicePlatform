package com.service.customer.components.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtil {

    private static ReflectUtil mReflectUtilUtil;

    private ReflectUtil() {
        // cannot be instantiated
    }

    public static synchronized ReflectUtil getInstance() {
        if (mReflectUtilUtil == null) {
            mReflectUtilUtil = new ReflectUtil();
        }
        return mReflectUtilUtil;
    }

    public static void releaseInstance() {
        if (mReflectUtilUtil != null) {
            mReflectUtilUtil = null;
        }
    }

    public Type getGenericSuperclassType(Class<?> subclass) {
        return getGenericSuperclassTypes(0, subclass);
    }

    private Type getGenericSuperclassTypes(int index, Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) superclass).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index outof bounds");
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return params[index];
    }
}
