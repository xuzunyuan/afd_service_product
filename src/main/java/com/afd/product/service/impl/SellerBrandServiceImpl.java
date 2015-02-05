/**
 * Copyright (c)2015-? by www.afd.com. All rights reserved.
 * 
 */
package com.afd.product.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.afd.common.mybatis.Page;
import com.afd.model.product.Brand;
import com.afd.model.product.SellerBrand;
import com.afd.service.product.ISellerBrandService;

/**
 * 卖家品牌服务
 * 
 * @author xuzunyuan
 * @date 2015年2月5日
 */
@Service("sellerBrandService")
public class SellerBrandServiceImpl implements ISellerBrandService {

	@Override
	public SellerBrand getSellerBrandById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SellerBrand> getSellerBrandList(int sellerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Brand> getValidBrandListOfSeller(int sellerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int applySellerBrand(SellerBrand sellerBrand) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateApplySellerBrand(SellerBrand sellerBrand) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<SellerBrand> queryWaitAuditApplyByPage(
			Map<String, Object> cond, int... page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int passApply(int sellerBrandId, String auditor, String opinion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rejectApply(int sellerBrandId, String auditor, String opinion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean existSellerBrand(int sellerId, int brandId) {
		// TODO Auto-generated method stub
		return false;
	}

}
