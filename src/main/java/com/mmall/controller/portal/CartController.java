package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Cart;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Administrator on 2017/7/11/011.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService iCartService;


    /**
     * 查询购物车中的商品
     * @param session
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登陆，无法添加购物车");
        }
        return iCartService.list(user.getId());
    }


    /**
     * 添加商品到购物车
     * @param session
     * @param count
     * @param productId
     * @return
     */
//    @RequestMapping("add.do")
//    @ResponseBody
//    public ServerResponse add(
//            HttpServletRequest request, HttpServletResponse response,
//            HttpSession session, Integer count, Integer productId )
//            throws  IOException {
//
//       //将对象转换成json字符串/json字符串转成对象
//        ObjectMapper om = new ObjectMapper();
//        om.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
//        Cart cookieCart = null;
//        //获取cookie中的购物车
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null && cookies.length > 0){
//            for(Cookie cookie : cookies){
//                if((Const.CURRENT_COOKIES+"_"+productId.toString()).equals(cookie.getName())){
//                    cookieCart = om.readValue(cookie.getValue(),Cart.class);
//                    break;
//                }
//            }
//        }
//
//        //cookie中的没有购物车
//        if(cookieCart == null){
//            //将当前商品组装成cart对象
//            if(count == null || productId == null){
//                return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGAL_ARGUMENT.getCode(),ResponseCode.ILLEAGAL_ARGUMENT.getDesc());
//            }
//            cookieCart = new Cart();
//            cookieCart.setQuantity(count);
//            cookieCart.setProductId(productId);
//        }else{  //此时已经存在该商品的cookie,则需要做商品数量相加
//            cookieCart.setQuantity(count + cookieCart.getQuantity());
//        }
//
//        User user =(User) session.getAttribute(Const.CURRENT_USER);
//        if(user != null){
//            //先将cookie中的所有的购物车添加至数据库
//            for(Cookie cookie : cookies){
//                if(cookie.getName().matches(Const.CURRENT_COOKIES +"_"+ "\\d+")){
//                    cookieCart = om.readValue(cookie.getValue(),Cart.class);
//                    iCartService.add(user.getId(),cookieCart.getQuantity(),cookieCart.getProductId());
//                    //清空Cookie 设置存活时间为0, 立马销毁
//                    cookie.setPath("/");
//                    cookie.setMaxAge(-0);
//                    response.addCookie(cookie);
//                }
//            }
//            //添加本次的新建购物车
//            iCartService.add(user.getId(),count,productId);
//            return ServerResponse.createBySuccess("购物车成功添加到数据库中！");
//        }else{
//            //未登录,将保存购物车到Cookie中
//            Writer w = new StringWriter();
//            om.writeValue(w,cookieCart);
//            Cookie cookie = new Cookie(Const.CURRENT_COOKIES+"_"+productId.toString(),w.toString());
//            //设置path是可以共享cookie
//            cookie.setPath("/");
//            //设置Cookie过期时间: -1 表示关闭浏览器失效  0: 立即失效  >0: 单位是秒, 多少秒后失效
//            cookie.setMaxAge(24*60*60);
//            //Cookie写回浏览器
//            response.addCookie(cookie);
//            return ServerResponse.createBySuccess("购物车成功添加到cookie中",cookie);
//        }
//
//
//    }

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(),count,productId);
    }

    /**
     * 更新购物车中的商品
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登陆，无法添加购物车");
        }
        return iCartService.update(user.getId(),count,productId);
    }

    /**
     * 删除购物车中的商品
     * @param session
     * @param productIds
     * @return
     */
    @RequestMapping("delete.do")
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpSession session, String  productIds){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登陆，无法添加购物车");
        }
        return iCartService.delete(user.getId(),productIds);
    }


    /**
     * 全选购物车中的商品
     * @param session
     * @return
     */
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登陆，无法添加购物车");
        }
        return iCartService.selectOrUnselect(user.getId(),null,Const.Cart.CHECKD);
    }

    /**
     * 全反选购物车中的商品
     * @param session
     * @return
     */
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登陆，无法添加购物车");
        }
        return iCartService.selectOrUnselect(user.getId(),null,Const.Cart.UN_CHECKD);
    }


    /**
     * 单选选购物车中的商品
     * @param session
     * @return
     */
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpSession session,Integer productId){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登陆，无法添加购物车");
        }
        return iCartService.selectOrUnselect(user.getId(),productId,Const.Cart.CHECKD);
    }


    /**
     * 单反选购物车中的商品
     * @param session
     * @return
     */
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session,Integer productId){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登陆，无法添加购物车");
        }
        return iCartService.selectOrUnselect(user.getId(),productId,Const.Cart.UN_CHECKD);
    }

    /**
     * 查询购物车中的商品数量
     * @param session
     * @return
     */
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpSession session){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }

}
