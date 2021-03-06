package com.cikers.wechat.mall.modules.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cikers.wechat.mall.modules.app.entity.UserEntity;
import com.cikers.wechat.mall.modules.app.service.UserService;
import com.cikers.wechat.mall.modules.app.utils.*;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Data
@RestController
@RequestMapping("wechat")
@ConfigurationProperties(prefix = "wechat")
public class WechatController {
    private String appId;
    private String appSecret;
    private String grantType;
    private String mchId;
    private String key;
    private String notifyUrl;
    private String TRADETYPE;
    @Autowired
    private UserService userService;

    /**
     * 小程序登录
     */
    @PostMapping("/login")
    public BaseResp login(@RequestBody WechatLoginForm wechatLoginForm) {
        log.warn("接收到微信授权登录参数为 : " + wechatLoginForm.toString());
        if (StringUtils.isBlank(wechatLoginForm.getCode())) {
            return BaseResp.error("code不能为空");
        }
        if (StringUtils.isBlank(wechatLoginForm.getAvatarUrl())) {
            return BaseResp.error("头像信息不能为空");
        }
        if (StringUtils.isBlank(wechatLoginForm.getGender())) {
            return BaseResp.error("性别信息不能为空");
        }
        if (StringUtils.isBlank(wechatLoginForm.getNickName())) {
            return BaseResp.error("昵称不能为空");
        }
        if (StringUtils.isBlank(wechatLoginForm.getEncryptedData())) {
            return BaseResp.error("用户信息密文不能为空");
        }
        if (StringUtils.isBlank(wechatLoginForm.getIv())) {
            return BaseResp.error("ivc参数不能为空");
        }
        String avatarUrl = wechatLoginForm.getAvatarUrl();
        String gender = wechatLoginForm.getGender();
        String nickName = wechatLoginForm.getNickName();
        String code = wechatLoginForm.getCode();
        String param = "?grant_type=" + grantType + "&appid=" + appId + "&secret=" + appSecret + "&js_code=" + code;

        String url = "https://api.weixin.qq.com/sns/jscode2session" + param;
        log.warn("请求微信登录url : " + url);
        String result = HttpClientUtil.getGetResponse(url);
        if (StringTools.isNullOrEmpty(result)) {
            log.error("小程序登录接口返回结果为空");
            return BaseResp.error("小程序登录接口返回结果为空");
        }
        log.warn("小程序登录接口返回参数：{}", result);
        JSONObject rsJosn = JSON.parseObject(result);
        if (rsJosn.get("errcode") != null) {
            //返回异常信息
            log.error("小程序登陆返回异常信息：" + rsJosn.get("errmsg").toString());
            return BaseResp.error(rsJosn.get("errmsg").toString());
        }
        String sessionKey = null;
        Map<String, Object> map = new HashMap<>();
        String unionId = null;
        JSONObject userInfoJSON = null;
        String openId = null;
        if (null != rsJosn.get("session_key")) {
            sessionKey = rsJosn.get("session_key").toString();
        } else {
            log.error("小程序登录接口无法获得session_key");
            return BaseResp.error("小程序登录接口无法获得session_key");
        }
        if (null != rsJosn.get("openid")) {
            openId = rsJosn.get("openid").toString();
        } else {
            log.error("小程序登录接口无法获得openid");
            return BaseResp.error("小程序登录接口无法获得openid");
        }
        if (!StringTools.isNullOrEmpty(wechatLoginForm.getEncryptedData())) {
            //////////////// 2、对encryptedData加密数据进行AES解密其中包含这openid和unionid ////////////////
            try {
                log.warn("收到encryptedData为：" + wechatLoginForm.getEncryptedData() + "用户openid为" + openId + "昵称为" + nickName);
                String decryptResult = AesCbcUtil.decrypt(wechatLoginForm.getEncryptedData(), sessionKey, wechatLoginForm.getIv(), "UTF-8");
                if (null != decryptResult && decryptResult.length() > 0) {
                    userInfoJSON = JSON.parseObject(decryptResult);
                    log.warn("对encryptedData加密数据进行AES解密得到数据" + userInfoJSON);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("对encryptedData加密数据进行AES解密失败，encryptedData数据为：" + wechatLoginForm.getEncryptedData());
                return BaseResp.error("对encryptedData加密数据进行AES解密失败");
            }
        }
        if (null == userInfoJSON) {
            log.error("用户微信登陆encryptedData无法解密，用户encryptedData为" + wechatLoginForm.getEncryptedData());
        } else {
            if (null != userInfoJSON.get("unionId") && !StringTools.isNullOrEmpty(userInfoJSON.get("unionId").toString())) {
                unionId = userInfoJSON.get("unionId").toString();
            } else if (null != rsJosn.get("unionid") && !StringTools.isNullOrEmpty(rsJosn.get("unionid").toString())) {
                unionId = rsJosn.get("unionid").toString();
            } else {
                log.error("用户微信登陆无法获得unionid，暂用openid代替，用户openid为" + openId + "昵称为" + nickName);
                unionId = openId;
            }
        }
        try {
            //如果通过open_id能查出存在用户，则直接返回用户信息
            synchronized (this) {
                if (null == userService.selectOne(new EntityWrapper<UserEntity>().eq("open_id", openId))) {
                    //抽空不全插入检查，唯一键等；
                    UserEntity userEntity = new UserEntity();
                    userEntity.setOpenId(openId);
                    userEntity.setUnionId(unionId);
                    userEntity.setUsername(openId);
                    userEntity.setAvatarUrl(avatarUrl);
                    userEntity.setGender(gender);
                    userEntity.setNickName(nickName);
                    userEntity.setCreatedAt(LocalDateTime.now());
                    if (!userService.insert(userEntity)) {
                        log.error("登陆接口插入用户数据失败,用户openid为" + openId + "昵称为" + nickName);
                        return BaseResp.error("登陆接口插入用户数据失败,用户openid为" + openId + "昵称为" + nickName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("登陆接口插入用户数据异常" + e.getMessage() + "用户openid为" + openId + "昵称为" + nickName);
            return BaseResp.error("登陆接口插入用户数据异常,用户openid为" + openId + "昵称为" + nickName);
        }

        String token = JWTUtil.sign(openId);
        map.put("openId", openId);
        map.put("unionId", unionId);
        map.put("sessionKey", sessionKey);
        map.put("token", token);
        return BaseResp.ok(map);
    }





}
