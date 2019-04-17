package com.cikers.wechat.mall.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cikers.wechat.mall.modules.app.dao.EquipmentDao;
import com.cikers.wechat.mall.modules.app.entity.*;
import com.cikers.wechat.mall.modules.app.form.ProductForm;
import com.cikers.wechat.mall.modules.app.service.*;
import com.cikers.wechat.mall.modules.app.utils.BaseResp;
import com.cikers.wechat.mall.modules.app.utils.ListUtil;
import com.cikers.wechat.mall.modules.app.utils.PageUtils;
import com.cikers.wechat.mall.modules.app.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service("equipmentService")
public class EquipmentServiceImpl extends ServiceImpl<EquipmentDao, EquipmentEntity> implements EquipmentService {
    @Autowired
    private ProductService productService;
    @Autowired
    private PropertyRelationService propertyRelationService;
    @Autowired
    private PlayerPriceService playerPriceService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductPriceService productPriceService;
    @Autowired
    private EquipmentImgService equipmentImgService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private PropertyService propertyService;

    @Override
    public int truncate() {
        return baseMapper.truncate();
    }

    @Override
    public Integer maoQueryListTotal(String articleNumber) {
        return baseMapper.maoQueryListTotal(articleNumber);
    }

    @Override
    public List<EquipmentEntity> maoQueryList(ProductForm productForm) {
        List<EquipmentEntity> equipmentEntities = baseMapper.maoQueryList(productForm);
        try {
            if (equipmentEntities != null) {
                for (EquipmentEntity equipmentEntity : equipmentEntities) {
                    List<String> color =
                            propertyRelationService.getColorByEqptId(equipmentEntity.getId());
                    if (color != null) {
                        equipmentEntity.setColor(color);
                    }
                    Map<String, PropertyEntity> props =
                            propertyRelationService.getPropsByEqptId(equipmentEntity.getId());
                    if (props != null) {
                        equipmentEntity.setProps(props);
                    }
                    StringBuffer title = new StringBuffer();
                    if (props.get("SERIES") != null) {
                        title.append(props.get("SERIES").getDesc());
                    }
                    title.append(" ");
                    if (props.get("STYLE") != null) {
                        title.append(props.get("STYLE").getDesc());
                    }
                    title.append(" ");
                    if (props.get("TYPE") != null) {
                        title.append(props.get("TYPE").getDesc());
                    }
                    equipmentEntity.setTitle(title.toString());
                    String instroductionStrings = equipmentEntity.getIntroduction_strings();
                    if (instroductionStrings != null) {
                        List<String> introductions = Arrays.asList(instroductionStrings.split(","));
                        equipmentEntity.setIntroductions(introductions);
                    }
                    List<ProductPriceEntity> wholePrices
                            = productPriceService.selectList(new EntityWrapper<ProductPriceEntity>().eq("equipment_id", equipmentEntity.getId()));
                    if (wholePrices != null) {
                        equipmentEntity.setWholePrices(wholePrices);
                    }
                    Map<String, Double> prices = new HashMap<>();
                    List<PlayerPriceEntity> playerPriceEntities
                            = playerPriceService.getAllByEquipmentId(equipmentEntity.getId());
                    if (playerPriceEntities != null) {
                        for (PlayerPriceEntity playerPriceEntity : playerPriceEntities) {
                            prices.put(playerPriceEntity.getAgent_level(), playerPriceEntity.getAgent_price());
                        }
                        equipmentEntity.setPrices(prices);
                    }
                }
                return equipmentEntities;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<EquipmentEntity> getByArticleNumber(String articleNumber) {
        return baseMapper.queryByarticleNumber(articleNumber);
    }

    @Override
    public List<EquipmentEntity> queryList(ProductForm productForm, String user_level) {
        productForm.setOffset((productForm.getPageIndex() - 1) * productForm.getPageSize());  //设置页码，设Offset的值
        List<String> articleNumberList = productForm.getArticleNumbers();
        List<String> searchKeysList = productForm.getSearch_keys();
        List<EquipmentEntity> equipmentEntities = new ArrayList<EquipmentEntity>();
        //校验productForm的属性articleNumbers和search_keys为空的时候
//        if (articleNumberList == null && searchKeysList == null) {
//            //调用dao的queryByPage方法来分页查询出列表，注意这里的方法用到了去重，是为了列表只显示一张图片，equipment是衣服图片
//            equipmentEntities = baseMapper.queryByPage(productForm);
//        } else {
        //调用dao的queryList方法来查询出列表，注意这里的方法用到了去重，是为了列表只显示一张图片，equipment是衣服图片
        equipmentEntities = baseMapper.queryList(productForm);
//        }
        List<EquipmentEntity> equipmentEntityLinkedList = new LinkedList<EquipmentEntity>();

        //如果有传articleNumber参数过来，则将查询结果进行重新排序
        if (articleNumberList != null && !articleNumberList.isEmpty()) {
            articleNumberList = ListUtil.removeDuplicateWithOrder(articleNumberList);
            for (int i = 0; i < articleNumberList.size(); i++) {
                String articleNumber = articleNumberList.get(i);
                for (EquipmentEntity equipmentEntity : equipmentEntities) {
                    if (equipmentEntity.getArticleNumber().equalsIgnoreCase(articleNumber)) {
                        equipmentEntityLinkedList.add(equipmentEntity);
                    }
                }
            }

            //排序完分页
            equipmentEntityLinkedList = this.fenye(productForm.getPageIndex(), productForm.getPageSize(), equipmentEntityLinkedList);
        } else if (searchKeysList != null && !searchKeysList.isEmpty()) {
//            searchKeysList = ListUtil.removeDuplicateWithOrder(searchKeysList);
            for (int i = 0; i < searchKeysList.size(); i++) {
                String searchkey = searchKeysList.get(i);
                for (EquipmentEntity equipmentEntity : equipmentEntities) {
                    if (equipmentEntity.getSearch_key().toLowerCase().contains(searchkey.toLowerCase())) {
                        equipmentEntityLinkedList.add(equipmentEntity);
                    }
                }
            }
            //排序完分页且去除重复
            if (null != equipmentEntityLinkedList && !equipmentEntityLinkedList.isEmpty()) {
                log.warn("搜索排序之后商品数量为: " + equipmentEntityLinkedList.size());
                equipmentEntityLinkedList = ListUtil.removeDuplicateWithOrder(equipmentEntityLinkedList);
                log.warn("去重之后商品数量为: " + equipmentEntityLinkedList.size());
                equipmentEntityLinkedList = this.fenye(productForm.getPageIndex(), productForm.getPageSize(), equipmentEntityLinkedList);
                log.warn("分页之后商品数量为: " + equipmentEntityLinkedList.size());
            } else {
                log.warn("搜索排序之后商品数量为0");
            }
//            if (null!=equipmentEntityLinkedList&&!equipmentEntityLinkedList.isEmpty()) {
//                equipmentEntityLinkedList = ListUtil.removeDuplicateWithOrder(equipmentEntityLinkedList);
//            }
        }
        //否则equipmentEntityLinkedList即为equipmentEntities;
        else {
            equipmentEntityLinkedList = equipmentEntities;
        }
        if (equipmentEntityLinkedList != null) {
            for (EquipmentEntity equipmentEntity : equipmentEntityLinkedList) {
                Map<String, PropertyEntity> propertyEntityMap = null;
                List<String> color = new ArrayList<>();
                if (null != equipmentEntity.getIds()) {
                    propertyEntityMap = propertyRelationService.selectByEqptIds(equipmentEntity.getIds());
                    color = propertyRelationService.selectColorsByEqptIds(equipmentEntity.getIds());
                }
                if (propertyEntityMap != null) {
                    equipmentEntity.setProps(propertyEntityMap);
                }
                equipmentEntity.setColor(color);
                //增加title
                StringBuffer sb = new StringBuffer();
                if (null != propertyEntityMap.get("SERIES")) {
                    sb.append(propertyEntityMap.get("SERIES").getDesc() + " ");
                }
                if (null != propertyEntityMap.get("STYLE")) {
                    sb.append(propertyEntityMap.get("STYLE").getDesc() + " ");
                }
                if (null != propertyEntityMap.get("TYPE")) {
                    sb.append(propertyEntityMap.get("TYPE").getDesc());
                }
                equipmentEntity.setTitle(sb.toString());
//                String img_strings = equipmentEntity.getImg_strings();
                String instroduction_strings = equipmentEntity.getIntroduction_strings();
//                if (null != img_strings) {
////                    List<Map<String,String>> imgs = Arrays.asList(img_strings.split(","));
//                    List<EquipmentImgEntity> imgs = (List<EquipmentImgEntity>) JSONArray.parseArray(img_strings, EquipmentImgEntity.class);
//                    equipmentEntity.setImgs(imgs);
//                }
                List<EquipmentImgEntity> imgs = equipmentImgService.selectList(new EntityWrapper<EquipmentImgEntity>().eq("equipment_id", equipmentEntity.getId()));
                equipmentEntity.setImgs(imgs);
                if (null != instroduction_strings) {
                    List<String> instroductions = Arrays.asList(instroduction_strings.split(","));
                    equipmentEntity.setIntroductions(instroductions);
                }
                //PlayerPriceEntity playerPriceEntity =null;
                Map<String, Double> prices = new HashMap<String, Double>();
                long equipment_id = equipmentEntity.getId();
                try {
                    List<ProductPriceEntity> productPriceEntities = productPriceService.selectList(new EntityWrapper<ProductPriceEntity>().eq("equipment_id", equipment_id));
                    if (null != productPriceEntities && !productPriceEntities.isEmpty()) {
                        equipmentEntity.setWholePrices(productPriceEntities);
                    }
//                    if (null==user_level||user_level.equalsIgnoreCase("BUYER")){
//                        playerPriceEntity = playerPriceService.getByEquipmentIdAndUserLevel(equipment_id,"BUYER");
//                    }else {
//                        playerPriceEntity = playerPriceService.getByEquipmentIdAndUserLevel(equipmentEntity.getId(),user_level);
//                    }
//                    //匹配对应价格
//                    if (null!=playerPriceEntity){
//                        prices.put(playerPriceEntity.getAgent_level(),playerPriceEntity.getAgent_price());
//                    }
//                    //匹配吊牌价
//                    if (null!=playerPriceService.getStandardByEquipmentId(equipmentEntity.getId())) {
//                        PlayerPriceEntity standardPlayerPrice = playerPriceService.getStandardByEquipmentId(equipmentEntity.getId());
//                        prices.put(standardPlayerPrice.getAgent_level(),standardPlayerPrice.getAgent_price());
//                    }
                    List<PlayerPriceEntity> playerPriceEntityList = playerPriceService.getAllByEquipmentId(equipment_id);
                    if (!playerPriceEntityList.isEmpty() && playerPriceEntityList.size() > 0) {
                        for (PlayerPriceEntity playerPriceEntity : playerPriceEntityList) {
                            prices.put(playerPriceEntity.getAgent_level(), playerPriceEntity.getAgent_price());
                        }
                        equipmentEntity.setPrices(prices);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("首页商品列表接口匹配价格异常：" + e.getMessage());
                }

            }
            return equipmentEntityLinkedList;
        }
        return null;
    }

    @Override
    public Integer queryListTotal(ProductForm productForm) {
        return baseMapper.queryListTotal(productForm);
    }

    @Override
    public List<EquipmentEntity> queryByarticleNumber(String articleNumber) {
        List<EquipmentEntity> equipmentEntities = baseMapper.queryByarticleNumber(articleNumber);//查出了7个装备
        if (equipmentEntities != null) {
            for (EquipmentEntity equipmentEntity : equipmentEntities) {
                //遍历这7个装备,查出每个装备有9个商品,63个
                List<ProductEntity> productEntities = productService.getProductByEqptId(equipmentEntity.getId());
                if (productEntities != null) {
                    equipmentEntity.setProudcts(productEntities);  //设proudcts值
                }
                Map<String, PropertyEntity> propertyEntityMap = null;
                List<String> color = new ArrayList<>();
                String ids = String.valueOf(equipmentEntity.getId());
                if (null != ids) {
                    propertyEntityMap = propertyRelationService.selectByEqptIds(ids);
                    color = propertyRelationService.selectColorsByEqptIds(ids);
                }
                if (propertyEntityMap != null) {
                    equipmentEntity.setProps(propertyEntityMap);   //设props值
                }
                //增加color
//                List<String> color = new LinkedList<String>();
//                color.add(propertyEntityMap.get("MAINCOLOR").getValue());
//                color.add(propertyEntityMap.get("SECONDARYCOLOR").getValue());
                equipmentEntity.setColor(color);    //设color值
                //增加title
                StringBuffer sb = new StringBuffer();
                if (null != propertyEntityMap.get("SERIES")) {
                    sb.append(propertyEntityMap.get("SERIES").getDesc() + " ");
                }
                if (null != propertyEntityMap.get("STYLE")) {
                    sb.append(propertyEntityMap.get("STYLE").getDesc() + " ");
                }
                if (null != propertyEntityMap.get("TYPE")) {
                    sb.append(propertyEntityMap.get("TYPE").getDesc());
                }
                //sb.append(propertyEntityMap.get("SPORT").getDesc()+",").append(propertyEntityMap.get("SERIES").getDesc()+",").append(propertyEntityMap.get("TYPE").getDesc());
                equipmentEntity.setTitle(sb.toString());   //设title值
//                String img_strings = equipmentEntity.getImg_strings();
//                if (null != img_strings) {
////                    List<String> imgs = Arrays.asList(img_strings.split(","));
//                    List<EquipmentImgEntity> imgs = (List<EquipmentImgEntity>) JSONArray.parseArray(img_strings, EquipmentImgEntity.class);
//                    equipmentEntity.setImgs(imgs);
//                }
                List<EquipmentImgEntity> imgs = equipmentImgService.selectList(new EntityWrapper<EquipmentImgEntity>().eq("equipment_id", equipmentEntity.getId()));
                equipmentEntity.setImgs(imgs);   //设imgs值
                String introductions_strings = equipmentEntity.getIntroduction_strings();
                if (null != introductions_strings) {
                    List<String> introductions = Arrays.asList(introductions_strings.split(","));
                    equipmentEntity.setIntroductions(introductions);  //设introductions值
                }
                //匹配价格
                Map<String, Double> prices = new HashMap<String, Double>();
                long equipment_id = equipmentEntity.getId();
                //PlayerPriceEntity playerPriceEntity =null;
                try {
                    //设置新价格结构
                    List<ProductPriceEntity> productPriceEntities = productPriceService.selectList(new EntityWrapper<ProductPriceEntity>().eq("equipment_id", equipment_id));
                    if (null != productPriceEntities && !productPriceEntities.isEmpty()) {
                        equipmentEntity.setWholePrices(productPriceEntities);   //设WholePrices值
                    }
                    List<PlayerPriceEntity> playerPriceEntityList = playerPriceService.getAllByEquipmentId(equipment_id);
                    if (!playerPriceEntityList.isEmpty() && playerPriceEntityList.size() > 0) {
                        for (PlayerPriceEntity playerPriceEntity : playerPriceEntityList) {
                            prices.put(playerPriceEntity.getAgent_level(), playerPriceEntity.getAgent_price());
                        }
                        equipmentEntity.setPrices(prices);   //设prices值
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("首页商品详情接口匹配价格异常：" + e.getMessage());
                }
            }

            return equipmentEntities;
        }
        return null;
    }

    @Override
    public List<EquipmentEntity> maoQueryByarticleNumber(String articleNumber) {
        List<EquipmentEntity> equipmentEntities =
                equipmentService.getByArticleNumber(articleNumber);
        if (equipmentEntities != null) {
            for (EquipmentEntity equipmentEntity : equipmentEntities) {
                Map<String, PropertyEntity> props = new HashMap<>();
                List<ProductEntity> productEntities = productService.maoGetProductByEqptId(equipmentEntity.getId());
                if (productEntities != null) {
                    for (ProductEntity productEntity : productEntities) {
                        List<PropertyRelationEntity> propertyRelationEntities = propertyRelationService.selectList(new EntityWrapper<PropertyRelationEntity>().eq("product_id", productEntity.getId()));
                        if (propertyRelationEntities != null) {
                            for (PropertyRelationEntity propertyRelationEntity : propertyRelationEntities) {
                                List<PropertyEntity> propertyEntities = propertyService.getByPropertyId(propertyRelationEntity.getPropertyId());
                                if (propertyEntities != null) {
                                    String propertyName = propertyRelationEntity.getPropertyName();
                                    for (PropertyEntity propertyEntity : propertyEntities) {
                                        props.put(propertyName, propertyEntity);
                                        productEntity.setProps(props);
                                    }
                                }
                            }
                        }
                    }
                }
                equipmentEntity.setProudcts(productEntities);
                Map<String, PropertyEntity> propertyEntityMap =
                        propertyRelationService.getPropsByEqptId(equipmentEntity.getId());
                if (propertyEntityMap != null) {
                    equipmentEntity.setProps(propertyEntityMap);
                }
                List<String> color = propertyRelationService.getColorByEqptId(equipmentEntity.getId());
                if (color != null) {
                    equipmentEntity.setColor(color);
                }
                StringBuffer title = new StringBuffer();
                if (propertyEntityMap.get("SERIES") != null) {
                    title.append(propertyEntityMap.get("SERIES").getDesc());
                }
                title.append(" ");
                if (propertyEntityMap.get("STYLE") != null) {
                    title.append(propertyEntityMap.get("STYLE").getDesc());
                }
                title.append(" ");
                if (propertyEntityMap.get("TYPE") != null) {
                    title.append(propertyEntityMap.get("TYPE").getDesc());
                }
                equipmentEntity.setTitle(title.toString());
                List<EquipmentImgEntity> imgs =
                        equipmentImgService.selectList(new EntityWrapper<EquipmentImgEntity>().eq("equipment_id", equipmentEntity.getId()));
                equipmentEntity.setImgs(imgs);
                String intro = equipmentEntity.getIntroduction_strings();
                if (!StringTools.isNullOrEmpty(intro)) {
                    List<String> introductions = Arrays.asList(intro.split(","));
                    equipmentEntity.setIntroductions(introductions);
                }
                List<ProductPriceEntity> wholePrices =
                        productPriceService.selectList(new EntityWrapper<ProductPriceEntity>().eq("equipment_id", equipmentEntity.getId()));
                if (wholePrices != null) {
                    equipmentEntity.setWholePrices(wholePrices);
                }
                Map<String, Double> prices = new HashMap<>();
                List<PlayerPriceEntity> playerPriceEntities =
                        playerPriceService.getAllByEquipmentId(equipmentEntity.getId());
                if (playerPriceEntities != null && !playerPriceEntities.isEmpty()) {
                    for (PlayerPriceEntity playerPriceEntity : playerPriceEntities) {
                        prices.put(playerPriceEntity.getAgent_level(), playerPriceEntity.getAgent_price());
                    }
                    equipmentEntity.setPrices(prices);
                }
            }
        }
        return equipmentEntities;
    }

    @Override
    public EquipmentEntity queryById(Long id) {

        EquipmentEntity equipmentEntity = baseMapper.queryById(id);
        Map<String, PropertyEntity> propertyEntityMap = propertyRelationService.selectByEqptId(equipmentEntity.getId());

        if (propertyEntityMap != null) {
            equipmentEntity.setProps(propertyEntityMap);
        }
        //增加color
        List<String> color = new LinkedList<String>();
        color.add(propertyEntityMap.get("MAINCOLOR").getValue());
        color.add(propertyEntityMap.get("SECONDARYCOLOR").getValue());
        equipmentEntity.setColor(color);
        //增加title
        StringBuffer sb = new StringBuffer();
        sb.append(propertyEntityMap.get("SPORT").getDesc() + ",").append(propertyEntityMap.get("SERIES").getDesc() + ",").append(propertyEntityMap.get("TYPE").getDesc());
        equipmentEntity.setTitle(sb.toString());
        return equipmentEntity;
    }

    @Override
    public EquipmentEntity queryAllById(Long id) {

        EquipmentEntity equipmentEntity = baseMapper.queryAllById(id);
        Map<String, PropertyEntity> propertyEntityMap = propertyRelationService.selectByEqptId(equipmentEntity.getId());

        if (propertyEntityMap != null) {
            equipmentEntity.setProps(propertyEntityMap);
        }
        //增加color
        List<String> color = new LinkedList<String>();
        if (propertyEntityMap.get("MAINCOLOR") != null) {
            color.add(propertyEntityMap.get("MAINCOLOR").getValue());
        }
        if (propertyEntityMap.get("SECONDARYCOLOR") != null) {
            color.add(propertyEntityMap.get("SECONDARYCOLOR").getValue());
        }

        equipmentEntity.setColor(color);
        //增加title
        StringBuffer sb = new StringBuffer();
        sb.append(propertyEntityMap.get("SPORT").getDesc() + ",").append(propertyEntityMap.get("SERIES").getDesc() + ",").append(propertyEntityMap.get("TYPE").getDesc());
        equipmentEntity.setTitle(sb.toString());
        return equipmentEntity;
    }

    @Override
    @Transactional
    public BaseResp updateStateByState(Long id, String state) {
        if (null == baseMapper.queryAllById(id)) {
            return BaseResp.error("找不到equipmentId对应的记录");
        }
        int result = baseMapper.updateStateByState(id, state);
        if (1 != result) {
            throw new RuntimeException();
        }
        return BaseResp.ok();
    }

    public List<EquipmentEntity> fenye(Integer currentPage, Integer pageSize, List<EquipmentEntity> list) {
        com.cikers.wechat.mall.modules.app.utils.Page page = new com.cikers.wechat.mall.modules.app.utils.Page();
        //log.warn("分页前list数据量" + list.size());
        try {
            //刚开始的页面为第一页
            if (currentPage == null) {
                page.setCurrentPage(1);
            } else {
                page.setCurrentPage(currentPage);
            }
            //如果没有pageSize，默认每页数据为十条
            if (pageSize == null || pageSize == 0) {
                page.setPageSize(10);
            } else {
                page.setPageSize(pageSize);
            }
            if (pageSize >= list.size()) {
                pageSize = list.size();
            }
            //每页的开始数
            page.setStar((page.getCurrentPage() - 1) * page.getPageSize());
            //list的大小
            int count = list.size();
            //设置总页数
            page.setTotalPage(count % 10 == 0 ? count / 10 : count / 10 + 1);
            //对list进行截取
            List<EquipmentEntity> newlist = new ArrayList<>();
            int fromindex = page.getStar();
            int toindex = page.getStar() + pageSize;
            if (toindex > count) {
                toindex = count;
            }
            newlist = list.subList(fromindex, toindex);
            //log.warn("分页后list数据量" + list.size());
            return newlist;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
