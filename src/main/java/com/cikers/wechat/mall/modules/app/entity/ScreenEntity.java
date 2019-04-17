package com.cikers.wechat.mall.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_screen")
public class ScreenEntity {
    private long id;
    private String articleNumber;
    private Date createdDate;
    private Date updatedDate;
}
