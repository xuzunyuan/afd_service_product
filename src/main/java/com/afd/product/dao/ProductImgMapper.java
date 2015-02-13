package com.afd.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.afd.model.product.ProductImg;

public interface ProductImgMapper {
    int deleteByPrimaryKey(Integer prodImgId);

    int insert(ProductImg record);

    int insertSelective(ProductImg record);

    ProductImg selectByPrimaryKey(Integer prodImgId);

    int updateByPrimaryKeySelective(ProductImg record);

    int updateByPrimaryKey(ProductImg record);

	@Update("update t_product_img pi set pi.img_status = #{1}  where pi.prod_img_id = #{0}")
	boolean delProductImgById(Integer prodImgId, String imgStatus);
	
	@Update("update t_product_img pi set pi.img_status = #{1}  where pi.prod_id = #{0}")
	boolean delProductImgByProdId(Integer prodId, String prodImgStatusRemove);

	List<ProductImg> selectProductImgByProdId(@Param("prodId") Integer prodId,@Param("imgStatus") String imgStatus);

}