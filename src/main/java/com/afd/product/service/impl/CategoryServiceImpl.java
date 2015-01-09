package com.afd.product.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.afd.common.mybatis.Page;
import com.afd.constants.SystemConstants;
import com.afd.constants.product.ProductConstants;
import com.afd.model.product.AttrAttrValue;
import com.afd.model.product.Attribute;
import com.afd.model.product.AttributeValue;
import com.afd.model.product.BaseCategory;
import com.afd.model.product.BcAttrValue;
import com.afd.model.product.BcAttribute;
import com.afd.model.product.BcSpec;
import com.afd.model.product.BcSpecValue;
import com.afd.model.product.Brand;
import com.afd.model.product.Spec;
import com.afd.model.product.SpecSpecValue;
import com.afd.model.product.SpecValue;
import com.afd.model.product.vo.AttrAttrValueVO;
import com.afd.model.product.vo.BaseCategoryInfoVO;
import com.afd.model.product.vo.BaseCategoryInfoVO.Attr;
import com.afd.model.product.vo.BaseCategoryInfoVO.Attr.AttrValue;
import com.afd.model.product.vo.BcAttrValueVO;
import com.afd.model.product.vo.BcAttributeVO;
import com.afd.model.product.vo.BcSpecVO;
import com.afd.model.product.vo.SpecSpecValueVO;
import com.afd.product.dao.AttrAttrValueMapper;
import com.afd.product.dao.AttributeMapper;
import com.afd.product.dao.AttributeValueMapper;
import com.afd.product.dao.BaseCategoryMapper;
import com.afd.product.dao.BcAttrValueMapper;
import com.afd.product.dao.BcAttributeMapper;
import com.afd.product.dao.BcSpecMapper;
import com.afd.product.dao.BcSpecValueMapper;
import com.afd.product.dao.BrandMapper;
import com.afd.product.dao.SpecMapper;
import com.afd.product.dao.SpecSpecValueMapper;
import com.afd.product.dao.SpecValueMapper;
import com.afd.service.product.ICategoryService;


