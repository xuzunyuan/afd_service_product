package com.afd.product.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.afd.common.mybatis.Page;
import com.afd.constants.SystemConstants;
import com.afd.constants.product.ProductConstants;
import com.afd.model.product.BcSpecValue;
import com.afd.model.product.Spec;
import com.afd.model.product.SpecSpecValue;
import com.afd.model.product.SpecValue;
import com.afd.model.product.vo.SpecSpecValueVO;
import com.afd.product.dao.BcSpecMapper;
import com.afd.product.dao.BcSpecValueMapper;
import com.afd.product.dao.SpecMapper;
import com.afd.product.dao.SpecSpecValueMapper;
import com.afd.product.dao.SpecValueMapper;
import com.afd.service.product.ISpecService;

@Service("specService")
public class SpecServiceImpl implements ISpecService {

	@Autowired
	private SpecMapper specMapper;
	@Autowired
	private SpecValueMapper specValueMapper;
	@Autowired
	private SpecSpecValueMapper specSpecValueMapper;
	@Autowired
	private BcSpecMapper bcSpecMapper;
	@Autowired
	private BcSpecValueMapper bcSpecValueMapper;
	
	@Resource(name="redisTemplate")
	private RedisTemplate<String, Serializable> redisTemplate;
	
	@Override
	public Long insertSpec(Spec spec) {
		Long result = 0l;
		
		if(this.specMapper.insertSpec(spec)){
			result =  spec.getSpecId();
		}
		
		return result;
	}

	@Override
	public int deleteSpecById(Long specId) {
		int result = 1;
		//根据规格ID获取关联该规格的类目ID列表
		List<Integer> bcIds = this.bcSpecMapper.getBcIdBySpecId(specId, SystemConstants.DB_STATUS_VALID);
		if(bcIds==null || bcIds.isEmpty()){
			//删除规格和规格值关系
			this.specSpecValueMapper.deleteSpecSpecValueBySpecId(specId);
			if(!this.specMapper.deleteSpecById(specId)){
				result = 0;
			}
		}else{
			result = -1;
		}
		
		return result;
	}

	@Override
	public Spec getSpecById(Long specId) {
		return this.specMapper.getSpecById(specId);
	}

	@Override
	public Spec getSpecByName(String name, String status) {
		return this.specMapper.getSpecByName(name, status);
	}

	@Override
	public Page<Spec> getSpecByPage(Map<?, ?> map, Page<Spec> page) {
		page.setResult(this.specMapper.getSpecByPage(map, page));
		
		return page;
		
	}

	@Override
	public List<Spec> getSpecs(Map<?, ?> map) {
		return this.specMapper.getSpecs(map);
	}

