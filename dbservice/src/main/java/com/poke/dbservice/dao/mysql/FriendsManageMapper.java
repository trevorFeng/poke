package com.poke.dbservice.dao.mysql;

import com.poke.common.bean.domain.mysql.FriendsManage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsManageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FriendsManage record);

    int insertSelective(FriendsManage record);

    FriendsManage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FriendsManage record);

    int updateByPrimaryKey(FriendsManage record);

    /**
     * 根据用户id查询管理的好友
     * @param userId
     * @return
     */
    List<FriendsManage> findByUserId(@Param("userId") Integer userId);


    Integer passOrRemoveFriend(@Param("friendsManage") FriendsManage friendsManage);

}