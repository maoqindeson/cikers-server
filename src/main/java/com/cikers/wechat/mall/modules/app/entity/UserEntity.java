package com.cikers.wechat.mall.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:06
 */
@Data
@TableName("tb_user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Long userId;
    private String player_id;
    private String username;
    private String mobile;
    private String password;
    private Date created_date;
    private String open_id;
    private String officialAccountsOpenId;
    private String union_id;
    private String nick_name;
    private String avatar_url;
    private String gender;
    private String user_level;
    private String form_id;
    private String prepay_id;
    private Date formid_date;
    private Date prepayid_date;
}
