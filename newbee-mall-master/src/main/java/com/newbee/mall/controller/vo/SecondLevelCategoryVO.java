package com.newbee.mall.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 首页分类数据VO(第二级)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecondLevelCategoryVO implements Serializable {

    private Long categoryId;

    private Long parentId;

    private Byte categoryLevel;

    private String categoryName;

    private List<ThirdLevelCategoryVO> thirdLevelCategoryVOS;


}
