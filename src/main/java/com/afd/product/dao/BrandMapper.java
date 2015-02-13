package com.afd.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.afd.common.mybatis.Page;
import com.afd.model.product.Brand;

public interface BrandMapper {
	/**
	 * @param brand 
	 * @return 
	 */
	boolean insertBrand(Brand brand);

	boolean updateByBrandId(Brand record);
	
	Brand getByBrandId(Long brandId);
	
	public List<Brand> getBrandsByName(@Param(value = "name") String name);
	
	/**
	 * @param brandName 中文名 精确匹配
	 * @param brandEname 英文名  精确匹配
	 * @param status 1:有效,0:无效
	 * @return
	 */
	Brand getBrandByName(@Param(value = "brandName") String brandName, @Param(value = "brandEname") String brandEname, @Param(value = "status") String status);
	
	public List<Brand> getBrandsByPage(@Param("cond") Map<?, ?> map, @Param(value = "page") Page<Brand> page);
}