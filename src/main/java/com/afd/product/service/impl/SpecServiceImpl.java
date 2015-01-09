package com.afd.product.service.impl;

import java.util.List;
import java.util.Map;

import com.afd.common.mybatis.Page;
import com.afd.model.product.Spec;
import com.afd.model.product.SpecSpecValue;
import com.afd.model.product.SpecValue;
import com.afd.model.product.vo.SpecSpecValueVO;
import com.afd.service.product.ISpecService;

public class SpecServiceImpl implements ISpecService {

	@Override
	public Long insertSpec(Spec spec) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteSpecById(Long specId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Spec getSpecById(Long specId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Spec getSpecByName(String name, String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Spec> getSpecByPage(Map<?, ?> map, Page<Spec> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Spec> getSpecs(Map<?, ?> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateSpecById(Spec spec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int deleteSpecValueById(Long specValueId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Long insertSpecValue(SpecValue SpecValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpecValue getSpecValueById(Long specValueId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SpecValue> getSpecValueByName(String specValueName,
			String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SpecValue> getSpecValues(Map<?, ?> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<SpecValue> getSpecValueByPage(Map<?, ?> map,
			Page<SpecValue> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateSpecValueById(SpecValue SpecValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long insertSpecSpecValue(SpecSpecValue specSpecValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteSpecSpecValueById(Long sSVId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int deleteSpecSpecValuesById(List<Long> sSVIds) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<SpecSpecValueVO> getSpecSpecValueByspecId(Long specId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpecSpecValue getSpecSpecValueBySpecIdAndSpecValueId(Long specId,
			Long specValueId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateSpecSpecValueOrder(Long sssvId, Long dssvId) {
		// TODO Auto-generated method stub
		return false;
	}

}
