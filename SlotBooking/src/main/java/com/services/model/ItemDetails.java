package com.services.model;

import java.math.BigDecimal;

/**
 * Domain object for Item_Details table
 * @author Shahbaz
 *
 */
public class ItemDetails {
	
	private Integer id;
	
	private Integer noOfUnits;
	
	private String name;
	
	private BigDecimal height;
	
	private BigDecimal width;
	
	private BigDecimal breadth;
	
	private BigDecimal volume;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNoOfUnits() {
		return noOfUnits;
	}

	public void setNoOfUnits(Integer noOfUnits) {
		this.noOfUnits = noOfUnits;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	public BigDecimal getBreadth() {
		return breadth;
	}

	public void setBreadth(BigDecimal breadth) {
		this.breadth = breadth;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}	

}
