package com.cikers.wechat.mall.modules.app.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cikers.wechat.mall.modules.app.dao.ScreenDao;
import com.cikers.wechat.mall.modules.app.entity.ScreenEntity;
import com.cikers.wechat.mall.modules.app.service.ScreenService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("screenService")
public class ScreenServiceImpl extends ServiceImpl<ScreenDao, ScreenEntity> implements ScreenService {

    @Override
    public List<String> getArticleNumbers() {
        List<ScreenEntity> list = this.selectList(new EntityWrapper<>());
        List<String> articleNumbers = new ArrayList<>();
        if (null!=list&&!list.isEmpty()){
            for (ScreenEntity screenEntity : list){
                articleNumbers.add(screenEntity.getArticleNumber());
            }
        }
        return articleNumbers;
    }
}
