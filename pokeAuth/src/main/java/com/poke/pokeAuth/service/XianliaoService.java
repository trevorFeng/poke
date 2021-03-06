package com.poke.pokeAuth.service;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.bo.WebKeys;
import com.poke.common.bean.domain.mysql.PersonalCard;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.common.client.PersonalCardDbClient;
import com.poke.common.client.UserDbClient;
import com.poke.common.util.RandomUtils;
import com.poke.common.util.TokenUtil;
import com.poke.pokeAuth.util.XianliaoAuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author trevor
 * @date 03/21/19 18:24
 */
@Service
@Slf4j
public class XianliaoService{

    @Resource
    private UserDbClient userDbClient;

    @Resource
    private PersonalCardDbClient personalCardDbClient;

    /**
     * 根据code获取闲聊用户基本信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonEntity<String> weixinAuth(String code) throws IOException {
        //获取access_token
        Map<String, String> accessTokenMap = XianliaoAuthUtils.getXianliaoToken(code);
        //拉取用户信息
        Map<String, String> userInfoMap = XianliaoAuthUtils.getUserInfo(accessTokenMap.get(WebKeys.ACCESS_TOKEN));
        //有可能access token以被使用
        if (Objects.equals(WebKeys.SUCCESS ,userInfoMap.get(WebKeys.ERRMSG))) {
            log.error("拉取用户信息 失败啦,快来围观:-----------------" + userInfoMap.get(WebKeys.ERRMSG));
            //刷新access_token
            Map<String, String> accessTokenByRefreshTokenMap = XianliaoAuthUtils.getXianliaoTokenByRefreshToken(accessTokenMap.get(WebKeys.REFRESH_TOKEN));
            // 再次拉取用户信息
            userInfoMap = XianliaoAuthUtils.getUserInfo(accessTokenByRefreshTokenMap.get(WebKeys.ACCESS_TOKEN));
        }
        String openid = userInfoMap.get(WebKeys.OPEN_ID);
        if (openid == null) {
            return ResponseHelper.withErrorInstance(MessageCodeEnum.AUTH_FAILED);
        } else {
            //判断用户是否存在
            Boolean isExist = userDbClient.isExistByOpnenId(openid).getData();
            Map<String,Object> claims = new HashMap<>(2<<4);
            if (!isExist) {
                //新增
                String hash = RandomUtils.getRandomChars(10);
                User user = new User();
                user.setOpenId(userInfoMap.get(WebKeys.OPEN_ID));
                user.setAppName(userInfoMap.get("nickName"));
                user.setAppPictureUrl(userInfoMap.get("smallAvatar"));
                user.setHash(hash);
                user.setType((byte)1);
                user.setFriendManageFlag((byte)0);
                userDbClient.save(user);
                //新增用户房卡记录
                PersonalCard personalCard = new PersonalCard();
                personalCard.setUserId(user.getId());
                personalCard.setRoomCardNum(0);
                personalCardDbClient.save(personalCard);

                claims.put("hash" ,user.getHash());
                claims.put("openid" ,user.getOpenId());
                claims.put("timestamp" ,System.currentTimeMillis());
            } else {
                //更新头像，昵称，hash
                String hash = RandomUtils.getRandomChars(10);
                User user = new User();
                user.setAppName(userInfoMap.get("nickName"));
                user.setHash(hash);
                user.setAppPictureUrl(userInfoMap.get("smallAvatar"));
                userDbClient.updateUser(user);

                claims.put("userid" ,user.getId());
                claims.put("openid" ,user.getOpenId());
                claims.put("timestamp" ,System.currentTimeMillis());
            }
            String token = TokenUtil.generateToken(claims);
            return ResponseHelper.createInstance(token ,MessageCodeEnum.AUTH_SUCCESS);
        }
    }

}
