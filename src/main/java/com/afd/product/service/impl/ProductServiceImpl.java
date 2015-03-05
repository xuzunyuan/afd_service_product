package com.afd.product.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afd.common.mybatis.Page;
import com.afd.common.util.DateUtils;
import com.afd.constants.product.ProductConstants;
import com.afd.model.product.Product;
import com.afd.model.product.ProductImg;
import com.afd.model.product.Sku;
import com.afd.param.product.ProductCondition;
import com.afd.product.dao.ProductImgMapper;
import com.afd.product.dao.ProductMapper;
import com.afd.product.dao.SkuMapper;
import com.afd.service.product.IProductService;

@Service("productService")
public class ProductServiceImpl implements IProductService {

	private static final String AUDIT_PASS_COMMENT = "审核通过";
	
	@Autowired
	ProductMapper productMapper;
	@Autowired
	SkuMapper skuMapper;
	@Autowired
	ProductImgMapper productImgMapper;
	
	@Override
	public boolean addSku(Sku sku) {
		int i = this.skuMapper.insertSelective(sku);
		return i > 0 ? true : false;
	}

	@Override
	public boolean batchAddSkus(List<Sku> skus) {
		return this.skuMapper.batchInsertSkus(skus);
	}

	@Override
	public boolean removeSkuById(Integer skuId) {
		return this.skuMapper.removeSkuById(skuId,ProductConstants.SKU_STATUS_DELETE);
	}

	@Override
	public boolean editSkuById(Sku sku, Integer increaseNum) {
		int i = this.skuMapper.updateByPrimaryKeySelective(sku);
		return i > 0 ? true : false;
	}

	@Override
	public boolean batchEditSkus(List<Sku> skus, List<Integer> increaseNum) {
		
		return this.skuMapper.batchEditSkus(skus);
	}

	@Override
	public Sku getSkuById(Integer skuId) {
		return this.skuMapper.selectByPrimaryKey(skuId);
	}

	@Override
	public List<Sku> getSkusByProdId(Integer prodId) {
		return this.skuMapper.getSkusByProdId(prodId);
	}

	@Override
	public Sku getSkuBySkuCode(String skuCode) {
		return this.skuMapper.getSkuBySkuCode(skuCode);
	}

	@Override
	public Integer getStockBalance(Integer prodId) {
		return this.skuMapper.getStockBalance(prodId);
	}

	@Override
	public Map<Integer, List<Sku>> getSkusByProdIds(List<Integer> prodIds) {
		if (prodIds != null && prodIds.size() > 0) {
			Map<Integer, List<Sku>> skuMap = new HashMap<Integer, List<Sku>>();
			for (Integer prodId : prodIds) {
				List<Sku> skus = this.getSkusByProdId(prodId);
				List<Integer> skuIds = new ArrayList<Integer>();
				for (Sku sku : skus) {
					skuIds.add(sku.getSkuId());
				}
				List<Sku> skuss = this.getSkusBySkuIds(skuIds);
				skuMap.put(prodId, skuss);
			}
			return skuMap;
		}
		return new HashMap<Integer, List<Sku>>();
	}

	@Override
	public List<Sku> getSkusBySkuIds(List<Integer> skuIds) {
		return this.skuMapper.getSkusBySkuIds(skuIds);
	}
	
	@Override
	public Integer addProductImg(ProductImg productImg) {
		this.productImgMapper.insertSelective(productImg);
		return productImg.getProdImgId();
	}

	@Override
	public Integer updateProductImg(ProductImg productImg) {
		this.productImgMapper.updateByPrimaryKeySelective(productImg);
		return productImg.getProdImgId();
	}

	@Override
	public boolean delProductImgById(Integer prodImgId) {
		return this.productImgMapper.delProductImgById(prodImgId,ProductConstants.PROD_IMG_STATUS_REMOVE);
	}

	@Override
	public boolean delProductImgByProdId(Integer prodId) {
		return this.productImgMapper.delProductImgByProdId(prodId,ProductConstants.PROD_IMG_STATUS_REMOVE);
	}

	@Override
	public ProductImg getProductImgByPrimaryKey(Integer prodImgId) {
		return this.productImgMapper.selectByPrimaryKey(prodImgId);
	}

