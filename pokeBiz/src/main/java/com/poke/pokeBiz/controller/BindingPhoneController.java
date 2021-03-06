package com.poke.pokeBiz.controller;


import com.poke.common.bean.bo.JsonEntity;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.common.core.UserContextHolder;
import com.poke.pokeBiz.bo.PhoneCode;
import com.poke.pokeBiz.service.BindingPhoneService;
import com.poke.pokeBiz.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @author trevor
 * @date 2019/3/8 16:50
 */
@Api(value = "绑定手机号" ,description = "绑定手机号")
@RestController
@Validated
public class BindingPhoneController {

    @Resource
    private BindingPhoneService bindingPhoneService;

    @Resource
    private RedisService redisService;


    @ApiOperation(value = "发送验证码")
    @ApiImplicitParam(name = "phoneNum" ,value = "phoneNum" , required = true ,paramType = "path" ,dataType = "string")
    @RequestMapping(value = "/api/binding/phone/{phoneNum}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<String> bindPhoneNum(@PathVariable("phoneNum") @Pattern(regexp = "^[0-9]{11}$" ,message = "手机号格式不正确") String phoneNum){
        JsonEntity<String> stringJsonEntity = bindingPhoneService.generatePhoneCode(phoneNum);
        if (stringJsonEntity.getCode() < 0) {

            return stringJsonEntity;
        }
        String code = stringJsonEntity.getData();
        redisService.setValueWithExpire(phoneNum ,"123456" ,60*5L , TimeUnit.SECONDS);
        return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.SEND_MESSAGE);
    }


    @ApiOperation("校验用户的验证码是否正确,并绑定")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "body", name = "phoneCode", dataType = "PhoneCode", required = true, value = "phoneCode")})
    @RequestMapping(value = "/api/front/phone/submit", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonEntity<String> submit(@RequestBody @Validated PhoneCode phoneCode){
        //校验验证码是否正确
        String code = redisService.getValue(phoneCode.getPhoneNum());
        if (code == null) {
            return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.CODE_EXPIRE);
        }
        if (Objects.equals(code ,phoneCode.getCode())) {
            User user = UserContextHolder.currentUser();
            JsonEntity<String> stringJsonEntity = bindingPhoneService.bindingPhone(user.getId(), phoneCode.getPhoneNum());
            return stringJsonEntity;
        }else {
            return ResponseHelper.createInstanceWithOutData(MessageCodeEnum.CODE_ERROR);
        }
    }


}
