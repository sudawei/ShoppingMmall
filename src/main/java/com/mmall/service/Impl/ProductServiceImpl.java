package com.mmall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/7/10/010.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;
    /**
     * 新增或者更新商品
     * @param product
     * @return
     */

    public ServerResponse saveOrUpdateProduct(Product product){
        if(product != null){
            if(StringUtils.isNotBlank(product.getSubImages())){//如果子图不为空
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }

            if(product.getId() != null){
               int rowCount =  productMapper.updateByPrimaryKey(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccessMessage("更新产品成功！");
                }
                return ServerResponse.createBySuccessMessage("更新产品失败！");
            }else {
                int rowCount = productMapper.insert(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccessMessage("新增产品成功！");
                }
                return ServerResponse.createBySuccessMessage("新增产品失败！");
            }
        }

        return ServerResponse.createByErrorMessage("新增或者更新商品参数错误！");
    }

    /**
     * 设置商品上下架状态
     * @param productId
     * @param status
     * @return
     */
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status){
        if(productId == null || status == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGAL_ARGUMENT.getCode(),ResponseCode.ILLEAGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("商品销售状态设置成功！");
        }
        return  ServerResponse.createByErrorMessage("商品销售状态设置失败！");
    }

    /**
     * 获取商品的详细信息
     * @param productId
     * @return
     */
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if(productId == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGAL_ARGUMENT.getCode(),ResponseCode.ILLEAGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return  ServerResponse.createByErrorMessage("产品已下架或者已删除");
        }
        //vo（value object）对象
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);

    }
    //将pojo包装为vo
    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        //imageHost
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);//默认根节点
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    /**
     * 商品列表分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    //@GetCache(name="getProductList",value = "30")
    public ServerResponse<PageInfo> getProductList(int pageNum,int pageSize){
        //startPage
        //填充分页SQL逻辑
        //pageHelper收尾

        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product productItem :productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);

    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        return productListVo;
    }

    /**
     * 查询商品
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNoneBlank(productName)){
            productName = new StringBuffer().append("%").append(productName).append("%").toString();
        }
        List<Product> productList  = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product productItem :productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    /**
     * 前台的获取商品详细信息
     * @param productId
     * @return
     */
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId){
        if(productId == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGAL_ARGUMENT.getCode(),ResponseCode.ILLEAGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return  ServerResponse.createByErrorMessage("商品不存在");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
            return  ServerResponse.createByErrorMessage("商品已下架或者已删除");
        }
        //vo（value object）对象
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /**
     * 前台搜索商品
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId,int pageNum,int pageSize,String orderBy){
        if(StringUtils.isBlank(keyword) && categoryId == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGAL_ARGUMENT.getCode(),ResponseCode.ILLEAGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = new ArrayList<Integer>();

        if(categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyword)){
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.getCategoryAndDeepChildrenCategory(categoryId).getData();
        }

        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuffer().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum,pageSize);
        //动态排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,
                categoryIdList.size()==0?null:categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product productItem :productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);

    }
















}
