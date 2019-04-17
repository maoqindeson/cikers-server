package com.cikers.wechat.mall.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tb_equipment_img")
public class EquipmentImgEntity implements Serializable {
    @TableId
    private Integer id;
    private long equipmentId;
    private String type;
    private String url;
}
