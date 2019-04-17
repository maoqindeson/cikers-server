package com.cikers.wechat.mall.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("tb_product_price")
public class ProductPriceEntity {

//    private int id;
    private Long equipmentId;
    private String level;
    private Double price;
    private String type;
}
