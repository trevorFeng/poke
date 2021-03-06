package com.poke.pokeAuth.service;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.common.client.UserDbClient;
import com.poke.common.util.GetMessageCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author trevor
 * @date 03/22/19 13:15
 */
@Service
public class BrowserLoginService{

    @Resource
    private UserDbClient userDbClient;

    /**
     * 生成验证码发给用户
     * @param phoneNum
     * @return
     */
    public JsonEntity<String> generatePhoneCode(String phoneNum) {
        //检查手机号是否已经注册
        Boolean existByPhoneNum = userDbClient.isExistByPhoneNum(phoneNum).getData();
        if (!existByPhoneNum) {
            return ResponseHelper.withErrorInstance(MessageCodeEnum.PHONE_NOT_EXIST);
        }
        String code = GetMessageCodeUtil.getCode(phoneNum);
        if (Objects.equals(code ,"000000")) {
            return ResponseHelper.withErrorInstance(MessageCodeEnum.CODE_FILED);
        }
        return ResponseHelper.createInstance(code ,MessageCodeEnum.CREATE_SUCCESS);
    }

    /**
     * 查询用户
     * @param phoneNum
     * @return
     */
    public User getUserHashAndOpenidByPhoneNum(String phoneNum) {
        User user = userDbClient.findByPhoneNum(phoneNum).getData();
        return user;
    }
}
