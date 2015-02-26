package com.afd.product.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.afd.common.mybatis.Page;
import com.afd.model.product.BrandShowDetail;

public interface BrandShowDetailMapper {
	int deleteByPrimaryKey(Integer bSDId);

	int insert(BrandShowDetail record);

	int insertSelective(BrandShowDetail record);

	BrandShowDetail selectByPrimaryKey(Integer bSDId);

	int updateByPrimaryKeySelective(BrandShowDetail record);

	int updateByPrimaryKey(BrandShowDetail record);

	List<BrandShowDetail> getBrandShowDetailsByIds(
			@Param("brandShowDetailIds") List<Integer> brandShowDetailIds);

	BigDecimal getLowestPrice(@Param("bsid") Integer bsid);

	List<BrandShowDetail> getBrandShowDetailByPage(
			@Param("cond") Map<?, ?> map,
			@Param(value = "page") Page<BrandShowDetail> page);

	void addStock(@Param("bsdId") Integer bsdId, @Param("stock") Integer stock);
}