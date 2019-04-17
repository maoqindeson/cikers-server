package com.cikers.wechat.mall.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import com.cikers.wechat.mall.modules.app.entity.EquipmentEntity;
import com.cikers.wechat.mall.modules.app.form.ProductForm;
import com.cikers.wechat.mall.modules.app.utils.BaseResp;
import com.cikers.wechat.mall.modules.app.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * @author hwp
 * @email weiping_david@foxmail.com
 * @date 2018-06-10 15:14:01
 */
public interface EquipmentService extends IService<EquipmentEntity> {

    int truncate();

    Integer maoQueryListTotal(String articleNumber);

    List<EquipmentEntity> maoQueryList(ProductForm productForm);

    List<EquipmentEntity> getByArticleNumber(String articleNumber);

    List<EquipmentEntity> queryList(ProductForm productForm, String user_level);

    Integer queryListTotal(ProductForm productForm);

    List<EquipmentEntity> queryByarticleNumber(String articleNumber);

    List<EquipmentEntity> maoQueryByarticleNumber(String articleNumber);

    EquipmentEntity queryById(Long id);

    EquipmentEntity queryAllById(Long id);

    BaseResp updateStateByState(Long id, String state);
}
