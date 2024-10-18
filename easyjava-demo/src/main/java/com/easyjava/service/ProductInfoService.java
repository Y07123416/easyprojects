package com.easyjava.service;

import com.easyjava.entity.vo.PaginationResultVO;
import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;

import java.util.List;
/**
 * @author:花海
 * @description:商品信息Service
 * @date:2024/10/18
*/
public interface ProductInfoService{

	/**
	 * 根据条件查询列表
	*/
	List<ProductInfo> findListByParam(ProductInfoQuery query);

	/**
	 * 根据条件查询数量
	*/
	Integer findCountByParam(ProductInfoQuery query);

	/**
	 * 分页查询
	*/
	PaginationResultVO<ProductInfo> findListByPage(ProductInfoQuery query);

	/**
	 * 新增
	*/
	Integer add(ProductInfo bean);

	/**
	 * 批量新增
	*/
	Integer addBatch(List<ProductInfo> listBean);

	/**
	 * 批量新增或修改
	*/
	Integer addBatchOrUpdateBatch(List<ProductInfo> listBean);
	/**
	 * 根据Id查询
	*/
	ProductInfo getProductInfoById(Integer id);

	/**
	 * 根据Id更新
	*/
	Integer updateProductInfoById(ProductInfo bean, Integer id);

	/**
	 * 根据Id删除
	*/
	Integer deleteProductInfoById(Integer id);

}
