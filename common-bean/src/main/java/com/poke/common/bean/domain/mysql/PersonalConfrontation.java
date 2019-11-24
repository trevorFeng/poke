package com.poke.common.bean.domain.mysql;

import lombok.Data;

/**
 * 一句话描述该类作用:【玩家在一间房的对局情况】
 *
 * @author: trevor
 * @create: 2019-03-04 23:21
 **/
@Data
public class PersonalConfrontation {

    private Integer id;

    private Integer userId;

    private Integer roomId;

    private Long endTime;

    /**
     * 积分情况
     */
    private Integer integralCondition;

}