package com.poke.common.bean.bo;

import lombok.Data;

/**
 * @Auther: trevor
 * @Date: 2019\4\16 0016 22:41
 * @Description:
 */
@Data
public class FriendInfo {

    private Integer userId;

    private String appName;

    private String pictureUrl;

    /**
     * 1通过 ，0为未通过
     */
    private Byte allowFlag;
}
