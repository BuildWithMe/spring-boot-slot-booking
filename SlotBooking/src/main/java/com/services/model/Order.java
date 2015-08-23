package com.services.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request object
 * @author Shahbaz
 *
 */
public class Order {
	
	private List<Item> items;
	
	private BigDecimal totalVolume;

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public BigDecimal getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}
	
}
