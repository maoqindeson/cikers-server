package com.cikers.wechat.mall.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import com.cikers.wechat.mall.modules.app.entity.EquipmentImgEntity;

/**
 * 用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:06
 */
public interface EquipmentImgService extends IService<EquipmentImgEntity> {
    int truncate();
}
