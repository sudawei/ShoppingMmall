package com.mmall.controller.portal;


import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/7/8/008.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     *用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){

        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
           // session.setAttribute(Const.CURRENT_USER,response.getData());
            CookieUtil.writeLoginToken(httpServletResponse,session.getId());
            RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    /**
     * 用户退出
     * @return
     */
    @RequestMapping(value = "logout.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest request,HttpServletResponse response){
        String loginToken = CookieUtil.readLoginToken(request);
        //删除cookie
        CookieUtil.delLoginToken(request,response);

        //删除redis中的数据
        RedisPoolUtil.del(loginToken);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    /**
     * 用户注册时实时检测用户名和邮箱是否已经存在
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "check_valid.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    /**
     * 获取用户信息
     * @param request
     * @return
     */
    @RequestMapping(value = "get_user_info.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest request){
        //从cookie中取出登录的cookie
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
        }

        //从redis中取出对应的值
        String userJsdnStr = RedisPoolUtil.get(loginToken);

        //反序列化为user对象
        User user = JsonUtil.string2Obj(userJsdnStr, User.class);
        if(user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登陆，无法获取当前用户的信息");
    }

    /**
     * 忘记密码的提示问题
     * @param username
     * @return
     */
    @RequestMapping(value = "for_get_question.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse forgetGetQuestion(String username){
        return  iUserService.selectQuestion(username);
    }

    /**
     * 校验提示问题的答案是否在正确
     * @param username
     * @param question
     * @return
     */
    @RequestMapping(value = "for_check_answer.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    /**
     * 忘记密码时重置密码
     * @param username
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "for_reset_password.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetRestPassword(username,passwordNew,forgetToken);
    }

    /**
     * 登录状态下的重置密码
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "reset_password.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登陆，无法修改密码");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    /**
     * 更新用户个人信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "update_infomation.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_infomation(HttpSession session,User user){
        User currentUser =(User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_information.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser =(User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登录status=10");
        }
        return iUserService.get_information(currentUser.getId());
    }
}
