package com.mmall.vo;

/**
 * Created by Administrator on 2017/7/11/011.
 */

import java.math.BigDecimal;

/**
 * 结合了商品和购物车的一个抽象对象
 */
public class CartProductVo {

    private Integer id; //购物车的ID
    private Integer userId;
    private Integer productId;
    private Integer quantity; // 购物车中此商品的数量
    private String productName;
    private String productSubTitle;
    private String productMainImage;
    private BigDecimal productPrice;
    private Integer productstatus;
    private BigDecimal productTotalPrice;
    private Integer productStock;
    private Integer productCheckd;  //此商品在购物车中是否被勾选

    private String limitQuantity;   //限制数量的返回结果

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSubTitle() {
        return productSubTitle;
    }

    public void setProductSubTitle(String productSubTitle) {
        this.productSubTitle = productSubTitle;
    }

    public String getProductMainImage() {
        return productMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        this.productMainImage = productMainImage;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductstatus() {
        return productstatus;
    }

    public void setProductstatus(Integer productstatus) {
        this.productstatus = productstatus;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public Integer getProductCheckd() {
        return productCheckd;
    }

    public void setProductCheckd(Integer productCheckd) {
        this.productCheckd = productCheckd;
    }

    public String getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(String limitQuantity) {
        this.limitQuantity = limitQuantity;
    }
}
