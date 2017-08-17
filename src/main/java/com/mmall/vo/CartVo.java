package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11/011.
 */
public class CartVo {

    private List<CartProductVo> cartProductVoList ;
    private BigDecimal cartTotalPrice;
    private Boolean allCheckd ; //是否已经都勾选
    private String imageHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllCheckd() {
        return allCheckd;
    }

    public void setAllCheckd(Boolean allCheckd) {
        this.allCheckd = allCheckd;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
