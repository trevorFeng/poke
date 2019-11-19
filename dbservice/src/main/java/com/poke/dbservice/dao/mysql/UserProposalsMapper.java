package com.poke.dbservice.dao.mysql;

import com.poke.common.bean.domain.mysql.UserProposals;
import org.apache.ibatis.annotations.Param;

public interface UserProposalsMapper {

    /**
     * 新增一条记录
     * @param userProposals
     */
    void insert(@Param("userProposals") UserProposals userProposals);
}
