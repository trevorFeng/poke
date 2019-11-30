package com.poke.dbservice.service;

import com.poke.common.bean.domain.mysql.FriendsManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendsManageService {

    List<FriendsManage> findByUserId(Integer userId);

    void save(FriendsManage friendsManage);

    void update(FriendsManage friendsManage);

}
