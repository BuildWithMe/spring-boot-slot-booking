package com.services.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.services.dao.SlotBookingDao;
import com.services.exception.ServiceException;
import com.services.model.Item;
import com.services.model.ItemDetails;
import com.services.model.Order;
import com.services.model.Slot;
import com.services.model.SlotInfo;

/**
 * The Manager class for all business logic
 * 
 * @author Shahbaz
 *
 */
@Component
public class BookingManager {
	@Autowired
	private SlotBookingDao slotBookingdao;

	public SlotInfo bookSlot(Order order) throws ServiceException{
		Slot slot = getTime();
		int count = 0;
		Integer eligibleSlotStartHour = 0;
		if(slot != null){
			count = slotBookingdao.checkEntry(getCurrentDate());
			eligibleSlotStartHour = slot.getStartHours();
			if(count == 0){
				slotBookingdao.insertSlotInfo(getCurrentDate());
			}
		}		
		calculateOrderVolume(order);
		
		SlotInfo allotedSlot = allotSlot(order, getCurrentDate(), eligibleSlotStartHour);
		return allotedSlot;
	}

	private SlotInfo allotSlot(Order order, String date, Integer eligibleSlotStartHour) throws ServiceException{
		SlotInfo value = null;		
		switch(eligibleSlotStartHour){
		
			case 9:	value = ifSlot1available(order, date);
					if(value != null)
						return value;			
			
			case 11: value = ifSlot2Available(order, date);
					if(value != null)
						return value;	
				
			case 14: value = ifSlot3Available(order, date);
					if(value != null)
						return value;	
				
			case 16: value = ifSlot4Available(order, date);
					if(value != null)
						return value;
			default : checkNextDaySlot(order);
					 return allotSlot(order, getNextDate(date), 9);					
		}
	}

	private void checkNextDaySlot(Order order) throws ServiceException {
		String nextDate = getNextDate(getCurrentDate());
		int count = slotBookingdao.checkEntry(nextDate);
		if(count == 0){
			slotBookingdao.insertSlotInfo(nextDate);
		}
		
	}
	
	private String getCurrentDate() throws ServiceException{
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date crdate = null;
		try {
			crdate = format.parse(format.format(new Date()));
		} catch (ParseException e) {
			throw new ServiceException("Parsing Exception", e);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(crdate);
		String date = format.format(cal.getTime());
		return date;
	}
	
	private String getNextDate(String currentDate) throws ServiceException{
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try{
			date = format.parse(currentDate);
		}catch(ParseException ex){
			throw new ServiceException("Parsing Exception", ex);
		}
		
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		return format.format(calendar.getTime()); 
	}

	private void calculateOrderVolume(Order order) throws ServiceException{
		Map<Integer,ItemDetails> itemMap = slotBookingdao.getItemdetails(order.getItems());
		BigDecimal totalVolume = BigDecimal.ZERO;
		for(Item item : order.getItems()){
			ItemDetails itemDetail = itemMap.get(item.getId());
			BigDecimal volForTotalUnitsOfItem = itemDetail.getVolume().multiply(new BigDecimal(item.getNoOfUnits()));
			totalVolume = totalVolume.add(volForTotalUnitsOfItem);
		}
		order.setTotalVolume(totalVolume);		
	}

	private SlotInfo ifSlot4Available(Order order, String date) throws ServiceException {
		List<SlotInfo> listSlotInfo = slotBookingdao.getSlotDetails(4, date);
		for(SlotInfo slotInfo : listSlotInfo){
			if(order.getTotalVolume().intValue() <= slotInfo.getAvailableSpace().intValue()){
				slotBookingdao.updateBookDetails(slotInfo, order.getTotalVolume());
				return slotInfo;
			}
		}
		return null;
	}

	private SlotInfo ifSlot3Available(Order order, String date) throws ServiceException{
		List<SlotInfo> listSlotInfo = slotBookingdao.getSlotDetails(3, date);
		for(SlotInfo slotInfo : listSlotInfo){
			if(order.getTotalVolume().intValue() <= slotInfo.getAvailableSpace().intValue()){
				slotBookingdao.updateBookDetails(slotInfo, order.getTotalVolume());
				return slotInfo;
			}
		}
		return null;
	}

	private SlotInfo ifSlot2Available(Order order, String date) throws ServiceException{
		List<SlotInfo> listSlotInfo = slotBookingdao.getSlotDetails(2, date);
		for(SlotInfo slotInfo : listSlotInfo){
			if(order.getTotalVolume().intValue() <= slotInfo.getAvailableSpace().intValue()){
				slotBookingdao.updateBookDetails(slotInfo, order.getTotalVolume());
				return slotInfo;
			}
		}
		return null;
	}

	private SlotInfo ifSlot1available(Order order, String date) throws ServiceException{
		List<SlotInfo> listSlotInfo = slotBookingdao.getSlotDetails(1, date);
		for(SlotInfo slotInfo : listSlotInfo){
			if(order.getTotalVolume().intValue() <= slotInfo.getAvailableSpace().intValue()){
				slotBookingdao.updateBookDetails(slotInfo, order.getTotalVolume());
				return slotInfo;
			}
		}
		return null;
	}

	private Slot getTime() {
		SimpleDateFormat format = new SimpleDateFormat("HH");
		String formattedDate = format.format(new Date());
		Integer currentHour = Integer.parseInt(formattedDate);
		if(currentHour < Slot.Slot1.getStartHours()){
			return Slot.Slot1;
		}
		else if(currentHour < Slot.Slot2.getStartHours()){
			return Slot.Slot2;
		}
		else if(currentHour < Slot.Slot3.getStartHours()){
			return Slot.Slot3;
		}
		else if(currentHour < Slot.Slot4.getStartHours()){
			return Slot.Slot4;
		}
		else{
				return null;
			}
		}	
}

