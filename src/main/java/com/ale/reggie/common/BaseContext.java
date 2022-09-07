package com.ale.reggie.common;

/**
 * 基于ThreadLocal封装的工具类，用于获取和保存当前用户id
 */

public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
