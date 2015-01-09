package com.afd.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.afd.model.product.BcAttrValue;
import com.afd.model.product.vo.BcAttrValueVO;

public interface BcAttrValueMapper {
    int deleteByPrimaryKey(Integer bcAvId);

    int insert(BcAttrValue record);

    int insertSelective(BcAttrValue record);

    /**
	 * 根据ID获取属性值信息
	 * @param bcAvId
	 * @return
	 */
	BcAttrValue getBcAttrValueById(Long bcAvId);

    int updateByPrimaryKeySelective(BcAttrValue record);

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
}