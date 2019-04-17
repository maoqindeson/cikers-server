package com.cikers.wechat.mall.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by WeiPing He on 2018/6/10 14:22.
 * Email: weiping_he@hansight.com
 */
@Data
@TableName("tb_player_price")
public class PlayerPriceEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;

    private Long equipment_id;

    private String agent_level;
    private Double agent_price;
    private Date created_date;
    private Date updated_date;

    public PlayerPriceEntity(long equipment_id,String agent_level,Double agent_price){
        super();
        this.equipment_id=equipment_id;
        this.agent_level=agent_level;
        this.agent_price=agent_price;
        this.created_date=new Date();
    }
    public PlayerPriceEntity(){
    }
}
