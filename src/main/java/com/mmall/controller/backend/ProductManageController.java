package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/10/010.
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * 新增或者更新商品
     * @param request
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpServletRequest request, Product product){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
        }
        String userJsdnStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAmdinRole(user).isSuccess()){//是管理员
            //新增或者更新商品的实现方法
            return iProductService.saveOrUpdateProduct(product);
        }else {
            return  ServerResponse.createByErrorMessage("不是管理员权限！");
        }*/

        //全部通过拦截器AuthorityInterceptor验证是否登录以及权限
        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * 设置商品上下架的状态
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpServletRequest request,Integer productId, Integer status){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
        }
        String userJsdnStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAmdinRole(user).isSuccess()){//是管理员
            return iProductService.setSaleStatus(productId,status);
        }else {
            return  ServerResponse.createByErrorMessage("不是管理员权限！");
        }*/

        //全部通过拦截器AuthorityInterceptor验证是否登录以及权限
        return iProductService.setSaleStatus(productId,status);
    }


    /**
     * 获取商品详细信息
     * @param request
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpServletRequest request,Integer productId){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
        }
        String userJsdnStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAmdinRole(user).isSuccess()){//是管理员
            return iProductService.manageProductDetail(productId);
        }else {
            return  ServerResponse.createByErrorMessage("不是管理员权限！");
        }*/

        //全部通过拦截器AuthorityInterceptor验证是否登录以及权限
        return iProductService.manageProductDetail(productId);
    }

    /**
     * 商品列表
     * @param request
     * @param pageNum
     * @param pageSize
     * @return
     */

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpServletRequest request, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
        }
        String userJsdnStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);         if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAmdinRole(user).isSuccess()){//是管理员
            return iProductService.getProductList(pageNum,pageSize);
        }else {
            return  ServerResponse.createByErrorMessage("不是管理员权限！");
        }*/

        //全部通过拦截器AuthorityInterceptor验证是否登录以及权限
        return iProductService.getProductList(pageNum,pageSize);
    }

    /**
     * 搜索商品
     * @param request
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpServletRequest request,String productName,Integer productId,
           @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
           @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
        }
        String userJsdnStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);         if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAmdinRole(user).isSuccess()){//是管理员
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else {
            return  ServerResponse.createByErrorMessage("不是管理员权限！");
        }*/

        //全部通过拦截器AuthorityInterceptor验证是否登录以及权限
        return iProductService.searchProduct(productName,productId,pageNum,pageSize);
    }

    /**
     * 文件上传
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
        }
        String userJsdnStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAmdinRole(user).isSuccess()){//是管理员
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccess(fileMap);
        }else {
            return  ServerResponse.createByErrorMessage("不是管理员权限！");
        }*/

        //全部通过拦截器AuthorityInterceptor验证是否登录以及权限
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);
        return ServerResponse.createBySuccess(fileMap);
    }

    /**
     * 富文本上传
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session,
                                 @RequestParam(value = "upload_file",required = false) MultipartFile file,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        /*Map resultMap = Maps.newHashMap();
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            resultMap.put("success",false);
            resultMap.put("msg","请登录");
            return resultMap;
        }
        String userJsdnStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录");
            return resultMap;
        }
        //富文本中对于返回值有自己的要求，我们使用simditor所以按照simditor的要求返回
        if(iUserService.checkAmdinRole(user).isSuccess()){//是管理员
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Arrow-Headers","X-File-Name");
            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }*/

        //全部通过拦截器AuthorityInterceptor验证是否登录以及权限
        Map resultMap = Maps.newHashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        resultMap.put("success",true);
        resultMap.put("msg","上传成功");
        resultMap.put("file_path",url);
        response.addHeader("Access-Control-Arrow-Headers","X-File-Name");
        return resultMap;

    }





}
