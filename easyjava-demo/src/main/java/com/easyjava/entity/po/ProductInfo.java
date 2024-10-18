package com.easyjava.entity.po;

import com.easyjava.enums.DateTimePatternEnum;
import com.easyjava.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
/**
 * @author:花海
 * @description:商品信息
 * @date:2024/10/18
*/
public class ProductInfo implements Serializable {
	/**
	 * 自增ID
	*/
	private Integer id;
	/**
	 * 商品名称
	*/
	private String productName;
	/**
	 * 颜色类型
	*/
	@JsonIgnore
	private Integer colorType;
	/**
	 * 创建时间
	*/
	@JsonFormat(pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")
	private Date createtime;
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return id;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductName() {
		return productName;
	}

	public void setColorType(Integer colorType) {
		this.colorType = colorType;
	}
	public Integer getColorType() {
		return colorType;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getCreatetime() {
		return createtime;
	}

	@Override
	public String toString() {
		return "自增ID: " + (id == null ? "空": id) + ",商品名称: " + (productName == null ? "空": productName) + ",颜色类型: " + (colorType == null ? "空": colorType) + ",创建时间: " + (createtime == null ? "空": DateUtils.format(createtime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}
}