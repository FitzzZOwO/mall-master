package com.newbee.mall.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 首页分类数据VO(第三级)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdLevelCategoryVO implements Serializable {

    private Long categoryId;

    private Byte categoryLevel;

    private String categoryName;


}
