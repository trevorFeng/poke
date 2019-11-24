package com.poke.dbservice.dao.mysql;


import com.poke.common.bean.domain.mysql.UserProposals;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProposalsMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserProposals record);

    int insertSelective(UserProposals record);

    UserProposals selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserProposals record);

    int updateByPrimaryKey(UserProposals record);
}