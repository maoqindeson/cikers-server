/**
 * EquipmentEntity.java Create on 2018-5-11
 * www.cikers.com.
 */


package com.cikers.wechat.mall.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.*;

/**
 * 装备产品
 *
 * @author WangXP
 * @version 1.0
 * @date 2018-5-11
 */
@Data
@TableName("tb_equipment")
public class EquipmentEntity {


    @TableId
    private long id;   //in
    private String ids;

    /**
     * 2018-06-14增加coler和title
     */
    private String title;
    private List<String> color = new LinkedList<>();
    /**
     * 装备的尺码、版型列表
     */
    @TableField(exist=false)
    private List<ProductEntity> proudcts = new ArrayList<>();
    private ProductEntity proudct ;
    private String articleNumber;   //in
    private String imgURL;   //in
    private List<EquipmentImgEntity> imgs;
    private List<String> introductions;
    private String img_strings;   //in
    private String introduction_strings;  //in
    private Double price;  //in
    private Map<String,Double> prices;
    private List<ProductPriceEntity> wholePrices;
    private String descURL; //in
    private String search_key;  //in
    private int dispOrder;  //in
    private String state;  //in
    private Long freshStartTime; //in
    private Long freshEndTime;  //in
    /**
     * 属性集
     * 品牌：brand
     * 运动类型： sport
     * 款式： type
     * 系列： series
     * 样式： style
     * 适用人群： crowd
     * 材质： material
     */
    @TableField(exist = false)
    private Map<String, PropertyEntity> props = new HashMap<>();
}