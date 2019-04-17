
package com.cikers.wechat.mall.modules.app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cikers.wechat.mall.modules.app.entity.EscapeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface Escapedao extends BaseMapper<EscapeEntity> {
  String getSearchKeyByKeyword(@Param("keyWord") String keyWord);
  int updateByKeyword(@Param("keyWord") String keyWord, @Param("searchKey") String searchKey);
  EscapeEntity getByKeyword(@Param("keyWord") String keyWord);
  List<EscapeEntity> getAll();
}
