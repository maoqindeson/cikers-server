package com.cikers.wechat.mall.modules.app.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cikers.wechat.mall.modules.app.dao.EquipmentImgDao;
import com.cikers.wechat.mall.modules.app.entity.EquipmentImgEntity;
import com.cikers.wechat.mall.modules.app.service.EquipmentImgService;
import org.springframework.stereotype.Service;


@Service("equipmentImgService")
public class EquipmentImgServiceImpl extends ServiceImpl<EquipmentImgDao, EquipmentImgEntity> implements EquipmentImgService {
    @Override
    public int truncate() {
        return baseMapper.truncate();
    }
}
