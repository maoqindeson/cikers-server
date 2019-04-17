package com.cikers.wechat.mall.modules.app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cikers.wechat.mall.modules.app.entity.EquipmentEntity;
import com.cikers.wechat.mall.modules.app.form.ProductForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hwp
 * @email weiping_david@foxmail.com
 * @date 2018-06-10 15:14:01
 */
@Mapper
public interface EquipmentDao extends BaseMapper<EquipmentEntity> {

    int truncate();
    Integer maoQueryListTotal(String articleNumber);

    List<EquipmentEntity> maoQueryList(ProductForm productForm);

    List<EquipmentEntity> queryList(ProductForm productForm);

    List<EquipmentEntity> queryByPage(ProductForm productForm);

    List<EquipmentEntity> queryByarticleNumber(String article_number);

    EquipmentEntity queryById(Long id);

    EquipmentEntity queryAllById(Long id);

    Integer queryListTotal(ProductForm productForm);

    int updateStateByState(@Param("id") Long id, @Param("state") String state);
}
