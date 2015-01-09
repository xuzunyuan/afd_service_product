package com.afd.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.afd.model.product.BcAttrValue;
import com.afd.model.product.vo.BcAttrValueVO;

public interface BcAttrValueMapper {
	
	/**
     * 删除指定ID的属性值
     * @param bcAvId
     * @return
     */
    boolean deleteBcAttrValueById(Long bcAvId);

    /**
     * 插入属性值信息
     * @param bcAttrValue
     * @return
     */
    boolean insertBcAttrValue(BcAttrValue bcAttrValue);

    int insertSelective(BcAttrValue record);

    /**
	 * 根据ID获取属性值信息
	 * @param bcAvId
	 * @return
	 */
	BcAttrValue getBcAttrValueById(Long bcAvId);
	
	BcAttrValue getBcAttrValue(@Param(value = "bcAttrId")Long bcAttrId, @Param(value = "attrValueId")Long attrValueId, @Param(value = "pBcAvId")Long pBcAvId);
	
	/**
	 * @param bcAttrId 类目属性关系ID
	 * @param pBcAvId 一级目属性值关系ID
	 * @return
	 */
	Integer getMaxDisplayOrder(@Param(value = "bcAttrId")Long bcAttrId, @Param(value = "pBcAvId")Long pBcAvId);

	/**
     * 更新属性值信息
     * @param bcAttrValue
     * @return
     */
    boolean updateBcAttrValueById(BcAttrValue bcAttrValue);

    int updateByPrimaryKey(BcAttrValue record);
    
    /**
     * 删除指定父ID的所有子属性关系
     * @param pBcAvId
     * @return
     */
    boolean deleteBcAttrValueByPId(Long pBcAvId);
    
    /**
     * @param bcAttrId 类目属性关系ID
     * @return
     */
    boolean deleteBcAttrValueByBcAttrId(Long bcAttrId);
    
    /**
	 * 根据属性值ID获取关联的类目属性关系ID列表
	 * @param attrValueId 属性值ID
	 * @return 
	 */
	List<BcAttrValue> getBcAttrValueByAttrValueId(Long attrValueId);
    
    /**
	 * 获取类目属性的所有一级值
	 * @param bcAttrId 类目属性关系ID
	 * @return
	 */
	List<BcAttrValueVO> getBcAttrValueByBcAttrId(@Param(value = "bcAttrId") Long bcAttrId, @Param(value = "status") String status);
	
	/**
	 * 获取指定属性值ID的子属性列表
	 * @param pBcAvId 父属性值ID
	 * @param status
	 * @return
	 */
	List<BcAttrValueVO> getBcAttrValueByPBcAvId(@Param(value = "pBcAvId") Long pBcAvId, @Param(value = "status") String status);
	
	/**
	 * @param flag 0:按类目属性关系,1:按父类目属性关系值ID
	 * @param id flag＝0时:类目属性关系ID,flag＝1时:类目属性关系值ID,
	 * @param order 显示位置
	 * @param value －1:置底,1置顶
	 * @return
	 */
	boolean updateBcAttrValueOrders(@Param("flag") Integer flag, @Param("id") Long id, @Param("order") Integer order, @Param("value") Integer value);
}