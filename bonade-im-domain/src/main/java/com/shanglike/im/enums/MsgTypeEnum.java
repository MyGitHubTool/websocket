package com.shanglike.im.enums;

/**
 * 消息类型枚举
 *
 * @author GuoJie
 * @date 2019/8/23 16:10
 */

public enum MsgTypeEnum {

    //消息类型 1.聊天消息 2.推送消息

    /**
     * 1.聊天消息
     */
    CHAT_MESSAGE(1, "聊天消息"),
    /**
     * 2.推送消息
     */
    PUSH_MESSAGE(2, "推送消息"),


    ;
    /**
     * 值
     */
    private Integer key;

    /**
     * 描述
     */
    private String name;


    MsgTypeEnum(Integer key, String name) {
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
        for (MsgTypeEnum e : MsgTypeEnum.values()) {
            if (!e.getKey().equals(key)) {
                continue;
            }
            return e.getName();
        }
        return null;
    }
}


