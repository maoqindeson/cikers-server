package com.cikers.wechat.mall.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tb_user")
public class UserEntity extends BaseEntity implements Serializable {
    private String username;
    private String openId;
    private String unionId;
    private String nickName;
    private String password;
    private String gender;
    private String avatarUrl;
}
