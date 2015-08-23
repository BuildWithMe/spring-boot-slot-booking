package com.services.model;

/**
 * Slot Enum
 * @author Shahbaz
 *
 */
public enum Slot {
	Slot1(9,11), Slot2(11,13), Slot3(14,16), Slot4(16,18);
	
	private Integer startHours;
	private Integer endHours;
	
	private Slot(Integer startHours, Integer endHours){
		this.setStartHours(startHours);
		this.setEndHours(endHours);
	}

	public Integer getStartHours() {
		return startHours;
	}

	public void setStartHours(Integer startHours) {
		this.startHours = startHours;
	}

	public Integer getEndHours() {
		return endHours;
	}

	public void setEndHours(Integer endHours) {
		this.endHours = endHours;
	}

}
