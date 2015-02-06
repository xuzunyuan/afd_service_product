/**
 * Copyright (c)2015-? by www.afd.com. All rights reserved.
 * 
 */
package com.afd.product.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afd.common.mybatis.Page;
import com.afd.common.util.DateUtils;
import com.afd.constants.product.ProductConstants.SellerBrand$Status;
import com.afd.model.product.Brand;
import com.afd.model.product.SellerBrand;
import com.afd.product.dao.SellerBrandMapper;
import com.afd.service.product.ISellerBrandService;

/**
 * 卖家品牌服务
 * 
 * @author xuzunyuan
 * @date 2015年2月5日
 */
@Service("sellerBrandService")
public class SellerBrandServiceImpl implements ISellerBrandService {
	@Autowired
	SellerBrandMapper sellerBrandMapper;

	@Override
	public SellerBrand getSellerBrandById(int id) {
		return sellerBrandMapper.selectByPrimaryKey(id);
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
		// 校验卖家是否已申请该品牌
		if (existSellerBrand(sellerBrand.getSellerId(),
				sellerBrand.getBrandId()))
			return -1;

		sellerBrand.setStatus(SellerBrand$Status.WAIT_AUDIT); // 初始状态待审核
		sellerBrand.setSubmitDate(DateUtils.currentDate());

		if (sellerBrandMapper.insert(sellerBrand) == 0) {
			return 0;
		}

		return sellerBrand.getSellerBrandId();
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
