package com.afd.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.afd.common.mybatis.Page;
import com.afd.model.product.SpecValue;

public interface SpecValueMapper {
	int deleteSpecValueById(Long specValueId);

	boolean insertSpecValue(SpecValue SpecValue);

    int insertSelective(SpecValue record);

    SpecValue getSpecValueById(Long specValueId);

    boolean updateSpecValueById(SpecValue SpecValue);

    int updateByPrimaryKey(SpecValue record);
    
    List<SpecValue> getSpecValueByName(@Param("specValueName") String specValueName,@Param("status") String status);
    
    List<SpecValue> getSpecValues(@Param("cond") Map<?, ?> map);
    
    List<SpecValue> getSpecValueByPage(@Param("cond") Map<?, ?> map, @Param(value = "page") Page<SpecValue> page);
}