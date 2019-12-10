package com.poke.dbservice.service.impl;

import com.poke.common.bean.domain.mysql.FriendsManage;
import com.poke.common.bean.exception.BizException;
import com.poke.dbservice.dao.mysql.FriendsManageMapper;
import com.poke.dbservice.service.FriendsManageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class FriendsManageSerrviceImpl implements FriendsManageService {

    @Resource
    private FriendsManageMapper friendsManageMapper;

    @Override
    public List<FriendsManage> findByUserId(Integer userId) {
        return friendsManageMapper.findByUserId(userId);
    }

    @Override
    public void save(FriendsManage friendsManage) {
        friendsManageMapper.insert(friendsManage);
    }

    @Override
    public void update(FriendsManage friendsManage) {
        Integer integer = friendsManageMapper.passOrRemoveFriend(friendsManage);
        if (Objects.equals(integer ,0)) {
            throw new BizException(-1 ,"通过或移除好友失败");
        }
    }

    @Override
    public Boolean countFriendByUserIdAndManageId(Integer userId, Integer manageFriendId) {
        Integer count = friendsManageMapper.countFriendByUserIdAndManageId(userId, manageFriendId);
        if (count == 0) {
            return false;
        }
        return true;
    }

}
