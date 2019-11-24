package com.poke.common.bean.domain.mysql;

import lombok.Data;

/**
 * 一句话描述该类作用:【好友管理】
 *
 * @author: trevor
 * @create: 2019-03-09 14:08
 **/
@Data
public class FriendsManage {
    private Integer id;

    /**
     * 玩家id
     */
    private Integer userId;

    /**
     * 管理的好友id
     */
    private Integer manageFriendId;

    /**
     * 1未通过 ，0为未通过
     */
    private Byte allowFlag;

}