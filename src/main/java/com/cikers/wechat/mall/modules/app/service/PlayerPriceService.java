package com.cikers.wechat.mall.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import com.cikers.wechat.mall.modules.app.entity.PlayerPriceEntity;

import java.util.List;

/**
 * 用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:06
 */
public interface PlayerPriceService extends IService<PlayerPriceEntity> {
public PlayerPriceEntity getByEquipmentIdAndUserLevel(Long equipment_id, String user_levle);
public PlayerPriceEntity getStandardByEquipmentId(Long equipment_id);
public List<PlayerPriceEntity> getAllByEquipmentId(Long equipment_id);

    int truncate();
}
