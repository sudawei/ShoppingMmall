package com.mmall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/11/011.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    /**
     * 添加收货地址
     * @param shipping
     * @return
     */
    public ServerResponse add(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map resultMap = Maps.newHashMap();
            resultMap.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建收获地址成功！",resultMap);
        }
        return ServerResponse.createByErrorMessage("新建收获地址失败！");
    }

    /**
     * 删除收获地址
     * @param userId
     * @param shippingId
     * @return
     */
    public ServerResponse<String> del(Integer userId,Integer shippingId){
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if(resultCount > 0){
            return ServerResponse.createBySuccess("删除收获地址成功");
        }
        return ServerResponse.createByErrorMessage("删除收获地址失败！");
    }

    /**
     * 更新收获地址，收获地址的user_id是不允许更改的，防止横向越权
     * @param userId
     * @param shipping
     * @return
     */
    public ServerResponse update(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新新建收获地址成功！");
        }
        return ServerResponse.createByErrorMessage("更新收获地址失败！");
    }

    /**
     * 查询收获地址的信息
     * @param userId
     * @param shippingId
     * @return
     */
    public ServerResponse<Shipping> select(Integer userId,Integer shippingId){
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping != null){
            return ServerResponse.createBySuccess("查询收获地址成功",shipping);
        }
        return ServerResponse.createByErrorMessage("查询收获地址失败！");
    }

    /**
     * 分页
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> list(Integer userId,Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }

}
