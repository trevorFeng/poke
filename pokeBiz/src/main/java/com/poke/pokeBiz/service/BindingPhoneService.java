package com.poke.pokeBiz.service;

import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.common.client.UserDbClient;
import com.poke.common.util.GetMessageCodeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author trevor
 * @date 03/22/19 13:15
 */
@Service
public class BindingPhoneService {

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
     * 绑定手机号
     * @param userId
     * @param phoneNum
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonEntity<String> bindingPhone(Integer userId, String phoneNum) {
        User user = new User();
        user.setId(userId);
        user.setPhoneNumber(phoneNum);
        userDbClient.updateUser(user);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.BINDING_SUCCESS);
    }
}
