package com.poke.common.bean.domain.mysql;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class RechargeRecord {
    private Integer id;

    private Integer userId;

    /**
     * 充值房卡数量
     */
    private Integer rechargeCard;

    /**
     * 房卡单价
     */
    private BigDecimal unitPrice;

    /**
     * 本次充值的总价
     */
    private BigDecimal totalPrice;

    /**
     * 充值时间
     */
    private Long time;

}