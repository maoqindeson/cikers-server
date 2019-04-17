package com.cikers.wechat.mall.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import com.cikers.wechat.mall.modules.app.entity.PropertyEntity;
import com.cikers.wechat.mall.modules.app.entity.PropertyRelationEntity;
import com.cikers.wechat.mall.modules.app.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 装备属性关系表
 *
 * @author hwp
 * @email weiping_david@foxmail.com
 * @date 2018-06-10 15:14:01
 */
public interface PropertyRelationService extends IService<PropertyRelationEntity> {


    int truncate();
    Map<String, PropertyEntity> selectByEqptId(Long eqptId);
    Map<String, PropertyEntity> selectByEqptIds(String ids);
    List<String> selectColorsByEqptIds(String eqptIds);
    Map<String, PropertyEntity> selectByProductId(Long productId);
    Map<String, PropertyEntity> getPropsByEqptId(Long eqptId);
    List<String>getColorByEqptId(Long eqptId);

}

