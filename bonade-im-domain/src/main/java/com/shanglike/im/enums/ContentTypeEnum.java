package com.shanglike.im.enums;

/**
 * 消息内容枚举
 *
 * @author GuoJie
 * @date 2019/8/23 16:10
 */

public enum ContentTypeEnum {

    //消息内容类型 1.文本 2.图片 3.文件

    /**
     * 1 文本
     */
    TEXT(1, "文本"),
    /**
     * 2 图片
     */
    IMAGE(2, "图片"),
    /**
     * 3 文件
     */
    FILE(3, "文件"),

    ;
    /**
     * 值
     */
    private Integer key;

    /**
     * 描述
     */
    private String name;


    ContentTypeEnum(Integer key, String name) {
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
        for (ContentTypeEnum e : ContentTypeEnum.values()) {
            if (!e.getKey().equals(key)) {
                continue;
            }
            return e.getName();
        }
        return null;
    }
}


