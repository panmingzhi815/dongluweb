package com.donglu.bean;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
public enum  EventTypeEnum {
    正常刷卡(0x41),
    非法刷卡(0x42),
    正常开门(0x43),
    内手柄开门(0x44),
    非法开门(0x45),
    机械钥匙开门(0x46),
    消防报警(0x47),
    防拆报警(0x48),
    密码开门(0x49),
    内读头正常刷卡(0x4A),
    内读头非法刷卡(0x4B),
    出门开关(0x4D),
    电脑远程开门(0x4F),
    门虚掩报警(0x50),
    紧急按钮(0x51),
    电脑远程关门(0x52),
    Error(0x00),
    停车场正常刷卡(0x0A),
    停车场非法刷卡(0x0A),
    车位计数出场(0x5A),
    车位计数入场(0x59),
    停车场手动开闸(0x0C),
    发卡器(0x99),
    考勤打卡(0x0D),
    上机(0x60),
    下机(0x61);
    private final int ascii;

    EventTypeEnum(int ascii) {
        this.ascii = ascii;
    }

}
