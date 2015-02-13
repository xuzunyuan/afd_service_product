package com.afd.product.service.impl;

import java.util.List;
import java.util.Map;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afd.common.mybatis.Page;
import com.afd.constants.SystemConstants;
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
			brand = this.getBrandByName(name, null, SystemConstants.DB_STATUS_VALID);
			
			if(brand == null){
				brand = this.getBrandByName(null, name, SystemConstants.DB_STATUS_VALID);
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
		
		List<SellerBrand> sb = this.sellerBrandMapper.getSellerBrandByBrandId(brandId, SystemConstants.DB_STATUS_INVALID);
		
		if(sb==null || sb.isEmpty()){
			Brand brand = new Brand();
			brand.setBrandId(brandId);
			brand.setStatus(SystemConstants.DB_STATUS_INVALID);
			
			if(!this.brandMapper.updateByBrandId(brand)){
				result = 0;
			}
		}else{
			result = -1;
		}
		
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
