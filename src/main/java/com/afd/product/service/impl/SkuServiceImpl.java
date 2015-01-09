package com.afd.product.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.afd.common.mybatis.Page;
import com.afd.model.product.Sku;
import com.afd.service.product.ISkuService;

public class SkuServiceImpl implements ISkuService {

	@Override
	public boolean addSku(Sku sku) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchAddSkus(List<Sku> skus) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeSkuById(Long skuId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editSkuById(Sku sku, Long increaseNum) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchEditSkus(List<Sku> skus, List<Long> increaseNum) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProductPrice(Long prodId, BigDecimal oldPrice,
			BigDecimal newPrice) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Sku getSkuById(Long skuId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sku> getSkuByProdId(Long prodId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sku getSkuBySkuCode(String skuCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getStockBalance(long prodId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<Long, List<Sku>> getSkusByProdId(List<Long> prodIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Sku> getSkusByProdIdPage(String prodId, Page<Sku> page) {
		// TODO Auto-generated method stub
		return null;
	}

}
