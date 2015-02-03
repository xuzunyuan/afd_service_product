package com.afd.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afd.model.product.BrandShow;
import com.afd.model.product.BrandShowDetail;
import com.afd.product.dao.BrandShowDetailMapper;
import com.afd.product.dao.BrandShowMapper;
import com.afd.service.product.IBrandShowService;

@Service("brandShowService")
public class BrandShowServiceImpl implements IBrandShowService{

	@Autowired
	private BrandShowMapper brandShowMapper;
	@Autowired
	private BrandShowDetailMapper brandShowDetailMapper;
	
	@Override
	public BrandShow getBrandShowById(Long brandShowId) {
		return this.brandShowMapper.selectByPrimaryKey(brandShowId.intValue());
	}

	@Override
	public BrandShowDetail getBrandShowDetailById(Long brandShowDetailId) {
		return this.brandShowDetailMapper.selectByPrimaryKey(brandShowDetailId.intValue());
	}

	@Override
	public List<BrandShow> getBrandShowByIds(List<Long> brandShowIds) {
		return this.brandShowMapper.getBrandShowByIds(brandShowIds);
	}

	@Override
	public List<BrandShowDetail> getBrandShowDetailsByIds(List<Long> brandShowDetailIds) {
		return this.brandShowDetailMapper.getBrandShowDetailsByIds(brandShowDetailIds);
	}

	@Override
	public List<BrandShow> getValidBrandShows(BrandShow record) {
		return this.brandShowMapper.getValidBrandShows(record);
	}

}
