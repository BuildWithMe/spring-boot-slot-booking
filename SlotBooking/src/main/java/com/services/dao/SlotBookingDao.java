package com.services.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.services.exception.ServiceException;
import com.services.model.Item;
import com.services.model.ItemDetails;
import com.services.model.SlotInfo;

/**
 * The interface for all dao methods
 * 
 * @author Shahbaz
 *
 */
public interface SlotBookingDao {

	List<SlotInfo> getSlotDetails(Integer slotId, String date) throws ServiceException;
	
	void insertSlotInfo(String date) throws ServiceException;
	
	Map<Integer, ItemDetails> getItemdetails(List<Item> items) throws ServiceException;
	
	void updateBookDetails(SlotInfo slotInfo, BigDecimal bigDecimal) throws ServiceException;

	int checkEntry(String date) throws ServiceException;

}
