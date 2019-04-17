package com.cikers.wechat.mall.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.cikers.wechat.mall.modules.app.utils.BaseResp;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Data
@TableName("tb_escape")
public class EscapeEntity {
    @TableId
    private long id;
    private String keyWord;
    private String searchKey;
    private Date createdDate;
    private Date updatedDate;
}
