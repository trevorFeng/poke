package com.poke.common.bean.bo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserPokesIndex {
    /**
     * 第几局
     */
    private Integer index;

    /**
     * 这一句玩家的情况
     */
    private List<UserPoke> userPokeList = new ArrayList<>(2<<4);
}
