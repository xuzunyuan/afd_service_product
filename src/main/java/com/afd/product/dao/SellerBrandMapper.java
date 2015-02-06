package com.afd.product.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.afd.model.product.Brand;
import com.afd.model.product.SellerBrand;

public interface SellerBrandMapper {
	int deleteByPrimaryKey(Integer sellerBrandId);

	int insert(SellerBrand record);

	int insertSelective(SellerBrand record);

	SellerBrand selectByPrimaryKey(Integer sellerBrandId);

	int updateByPrimaryKeySelective(SellerBrand record);

	int updateByPrimaryKey(SellerBrand record);

	// 扩展
	List<SellerBrand> getSellerBrandByBrandId(
			@Param(value = "brandId") Long brandId,
			@Param(value = "status") String status);

	@Update("update t_seller_brand set status = #{1}, audit_date = #{2}, audit_by_name = #{3}, audit_content = #{4} where seller_brand_id = #{0}")
	int updateAuditStatus(int sellerBrandId, String status, Date auditDate,
			String auditByName, String opinion);

	@Select("select 1 from t_seller_brand where seller_id = #{0} and brand_id = #{1} and status != '0'")
	Integer selectBySellerAndBrand(int sellerId, int brandId);

	List<Brand> selectValidBrandOfSeller(@Param("sellerId") int sellerId);

	List<SellerBrand> selectSellerBrandList(@Param("sellerId") int sellerId);

	List<SellerBrand> queryWaitAurditApplyByPage(
			@Param("cond") Map<String, Object> map,
			@Param("page") com.afd.common.mybatis.Page<SellerBrand> page);
}