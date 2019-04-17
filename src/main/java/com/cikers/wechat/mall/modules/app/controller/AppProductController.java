package com.cikers.wechat.mall.modules.app.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cikers.wechat.mall.modules.app.entity.*;
import com.cikers.wechat.mall.modules.app.form.ProductForm;
import com.cikers.wechat.mall.modules.app.service.*;
import com.cikers.wechat.mall.modules.app.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/wechat/products")
public class AppProductController {


    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private EscapeService escapeService;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private PropertyRelationService propertyRelationService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private EquipmentImgService equipmentImgService;
    @Autowired
    private ProductPriceService productPriceService;
    @Autowired
    private PlayerPriceService playerPriceService;

    @PostMapping("/maoQueryList")
    @ResponseBody
    public BaseResp maoQueryList(@RequestBody ProductForm productForm, HttpServletRequest request) {
        if (productForm.getPageIndex() == null) {
            productForm.setPageIndex(1);
        }
        if (productForm.getPageSize() == null) {
            productForm.setPageSize(10);
        }
        Integer offset = (productForm.getPageIndex() - 1) * productForm.getPageSize();
        if (offset < 0) {
            return BaseResp.error("页码不正确，必须大于0的整数");
        }
        List<String> articleNumbers = productForm.getArticleNumbers();
        for (String articleNumber : articleNumbers) {
            Integer total = equipmentService.maoQueryListTotal(articleNumber);
            if (offset > total) {
                return BaseResp.error("超出列表范围");
            }
        }
        productForm.setOffset(offset);
        if (productForm.getKeyword() != null) {
            List<String> search_keys = Arrays.asList(productForm.getKeyword().split(","));
            List<String> newsearch_key = new ArrayList<>();
            for (String search_key : search_keys) {
                String newsearchKey = escapeService.getSearchKeyByKeyword(search_key);
                if (!StringTools.isNullOrEmpty(newsearchKey)) {
                    newsearch_key.add(newsearchKey);
                }
            }

            if (newsearch_key != null && newsearch_key.isEmpty()) {
                productForm.setSearch_keys(newsearch_key);
            } else {
                productForm.setSearch_keys(search_keys);
            }
        }
            List<EquipmentEntity> equipmentEntities = equipmentService.maoQueryList(productForm);
            if (productForm.getArticleNumbers() == null && equipmentEntities != null && !equipmentEntities.isEmpty()) {
                List<String> arts = screenService.getArticleNumbers();
                if (arts != null && arts.isEmpty()) {
                    Iterator<EquipmentEntity> iterator = equipmentEntities.iterator();
                    while (iterator.hasNext()) {
                        if (arts.contains(iterator.next().getArticleNumber())) {
                            iterator.remove();
                        }
                    }
                }
            }
            return BaseResp.ok(equipmentEntities);
        }




