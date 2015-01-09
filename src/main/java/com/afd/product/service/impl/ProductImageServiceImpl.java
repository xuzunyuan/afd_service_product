package com.afd.product.service.impl;

import java.util.List;

import com.afd.common.mybatis.Page;
import com.afd.model.product.ProductImg;
import com.afd.service.product.IProductImageService;

public class ProductImageServiceImpl implements IProductImageService {

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
	public List<ProductImg> getProductImgByProdId(Long prodId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductImg getProductImgByPrimaryKey(Long prodImgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<ProductImg> getProductImgByProdIdOfPage(long prodId,
			Page<ProductImg> page) {
		// TODO Auto-generated method stub
		return null;
	}

}
