package com.poke.dbservice.service;

import com.poke.common.bean.domain.mysql.PersonalCard;

public interface PersonalCardService {

    Integer findCardNumByUserId(Integer userId);

    void updatePersonalCardNum(Integer userId, Integer card);

    void save(PersonalCard personalCard);
}
