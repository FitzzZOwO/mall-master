package com.newbee.mall.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 分类级别
 *
 * @author mhx
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MallCategoryLevelEnum {

    DEFAULT(0, "ERROR"),
    LEVEL_ONE(1, "一级分类"),
    LEVEL_TWO(2, "二级分类"),
    LEVEL_THREE(3, "三级分类");

    private int level;

    private String name;

    public static MallCategoryLevelEnum getNewBeeMallOrderStatusEnumByLevel(int level) {
        for (MallCategoryLevelEnum mallCategoryLevelEnum : MallCategoryLevelEnum.values()) {
            if (mallCategoryLevelEnum.getLevel() == level) {
                return mallCategoryLevelEnum;
            }
        }
        return DEFAULT;
    }

}