	@Override
	public boolean updateSpecById(Spec spec) {
		boolean result = this.specMapper.updateSpecById(spec);
		if(result){
			//根据规格ID获取关联该规格的类目ID列表
			List<Integer> bcIds = this.bcSpecMapper.getBcIdBySpecId(spec.getSpecId(), SystemConstants.DB_STATUS_VALID);
			if(bcIds!=null && bcIds.size()>0){
				Object[] keys = new Object[bcIds.size()];
				
				for(int i=0; i<bcIds.size(); i++){
					keys[i] = ProductConstants.BC_ATTR_SPEC_BRAND + bcIds.get(i);
				}
				
				try {
					//清空缓存
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, keys);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	@Override
	public int deleteSpecValueById(Long specValueId) {
		int result = 0;
		List<BcSpecValue> BcSpecValues = this.bcSpecValueMapper.getBcSpecValueBySpecValueId(specValueId, SystemConstants.DB_STATUS_VALID);
		if(BcSpecValues!=null && BcSpecValues.size()>0){
			result = -1;
		}else{
			result = this.specValueMapper.deleteSpecValueById(specValueId);
			//删除规格和规格值关系
			this.specSpecValueMapper.deleteSpecSpecValueBySpecValueId(specValueId);
		}
		
		return result;
	}

	@Override
	public Long insertSpecValue(SpecValue SpecValue) {
		Long id = 0l;
		if(this.specValueMapper.insertSpecValue(SpecValue)){
			id = SpecValue.getSpecValueId();
		}
		 
		return id;
	}

	@Override
	public SpecValue getSpecValueById(Long specValueId) {
		return this.specValueMapper.getSpecValueById(specValueId);
	}

	@Override
	public List<SpecValue> getSpecValueByName(String specValueName, String status) {
		return this.specValueMapper.getSpecValueByName(specValueName, status);
	}

	@Override
	public List<SpecValue> getSpecValues(Map<?, ?> map) {
		return this.specValueMapper.getSpecValues(map);
	}

	@Override
	public Page<SpecValue> getSpecValueByPage(Map<?, ?> map, Page<SpecValue> page) {
		page.setResult(this.specValueMapper.getSpecValueByPage(map, page));
		 
		return page;
	}

	@Override
	public boolean updateSpecValueById(SpecValue SpecValue) {
		boolean result = this.specValueMapper.updateSpecValueById(SpecValue);
		 
		//修改成功清除缓存
		if(result){
			List<BcSpecValue> BcSpecValues = this.bcSpecValueMapper.getBcSpecValueBySpecValueId(SpecValue.getSpecValueId(), SystemConstants.DB_STATUS_VALID);
			if(BcSpecValues!=null && BcSpecValues.size()>0){
				Object[] keys = new Object[BcSpecValues.size()];
				  
				for(int i=0; i<BcSpecValues.size(); i++){
					keys[i] = ProductConstants.BC_ATTR_SPEC_BRAND + this.bcSpecMapper.getBcSpecById(BcSpecValues.get(i).getBcSpecId()).getBcId();
				}
				
				try {
					//清空缓存
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, keys);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	@Override
	public Long insertSpecSpecValue(SpecSpecValue specSpecValue) {
		Long id = 0l;
		if(this.specSpecValueMapper.insertSpecSpecValue(specSpecValue)){
			id = specSpecValue.getsSVId();
		}
		
		return id;
	}

	@Override
	public boolean deleteSpecSpecValueById(Long sSVId) {
		return this.specSpecValueMapper.deleteSpecSpecValueById(sSVId);
	}

	@Override
	public int deleteSpecSpecValuesById(List<Long> sSVIds) {
		return this.specSpecValueMapper.deleteSpecSpecValuesById(sSVIds);
	}

	@Override
	public List<SpecSpecValueVO> getSpecSpecValueByspecId(Long specId) {
		return this.specSpecValueMapper.getSpecSpecValueByspecId(specId);
	}

	@Override
	public SpecSpecValue getSpecSpecValueBySpecIdAndSpecValueId(Long specId, Long specValueId) {
		return this.specSpecValueMapper.getSpecSpecValueBySpecIdAndSpecValueId(specId, specValueId);
	}

	@Override
	public boolean updateSpecSpecValueOrder(Long sssvId, Long dssvId) {
		boolean result = false;
		
		SpecSpecValue sssv = this.specSpecValueMapper.getSpecSpecValueById(sssvId);
		SpecSpecValue dssv = this.specSpecValueMapper.getSpecSpecValueById(dssvId);
		
		//向上或向下
		if(Math.abs(sssv.getDisplayOrder()-dssv.getDisplayOrder()) == 1){
			if(this.specSpecValueMapper.updateSpecSpecOrderById(sssvId, dssv.getDisplayOrder()) && 
					this.specSpecValueMapper.updateSpecSpecOrderById(dssvId, sssv.getDisplayOrder())){
				result = true;
			}
		}else{
			Integer value = 0;
			if(sssv.getDisplayOrder()>dssv.getDisplayOrder() && sssv.getDisplayOrder()-dssv.getDisplayOrder()>1){//置顶
				value = 1;
			}else if(sssv.getDisplayOrder()<dssv.getDisplayOrder() && dssv.getDisplayOrder()-sssv.getDisplayOrder()>1){//置底
				value = -1;
			}	
			
			if(this.specSpecValueMapper.updateSpecSpecOrders(sssv.getSpecId(), sssv.getDisplayOrder(), value) && 
					this.specSpecValueMapper.updateSpecSpecOrderById(sssvId, dssv.getDisplayOrder())){
				result = true;
			}
		}
		
		return result;
	}

}
