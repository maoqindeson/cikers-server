package com.cikers.wechat.mall.modules.app.form;

import lombok.Data;

import java.util.List;


@Data
public class ProductForm {
    private String keyword;
    private String type;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer offset;
    private List<String> articleNumbers;
    private List<String> search_keys;
}