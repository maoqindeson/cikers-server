
package com.cikers.wechat.mall.modules.app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cikers.wechat.mall.modules.app.entity.PlayerPriceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:06
 */
@Mapper
public interface PlayerPriceDao extends BaseMapper<PlayerPriceEntity> {
    public PlayerPriceEntity getByEquipmentIdAndUserLevel(@Param("equipment_id") Long equipment_id, @Param("agent_level") String agent_level);
    public PlayerPriceEntity getStandardByEquipmentId(Long equipment_id);
    List<PlayerPriceEntity> getAllByEquipmentId(Long equipment_id);
    int truncate();
}
