package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Administrator on 2017/7/8/008.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";
    public static final String CURRENT_COOKIES = "currentCookies";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface RedisCacheExTime{
        int REDIS_SESSION_EXTIME = 60*30;//30分钟
    }

    //商品排序
    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    //用户权限
    public interface Role{
        int ROLE_CUSTOMER = 0;  //普通用户
        int ROLE_ADMIN = 1; //管理员
    }

    //商品销售状态
    public enum ProductStatusEnum{
        ON_SALE(1,"在线");
        private String value;
        private int code;
        ProductStatusEnum(int code,String value){
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }
    }

    //购物车中商品的状态
    public interface Cart{
        int CHECKD = 1; //被选中
        int UN_CHECKD = 0;  //未选中

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    //订单状态
    public  enum orderStatusEnum{
        CANCELED(0,"已取消"),
        NOPAY(10,"未支付"),
        PAID(20,"已支付"),
        SHIPPID(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_FINISHED(60,"订单关闭");
        private String value;
        private int code;

        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }
        orderStatusEnum(int code,String value) {
            this.value = value;
            this.code = code;
        }

        public static orderStatusEnum codeof(int code){
            for(orderStatusEnum orderStatus :values()){
                if(orderStatus.getCode() == code){
                    return orderStatus ;
                }
            }
            throw new RuntimeException("没有找到对应的类型");
        }
    }

    //支付宝回调函数的状态
    public interface AlipayCallBack{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";

    }

    //支付平台
    public enum payPlatformEnum{
        ALIPAY(1,"支付宝");


        private String value;
        private int code;

        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }
        payPlatformEnum(int code,String value) {
            this.value = value;
            this.code = code;
        }
    }

    //支付方式，目前只有在线支付
    public enum PaymentTypeEnum{
        ONLINE_PAY(1,"在线支付");

        private String value;
        private int code;

        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }
        PaymentTypeEnum(int code,String value) {
            this.value = value;
            this.code = code;
        }

        public static PaymentTypeEnum codeof(int code){
            for(PaymentTypeEnum paymentTypeEnum :values()){
                if(paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的类型");
        }
    }


}
