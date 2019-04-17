/**
 * ProductEntity.java Create on 2018年5月9日
 * www.cikers.com.
 */

package com.cikers.wechat.mall.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品详情
 *
 * @author WangXP
 * @version 1.0
 * @date 2018年5月9日
 */
@Data
@TableName("tb_product")
public class ProductEntity {
    /** ID */
    @TableId
    private Long id;
    /** Equipment ID */
    private Long eqptId;
    /** 编码 */
    private String articleNo;
    /** 排序 */
    private Integer dispOrder;
    private String state;

    /**
     * 、属性集
     *  尺码： size
     *  版型： shape
     */
    @TableField(exist=false)
    private Map<String, PropertyEntity> props = new HashMap<>();
}
