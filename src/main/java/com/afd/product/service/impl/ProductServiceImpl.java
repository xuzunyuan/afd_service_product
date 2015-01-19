package com.afd.product.service.impl;

import java.util.List;
import java.util.Map;

import com.afd.common.mybatis.Page;
import com.afd.model.product.Product;
import com.afd.model.product.ProductImg;
import com.afd.model.product.Sku;
import com.afd.param.product.ProductCondition;
import com.afd.service.product.IProductService;

public class ProductServiceImpl implements IProductService {

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
	public boolean removeSkuById(Integer skuId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editSkuById(Sku sku, Integer increaseNum) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchEditSkus(List<Sku> skus, List<Integer> increaseNum) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Sku getSkuById(Integer skuId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sku> getSkusByProdId(Integer prodId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sku getSkuBySkuCode(String skuCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getStockBalance(Integer prodId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, List<Sku>> getSkusByProdId(List<Integer> prodIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Sku> getSkusByProdIdPage(String prodId, Page<Sku> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long addProductImg(ProductImg productImg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long updateProductImg(ProductImg productImg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean delProductImg(Long prodImgId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delProductImgByProdId(Long prodId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ProductImg getProductImgByPrimaryKey(Long prodImgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductImg> getProductImgByProdId(Long prodId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<ProductImg> getProductImgByProdIdOfPage(long prodId,
			Page<ProductImg> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addProduct(Product product) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int editProductById(Product product) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean putawayProduct(Integer prodId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchPutawayProduct(List<Integer> idList) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelAuditProduct(Integer prodId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean downawayProductByProdId(Integer prodId, String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchDownawayProduct(List<Integer> idList, String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putawayProductByBoss(Integer prodId, String auditName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchPutawayProductByBoss(List<Integer> idList,
			String auditName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delProduct(Integer prodId, String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchdelProduct(List<Integer> idList, String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchSuspendSaleProduct(Integer sellerId,
			List<Integer> idList, String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean batchOpenSaleProduct(Integer sellerId, List<Integer> idList,
			String optName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Product getProductById(Integer prodId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product getProductByProdCode(String prodCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> searchOnlineProduct(ProductCondition productCondition,
			String sortField, String sortDirection, Page<Product> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> searchAuditProductPage(
			ProductCondition productCondition, String sortField,
			String sortDirection, Page<Product> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> searchStockProductPage(
			ProductCondition productCondition, String sortField,
			String sortDirection, Page<Product> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> searchProductByConditionPage(
			ProductCondition productCondition, String sortField,
			String sortDirection, Page<Product> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductBysellerId(Integer sellerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getProductIdsBysellerId(Integer sellerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sku> getSkusBySkuIds(List<Integer> skuIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductsByProdIds(List<Integer> prodIds) {
		// TODO Auto-generated method stub
		return null;
	}

}
