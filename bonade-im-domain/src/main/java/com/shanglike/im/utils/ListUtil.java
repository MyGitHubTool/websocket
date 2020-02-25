package com.shanglike.im.utils;

import java.util.List;

/**
 * 集合工具类.
 *
 * @author GuoJie
 */
public class ListUtil {
    /**
     * 判断list是否为空或空集体.
     *
     * @param list 集合
     * @return true/false
     */
   public static Boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断list是否包含size个值.
     *
     * @param list 集合
     * @param size 个数
     * @return true/false
     */
    public static Boolean equalsSize(List<?> list, int size) {
        return !isEmpty(list) && list.size() == size;
    }
}
