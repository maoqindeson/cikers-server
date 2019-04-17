/**
 * PropertyEntity.java Create on 2018年5月9日
 * www.cikers.com.
 */

package com.cikers.wechat.mall.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @author WangXP
 * @version 1.0
 * @date 2018年5月9日
 */
@Data
@TableName("tb_property")
public class PropertyEntity {
    /**
     * ID
     */
    @TableId
    private Long id;
    private String value;
    /**
     * 属性说明
     */
    private String desc;

    public PropertyEntity() {
    }

    public PropertyEntity(PropertyEntity prop) {
        super();
        this.id = prop.getId();
        this.value = prop.getValue();
        this.desc = prop.getDesc();
    }

    public PropertyEntity(Long id, String value, String desc) {
        super();
        this.id = id;
        this.value = value;
        this.desc = desc;
    }
}
