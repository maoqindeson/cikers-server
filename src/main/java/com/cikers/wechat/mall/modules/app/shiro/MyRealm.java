package com.cikers.wechat.mall.modules.app.shiro;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cikers.wechat.mall.modules.app.entity.UserEntity;
import com.cikers.wechat.mall.modules.app.service.UserService;
import com.cikers.wechat.mall.modules.app.utils.JWTUtil;
import com.cikers.wechat.mall.modules.app.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class MyRealm extends AuthorizingRealm {


    @Autowired
    private UserService userService;



    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            return authorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getCurrentUserOpenIdByToken(token);
        if (StringTools.isNullOrEmpty(username)) {
            throw new AuthenticationException("token invalid");
        }

        UserEntity userBean = userService.selectOne(new EntityWrapper<UserEntity>().eq("open_id",username));
        if (userBean == null) {
            throw new AuthenticationException("User didn't existed!");
        }
//
//        if (! JWTUtil.verify(token, username)) {
//            throw new AuthenticationException("Username or password error");
//        }

        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
