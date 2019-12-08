package com.poke.common.bean.bo;

import lombok.Data;

/**
 * @author trevor
 * @date 06/28/19 12:40
 */
@Data
public class Player {

    private Integer userId;

    private String name;

    private String pictureUrl;

    /**
     * 观众，玩家可以参与打牌
     */
    private Boolean isGuanZhong;

    private Integer totalScore;

}
