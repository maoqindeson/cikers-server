package com.cikers.wechat.mall.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cikers.wechat.mall.modules.app.dao.PropertyRelationDao;
import com.cikers.wechat.mall.modules.app.entity.PropertyEntity;
import com.cikers.wechat.mall.modules.app.entity.PropertyRelationEntity;
import com.cikers.wechat.mall.modules.app.service.PropertyRelationService;
import com.cikers.wechat.mall.modules.app.service.PropertyService;
import com.cikers.wechat.mall.modules.app.utils.BaseResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service("equipmentPropertyRelationService")
public class PropertyRelationServiceImpl extends ServiceImpl<PropertyRelationDao, PropertyRelationEntity> implements PropertyRelationService {

    @Autowired
    private PropertyService propertyService;


    @Override
    public int truncate() {
        return baseMapper.truncate();
    }


    public Map<String, PropertyEntity> selectByEqptId(Long eqptId) {
        List<PropertyRelationEntity> propertyRelationEntities = baseMapper.selectByEqptId(eqptId);
        Map<String, PropertyEntity> propertyEntityMap = new HashMap<>();
        for (PropertyRelationEntity relationEntity : propertyRelationEntities) {
            PropertyEntity propertyEntity = propertyService.selectById(relationEntity.getPropertyId());
            if (propertyEntity != null) {
                propertyEntityMap.put(relationEntity.getPropertyName(), propertyEntity);
            }
        }
        return propertyEntityMap;
    }

    public Map<String, PropertyEntity> selectByEqptIds(String eqptIds) {
        String[] idstr = eqptIds.split(",");    //调用拆分字符串的方法，转换成字符串数组
        Long[] idlong = new Long[idstr.length];   //新建整数数组
        for (int i = 0; i < idstr.length; i++) {
            idlong[i] = Long.valueOf(idstr[i]);    //字符串数组转换成整数数组
        }
        List<Long> idlist = Arrays.asList(idlong);    //整数数组转换成list集合
        List<PropertyRelationEntity> propertyRelationEntities = baseMapper.selectByEqptIds(idlist);   //得到返回值list
        Map<String, PropertyEntity> propertyEntityMap = new HashMap<>();   //新建map集合，key是string类型，value是PropertyEntity对象
        for (PropertyRelationEntity relationEntity : propertyRelationEntities) {
            PropertyEntity propertyEntity = propertyService.selectById(relationEntity.getPropertyId());      //查询tb_property得到propertyEntity对象
            if (propertyEntity != null) {    //校验对象
                propertyEntityMap.put(relationEntity.getPropertyName(), propertyEntity);  //对新建的map集合放值进去
            }
        }
        return propertyEntityMap;             //返回值propertyEntityMap
    }

    public List<String> selectColorsByEqptIds(String eqptIds) {
        String[] idstr = eqptIds.split(",");
        Long[] idlong = new Long[idstr.length];
        for (int i = 0; i < idstr.length; i++) {
            idlong[i] = Long.valueOf(idstr[i]);
        }
        List<Long> idlist = Arrays.asList(idlong);
        List<String> color = baseMapper.selectColorsByEqptIds(idlist);
        return color;
    }


    @Override
    public Map<String, PropertyEntity> selectByProductId(Long productId) {
        List<PropertyRelationEntity> propertyRelationEntities = baseMapper.selectByProductId(productId);
        if (propertyRelationEntities.size() == 0) {
            return null;
        }
        Map<String, PropertyEntity> propertyEntityMap = new HashMap<>();
        for (PropertyRelationEntity relationEntity : propertyRelationEntities) {
            PropertyEntity propertyEntity = propertyService.selectById(relationEntity.getPropertyId());
            if (propertyEntity != null) {
                propertyEntityMap.put(relationEntity.getPropertyName(), propertyEntity);
            }
        }
        return propertyEntityMap;
    }

    @Override
    public Map<String, PropertyEntity> getPropsByEqptId(Long eqptId) {
        List<PropertyRelationEntity> propertyRelationEntities = baseMapper.selectByEqptId(eqptId);
        if (propertyRelationEntities == null || propertyRelationEntities.isEmpty()) {
            return null;
        }
        Map<String, PropertyEntity> props = new HashMap<>();
        for (PropertyRelationEntity propertyRelationEntity : propertyRelationEntities) {
            PropertyEntity propertyEntity =
                    propertyService.selectOne(new EntityWrapper<PropertyEntity>().eq("id", propertyRelationEntity.getPropertyId()));
            if (propertyEntity == null) {
                return null;
            }
            props.put(propertyRelationEntity.getPropertyName(), propertyEntity);
        }
        return props;
    }

    @Override
    public List<String> getColorByEqptId(Long eqptId) {
        List<String> color = new ArrayList<>();
        List<PropertyRelationEntity> propertyRelationEntities =
                baseMapper.selectList(new EntityWrapper<PropertyRelationEntity>().eq("equipment_id", eqptId).eq("property_name", "MAINCOLOR"));
        if (propertyRelationEntities == null || propertyRelationEntities.isEmpty()) {
            return null;
        }
        for (PropertyRelationEntity propertyRelationEntity : propertyRelationEntities) {
            PropertyEntity propertyEntity = propertyService.selectOne(new EntityWrapper<PropertyEntity>().eq("id", propertyRelationEntity.getPropertyId()));
            if (propertyEntity == null) {
                return null;
            }
            color.add(propertyEntity.getValue());
        }
        return color;
    }
}
