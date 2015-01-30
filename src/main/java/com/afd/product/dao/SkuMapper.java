package com.afd.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.afd.model.product.Sku;

public interface SkuMapper {
    int deleteByPrimaryKey(Integer skuId);

    int insert(Sku record);

    int insertSelective(Sku record);

    Sku selectByPrimaryKey(Integer skuId);

    int updateByPrimaryKeySelective(Sku record);

    int updateByPrimaryKey(Sku record);

	boolean batchInsertSkus(List<Sku> skus);

	@Update("update t_sku set sku_status= #{1} where sku_id = #{0}")
	boolean removeSkuById(Integer skuId, String skuStatus);

	boolean batchEditSkus(List<Sku> skus);

	List<Sku> getSkusByProdId(Integer prodId);

	Sku getSkuBySkuCode(String skuCode);

	@Select("select sum(stock_balance) from t_sku where prod_id = #{0}")
	Integer getStockBalance(Integer prodId);

	List<Sku> getSkusBySkuIds(@Param("idList")List<Integer> idList);
	
	
}