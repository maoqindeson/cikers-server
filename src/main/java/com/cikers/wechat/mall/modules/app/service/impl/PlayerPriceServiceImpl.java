package com.cikers.wechat.mall.modules.app.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cikers.wechat.mall.modules.app.dao.PlayerPriceDao;
import com.cikers.wechat.mall.modules.app.entity.PlayerPriceEntity;
import com.cikers.wechat.mall.modules.app.service.PlayerPriceService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("playerPriceService")
public class PlayerPriceServiceImpl extends ServiceImpl<PlayerPriceDao, PlayerPriceEntity> implements PlayerPriceService {
 @Override
 public PlayerPriceEntity getByEquipmentIdAndUserLevel(Long equipment_id, String user_level){
     return  baseMapper.getByEquipmentIdAndUserLevel(equipment_id,user_level);
 }
    @Override
    public PlayerPriceEntity getStandardByEquipmentId(Long equipment_id){
        return  baseMapper.getStandardByEquipmentId(equipment_id);
    }

    @Override
    public List<PlayerPriceEntity> getAllByEquipmentId(Long equipment_id) {
        return baseMapper.getAllByEquipmentId(equipment_id);
    }

    @Override
    public int truncate() {
        return baseMapper.truncate();
    }
}
