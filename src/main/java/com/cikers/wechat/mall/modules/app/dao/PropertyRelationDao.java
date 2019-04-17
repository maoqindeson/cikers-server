package com.cikers.wechat.mall.modules.app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cikers.wechat.mall.modules.app.entity.PropertyRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 装备属性关系表
 *
 * @author hwp
 * @email weiping_david@foxmail.com
 * @date 2018-06-10 15:14:01
 */
@Mapper
public interface PropertyRelationDao extends BaseMapper<PropertyRelationEntity> {
    int truncate();

    List<PropertyRelationEntity> selectByEqptId(Long eqptId);
    List<PropertyRelationEntity> selectByEqptIds(@Param("Ids") List<Long> Ids);
    List<String> selectColorsByEqptIds(@Param("Ids") List<Long> Ids);

    List<PropertyRelationEntity> selectByProductId(Long productId);
}
