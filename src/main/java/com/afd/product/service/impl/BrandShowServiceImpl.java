package com.afd.product.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.afd.common.mybatis.Page;
import com.afd.common.util.DateUtils;
import com.afd.constants.product.ProductConstants;
import com.afd.constants.product.ProductConstants.BrandShow$Status;
import com.afd.constants.product.ProductConstants.BrandShowDetail$Status;
import com.afd.model.order.LogisticsCompany;
import com.afd.model.product.BrandShow;
import com.afd.model.product.BrandShowDetail;
import com.afd.product.dao.BrandShowDetailMapper;
import com.afd.product.dao.BrandShowMapper;
import com.afd.service.order.ILogisticsCompanyService;
import com.afd.service.product.IBrandShowService;
import com.google.common.collect.Lists;

@Service("brandShowService")
public class BrandShowServiceImpl implements IBrandShowService {
	private static Logger logger = Logger.getLogger(BrandShowServiceImpl.class);

	@Autowired
	private BrandShowMapper brandShowMapper;
	@Autowired
	private BrandShowDetailMapper brandShowDetailMapper;

	private ILogisticsCompanyService logicticsCompanyService;

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public BrandShow getBrandShowById(int brandShowId) {
		return brandShowMapper.selectByPrimaryKey(brandShowId);
	}

