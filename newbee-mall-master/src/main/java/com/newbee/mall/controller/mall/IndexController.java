package com.newbee.mall.controller.mall;

import com.newbee.mall.common.Constants;
import com.newbee.mall.common.IndexConfigTypeEnum;
import com.newbee.mall.controller.vo.NewBeeMallIndexCarouselVO;
import com.newbee.mall.controller.vo.NewBeeMallIndexCategoryVO;
import com.newbee.mall.controller.vo.NewBeeMallIndexConfigGoodsVO;
import com.newbee.mall.service.NewBeeMallCarouselService;
import com.newbee.mall.service.NewBeeMallCategoryService;
import com.newbee.mall.service.NewBeeMallIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private NewBeeMallCarouselService newBeeMallCarouselService;

    @Resource
    private NewBeeMallIndexConfigService newBeeMallIndexConfigService;

    @Resource
    private NewBeeMallCategoryService newBeeMallCategoryService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<NewBeeMallIndexCategoryVO> categories = newBeeMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }
        List<NewBeeMallIndexCarouselVO> carousels = newBeeMallCarouselService.getCarouselsForIndex(Constants.HOME_BANNER_AMOUNT);
        List<NewBeeMallIndexConfigGoodsVO> hotGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.HOME_GOODS_HOT_AMOUNT);
        List<NewBeeMallIndexConfigGoodsVO> newGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.HOME_GOODS_NEW_AMOUNT);
        List<NewBeeMallIndexConfigGoodsVO> recommendGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.HOME_GOODS_RECOMMENDED_AMOUNT);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//banner图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
        return "mall/index";
    }
}
