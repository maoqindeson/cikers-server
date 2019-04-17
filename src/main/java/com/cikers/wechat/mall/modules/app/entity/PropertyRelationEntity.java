package com.cikers.wechat.mall.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * Created by WeiPing He on 2018/6/10 14:22.
 * Email: weiping_he@hansight.com
 */
@Data
@TableName("tb_property_relation")
public class PropertyRelationEntity {
    @TableId
    private Long id;
    private Long equipmentId;
    private Long productId;
    private Long propertyId;
    private String propertyName;
}