@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {
	
	protected final static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	@Autowired
	private BaseCategoryMapper bcMapper;
	@Autowired
	private AttributeMapper attrMapper;
	@Autowired
	private AttributeValueMapper attrValueMapper;
	@Autowired
	private AttrAttrValueMapper attrAttrValueMapper;
	@Autowired
	private SpecMapper specMapper;
	@Autowired
	private SpecValueMapper specValueMapper;
	@Autowired
	private SpecSpecValueMapper specSpecValueMapper;
	@Autowired
	private BcAttributeMapper bcAttrMapper;
	@Autowired
	private BcSpecMapper bcSpecMapper;
	@Autowired
	private BcAttrValueMapper bcAttrValueMapper;
	@Autowired
	private BcSpecValueMapper bcSpecValueMapper;
	@Autowired
	private BrandMapper brandMapper;
	
	@Resource(name="redisTemplate")
	private RedisTemplate<String, Serializable> redisTemplate;
	
	@Override
	public List<BaseCategory> getAllBaseCategory(String status) {
		//一级类目
		List<BaseCategory> list = this.getBaseCategorysByPId(ProductConstants.CATE_LEVEL_FIRST_PID, status);
		
		//获取子类目
		if(list != null){
			this.getChildBaseCategory(list, SystemConstants.DB_STATUS_VALID);
		}
		 
		return list;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<BaseCategory> getBaseCategorysByPId(Integer pId, String status) {
		List<BaseCategory> list = null;
		
		try {
			//从缓存获取
			list =  (List<BaseCategory>) this.redisTemplate.opsForHash().get(ProductConstants.BC, ProductConstants.BC_LIST+pId);
			logger.debug("read from cache bcPid:"+pId+" result size "+(list==null?0:list.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}	
			
		if(list == null){
			//获取子级类目
			list =  this.bcMapper.getBaseCategorysByPId(pId, status);
			
			if(list!=null && list.size()>0){
				try {
					//写入缓存
					this.redisTemplate.opsForHash().put(ProductConstants.BC, ProductConstants.BC_LIST+pId, (Serializable) list);
					logger.debug("write bcPid:"+pId+" to cache, size-->"+list.size());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		 
		return list;
	}

	@Override
	public String getCodeByPid(int flag, Integer pId) {
		String code = "-1";
		if(flag == 1){
			pId = 0;
		}
		List<BaseCategory> list = this.getBaseCategorysByPId(pId, SystemConstants.DB_STATUS_VALID);
		
		if(list==null || list.size()==0){
			if(flag == 1){
				code = "11";
			}else if(flag == 2){
				code = this.getByBcId(pId).getBcCode().substring(0, 2) + "001";
			}else if(flag == 3){
				code = this.getByBcId(pId).getBcCode().substring(0, 5) + "0001";
			}
		}else{
			Collections.sort(list, new Comparator<BaseCategory>(){
				@Override
				public int compare(BaseCategory o1, BaseCategory o2) {
					int eq = 0;
					if(o1.getBcId().intValue() > o2.getBcId()){
						eq = -1;
					}else if(o1.getBcId().intValue() < o2.getBcId()){
						eq = 1;
					}
					return eq;
				}
			});
			
			BaseCategory bc = list.get(0);
			String maxCode = bc.getBcCode();
			try {
				if(flag == 1){
					int maxFirstCode = Integer.parseInt(maxCode.substring(0, 2));
					if(maxFirstCode < 99){
						code = ++maxFirstCode+"";
						
						Pattern pattern = Pattern.compile("0$");
						Matcher matcher = pattern.matcher(code);
						if (matcher.find()) {
							if(maxFirstCode < 99){
								code = ++maxFirstCode+"";
							}
						}
					}
				}else if(flag == 2){
					int maxSecondCode = Integer.parseInt(maxCode.substring(2, 5));
					if(maxSecondCode < 999){
						code = maxCode.substring(0, 2) + StringUtils.leftPad(++maxSecondCode+"", 3, '0');
					}
				}else if(flag == 3){
					int maxThirdCode = Integer.parseInt(maxCode.substring(5));
					if(maxThirdCode < 9999){
						code = maxCode.substring(0, 5) + StringUtils.leftPad(++maxThirdCode+"", 4, '0');
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return code;
	}
	
	@Override
	public BaseCategory getBaseCategoryByNameAndPid(Integer pId, String name, String status){
		return this.bcMapper.getBaseCategoryByNameAndPid(pId, name, status);
	}
	
	@Override
	public BaseCategoryInfoVO getBaseCategoryInfoByBcId(Integer bcId) {
		BaseCategoryInfoVO bcInfo = null;
		
		if(bcId > 0){
			try {
				//先从缓存中获取
				bcInfo = (BaseCategoryInfoVO) this.redisTemplate.opsForHash().get(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(bcInfo == null){
				BaseCategory bc = this.getByBcId(bcId);
				//只有叶子结点且该类目必须为三级或以下才有属性的规格
				if(bc!=null && bc.getIsLeaf() && Integer.parseInt(bc.getBcLevel())>Integer.parseInt(ProductConstants.CATE_LEVEL_SECOND)){
					bcInfo = new BaseCategoryInfoVO();
					bcInfo.setBcId(bcId);
					bcInfo.setBcCode(bc.getBcCode());
					bcInfo.setBcName(bc.getBcName());
					bcInfo.setPathId(bc.getPathId());
					bcInfo.setPathName(bc.getPathName());
					
					//获取类目规格列表
					List<BcSpecVO> speciList = this.getBcSpecByBcId(bcId, SystemConstants.DB_STATUS_VALID);
					if(speciList!=null && speciList.size()>0){
						List<BaseCategoryInfoVO.Spec> specList = new ArrayList<BaseCategoryInfoVO.Spec>(speciList.size());
						bcInfo.setSpecList(specList);
						
						BaseCategoryInfoVO.Spec spec = null;
						for(BcSpecVO bcSpec : speciList){
							spec = new BaseCategoryInfoVO.Spec();
							specList.add(spec);
							
							spec.setSpecId(bcSpec.getSpecId());
							spec.setDisplayOrder(bcSpec.getDisplayOrder());
							spec.setSpecName(bcSpec.getSpecName());
							spec.setBcSpecId(bcSpec.getBcSpecId());
							spec.setIsFilter(bcSpec.getIsFilter());
							
							List<BaseCategoryInfoVO.Spec.SpecValue> svList = null;
							//规格值列表
							List<BcSpecValue> bcSpecValueList = this.getBcSpecValueByBcSpecId(bcSpec.getBcSpecId(), SystemConstants.DB_STATUS_VALID);
							if(bcSpecValueList!=null && bcSpecValueList.size()>0){
								svList = new ArrayList<BaseCategoryInfoVO.Spec.SpecValue>(bcSpecValueList.size());
								spec.setSpecValueList(svList);
								
								BaseCategoryInfoVO.Spec.SpecValue sv = null;
								for(BcSpecValue bsv : bcSpecValueList){
									sv = new BaseCategoryInfoVO.Spec.SpecValue();
									svList.add(sv);
									
									sv.setSpecValueId(bsv.getSpecValueId());
									sv.setSpecValueName(bsv.getSpecValueName());
									sv.setDisplayOrder(bsv.getDisplayOrder());
									sv.setImgUrl(bsv.getImgUrl());
									sv.setBcSvId(bsv.getBcSvId());
									sv.setIsFilter(bsv.getIsFilter());
									sv.setmDisplayPosition(bsv.getmDisplayPosition());
									sv.setIsMobileDisplay(bsv.getIsMobileDisplay());
								}
							}
						}
					}
					
					//获取类目对应的属性列表
					List<BcAttributeVO> list = this.getBcAttributeByBcId(bcId, SystemConstants.DB_STATUS_VALID);
					if(list!=null && list.size()>0){
						List<BaseCategoryInfoVO.Attr> attrList = new ArrayList<BaseCategoryInfoVO.Attr>(list.size());
						bcInfo.setAttrList(attrList);
						
						Attr attr = null;
						for(BcAttributeVO bcAttr : list){
							attr = new Attr();
							attrList.add(attr);
							
							attr.setBcAttrId(bcAttr.getBcAttrId());
							attr.setAttrId(bcAttr.getAttrId());
							attr.setAttrName(bcAttr.getAttrName());
							attr.setDisplayOrder(bcAttr.getDisplayOrder());
							attr.setIsRequire(bcAttr.getIsRequire());
							attr.setDisplayMode(bcAttr.getDisplayMode());
							attr.setIsFilter(bcAttr.getIsFilter());
							//属性值列表
							List<AttrValue> attrValueList = null;
							List<BcAttrValueVO> bcAttrValueList  = this.getBcAttrValueByBcAttrId(bcAttr.getBcAttrId(), SystemConstants.DB_STATUS_VALID, false);
							if(bcAttrValueList!=null && bcAttrValueList.size()>0){
								attrValueList = new ArrayList<AttrValue>(bcAttrValueList.size());
								attr.setAttrValueList(attrValueList);
								
								for(BcAttrValueVO bav : bcAttrValueList){
									AttrValue av = new AttrValue();
									attrValueList.add(av);
									
									av.setBcAvId(bav.getBcAvId());
									av.setAttrValueId(bav.getAttrValueId());
									av.setAttrValue(bav.getAttrValueName());
									av.setDisplayOrder(bav.getDisplayOrder());
									av.setIcon(bav.getIcon());
									av.setIsSubAttr(bav.getIsSubAttr());
									av.setIsFilter(bav.getIsFilter());
									av.setIsMobileDisplay(bav.getIsMobileDisplay());
									//若有子属性循环获取
									if(bav.getIsSubAttr()){
										av.setSubAttrObj(this.getBcAttrValueList(bav));
									}
								}
							}
						}
					}
					
					try {
						//写入缓存
						this.redisTemplate.opsForHash().put(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcId, bcInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return bcInfo;
	}
	
	
	@Override
	public BaseCategory getByBcId(Integer bcId) {
		BaseCategory baseCategory = null;
		
		if(bcId > 0){
			try {
				//从缓存获取
				baseCategory = (BaseCategory) this.redisTemplate.opsForHash().get(ProductConstants.BC, ProductConstants.BC_+bcId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(baseCategory == null){
				baseCategory = this.bcMapper.getByBcId(bcId);
				if(baseCategory != null){
					try {
						//写入缓存
						this.redisTemplate.opsForHash().put(ProductConstants.BC, ProductConstants.BC_+bcId, (Serializable) baseCategory);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return baseCategory;
	}
	
	@Override
	public int updateBcDisplayOrder(Integer sbcId, Integer dbcId) {
		int result = 0;
		BaseCategory sbc = this.getByBcId(sbcId);
		BaseCategory dbc = this.getByBcId(dbcId);
		
		//向上或向下
		if(Math.abs(sbc.getDisplayOrder()-dbc.getDisplayOrder()) == 1){
			Integer order = sbc.getDisplayOrder();
			sbc.setDisplayOrder(dbc.getDisplayOrder());
			dbc.setDisplayOrder(order);
			
			if(this.updateBaseCategory(sbc) && this.updateBaseCategory(dbc)){
				result = 1;
			}
		}else{
			Integer value = 0;
			if(sbc.getDisplayOrder()>dbc.getDisplayOrder() && sbc.getDisplayOrder()-dbc.getDisplayOrder()>1){//置顶
				value = 1;
			}else if(sbc.getDisplayOrder()<dbc.getDisplayOrder() && dbc.getDisplayOrder()-sbc.getDisplayOrder()>1){//置底
				value = -1;
			}	
			Integer order = sbc.getDisplayOrder()+0;
			sbc.setDisplayOrder(dbc.getDisplayOrder());
			
			List<Integer> orderIds = this.bcMapper.getBCOrderIds(sbc.getpBcId(), order, value);
			
			if(this.bcMapper.updateBaseOrder(sbc.getpBcId(), order, value) && this.updateBaseCategory(sbc)){
				result = 1;
				
				//清空缓存
				if(orderIds!=null && orderIds.size()>0){
					Object[] keys = new Object[orderIds.size()+1];
					
					for(int i=0; i<orderIds.size(); i++){
						keys[i] = ProductConstants.BC_ + orderIds.get(i);
					}
					//父ID对应的列表缓存清空
					keys[keys.length-1] = ProductConstants.BC_LIST+sbc.getpBcId();
					
					try {
						this.redisTemplate.opsForHash().delete(ProductConstants.BC, keys);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return result;
	}

	@Override
	public int deleteByBcId(Integer bcId) {
		int result = 1;
		
		try {
			BaseCategory bc = this.getByBcId(bcId);
			
			//不存在
			if(bc == null){
				result = -2;
			}else{
				List<BaseCategory> list = this.getBaseCategorysByPId(bcId, SystemConstants.DB_STATUS_VALID);
				if(list==null || list.isEmpty()){//无子类目
					this.bcMapper.deleteByBcId(bcId);
					logger.debug("delete BcId ----->"+bcId);
					try {
						//清空缓存
						this.redisTemplate.opsForHash().delete(ProductConstants.BC, new Object[]{ProductConstants.BC_+bcId, ProductConstants.BC_LIST+bc.getpBcId()});
						logger.debug("remove hashkey from cache "+ProductConstants.BC+" key: "+ProductConstants.BC_+bcId+" "+ProductConstants.BC_LIST+bc.getpBcId());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//判断该类目的父类目是否需要修改为叶子结点
					if(bc.getpBcId() > 0){
						list = this.getBaseCategorysByPId(bc.getpBcId(), SystemConstants.DB_STATUS_VALID);
						if(list==null || list.isEmpty()){//父类目
							bc = this.getByBcId(bc.getpBcId());
							bc.setIsLeaf(true);;
							this.updateBaseCategory(bc);
						}
					}
				}else {//有子类目不允许删除
					result = -1;
				}
			} 
		} catch (Exception e) {
			result = 0;
		}
		
		return result;
	}
	
	@Override
	public Integer insertBaseCategory(BaseCategory category) {
		Integer re = 0;
		
		if(this.bcMapper.insertBaseCategory(category)){
			re = category.getBcId();
			try {
				//清空缓存
				this.redisTemplate.opsForHash().delete(ProductConstants.BC, ProductConstants.BC_LIST+category.getpBcId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(category.getpBcId() > 0){
				BaseCategory pBC = this.getByBcId(category.getpBcId());
				//若父节点为叶子节点，改为非叶子节点
				if(pBC.getIsLeaf()){
					pBC.setIsLeaf(false);
					this.updateBaseCategory(pBC);
				}
			}
			
		}
		
		return re;
	}

	@Override
	public boolean updateBaseCategory(BaseCategory category) {
		boolean result = this.bcMapper.updateBaseCategory(category);
		if(result){
			try {
				//清空缓存
				this.redisTemplate.opsForHash().delete(ProductConstants.BC, new Object[]{ProductConstants.BC_+category.getBcId(), ProductConstants.BC_LIST+category.getpBcId()});
				logger.debug("remove hashkey from cache "+ProductConstants.BC+" key: "+ProductConstants.BC_+category.getBcId()+" "+ProductConstants.BC_LIST+category.getpBcId());
				
				//三级类目清空相关属性规格品牌缓存
				if(ProductConstants.CATE_LEVEL_THREE.equals(category.getBcLevel())){
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+category.getBcId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	@Override
	public Long insertBcAttribute(BcAttribute bcAttr) {
		Long result = 0l;
		
		BcAttribute exist = this.bcAttrMapper.getBcAttributeByAttrIdAndBcId(bcAttr.getBcId(), bcAttr.getAttrId(), bcAttr.getStatus());
		if(exist != null){
			result = -1l;
		}else{
			if(bcAttr.getDisplayOrder() <= 0){
				Integer displayOrder = this.bcAttrMapper.getMaxOrder(bcAttr.getBcId());
				displayOrder = displayOrder==null?1:displayOrder+1;
				bcAttr.setDisplayOrder(displayOrder);
			}
			if(this.bcAttrMapper.insertBcAttribute(bcAttr)){
				result = bcAttr.getBcAttrId();
				
				try {
					//清除缓存中的类目相关信息
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcAttr.getBcId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	@Override
	public int deleteBcAttributeById(Long... bcAttrIds) {
		int result = 1;
		
		try {
			Integer bcId = 0;
			
			if(bcAttrIds!=null && bcAttrIds.length>0){
				for(Long bcAttrId : bcAttrIds){
					//查询该类目属性的值列表并删除
					List<BcAttrValueVO> bavList = this.getBcAttrValueByBcAttrId(bcAttrId, SystemConstants.DB_STATUS_VALID, false);
					if(bavList!=null && bavList.size()>0){
						for(BcAttrValueVO bcAttrValue: bavList){
							//有子属性删除子属性
							if(bcAttrValue.getIsSubAttr()){
								this.bcAttrValueMapper.deleteBcAttrValueByPId(bcAttrValue.getBcAvId());
							}
							
							if(bcId == 0){
								bcId = bcAttrValue.getBcId();
							}
						}
						
						this.bcAttrValueMapper.deleteBcAttrValueByBcAttrId(bcAttrId);
					}
					
					if(this.bcAttrMapper.deleteBcAttributeById(bcAttrId)){
						if(bcId == 0){
							bcId = this.getBcAttributeById(bcAttrId).getBcId();
						}
						
						result = 1 & result;
					}
				}
				
				if(bcId > 0){
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcId);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	@Override
	public boolean updateBcAttributeById(BcAttribute bcAttr) {
		boolean result = this.bcAttrMapper.updateBcAttributeById(bcAttr);
		if(result){
			try {
				//清除缓存中的类目相关信息
				this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcAttr.getBcId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	@Override
	public BcAttribute getBcAttributeById(Long bcAttrId) {
		return this.bcAttrMapper.getBcAttributeById(bcAttrId);
	}
	
	@Override
	public List<BcAttributeVO> getBcAttributeByBcId(Integer bcId, String status) {
		return this.bcAttrMapper.getBcAttributeByBcId(bcId, status);
	}
	
	@Override
	public boolean updateBcAttributeOrder(Long sbcaId, Long dbcaId) {
		boolean result = false;
		BcAttribute sbca = this.getBcAttributeById(sbcaId);
		BcAttribute dbca = this.getBcAttributeById(dbcaId);
		
		//向上或向下
		if(Math.abs(sbca.getDisplayOrder()-dbca.getDisplayOrder()) == 1){
			int order = sbca.getDisplayOrder();
			sbca.setDisplayOrder(dbca.getDisplayOrder());
			dbca.setDisplayOrder(order);
			
			if(this.updateBcAttributeById(sbca) && this.updateBcAttributeById(dbca)){
				result = true;
			}
		}else{
			Integer value = 0;
			if(sbca.getDisplayOrder()>dbca.getDisplayOrder() && sbca.getDisplayOrder()-dbca.getDisplayOrder()>1){//置顶
				value = 1;
			}else if(sbca.getDisplayOrder()<dbca.getDisplayOrder() && dbca.getDisplayOrder()-sbca.getDisplayOrder()>1){//置底
				value = -1;
			}	
			Integer order = sbca.getDisplayOrder();
			sbca.setDisplayOrder(dbca.getDisplayOrder());
			
			if(this.bcAttrMapper.updateBcAttributeOrders(sbca.getBcId(), order, value) && this.updateBcAttributeById(sbca)){
				result = true;
				
				//清空缓存
				try {
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+sbca.getBcId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	@Override
	public BcAttrValue getBcAttrValueById(Long bcAvId) {
		return this.bcAttrValueMapper.getBcAttrValueById(bcAvId);
	}
	@Override
	public List<BcAttrValueVO> getBcAttrValueByBcAttrId(Long bcAttrId, String status, boolean sub) {
		List<BcAttrValueVO> bcAttrValues = this.bcAttrValueMapper.getBcAttrValueByBcAttrId(bcAttrId, status);
		
		if(sub){
			for(BcAttrValueVO bcAttrValue : bcAttrValues){
				if(bcAttrValue.getIsSubAttr()){
					List<BcAttrValueVO> subList = this.bcAttrValueMapper.getBcAttrValueByPBcAvId(bcAttrValue.getBcAvId(), status);
					if(subList!=null && subList.size()>0){
						bcAttrValue.setSubList(subList);
					}
				}
			}
		}
		 
		return bcAttrValues;
	}
	@Override
	public Page<BcAttrValueVO> getBcAttrValueByBcAttrIdPage(Long bcAttrId,
			String status, Page<BcAttrValueVO> page) {
		page.setResult(this.bcAttrValueMapper.getBcAttrValueByBcAttrIdPage(bcAttrId, status, page));;
		return page;
	}
	@Override
	public Page<BcAttrValueVO> getBcAttrValueByPBcAvIdPage(Long pBcAvId,
			String status, Page<BcAttrValueVO> page) {
		page.setResult(this.bcAttrValueMapper.getBcAttrValueByPBcAvIdPage(pBcAvId, status, page));
		return page;
	}
	@Override
	public List<BcAttrValue> getBcAttrValueByAttrValueId(Long attrValueId) {
		return this.bcAttrValueMapper.getBcAttrValueByAttrValueId(attrValueId);
	}
	@Override
	public List<BcAttrValueVO> getBcAttrValueByPBcAvId(Long pBcAvId, String status) {
		return this.bcAttrValueMapper.getBcAttrValueByPBcAvId(pBcAvId, status);
	}
	@Override
	public int deleteBcAttrValueById(Long bcAvId) {
		int re = 0;
		
		BcAttrValue bv = this.getBcAttrValueById(bcAvId);
		if(bv != null){
			boolean result = true;
			try {
				//删除子属性关系列表
				if(bv.getIsSubAttr()){
					this.bcAttrValueMapper.deleteBcAttrValueByPId(bv.getBcAvId());
				}
				
				result = this.bcAttrValueMapper.deleteBcAttrValueById(bcAvId);
				
				if(result){
					re = 1;
					
					//清除缓存中的类目相关信息
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bv.getBcId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return re;
	}
	@Override
	public Long insertBcAttrValue(BcAttrValue bcAttrValue) {
		Long result = 0l;
		
		if(this.bcAttrValueMapper.insertBcAttrValue(bcAttrValue)){
			result = bcAttrValue.getBcAvId();
			
			try {
				//清除缓存中的类目相关信息
				this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcAttrValue.getBcId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//修改父属性值表示有子属性值的状态为真
			if(bcAttrValue.getpBcAvId() > 0){
				BcAttrValue pBcAttrValue = this.getBcAttrValueById(bcAttrValue.getpBcAvId());
				if(!pBcAttrValue.getIsSubAttr()){
					pBcAttrValue = new BcAttrValue();
					pBcAttrValue.setBcAvId(pBcAttrValue.getBcAvId());
					pBcAttrValue.setIsSubAttr(true);
					this.updateBcAttrValueById(pBcAttrValue);
				}
			}
		}
		
		return result;
	}
	
	@Override
	public Long insertBcAttrValue(BcAttrValue bcAttrValue, Long attrId, Long pAvId) {
		Long result = 0l;
		
		try {
			if(attrId > 0){
				//获取类目属性关系ID
				Long bcAttrId = 0l;
				BcAttribute exist = this.bcAttrMapper.getBcAttributeByAttrIdAndBcId(bcAttrValue.getBcId(), attrId, SystemConstants.DB_STATUS_VALID);
				if(exist != null){
					bcAttrId = exist.getBcAttrId();
				}else{
					exist = new BcAttribute();
					exist.setAttrId(attrId);
					exist.setBcId(bcAttrValue.getBcId());
					exist.setDisplayMode("1");
					Integer displayOrder = this.bcAttrMapper.getMaxOrder(bcAttrValue.getBcId());
					displayOrder = displayOrder==null?1:displayOrder+1;
					exist.setDisplayOrder(displayOrder);
					exist.setIsFilter(true);
					exist.setIsRequire(true);
					exist.setStatus(SystemConstants.DB_STATUS_VALID);
					 
					if(this.bcAttrMapper.insertBcAttribute(exist)){
						bcAttrId = exist.getBcAttrId();
					}
				}
				
				//一级类目属性值关系
				if(pAvId==null || pAvId<=0){
					BcAttrValue bcAttrValue2 = this.bcAttrValueMapper.getBcAttrValue(bcAttrId, bcAttrValue.getAttrValueId(), null);
					if(bcAttrValue2 == null){
						bcAttrValue.setBcAttrId(bcAttrId);
						Integer displayOrder = this.bcAttrValueMapper.getMaxDisplayOrder(bcAttrId, null);
						displayOrder = displayOrder==null?1:displayOrder+1;
						bcAttrValue.setDisplayOrder(displayOrder);
						bcAttrValue.setpBcAvId(0l);
						
						if(this.bcAttrValueMapper.insertBcAttrValue(bcAttrValue)){
							result = bcAttrValue.getBcAvId();
							
							try {
								//清除缓存中的类目相关信息
								this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcAttrValue.getBcId());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}else{//已存在
						result = -1l;
					}
				}else{//二级类目属性值关系
					//检查一级类目属性值关系
					Long pBcAvId = 0l;
					BcAttrValue bcAttrValue2 = this.bcAttrValueMapper.getBcAttrValue(bcAttrId, pAvId, null);
					Long attrValueId = bcAttrValue.getAttrValueId();
					//一级不存在先保存一级
					if(bcAttrValue2 == null){
						bcAttrValue.setBcAttrId(bcAttrId);
						bcAttrValue.setAttrValueId(pAvId);
						bcAttrValue.setIsSubAttr(true);
						bcAttrValue.setpBcAvId(0l);
						Integer displayOrder = this.bcAttrValueMapper.getMaxDisplayOrder(bcAttrId, null);
						displayOrder = displayOrder==null?1:displayOrder+1;
						bcAttrValue.setDisplayOrder(displayOrder);
						if(this.bcAttrValueMapper.insertBcAttrValue(bcAttrValue)){
							pBcAvId = bcAttrValue.getBcAvId();
						}
					}else{//已存在
						pBcAvId = bcAttrValue2.getBcAvId();
						bcAttrValue2.setIsSubAttr(true);
						
						this.updateBcAttrValueById(bcAttrValue2);
					}
					
					bcAttrValue2 = this.bcAttrValueMapper.getBcAttrValue(null, attrValueId, pBcAvId);
					//二级不存在
					if(bcAttrValue2 == null){
						bcAttrValue.setBcAvId(0l);
						bcAttrValue.setBcAttrId(0l);
						bcAttrValue.setAttrValueId(attrValueId);
						bcAttrValue.setpBcAvId(pBcAvId);
						bcAttrValue.setIsSubAttr(false);
						Integer displayOrder = this.bcAttrValueMapper.getMaxDisplayOrder(null, pBcAvId);
						displayOrder = displayOrder==null?1:displayOrder+1;
						bcAttrValue.setDisplayOrder(displayOrder);
						
						if(this.bcAttrValueMapper.insertBcAttrValue(bcAttrValue)){
							result = bcAttrId;
							
							try {
								//清除缓存中的类目相关信息
								this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcAttrValue.getBcId());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}else{//已存在
						result = -1l;
					}
				}
			}else{
				result = -2l;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public boolean updateBcAttrValueById(BcAttrValue bcAttrValue) {
		boolean result = this.bcAttrValueMapper.updateBcAttrValueById(bcAttrValue);
		if(result){
			try {
				//清除缓存中的类目相关信息
				this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcAttrValue.getBcId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	@Override
	public boolean updateBcAttrValueOrders(Integer flag, Long sBcAvId, Long dBcAvId) {
		boolean result = false;
		BcAttrValue sbcav = this.getBcAttrValueById(sBcAvId);
		BcAttrValue dbcav = this.getBcAttrValueById(dBcAvId);
		
		//向上或向下
		if(Math.abs(sbcav.getDisplayOrder()-dbcav.getDisplayOrder()) == 1){
			int order = sbcav.getDisplayOrder();
			sbcav.setDisplayOrder(dbcav.getDisplayOrder());
			dbcav.setDisplayOrder(order);
			
			if(this.updateBcAttrValueById(sbcav) && this.updateBcAttrValueById(dbcav)){
				result = true;
			}
		}else{
			Integer value = 0;
			if(sbcav.getDisplayOrder()>dbcav.getDisplayOrder() && sbcav.getDisplayOrder()-dbcav.getDisplayOrder()>1){//置顶
				value = 1;
			}else if(sbcav.getDisplayOrder()<dbcav.getDisplayOrder() && dbcav.getDisplayOrder()-sbcav.getDisplayOrder()>1){//置底
				value = -1;
			}	
			Integer order = sbcav.getDisplayOrder();
			sbcav.setDisplayOrder(dbcav.getDisplayOrder());
			 
			if(this.bcAttrValueMapper.updateBcAttrValueOrders(flag, flag==0?sbcav.getBcAttrId():sbcav.getpBcAvId(), order, value) && this.updateBcAttrValueById(sbcav)){
				result = true;
				
				//清空缓存
				try {
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+sbcav.getBcId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	
	@Override
	public Long insertBcSpec(BcSpec bcSpec) {
		Long result = 0l;
		
		if(this.bcSpecMapper.insertBcSpec(bcSpec)){
			result = bcSpec.getBcSpecId();
			
			try {
				//清除缓存中的类目相关信息
				this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcSpec.getSpecId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	@Override
	public int deleteBcSpecById(Long bcSpecId) {
		int result = 1;
		//获取规格值列表
		List<BcSpecValue> bsv = this.getBcSpecValueByBcSpecId(bcSpecId, SystemConstants.DB_STATUS_VALID);
		if(bsv==null || bsv.isEmpty()){
			BcSpec bs = this.getBcSpecById(bcSpecId);
			if(this.bcSpecMapper.deleteBcSpecById(bcSpecId) && bs!=null){
				try {
					//清除缓存中的类目相关信息
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bs.getBcId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				result = 0;
			}
		}else{
			result = -1;
		}
		
		return result;
	}
	
	@Override
	public boolean deleteBcSpecByIdForce(Long bcSpecId) {
		List<BcSpecValue> bsv = this.getBcSpecValueByBcSpecId(bcSpecId, SystemConstants.DB_STATUS_VALID);
		if(bsv!=null && !bsv.isEmpty()){
			if(this.bcSpecValueMapper.deleteBcSpecValueByBcSpecId(bcSpecId) <= 0){
				return false;
			}
		}
		BcSpec bs = this.getBcSpecById(bcSpecId);
		try {
			//清除缓存中的类目相关信息
			this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bs.getBcId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.bcSpecMapper.deleteBcSpecById(bcSpecId);
	}
	
	@Override
	public boolean updateBcSpecById(BcSpec bcSpec) {
		boolean result = this.bcSpecMapper.updateBcSpecById(bcSpec);
		if(result){
			try {
				//清除缓存中的类目相关信息
				this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcSpec.getBcId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public boolean updateBcSpecOrder(Long sbcsId, Long dbcsId) {
		boolean result = false;
		BcSpec sbcs = this.getBcSpecById(sbcsId);
		BcSpec dbcs = this.getBcSpecById(dbcsId);
		
		//向上或向下
		if(Math.abs(sbcs.getDisplayOrder()-dbcs.getDisplayOrder()) == 1){
			int order = sbcs.getDisplayOrder();
			sbcs.setDisplayOrder(dbcs.getDisplayOrder());
			dbcs.setDisplayOrder((short)order);
			
			if(this.updateBcSpecById(sbcs) && this.updateBcSpecById(dbcs)){
				result = true;
			}
		}else{
			Integer value = 0;
			if(sbcs.getDisplayOrder()>dbcs.getDisplayOrder() && sbcs.getDisplayOrder()-dbcs.getDisplayOrder()>1){//置顶
				value = 1;
			}else if(sbcs.getDisplayOrder()<dbcs.getDisplayOrder() && dbcs.getDisplayOrder()-sbcs.getDisplayOrder()>1){//置底
				value = -1;
			}	
			Integer order = sbcs.getDisplayOrder()+0;
			sbcs.setDisplayOrder(dbcs.getDisplayOrder());
			
			if(this.bcSpecMapper.updateBcSpecOrders(sbcs.getBcId(), order, value) && this.updateBcSpecById(sbcs)){
				result = true;
				
				//清空缓存
				try {
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+sbcs.getBcId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	@Override
	public List<BcSpecVO> getBcSpecByBcId(Integer bcId, String status) {
		RWPlugin.setUseWriteDbOnly(Boolean.TRUE);
		List<BcSpecVO> bcSpecList = this.bcSpecMapper.getBcSpecByBcId(bcId, status);
		RWPlugin.setUseWriteDbOnly(Boolean.FALSE);
		return bcSpecList;
	}

	@Override
	public BcSpec getBcSpecById(Long bcSpecId) {
		return this.bcSpecMapper.getBcSpecById(bcSpecId);
	}
	@Override
	public Page<BcSpecVO> getBcSpecByBcIdPage(Integer bcId, String status, Page<BcSpecVO> page) {
		page.setResult(this.bcSpecMapper.getBcSpecByBcIdPage(bcId, status, page));
		
		return page;
	}
	
	
	
	@Override
	public Long insertBcSpecValue(BcSpecValue bcSpecValue) {
		Long result = 0l;
		
		if(this.bcSpecValueMapper.insertBcSpecValue(bcSpecValue)){
			result = bcSpecValue.getBcSvId();
			
			try {
				//清除缓存中的类目相关信息
				RWPlugin.setUseWriteDbOnly(Boolean.TRUE);
				this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+this.getBcSpecById(bcSpecValue.getBcSpecId()).getBcId());
				RWPlugin.setUseWriteDbOnly(Boolean.FALSE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	@Override
	public List<BcSpecValue> getBcSpecValueBySpecValueId(Long specValueId, String status) {
		return this.bcSpecValueMapper.getBcSpecValueBySpecValueId(specValueId, status);
	}
	@Override
	public boolean deleteBcSpecValueById(Long bcSvId) {
		BcSpecValue bsv = this.bcSpecValueMapper.getBcSpecValueById(bcSvId);
		boolean result = this.bcSpecValueMapper.deleteBcSpecValueById(bcSvId);
		if(result && bsv!=null){
			try {
				//清除缓存中的类目相关信息
				this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bsv.getBcId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public boolean updateBcSpecValueById(BcSpecValue bcSpecValue) {
		boolean result = this.bcSpecValueMapper.updateBcSpecValueById(bcSpecValue);
		if(result){
			try {
				//清除缓存中的类目相关信息
				this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcSpecValue.getBcId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public int updateBcSpecValueBySpecValueId(Long specValueId, String specValueName, String imgUrl){
		int result = this.bcSpecValueMapper.updateBcSpecValueBySpecValueId(specValueId, specValueName, imgUrl);
		if(result > 0){
			List<BcSpecValue> bcSpecValues = this.getBcSpecValueBySpecValueId(specValueId, SystemConstants.DB_STATUS_VALID);
			try{
				for(int i=0;i<bcSpecValues.size();i++){
					//清除缓存中的类目相关信息
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+bcSpecValues.get(i).getBcId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public boolean updateBcSpecValueOrder(Long sbcsvId, Long dbcsvId) {
		boolean result = false;
		
		BcSpecValue sbcsv = this.getBcSpecValueById(sbcsvId);
		BcSpecValue dbcsv = this.getBcSpecValueById(dbcsvId);
		
		//向上或向下
		if(Math.abs(sbcsv.getDisplayOrder()-dbcsv.getDisplayOrder()) == 1){
			short order = sbcsv.getDisplayOrder();
			sbcsv.setDisplayOrder(dbcsv.getDisplayOrder());
			dbcsv.setDisplayOrder(order);
			
			if(this.updateBcSpecValueById(sbcsv) && this.updateBcSpecValueById(dbcsv)){
				result = true;
			}
		}else{
			Integer value = 0;
			if(sbcsv.getDisplayOrder()>dbcsv.getDisplayOrder() && sbcsv.getDisplayOrder()-dbcsv.getDisplayOrder()>1){//置顶
				value = 1;
			}else if(sbcsv.getDisplayOrder()<dbcsv.getDisplayOrder() && dbcsv.getDisplayOrder()-sbcsv.getDisplayOrder()>1){//置底
				value = -1;
			}	
			Integer order = sbcsv.getDisplayOrder()+0;
			sbcsv.setDisplayOrder(dbcsv.getDisplayOrder());
			
			if(this.bcSpecValueMapper.updateBcSpecValueOrders(sbcsv.getBcSpecId(), order, value) && this.updateBcSpecValueById(sbcsv)){
				result = true;
				
				//清空缓存
				try {
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+sbcsv.getBcId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	@Override
	public BcSpecValue getBcSpecValueById(Long bcSvId) {
		return this.bcSpecValueMapper.getBcSpecValueById(bcSvId);
	}
	@Override
	public List<BcSpecValue> getBcSpecValueByBcSpecId(Long bcSpecId, String status) {
		return this.bcSpecValueMapper.getBcSpecValueByBcSpecId(bcSpecId, status);
	}
	@Override
	public Page<BcSpecValue> getBcSpecValueByBcSpecIdPage(Long bcSpecId,
			String status, Page<BcSpecValue> page) {
		page.setResult(this.bcSpecValueMapper.getBcSpecValueByBcSpecIdPage(bcSpecId, status, page));
		return page;
	}
	@Override
	public Long insertBrand(Brand brand) {
		Long id = 0l;
		if(this.brandMapper.insertBrand(brand)){
			id = brand.getBrandId();
		}
		
		return id;
	}

	@Override
	public boolean updateByBrandId(Brand brand) {
		boolean re = this.brandMapper.updateByBrandId(brand);
		if(re){
			List<BrandBc> bbcList = this.brandBcMapper.getByBrandIdAndBcId(brand.getBrandId(), null);
			
			if(bbcList!=null && bbcList.size()>0){
				Object[] keys = new Object[bbcList.size()];
				
				for(int i=0; i<bbcList.size(); i++){
					keys[i] = ProductConstants.BC_ATTR_SPEC_BRAND + bbcList.get(i).getBcId();
				}
				
				try {
					//清空缓存
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, keys);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return re;
	}

	@Override
	public int deleteByBrandId(Long brandId) {
		int result = 1;
		
		//已关联类目不允许删除
		List<BrandBc> bbcList = this.brandBcMapper.getByBrandIdAndBcId(brandId, null);
		if(bbcList!=null && bbcList.size()>0){
			result = -1;
		}else{
			Brand brand = this.brandMapper.getByBrandId(brandId);
			brand.setStatus(SystemConstants.DB_STATUS_INVALID);
			//修改为无效状态
			result = this.brandMapper.updateByBrandId(brand)?1:0;
		}
		
		return result;
	}

	@Override
	public Brand getByBrandId(Long brandId) {
		return this.brandMapper.getByBrandId(brandId);
	}

	@Override
	public Brand getBrandByName(String brandName, String brandEname, String status) {
		return this.brandMapper.getBrandByName(brandName, brandEname, status);
	}

	@Override
	@UseWriteDbOnly
	public Page<BrandBcVO> getBrandsByPage(Map<?, ?> map, Page<BrandBcVO> page) {
		page.setResult(this.brandMapper.getBrandsByPage(map, page));
		
		return page;
	}
	
	
	@Override
	public boolean deleteById(Long brandBcId) {
		BrandBc brandBc = getBrandBcById(brandBcId);
		
		boolean re = this.brandBcMapper.deleteById(brandBcId);
		if(re){
			try {
				//清除缓存
				this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+brandBc.getBcId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//检查该品牌是否还有别的类目关联，若无则修改品牌关联状态
			RWPlugin.setUseWriteDbOnly(Boolean.TRUE);
			List<BrandBc> bbList = this.brandBcMapper.getByBrandIdAndBcId(brandBc.getBrandId(), null);
			RWPlugin.setUseWriteDbOnly(Boolean.FALSE);
			
			if(bbList==null || bbList.size()==0){
				//修改品牌状态
				Brand brand = this.brandMapper.getByBrandId(brandBc.getBrandId());
				brand.setBrandStatus(ProductConstants.BRAND_UNLINK);
				this.brandMapper.updateByBrandId(brand);
			}
		}
		
		return re;
	}
	
	
	@Override
	public boolean modBrandCateShow(Long brandBcId, Integer flag, Boolean value) {
		return this.brandBcMapper.modBrandCateShow(brandBcId, flag, value);
	}

	@Override
	public boolean delByBrandId(Long brandId) {
		boolean re = false;
		List<BrandBc> bbList = this.brandBcMapper.getByBrandIdAndBcId(brandId, null);
		
		if(bbList!=null && bbList.size()>0){
			re = this.brandBcMapper.deleteByBrandId(brandId);
			if(re){
				//修改品牌状态
				Brand brand = this.brandMapper.getByBrandId(brandId);
				brand.setBrandStatus(ProductConstants.BRAND_UNLINK);
				this.brandMapper.updateByBrandId(brand);
				
				Object[] keys = new Object[bbList.size()];
				
				for(int i=0; i<bbList.size(); i++){
					keys[i] = ProductConstants.BC_ATTR_SPEC_BRAND + bbList.get(i).getBcId();
				}
				
				try {
					//清空缓存
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, keys);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return re;
	}

	@Override
	public Long insertBrandBc(BrandBc brandBc) {
		Long id = 0l;
		boolean re = this.brandBcMapper.insertBrandBc(brandBc);
		if(re){
			id = brandBc.getBrandBcId();
			try {
				//清除缓存
				this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+brandBc.getBcId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return id;
	}
	
	@Override
	public BrandBc getBrandBcById(Long brandBcId) {
		return this.brandBcMapper.getBrandBcById(brandBcId);
	}
	
	@Override
	public List<BrandBc> getByBrandIdAndBcId(Long brandId, Integer bcId) {
		return this.brandBcMapper.getByBrandIdAndBcId(brandId, bcId);
	}
	
	@Override
	public boolean updateBrandBcById(BrandBc brandBc) {
		BrandBc brandBcS = getBrandBcById(brandBc.getBrandBcId());
		
		boolean re = this.brandBcMapper.updateBrandBcById(brandBc);
		if(re){
			try {
				//清除缓存
				this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+brandBcS.getBcId());
				if(brandBcS.getBcId().intValue() != brandBc.getBcId()){
					this.redisTemplate.opsForHash().delete(ProductConstants.BASB, ProductConstants.BC_ATTR_SPEC_BRAND+brandBc.getBcId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return re;
	}

	
	
	

	/**
	 * 获取所有子类目
	 * @param list
	 */
	protected void getChildBaseCategory(List<BaseCategory> list, String status) {
		if(list!=null && list.size()>0){
			for(BaseCategory bc : list){
				//非叶子类目
				if(!bc.isLeaf()){
					List<BaseCategory> subList = this.getBaseCategorysByPId(bc.getBcId(), status);
					bc.setCategories(subList);
					this.getChildBaseCategory(subList, status);
				}
			}
		}
	}
	/**
	 * 获取所有父类目
	 * @param list
	 */
	protected void getParentBaseCategory(BaseCategory bc) {
		if(bc.getpBcId() > 0){
			BaseCategory p = this.getByBcId(bc.getpBcId());
			bc.setpBc(p);
			this.getParentBaseCategory(p);
		}
	}
	
	/**
	 * 获取所有子类目
	 * @param list
	 */
	protected void getSubContractCategory(List<ContractCategory> list, String status) {
		if(list!=null && list.size()>0){
			for(ContractCategory cc : list){
				//非叶子类目
				if(!cc.isLeaf()){
					List<ContractCategory> subList = this.getContractCategorysByPId(cc.getCcId(), status);
					cc.setCategories(subList);
					this.getSubContractCategory(subList, status);
				}
			}
		}
	}
	
	/**
	 * 获取所有子类目
	 * @param list
	 */
	protected void getSubSaleCategory(List<SaleCategory> list, String status) {
		if(list!=null && list.size()>0){
			for(SaleCategory sc : list){
				//非叶子类目
				if(!sc.isLeaf()){
					List<SaleCategory> subList = this.getSaleCategorysByPId(sc.getScId(), null, status, null);
					sc.setCategories(subList);
					this.getSubSaleCategory(subList, status);
				}
			}
		}
	}
	
	/**
	 * 循环获取属性值的所有子属性名称和值
	 * @param bcav
	 * @return
	 */
	protected Attr getBcAttrValueList(BcAttrValue bcav){
		Attr subAttr = null;
		List<AttrValue> attrValueList = null;
		List<BcAttrValueVO> bcAttrValueList  = this.getBcAttrValueByPBcAvId(bcav.getBcAvId(), SystemConstants.DB_STATUS_VALID);
		if(bcAttrValueList!=null && bcAttrValueList.size()>0){
			subAttr = new Attr();
			attrValueList = new ArrayList<AttrValue>(bcAttrValueList.size());
			subAttr.setAttrValueList(attrValueList);
			AttrValue av = null;
			
			for(BcAttrValueVO bav : bcAttrValueList){
				av = new AttrValue();
				attrValueList.add(av);
				
				//子属性名相关信息,子属性名ID存放在bcAttrId字段
				/*if(StringUtils.isEmpty(subAttr.getAttrName())){
					Attribute at = this.attrMapper.getAttributeById(bav.getBcAttrId());
					subAttr.setAttrId(at.getAttrId());
					subAttr.setAttrName(at.getAttrName());
					subAttr.setDisplayMode(at.getDisplayMode());
					subAttr.setDisplayOrder(1);
				}*/
				av.setBcAvId(bav.getBcAvId());
				av.setAttrValueId(bav.getAttrValueId());
				av.setAttrValue(bav.getAttrValueName());
				av.setDisplayOrder(bav.getDisplayOrder());
				av.setIcon(bav.getIcon());
				av.setIsSubAttr(bav.getStatus());
				av.setIsFilter(bav.getIsFilter());
				av.setIsMobileDisplay(bav.getIsMobileDisplay());
				av.setmDisplayPosition(bav.getmDisplayPosition());
				//若有子属性循环获取
				if(bav.getIsSubAttr()){
					av.setSubAttrObj(this.getBcAttrValueList(bav));;
				}
			}
		}
		
		return subAttr;
	}

	@Override
	public Map<Integer,BaseCategory> getAllBcNameByBcIds(Set<Integer> bcIds,
			String status) {
		Map<Integer,BaseCategory> resultMap = new HashMap<Integer,BaseCategory>();
		for (Integer bcId : bcIds) {
			BaseCategory bc = new BaseCategory();
			BaseCategory threeBc = this.getByBcId(bcId);
			String pathName = threeBc.getPathName();
			pathName += "/"+ threeBc.getBcName();
			String bcName = pathName.replace("|", "/");
			bc.setBcName(bcName);
			bc.setBcCode(threeBc.getBcCode());
			resultMap.put(bcId, bc);
		}
		return resultMap;
	}

	@Override
	public String getAllBcNameByBcId(String cateLevelThree, int bcId,
			Object status) {
		BaseCategory threeBc = this.getByBcId(bcId);
		String pathName = threeBc.getPathName();
		pathName += "/"+ threeBc.getBcName();
		pathName.replace("|", "/");
		return pathName;
	}

}
