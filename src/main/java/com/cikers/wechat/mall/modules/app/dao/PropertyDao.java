package com.cikers.wechat.mall.modules.app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cikers.wechat.mall.modules.app.entity.PropertyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性表
 *
 * @author hwp
 * @email weiping_david@foxmail.com
 * @date 2018-06-10 15:14:01
 */
@Mapper
public interface PropertyDao extends BaseMapper<PropertyEntity> {
    int truncate();
    List<PropertyEntity> getByPropertyId(Long propertyId);
    List<PropertyEntity> queryTypes();
    List<PropertyEntity> selectByEqptIds(@Param("Ids") List<Long> Ids);
}
