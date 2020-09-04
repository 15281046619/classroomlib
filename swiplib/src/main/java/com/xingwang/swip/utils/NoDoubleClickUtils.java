package com.xingwang.swip.utils;

/**
 * @author 王丹丹
 *         防止快速点击
 *         Created by 18655 on 2017/10/11.
 */

public class NoDoubleClickUtils {
    private static long lastClickTime;
    private final static int SPACE_TIME = 1000;

    public static void initLastClickTime() {
        lastClickTime = 0;
    }

    public synchronized static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        if (currentTime - lastClickTime > SPACE_TIME) {
            isClick2 = false;
        } else {
            isClick2 = true;
        }
        lastClickTime = currentTime;
        return isClick2;
    }
}