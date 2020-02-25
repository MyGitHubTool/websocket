package com.shanglike.im.enums;

/**
 * 活动状态枚举
 *
 * @author GuoJie
 * @date 2019/8/23 16:10
 */

public enum ImMessageStateEnum {

    //0.未推送 1.已推送 2.已失效',

    /**
     * 0 未推送
     */
    NOT_PUSH(0, "未推送"),
    /**
     * 1 已推送
     */
    HAVE_PUSH(1, "已推送"),
    /**
     * 2 已失效
     */
    INVALID(2, "已失效"),

    ;
    /**
     * 值
     */
    private Integer key;

    /**
     * 描述
     */
    private String name;


    ImMessageStateEnum(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static String getNameByKey(Integer key) {
        for (ImMessageStateEnum e : ImMessageStateEnum.values()) {
            if (!e.getKey().equals(key)) {
                continue;
            }
            return e.getName();
        }
        return null;
    }
}


