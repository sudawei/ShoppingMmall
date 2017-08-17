package com.mmall.service.Impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11/011.
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;


    /**
     * 查询购物车中的商品
     * @param userId
     * @return
     */
    public ServerResponse<CartVo> list(Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }


    /**
     * 添加商品到购物车
     * @param userId
     * @param count
     * @param productId
     * @return
     */
    public ServerResponse<CartVo> add(Integer userId, Integer count, Integer productId){
        if(count == null || productId == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGAL_ARGUMENT.getCode(),ResponseCode.ILLEAGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart == null){
            //此时说明该用户并没有添加过该商品，需要添加一个记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKD);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        }else{
            //这个商品已经存在于购物车中，此时需要增加数量
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);

    }

    //购物车对象以及其中的商品对象的封装
    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        //初始化总价
        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (CollectionUtils.isNotEmpty(cartList)){
            for(Cart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubTitle(product.getSubtitle());
                    cartProductVo.setProductstatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setProductPrice(product.getPrice());

                    //判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() > cartItem.getQuantity()){
                        //库存充足时
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),buyLimitCount));
                    cartProductVo.setProductCheckd(cartItem.getChecked());
                }

                if(cartItem.getChecked() == Const.Cart.CHECKD){
                    //如果已经勾选
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllCheckd(getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }
    //判断购物车的商品是否全选
    private boolean getAllCheckedStatus(Integer userId){
        if (userId == null) {
            return  false;
        }
        return cartMapper.selectCartProductStatusByUserId(userId) == 0;
    }

    /**
     * 更新购物车
     * @param userId
     * @param count
     * @param productId
     * @return
     */
    public ServerResponse<CartVo> update(Integer userId, Integer count, Integer productId){
        if(count == null || productId == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGAL_ARGUMENT.getCode(),ResponseCode.ILLEAGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);
    }

    /**
     * 删除购物车中的商品
     * @param userId
     * @param productIds
     * @return
     */
    public ServerResponse<CartVo> delete(Integer userId,  String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGAL_ARGUMENT.getCode(),ResponseCode.ILLEAGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId,productList);
        return this.list(userId);
    }

    /**
     * 全选或者全不选
     * @param userId
     * @param checked
     * @return
     */
    public ServerResponse<CartVo> selectOrUnselect(Integer userId,Integer productId,Integer checked){
        cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
        return this.list(userId);
    }

    /**
     * 查询购物车中的商品数量
     * @param userId
     * @return
     */
    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }






}

