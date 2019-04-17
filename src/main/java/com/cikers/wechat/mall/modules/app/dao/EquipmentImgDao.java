
package com.cikers.wechat.mall.modules.app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cikers.wechat.mall.modules.app.entity.EquipmentImgEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:06
 */
@Mapper
public interface EquipmentImgDao extends BaseMapper<EquipmentImgEntity> {
    int truncate();
}
