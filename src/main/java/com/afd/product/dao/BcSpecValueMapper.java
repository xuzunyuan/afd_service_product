package com.afd.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.afd.model.product.BcSpecValue;

public interface BcSpecValueMapper {

    /**
	 * 增加规格值信息
	 * @param bcSpecValue
	 * @return
	 */
	boolean insertBcSpecValue(BcSpecValue bcSpecValue);

	/**根据主键ID获取规格值信息
     * @param bcSvId
     * @return
     */
    BcSpecValue getBcSpecValueById(Long bcSvId);

    /**
     * 修改规格值信息
     * @param bcSpecValue
     * @return
     */
    boolean updateBcSpecValueById(BcSpecValue bcSpecValue);

    int updateByPrimaryKey(BcSpecValue record);
    
    /**
     * 删除规格值信息(逻辑删除)
     * @param bcSvId
     * @return
     */
    boolean deleteBcSpecValueById(Long bcSvId);
    
    /**
     * 根据类目规格ID删除规格值信息(逻辑删除)
     * @param bcSpecId
     * @return
     */
    int deleteBcSpecValueByBcSpecId(Long bcSpecId);
    
    /**
     * @param specValueId 规格值ID
     * @param status 0:无效,1:有效
     * @return
     */
    List<BcSpecValue> getBcSpecValueBySpecValueId(@Param(value = "specValueId") Long specValueId, @Param(value = "status") String status);
    
    /**
	 * @param specValueId
	 * @param specValueName
	 * @param imgUrl
	 * @return
	 */
	int updateBcSpecValueBySpecValueId(@Param("specValueId") Long specValueId, @Param("specValueName") String specValueName, @Param("imgUrl") String imgUrl);
	
	
	/**
	 * @param bcSpecId 类目规格关系ID
	 * @param order 显示位置
	 * @param value －1:置底,1置顶
	 * @return
	 */
	boolean updateBcSpecValueOrders(@Param("bcSpecId") Long bcSpecId, @Param("order") Integer order, @Param("value") Integer value);
	
	
	 /**
     * 根据类目规格关系ID获取该规格值列表信息
     * @param bcSvId
     * @return
     */
    List<BcSpecValue> getBcSpecValueByBcSpecId(@Param(value = "bcSpecId") Long bcSpecId, @Param(value = "status") String status);
}