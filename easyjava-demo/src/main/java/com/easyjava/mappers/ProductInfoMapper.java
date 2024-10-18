package com.easyjava.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @author:花海
 * @description:商品信息Mapper
 * @date:2024/10/18
*/
public interface ProductInfoMapper<T, P> extends BaseMapper {

	/**
	 * 根据Id查询
	*/
	T selectById(@Param("id") Integer id);
	/**
	 * 根据Id更新
	*/
	Integer updateById(@Param("bean") T t, @Param("id") Integer id);
	/**
	 * 根据Id删除
	*/
	Integer deleteById(@Param("id") Integer id);
}