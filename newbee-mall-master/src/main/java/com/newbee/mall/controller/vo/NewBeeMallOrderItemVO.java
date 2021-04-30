package com.newbee.mall.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单详情页页面订单项VO
 *
 * @author mhx
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewBeeMallOrderItemVO implements Serializable {

    private Long goodsId;

    private Integer goodsCount;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;


}
