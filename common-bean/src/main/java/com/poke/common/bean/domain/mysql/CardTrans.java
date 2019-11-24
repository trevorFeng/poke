package com.poke.common.bean.domain.mysql;

import lombok.Data;

/**
 * 房卡交易记录
 * @author trevor
 * @date 2019/3/4 14:12
 */
@Data
public class CardTrans {
    private Integer id;

    /**
     * 交易的房卡数量
     */
    private Integer cardNum;

    /**
     * 转出时登陆玩家名字
     */
    private String turnOutUserName;

    /**
     * 转出玩家id
     */
    private Integer turnOutUserId;

    /**
     * 全局唯一的交易号
     */
    private String transNum;

    /**
     * 转出时间
     */
    private Long turnOutTime;

    /**
     * 转入时登陆玩家名字
     */
    private String turnInUserName;

    /**
     * 转入玩家id
     */
    private Integer turnInUserId;

    /**
     * 转入时间
     */
    private Long turnInTime;

    /**
     * 防止同时修改该条记录，房卡多次被领取的情况，初始值为0，每次修改版本号加1,最大值为1
     */
    private Byte version;

}