package com.services.model;

import java.math.BigDecimal;

/**
 * Domain object for Slot_Info
 * @author Ashish
 *
 */
public class SlotInfo {
	
	private Integer slotId;
	private Integer vanId;
	private Integer cartonId;
	private String date;
	private BigDecimal totalSpace;
	private BigDecimal availableSpace;
	private boolean isAvailable;
	
	public Integer getSlotId() {
		return slotId;
	}
	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}
	public Integer getVanId() {
		return vanId;
	}
	public void setVanId(Integer vanId) {
		this.vanId = vanId;
	}
	public Integer getCartonId() {
		return cartonId;
	}
	public void setCartonId(Integer cartonId) {
		this.cartonId = cartonId;
	}
	public BigDecimal getTotalSpace() {
		return totalSpace;
	}
	public void setTotalSpace(BigDecimal totalSpace) {
		this.totalSpace = totalSpace;
	}
	public BigDecimal getAvailableSpace() {
		return availableSpace;
	}
	public void setAvailableSpace(BigDecimal availableSpace) {
		this.availableSpace = availableSpace;
	}
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
