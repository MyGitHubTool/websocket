package com.shanglike.im.enums;

/**
 * 聊天是否可见枚举
 *
 * @author GuoJie
 * @date 2019/8/23 16:10
 */

public enum ChatVisibleEnum {

    //聊天是否可见 1.可见 0. 不可见

    /**
     * 1.可见
     */
    VISIBLE(1, "可见"),
    /**
     * 0. 不可见
     */
    INVISIBLE(0, "不可见"),


    ;
    /**
     * 值
     */
    private Integer key;

    /**
     * 描述
     */
    private String name;


    ChatVisibleEnum(Integer key, String name) {
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
        for (ChatVisibleEnum e : ChatVisibleEnum.values()) {
            if (!e.getKey().equals(key)) {
                continue;
            }
            return e.getName();
        }
        return null;
    }
}


