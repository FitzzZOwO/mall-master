package com.newbee.mall.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 订单状态:0.待支付 1.已支付 2.配货完成 3:出库成功 4.交易成功 -1.手动关闭 -2.超时关闭 -3.商家关闭
 *
 * @author mhx
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MallOrderStatusEnum {
    DEFAULT(-9, "ERROR"),
    PRE_PAY(0, "待支付"),
    PAID(1, "已支付"), packageD(2, "配货完成"),
    EXPRESS(3, "出库成功"),
    SUCCESS(4, "交易成功"),
    CLOSED_BY_MANUAL(-1, "手动关闭"),
    CLOSED_BY_EXPIRED(-2, "超时关闭"),
    CLOSED_BY_JUDGE(-3, "商家关闭");

    private int orderStatus;

    private String name;

    public static MallOrderStatusEnum getNewBeeMallOrderStatusEnumByStatus(int orderStatus) {
        for (MallOrderStatusEnum mallOrderStatusEnum : MallOrderStatusEnum.values()) {
            if (mallOrderStatusEnum.getOrderStatus() == orderStatus) {
                return mallOrderStatusEnum;
            }
        }
        return DEFAULT;
    }
}