	@Override
	public List<ProductImg> getProductImgByProdId(Integer prodId) {
		return  this.productImgMapper.selectProductImgByProdId(prodId,ProductConstants.PROD_IMG_STATUS_NORMAL);
	}

	@Override
	public int addProduct(Product product) {
		this.productMapper.insertSelective(product);
		return product.getProdId();
	}

	@Override
	public int editProductById(Product product) {
		this.productMapper.updateByPrimaryKeySelective(product);
		return product.getProdId();
	}

	@Override
	public boolean putawayProduct(Integer prodId) {
		Product product = new Product();
		product.setProdId(prodId);
		product.setAuditStatus(ProductConstants.PROD_AUDIT_STATUS_WAIT);
		product.setLastUpdateDate(DateUtils.currentDate());
		int i = this.productMapper.updateByPrimaryKeySelective(product);
		return i > 0 ? true : false;
	}

	@Override
	public boolean batchPutawayProduct(List<Integer> idList) {
		Product product = new Product();
		product.setAuditStatus(ProductConstants.PROD_AUDIT_STATUS_WAIT);
		product.setLastUpdateDate(DateUtils.currentDate());
		return this.productMapper.batchUpdateProdByCondition(idList,product);
	}

	@Override
	public boolean cancelAuditProduct(Integer prodId) {
		Product product = new Product();
		product.setProdId(prodId);
		product.setAuditStatus(ProductConstants.PROD_AUDIT_STATUS_WAIT);
		product.setLastUpdateDate(DateUtils.currentDate());
		int i = this.productMapper.updateByPrimaryKeySelective(product);
		return i > 0 ? true : false;
	}

	@Override
	public boolean downawayProductByProdId(Integer prodId, String optName) {
		Product product = new Product();
		product.setProdId(prodId);
		product.setStatus(ProductConstants.PROD_STATUS_DOWN);
		product.setAuditStatus(ProductConstants.PROD_AUDIT_STATUS_NO_PASS);
		product.setLastUpdateDate(DateUtils.currentDate());
		int i = this.productMapper.updateByPrimaryKeySelective(product);
		return i > 0 ? true : false;
	}

	@Override
	public boolean batchDownawayProduct(List<Integer> idList, String optName) {
		Product product = new Product();
		product.setStatus(ProductConstants.PROD_STATUS_DOWN);
		product.setAuditStatus(ProductConstants.PROD_AUDIT_STATUS_NO_PASS);
		product.setLastUpdateDate(DateUtils.currentDate());
		return this.productMapper.batchUpdateProdByCondition(idList,product);
	}

	@Override
	public boolean putawayProductByBoss(Integer prodId, String optName) {
		Product product = new Product();
		product.setProdId(prodId);
		product.setAuditStatus(ProductConstants.PROD_AUDIT_STATUS_PASS);
		product.setStatus(ProductConstants.PROD_STATUS_ON);
		product.setLastUpdateDate(DateUtils.currentDate());
		int i = this.productMapper.updateByPrimaryKeySelective(product);
		return i > 0 ? true : false;
	}

	@Override
	public boolean batchPutawayProductByBoss(List<Integer> idList,
			String optName) {
		Product product = new Product();
		product.setAuditStatus(ProductConstants.PROD_STATUS_DOWN);
		product.setLastUpdateDate(DateUtils.currentDate());
		return this.productMapper.batchUpdateProdByCondition(idList,product);
	}

	@Override
	public boolean delProduct(Integer prodId, String optName) {
		Product product = new Product();
		product.setProdId(prodId);
		product.setAuditStatus(ProductConstants.PROD_AUDIT_STATUS_WAIT);
		product.setStatus(ProductConstants.PROD_STATUS_REMOVE);
		product.setLastUpdateDate(DateUtils.currentDate());
		int i = this.productMapper.updateByPrimaryKeySelective(product);
		return i > 0 ? true : false;
	}

