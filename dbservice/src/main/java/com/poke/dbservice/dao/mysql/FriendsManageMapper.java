package com.poke.dbservice.dao.mysql;

import com.poke.common.bean.domain.mysql.FriendsManage;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsManageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FriendsManage record);

    int insertSelective(FriendsManage record);

    FriendsManage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FriendsManage record);

    int updateByPrimaryKey(FriendsManage record);
}