package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : suwei
 * @description :
 * @date : 2017\11\29 0029 9:31
 */
@Slf4j
public class CookieUtil {

    private static final String COOKIE_DOMAIN = ".happymmall.com";
    private static final String COOKIE_NAME = "mmall_login_token";

    /**
     * 写入cookie
     * @param response
     * @param token
     */
    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");

        //cookie有效期，单位为秒，-1表示永久；如果不设置，则保存在内存中，只在当前页面有效，不会写入硬盘
        cookie.setMaxAge(60 * 60);
        log.info("write cookieName:{} , cookieValue:{}",cookie.getName(),cookie.getValue());

        response.addCookie(cookie);
    }

    /**
     * 读取cookie
     * @param request
     * @return
     */
    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null ){
            for (Cookie ck : cookies){
                log.info("read cookieName:{} , cookieValue:{}",ck.getName(),ck.getValue());
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{} , cookieValue:{}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 删除cookie
     * @param request
     * @param response
     * @return
     */
    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie ck : cookies){
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    //cookie立即失效
                    ck.setMaxAge(0);
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }


}
