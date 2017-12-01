package com.mmall.controller.backend;


import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Administrator on 2017/7/9/009.
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加品类
     * @param request
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpServletRequest request, String categoryName,
                                      @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
        }
        String userJsdnStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAmdinRole(user).isSuccess()){
            //是管理员
            return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return  ServerResponse.createByErrorMessage("不是管理员权限！");
        }
    }

    /**
     * 更新category名称
     * @param request
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpServletRequest request ,Integer categoryId,String categoryName){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
        }
        String userJsdnStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAmdinRole(user).isSuccess()){
            //更新category名称
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return  ServerResponse.createByErrorMessage("不是管理员权限！");
        }
    }

    /**
     * 获取平级子节点，不递归
     * @param request
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request ,
                                                      @RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
        }
        String userJsdnStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAmdinRole(user).isSuccess()){
            //查询子节点的category的信息，并且不递归
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else {
            return  ServerResponse.createByErrorMessage("不是管理员权限！");
        }
    }

    /**
     * 递归获取所有的子节点
     * @param request
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest request,
                                                      @RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
        }
        String userJsdnStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAmdinRole(user).isSuccess()){
            //查询当前节点的id以及递归节点的ID
            return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
        }else {
            return  ServerResponse.createByErrorMessage("不是管理员权限！");
        }
    }


}
