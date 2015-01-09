package com.afd.product.service.impl;

import java.util.List;

import com.afd.common.mybatis.Page;
import com.afd.model.product.Product;
import com.afd.param.product.ProductCondition;
import com.afd.service.product.IProductService;

public class ProductServiceImpl implements IProductService {

	@Override
	public Long addProduct(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean editProductById(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putawayProduct(Long prodId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchPutawayProduct(List<Long> idList) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelAuditProduct(Long prodId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean downawayProductByProdId(Long prodId, String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchDownawayProduct(List<Long> idList, String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putawayProductBySys(Long prodId, String auditName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchPutawayProductBySys(List<Long> idList, String auditName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delProduct(Long prodId, String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchdelProduct(List<Long> idList, String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchSuspendSaleProduct(Long storeId, List<Long> idList,
			String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchOpenSaleProduct(Long toreId, List<Long> idList,
			String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Product getProductById(Long prodId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product getProductByProdCode(String prodCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> getSaleProductPage(ProductCondition productCondition,
			String sortField, String sortDirection, Page<Product> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> getBalanceProductPage(
			ProductCondition productCondition, String sortField,
			String sortDirection, Page<Product> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> getProductPage(ProductCondition productCondition,
			String sortField, String sortDirection, Page<Product> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> getProductByStoreId(Long storeId) {
		// TODO Auto-generated method stub
		return null;
	}

}
