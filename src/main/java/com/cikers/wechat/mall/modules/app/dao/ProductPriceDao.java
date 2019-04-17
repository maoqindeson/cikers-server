
package com.cikers.wechat.mall.modules.app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cikers.wechat.mall.modules.app.entity.ProductPriceEntity;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ProductPriceDao extends BaseMapper<ProductPriceEntity> {
    int truncate();
}