	@Override
	public BrandShowDetail getBrandShowDetailById(int brandShowDetailId) {
		BrandShowDetail bsd = this.brandShowDetailMapper
				.selectByPrimaryKey(brandShowDetailId);
		int stock;
		if (null == bsd.getShowBalance() || bsd.getShowBalance() <= 0) {
			bsd.setShowBalance(0);
			stock = 0;
		} else if (null == bsd.getSaleAmount() || bsd.getSaleAmount() <= 0) {
			bsd.setSaleAmount(0);
			stock = bsd.getShowBalance();
		} else {
			stock = bsd.getShowBalance() - bsd.getSaleAmount();
		}
		try {
			String strStock = this.redisTemplate.opsForValue().get(
					ProductConstants.CACHE_PERFIX_INVENTORY + bsd.getbSDId());
			if (StringUtils.isEmpty(strStock) || "null".equals(strStock)) {
				strStock = stock + "";
				this.redisTemplate.opsForValue().set(
						ProductConstants.CACHE_PERFIX_INVENTORY
								+ bsd.getbSDId(), strStock);
			} else {
				stock = Integer.parseInt(strStock);
				int saleAmount = bsd.getShowBalance() - stock;
				if (saleAmount >= 0) {
					bsd.setSaleAmount(saleAmount);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return bsd;
	}

	@Override
	public List<BrandShow> getBrandShowByIds(List<Integer> brandShowIds) {
		return brandShowMapper.getBrandShowByIds(brandShowIds);
	}

	@Override
	public List<BrandShowDetail> getBrandShowDetailsByIds(
			List<Integer> brandShowDetailIds) {
		List<BrandShowDetail> bsds = brandShowDetailMapper
				.getBrandShowDetailsByIds(brandShowDetailIds);
		if (null != bsds && bsds.size() != 0) {
			for (BrandShowDetail bsd : bsds) {
				int stock;
				if (null == bsd.getShowBalance() || bsd.getShowBalance() <= 0) {
					bsd.setShowBalance(0);
					stock = 0;
				} else if (null == bsd.getSaleAmount()
						|| bsd.getSaleAmount() <= 0) {
					bsd.setSaleAmount(0);
					stock = bsd.getShowBalance();
				} else {
					stock = bsd.getShowBalance() - bsd.getSaleAmount();
				}
				try {
					String strStock = this.redisTemplate.opsForValue().get(
							ProductConstants.CACHE_PERFIX_INVENTORY
									+ bsd.getbSDId());
					if (StringUtils.isBlank(strStock)
							|| "null".equals(strStock)) {
						strStock = stock + "";
						this.redisTemplate.opsForValue().set(
								ProductConstants.CACHE_PERFIX_INVENTORY
										+ bsd.getbSDId(), strStock);
					} else {
						stock = Integer.parseInt(strStock);
						int saleAmount = bsd.getShowBalance() - stock;
						if (saleAmount >= 0) {
							bsd.setSaleAmount(saleAmount);
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		return bsds;
	}

	@Override
	public List<BrandShow> getValidBrandShows(BrandShow record) {
		return this.brandShowMapper.getValidBrandShows(record);
	}

	@Override
	public BigDecimal getLowestPrice(int bsid) {
		return this.brandShowDetailMapper.getLowestPrice(bsid);
	}

	@Override
	public Page<BrandShowDetail> getBrandShowDetailByPage(Map<?, ?> map,
			Page<BrandShowDetail> page) {
		page.setResult(this.brandShowDetailMapper.getBrandShowDetailByPage(map,
				page));
		return page;
	}

	/**
	 * minus saled amount
	 * 
	 * @param stockMap
	 */
	@Override
	public void addStock(Map<Integer, Integer> stockMap) {
		if (null != stockMap && stockMap.size() > 0) {
			for (Entry<Integer, Integer> entry : stockMap.entrySet()) {
				Integer bsdId = entry.getKey();
				Integer stock = entry.getValue();
				this.brandShowDetailMapper.addStock(bsdId, stock);
			}
		}
	}

	@Override
	public int newBrandShow(BrandShow brandShow) {
		brandShow.setStatus(BrandShow$Status.EDITING);
		brandShow.setCreateByDate(DateUtils.currentDate());

		int result = brandShowMapper.insert(brandShow);

		return result > 0 ? brandShow.getBrandShowId() : 0;
	}

	@Override
	public int modifyBrandShow(BrandShow brandShow) {
		brandShow.setStatus(BrandShow$Status.EDITING);
		return brandShowMapper.updateByPrimaryKeySelective(brandShow);
	}

	@Override
	public int submitBrandShow(int brandShowId, BrandShowDetail[] details) {
		int result = brandShowMapper.updateStatus(brandShowId,
				BrandShow$Status.WAIT_AUDIT);

		if (result > 0) {
			List<BrandShowDetail> oldBrandShowDetailList = this
					.getDetailsOfBrandShow(brandShowId);

			for (BrandShowDetail oldBrandShow : oldBrandShowDetailList) {
				boolean found = false;

				for (BrandShowDetail detail : details) {
					if (oldBrandShow.getbSDId().equals(detail.getbSDId())) {
						found = true;
						break;
					}
				}

				if (!found) {
					brandShowDetailMapper.updateStatus(oldBrandShow.getbSDId(),
							BrandShowDetail$Status.REMOVED);
				}
			}

			for (BrandShowDetail detail : details) {
				if (detail.getbSDId() != null && detail.getbSDId() != 0) {
					brandShowDetailMapper.updateByPrimaryKeySelective(detail);

				} else {
					detail.setStatus(BrandShowDetail$Status.VALID);
					brandShowDetailMapper.insert(detail);
				}
			}
		}

		return result;
	}

	@Override
	public Page<BrandShow> queryMyBrandShowByPage(int sellerId,
			Map<String, ?> cond, int... page) {

		Page<BrandShow> brandShowPage = new Page<BrandShow>();

		if (page != null) {
			if (page.length > 0) {
				brandShowPage.setCurrentPageNo(page[0]);
			}

			if (page.length > 1) {
				brandShowPage.setPageSize(page[1]);
			}
		}

		brandShowPage.setResult(brandShowMapper.queryBrandShowOfSellerByPage(
				sellerId, cond, brandShowPage));

		return brandShowPage;
	}

	@Override
	public Page<BrandShow> queryWaitAuditBrandShowByPage(Map<String, ?> cond,
			int... page) {

		return null;
	}

	@Override
	public int passAuditBrandShow(int brandShowId, Date startDate,
			Date endDate, String auditor, String opinion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rejectAuditBrandShow(int brandShowId, String auditor,
			String opinion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int startBrandShow(int brandShowId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int endBrandSow(int brandShowId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<BrandShowDetail> getDetailsOfBrandShow(int brandShowId) {
		return brandShowDetailMapper.getValidDetailsOfBrandShow(brandShowId);
	}

	@Override
	public List<BrandShow> getOnlinedBrandShowsOfSeller(int sellerId) {
		return Lists.newArrayList();
	}

	@Override
	public List<LogisticsCompany> getLogisticsCompanyListOfBrandShow(
			int brandShowId) {
		BrandShow brandShow = this.getBrandShowById(brandShowId);

		if (brandShow == null
				|| StringUtils.isEmpty(brandShow.getLogisticsCompIds()))
			return null;

		String[] arr = brandShow.getLogisticsCompIds().split("\\,");
		List<Long> list = Lists.newArrayList();

		for (String id : arr) {
			if (StringUtils.isNotEmpty(id)) {
				list.add(Long.parseLong(id));
			}
		}

		return logicticsCompanyService.getLogisticsCompanyByIds(list);
	}
}