	@Override
	public boolean batchdelProduct(List<Integer> idList, String optName) {
		Product product = new Product();
		product.setAuditStatus(ProductConstants.PROD_AUDIT_STATUS_WAIT);
		product.setStatus(ProductConstants.PROD_STATUS_REMOVE);
		product.setLastUpdateDate(DateUtils.currentDate());
		return this.productMapper.batchUpdateProdByCondition(idList,product);
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
	public boolean auditProduct(Integer prodId, String auditStatus,
			String auditContent, String auditName) {
	
		Date currentDate = DateUtils.currentDate();
		Product product = new Product();
		product.setProdId(prodId);
		product.setAuditStatus(auditStatus);
		if (ProductConstants.PROD_AUDIT_STATUS_PASS.equals(auditStatus)) {// 审核通过
			product.setStatus(ProductConstants.PROD_STATUS_ON);
			product.setAuditContent(AUDIT_PASS_COMMENT);
			product.setLastUpdateDate(currentDate);
			product.setLastAuditName(auditName);
			product.setLastAuditDate(currentDate);
		} else if (ProductConstants.PROD_AUDIT_STATUS_NO_PASS
				.equals(auditStatus)) {// 审核驳回
			product.setStatus(ProductConstants.PROD_STATUS_DOWN);
			product.setAuditContent(auditContent);
			product.setLastUpdateDate(currentDate);
			product.setLastAuditName(auditName);
			product.setLastAuditDate(currentDate);
		}

		int i = productMapper.updateByPrimaryKeySelective(product);
		return i > 0 ? true : false ;
	}
	@Override
	public boolean batchAuditProduct(List<Integer> prodIds, String auditStatus,
			String auditContent, String auditName) {
		
		Date currentDate = DateUtils.currentDate();
		Product product = new Product();
		product.setAuditStatus(auditStatus);
		if (ProductConstants.PROD_AUDIT_STATUS_PASS.equals(auditStatus)) {// 审核通过
			product.setStatus(ProductConstants.PROD_STATUS_ON);
			product.setAuditContent(AUDIT_PASS_COMMENT);
			product.setLastUpdateDate(currentDate);
			product.setLastAuditName(auditName);
			product.setLastAuditDate(currentDate);
		} else if (ProductConstants.PROD_AUDIT_STATUS_NO_PASS
				.equals(auditStatus)) {// 审核驳回
			product.setStatus(ProductConstants.PROD_STATUS_DOWN);
			product.setAuditContent(auditContent);
			product.setLastUpdateDate(currentDate);
			product.setLastAuditName(auditName);
			product.setLastAuditDate(currentDate);
		}
		return productMapper.batchUpdateProdByCondition(prodIds, product);
	}
	
	@Override
	public Product getProductById(Integer prodId) {
		Product product = this.productMapper.selectByPrimaryKey(prodId);
		List<Sku> skus = this.skuMapper.getSkusByProdId(prodId);
		product.setSkus(skus);
		return product;
	}

	@Override
	public Product getProductByProdCode(String prodCode) {
		return this.productMapper.getProductByProdCode(prodCode);
	}

	@Override
	public Page<Product> searchOnlineProductPage(ProductCondition productCondition,
			String sortField, String sortDirection, Page<Product> page) {
		productCondition.setStatus(ProductConstants.PROD_STATUS_ON);
		page.setResult(this.productMapper.searchOnlineProductPage(productCondition,sortField,sortDirection,page));
		return page;
	}

	@Override
	public Page<Product> searchAuditProductPage(
			ProductCondition productCondition, String sortField,
			String sortDirection, Page<Product> page) {
		productCondition.setStatus(ProductConstants.PROD_STATUS_DOWN);
		page.setResult(this.productMapper.searchAuditProductByConditionPage(productCondition,sortField,sortDirection,page));
		return page;
	}

	@Override
	public Page<Product> searchProductByConditionPage(
			ProductCondition productCondition, String sortField,
			String sortDirection, Page<Product> page) {
		page.setResult(this.productMapper.searchProductByConditionPage(productCondition,sortField,sortDirection,page));
		return page;
	}

	@Override
	public List<Product> getOnlineProductBySellerId(Integer sellerId) {
		return this.productMapper.getOnlineProductBySellerId(sellerId);
	}

	@Override
	public List<Integer> getProductIdsBySellerId(Integer sellerId) {
		return this.productMapper.getProductIdsBySellerId(sellerId);
	}

	@Override
	public List<Product> getProductsByProdIds(List<Integer> prodIds) {
		return this.productMapper.getProductsByProdIds(prodIds);
	}


}
