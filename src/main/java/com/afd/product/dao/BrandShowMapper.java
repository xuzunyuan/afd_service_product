package com.afd.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.afd.common.mybatis.Page;
import com.afd.model.product.BrandShow;

public interface BrandShowMapper {
	int deleteByPrimaryKey(Integer brandShowId);

	int insert(BrandShow record);

	int insertSelective(BrandShow record);

	BrandShow selectByPrimaryKey(Integer brandShowId);

	int updateByPrimaryKeySelective(BrandShow record);

	int updateByPrimaryKey(BrandShow record);

	// 扩展
	List<BrandShow> getBrandShowByIds(
			@Param("brandShowIds") List<Integer> brandShowIds);

	List<BrandShow> getValidBrandShows(BrandShow record);

	@Update("update t_brand_show set status = #{1} where brand_show_id = #{0}")
	int updateStatus(int brandShowId, String status);

	List<BrandShow> queryBrandShowOfSellerByPage(
			@Param("sellerId") int sellerId,
			@Param("cond") Map<String, ?> cond,
			@Param("page") Page<BrandShow> page);

	List<BrandShow> getOnlinedBrandShowsOfSeller(@Param("sellerId") int sellerId);
}