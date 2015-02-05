package com.afd.product.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.afd.common.mybatis.Page;
import com.afd.constants.SystemConstants;
import com.afd.constants.product.ProductConstants;
import com.afd.model.product.Brand;
import com.afd.model.product.SellerBrand;
import com.afd.product.dao.BrandMapper;
import com.afd.product.dao.SellerBrandMapper;
import com.afd.service.product.IBrandService;

@Service("brandService")
public class BrandServiceImpl implements IBrandService {

	@Autowired
	private BrandMapper brandMapper;
	@Autowired
	private SellerBrandMapper sellerBrandMapper;
	
	@Resource(name="redisTemplate")
	private RedisTemplate<String, Serializable> redisTemplate;
	
	@Override
	public List<Brand> getBrandsByName(String name) {
		List<Brand> brands = null;
		
		if(StringUtils.isNotEmpty(name)){
			brands = this.brandMapper.getBrandsByName(name);
		}
		
		return brands;
	}

	@Override
	public Brand getBrandByName(String name) {
		Brand brand = null;
		
		if(StringUtils.isNotEmpty(name)){
			brand = this.getBrandByName(name, null, SystemConstants.DB_STATUS_INVALID);
			
			if(brand == null){
				brand = this.getBrandByName(null, name, SystemConstants.DB_STATUS_INVALID);
			}
		}
		
		return brand;
	}

	@Override
	public Page<Brand> getBrandsByPage(Map<?, ?> map, Page<Brand> page) {
		page.setResult(this.brandMapper.getBrandsByPage(map, page));
		
		return page;
	}

	@Override
	public Long insertBrand(Brand brand) {
		Long id = 0l;
		if(this.brandMapper.insertBrand(brand)){
			id = brand.getBrandId();
			if(id > 0){
				try {
					//写入缓存
					this.redisTemplate.opsForValue().set(ProductConstants.BRAND+brand.getBrandId(), brand);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return id;
	}

	@Override
	public boolean updateByBrandId(Brand brand) {
		boolean re = this.brandMapper.updateByBrandId(brand);
		if(re){
			try {
				this.redisTemplate.delete(ProductConstants.BRAND+brand.getBrandId());;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return re;
	}

	@Override
	public int deleteByBrandId(Long brandId) {
		int result = 1;
		
		List<SellerBrand> sb = this.sellerBrandMapper.getSellerBrandByBrandId(brandId, SystemConstants.DB_STATUS_INVALID);
		
		if(sb==null || sb.isEmpty()){
			Brand brand = this.brandMapper.getByBrandId(brandId);
			brand.setStatus(SystemConstants.DB_STATUS_INVALID);
			boolean re = this.brandMapper.updateByBrandId(brand);
			
			if(re){
				try {
					this.redisTemplate.delete(ProductConstants.BRAND+brandId);;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				result = 0;
			}
		}else{
			result = -1;
		}
		
		return result;
	}

	@Override
	public Brand getByBrandId(Long brandId) {
		Brand brand = null;
		
		try {
			//先从缓存中获取
			brand = (Brand) this.redisTemplate.opsForValue().get(ProductConstants.BRAND+brandId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(brand == null){
			brand = this.brandMapper.getByBrandId(brandId);
		}
		
		return brand;
	}

	@Override
	public Brand getBrandByName(String brandName, String brandEname, String status) {
		return this.brandMapper.getBrandByName(brandName, brandEname, status);
	}

}
