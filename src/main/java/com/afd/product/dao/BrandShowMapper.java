package com.afd.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.afd.model.product.BrandShow;

public interface BrandShowMapper {
	int deleteByPrimaryKey(Integer brandShowId);

	int insert(BrandShow record);

	int insertSelective(BrandShow record);

	BrandShow selectByPrimaryKey(Integer brandShowId);

	int updateByPrimaryKeySelective(BrandShow record);

	int updateByPrimaryKey(BrandShow record);

	List<BrandShow> getBrandShowByIds(
			@Param("brandShowIds") List<Long> brandShowIds);

	List<BrandShow> getValidBrandShows(BrandShow record);
}