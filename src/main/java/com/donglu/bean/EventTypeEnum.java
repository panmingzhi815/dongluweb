package com.donglu.bean;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
public enum  EventTypeEnum {
    正常刷卡(0x41, "正常刷卡"),
    非法刷卡(0x42, "非法刷卡"),
    正常开门(0x43, "正常开门"),
    内手柄开门(0x44, "内手柄开门"),
    非法开门(0x45, "非法开门"),
    机械钥匙开门(0x46, "机械钥匙开门"),
    消防报警(0x47, "消防报警"),
    防拆报警(0x48, "防拆报警"),
    密码开门(0x49, "密码开门"),
    内读头正常刷卡(0x4A, "内读头正常刷卡"),
    内读头非法刷卡(0x4B, "内读头非法刷卡"),
    出门开关(0x4D, "出门开关"),
    电脑远程开门(0x4F, "电脑远程开门"),
    门虚掩报警(0x50, "门虚掩报警"),
    紧急按钮(0x51, "紧急按钮"),
    电脑远程关门(0x52, "电脑远程关门"),
    Error(0x00, "未知动作"),
    停车场正常刷卡(0x0A,"停车场正常刷卡"),
    停车场非法刷卡(0x0A,"停车场非法刷卡"),
    车位计数出场(0x5A,"车位计数出场"),
    车位计数入场(0x59,"车位计数入场"),
    停车场手动开闸(0x0C,"停车场手动开闸"),
    发卡器(0x99,"发卡器"),
    考勤打卡(0x0D,"考勤打卡");

    private final int ascii;
    private String description;

    EventTypeEnum(int ascii, String description) {
        this.ascii = ascii;
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }


}