        @PostMapping("/list")
        @ResponseBody
        public BaseResp list (@RequestBody ProductForm productForm, HttpServletRequest request){
            //获得token,校验token正确性
//        if (null == request.getHeader("token") || null == JWTUtil.getCurrentUserOpenId(request)) {
//            log.warn("首页商品列表接口token校验失败，缺少token参数");
//            return BaseResp.error(-3, "token invalid.");
//        }
            //入参对象articleNumbers属性校验，articleNumbers数据类型是list
            if (null != productForm.getArticleNumbers() && productForm.getArticleNumbers().isEmpty()) {
                productForm.setArticleNumbers(null);
                log.error("articleNumbers为空,可能存在导航数据问题");
            }

            if (productForm.getPageIndex() == null) {        //入参对象pageIndex属性校验
                productForm.setPageIndex(1);
            }
            if (productForm.getPageSize() == null) {         //入参对象pageSize属性校验
                productForm.setPageSize(10);
            }

            try {
                if (!StringUtils.isBlank(productForm.getKeyword())) {
                    String search_key = productForm.getKeyword();
                    List<String> search_keys = Arrays.asList(search_key.split(" "));     //字符串通过split拆解后通过Arrays.asList方法转换成list
                    List<String> newsearchkeylist = new ArrayList<>();
                    for (String key : search_keys) {
                        //开始进行搜索关键字转换
                        if (null != escapeService.getSearchKeyByKeyword(key)) {
                            String searchkey = escapeService.getSearchKeyByKeyword(key);
                            log.warn("将原搜索关键字: " + key + "转换为: " + searchkey);
                            List<String> searchkeylist = Arrays.asList(searchkey.split(" "));
                            for (String newsearchkey : searchkeylist) {
                                newsearchkeylist.add(newsearchkey);
                            }
                        }
                    }
                    if (newsearchkeylist.isEmpty() || newsearchkeylist.size() < 1) {
                        productForm.setSearch_keys(search_keys);
                    } else {
                        productForm.setSearch_keys(newsearchkeylist);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("首页商品列表接口转换搜索关键字异常，异常信息为： " + e.getMessage());
            } finally {

                String open_id = JWTUtil.getCurrentUserOpenId(request);
                String userinfo = "  --用户open_id: " + open_id + "--  ";
                if (!StringUtils.isBlank(productForm.getKeyword())) {
                    log.warn(userinfo + "查询关键字为: " + productForm.getSearch_keys());

                }
                log.warn(userinfo + "首页商品列表接口入参：" + productForm.toString());
                String user_level = null;
                Integer total = 0;
                if (null != equipmentService.queryListTotal(productForm)) {
                    total = equipmentService.queryListTotal(productForm);
                }
                //防止前端传过来的页码超出索引
                if ((productForm.getPageIndex() - 1) * productForm.getPageSize() >= total) {
                    BaseResp baseResp = BaseResp.ok("已经没有数据了");
                    baseResp.setTotal(total);
                    baseResp.setData(new ArrayList<>());
                    return baseResp;
                }
                List<EquipmentEntity> equipmentEntityList = equipmentService.queryList(productForm, user_level);
                //如果不是导航数据过来的则需要屏蔽搜索货号
                if (null == productForm.getArticleNumbers() && equipmentEntityList != null && !equipmentEntityList.isEmpty()) {
                    //筛除屏蔽货号
                    List<String> articleNumbers = screenService.getArticleNumbers();
                    if (null != articleNumbers && !articleNumbers.isEmpty()) {
                        Iterator<EquipmentEntity> it = equipmentEntityList.iterator();
                        while (it.hasNext()) {
                            if (articleNumbers.contains(it.next().getArticleNumber())) {
                                it.remove();
                            }
                        }
                    }
                }
                BaseResp baseResp = BaseResp.ok(equipmentEntityList);
                baseResp.setTotal(total);
                return baseResp;
            }
        }


        @PostMapping("/detail")
        public BaseResp detail (HttpServletRequest request, String articleNumber){
            if (articleNumber == null || articleNumber == "") {   //校验入参是否为null或是空
                return BaseResp.error(98, "articleNumber参数异常");
            }
            //获得token,校验token正确性
//        if (null == request.getHeader("token") || null == JWTUtil.getCurrentUserOpenId(request)) {
//            log.warn("商品详情接口token校验失败，token为空");
//            return BaseResp.error(-3, "token invalid.");
//        }
            try {
                List<EquipmentEntity> list = equipmentService.queryByarticleNumber(articleNumber);   //重点看queryByarticleNumber方法
                if (list == null || list.isEmpty()) {           //校验列表为null或空的表达式，用||
                    return BaseResp.error("产品为空");
                }
                return BaseResp.ok(list);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("商品详情页异常，异常信息为：" + e.getMessage());
            }
            return BaseResp.error("没有加载成功，请重试");
        }

        @PostMapping("/maodetail")
        public BaseResp maodetail (String articleNumber){
            if (articleNumber == null || articleNumber.isEmpty()) {
                return BaseResp.error(500, "articleNumber参数异常");
            }
            try {
                List<EquipmentEntity> equipmentEntities = equipmentService.maoQueryByarticleNumber(articleNumber);
                if (equipmentEntities == null || equipmentEntities.isEmpty()) {
                    return BaseResp.error("产品为空");
                }
                return BaseResp.ok(equipmentEntities);
                //以下代码是先在这个接口里写完，测试没问题后，然后用一个在EquipmentServiceImpl里的maoQueryByarticleNumber()来实现
                //其中maoQueryByarticleNumber()中equipmentEntity.setProudcts(productEntities)的实现是直接在EquipmentServiceImpl的方法中的返回值是baseMapper调用dao的方法来得到的
                //其他的设值都是在EquipmentServiceImpl重写了dao的方法。
//            List<EquipmentEntity> equipmentEntities =
//                    equipmentService.getByArticleNumber(articleNumber);
//            if (equipmentEntities == null || equipmentEntities.isEmpty()) {
//                return BaseResp.error(500, "装备为空");
//            }
//            for (EquipmentEntity equipmentEntity : equipmentEntities) {
//                Map<String, PropertyEntity> props = new HashMap<>();
//                List<ProductEntity> productEntities = productService.maoGetProductByEqptId(equipmentEntity.getId());
//                if (productEntities == null || productEntities.isEmpty()) {
//                    return BaseResp.error(500, "产品为空");
//                }
//                for (ProductEntity productEntity : productEntities) {
//                    List<PropertyRelationEntity> propertyRelationEntities = propertyRelationService.selectList(new EntityWrapper<PropertyRelationEntity>().eq("product_id", productEntity.getId()));
//                    if (propertyRelationEntities == null || propertyRelationEntities.isEmpty()) {
//                        return BaseResp.error(500, "属性关系为空");
//                    }
//                    for (PropertyRelationEntity propertyRelationEntity : propertyRelationEntities) {
//                        List<PropertyEntity> propertyEntities = propertyService.getByPropertyId(propertyRelationEntity.getPropertyId());
//                        if (propertyEntities == null || propertyEntities.isEmpty()) {
//                            return BaseResp.error(500, "属性为空");
//                        }
//                        String propertyName = propertyRelationEntity.getPropertyName();
//                        for (PropertyEntity propertyEntity : propertyEntities) {
//                            props.put(propertyName, propertyEntity);
//                            productEntity.setProps(props);
//                        }
//                    }
//                }
//                equipmentEntity.setProudcts(productEntities); //难点
//                Map<String, PropertyEntity> propertyEntityMap =
//                        propertyRelationService.getPropsByEqptId(equipmentEntity.getId());
//                if (propertyEntityMap == null || propertyEntityMap.isEmpty()) {
//                    return BaseResp.error("没有得到属性");
//                }
//                equipmentEntity.setProps(propertyEntityMap);
//                List<String> color = propertyRelationService.getColorByEqptId(equipmentEntity.getId());
//                if (color == null || color.isEmpty()) {
//                    return BaseResp.error("没有得到颜色");
//                }
//                equipmentEntity.setColor(color);
//                StringBuffer title = new StringBuffer();
//                if (propertyEntityMap.get("SERIES") != null) {
//                    title.append(propertyEntityMap.get("SERIES").getDesc());
//                }
//                title.append(" ");
//                if (propertyEntityMap.get("STYLE") != null) {
//                    title.append(propertyEntityMap.get("STYLE").getDesc());
//                }
//                title.append(" ");
//                if (propertyEntityMap.get("TYPE") != null) {
//                    title.append(propertyEntityMap.get("TYPE").getDesc());
//                }
//                equipmentEntity.setTitle(title.toString());
//                List<EquipmentImgEntity> imgs =
//                        equipmentImgService.selectList(new EntityWrapper<EquipmentImgEntity>().eq("equipment_id", equipmentEntity.getId()));
//                equipmentEntity.setImgs(imgs);
//                String intro= equipmentEntity.getIntroduction_strings();
//            if(StringTools.isNullOrEmpty(intro)){
//                return BaseResp.error("没有得到instroductions");
//            }
//            List<String> introductions=Arrays.asList(intro.split(","));
//            equipmentEntity.setIntroductions(introductions);
//                List<ProductPriceEntity> wholePrices =
//                        productPriceService.selectList(new EntityWrapper<ProductPriceEntity>().eq("equipment_id", equipmentEntity.getId()));
//                if (wholePrices == null || wholePrices.isEmpty()) {
//                    return BaseResp.error("没有得到全部价格");
//                }
//                equipmentEntity.setWholePrices(wholePrices);
//                Map<String, Double> prices = new HashMap<>();
//                List<PlayerPriceEntity> playerPriceEntities =
//                        playerPriceService.getAllByEquipmentId(equipmentEntity.getId());
//                if (playerPriceEntities!= null && !playerPriceEntities.isEmpty()) {
//                    for (PlayerPriceEntity playerPriceEntity : playerPriceEntities) {
//                        prices.put(playerPriceEntity.getAgent_level(), playerPriceEntity.getAgent_price());
//                    }
//                }
//                equipmentEntity.setPrices(prices);
//            }
//            return BaseResp.ok(equipmentEntities);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return BaseResp.error("没有加载成功，请重试");
        }
    }



