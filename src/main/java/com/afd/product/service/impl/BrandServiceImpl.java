package com.afd.product.service.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.afd.constants.SystemConstants;
import com.afd.model.product.Brand;
import com.afd.product.dao.BrandMapper;
import com.afd.service.product.IBrandService;

@Service("brandService")
public class BrandServiceImpl implements IBrandService {

	@Autowired
	private BrandMapper brandMapper;
	
	@Resource(name="redisTemplate")
	private RedisTemplate<String, Serializable> redisTemplate;
	
	@Override
	public Long insertBrand(Brand brand) {
		Long id = 0l;
		if(this.brandMapper.insertBrand(brand)){
			id = brand.getBrandId();
		}
		
		return id;
	}

	@Override
	public boolean updateByBrandId(Brand brand) {
		return this.brandMapper.updateByBrandId(brand);
	}

	@Override
	public int deleteByBrandId(Long brandId) {
		int result = 1;
		
		Brand brand = this.brandMapper.getByBrandId(brandId);
		brand.setStatus(SystemConstants.DB_STATUS_INVALID);
		//修改为无效状态
		result = this.brandMapper.updateByBrandId(brand)?1:0;
		
		return result;
	}

	@Override
	public Brand getByBrandId(Long brandId) {
		return this.brandMapper.getByBrandId(brandId);
	}

	@Override
	public Brand getBrandByName(String brandName, String brandEname, String status) {
		return this.brandMapper.getBrandByName(brandName, brandEname, status);
	}

}
