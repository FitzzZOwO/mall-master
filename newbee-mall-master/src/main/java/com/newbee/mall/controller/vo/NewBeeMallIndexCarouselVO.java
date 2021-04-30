package com.newbee.mall.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 首页banner图VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewBeeMallIndexCarouselVO implements Serializable {

    private String carouselUrl;

    private String redirectUrl;


}
