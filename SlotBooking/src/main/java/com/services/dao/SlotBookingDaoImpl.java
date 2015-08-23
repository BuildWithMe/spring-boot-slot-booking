package com.services.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.services.exception.ServiceException;
import com.services.model.Item;
import com.services.model.ItemDetails;
import com.services.model.SlotInfo;

/**
 * The dao layer for all db interactions
 * 
 * @author Shahbaz
 *
 */
@Component
public class SlotBookingDaoImpl implements SlotBookingDao {
	@Autowired
	@Qualifier("primaryDs")
	private DataSource datasource;
	
	/**
	 * The method checks whether an entry is present for a 
	 * particular date.
	 * 
	 */
	public int checkEntry(String date) throws ServiceException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<Map<String, Object>> resultList = null;
		StringBuilder sql = new StringBuilder();
		Long count = 0l;
		sql.append("select count(*) from slot_info where date = '"+date+"'");
		try{
			resultList = template.queryForList(sql.toString());
		}catch(DataAccessException ex){
			throw new ServiceException("Exception in Slot Info retrieval"+ex);
		}
		if(resultList != null){
			Map<String, Object> map = resultList.get(0);
			count = (Long) map.get("count(*)");
		}
		return count.intValue();
	}

	/**
	 * The method retrieves the Slot_Info details from the db
	 */
	public List<SlotInfo> getSlotDetails(Integer slotId, String date) throws ServiceException{
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<Map<String, Object>> resultList = null;
		List<SlotInfo> listSlotInfo = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select * from slot_info where slot_id = "+slotId+" AND van_id in(1,2,3,4) ");
		sql.append("AND carton_id in(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20) AND is_available = 'Y' AND date = '"+date+"'");
		try{
			resultList = template.queryForList(sql.toString());
		}catch(DataAccessException ex){
			throw new ServiceException("Exception in Slot Info retrieval"+ex);
		}		
		if(resultList != null){
			listSlotInfo = populateSlotInfo(resultList);
		}
		return listSlotInfo;
	}
	
	private List<SlotInfo> populateSlotInfo(List<Map<String, Object>> resultList) {
		List<SlotInfo> listSlotInfo = new ArrayList<SlotInfo>();
		SlotInfo slotInfo = null;
		for(Map<String, Object> resultMap : resultList){
			slotInfo = new SlotInfo();
			slotInfo.setSlotId((Integer) resultMap.get("slot_id"));
			slotInfo.setVanId((Integer) resultMap.get("van_id"));
			slotInfo.setCartonId((Integer) resultMap.get("carton_id"));
			slotInfo.setDate(getStringDate((Date) resultMap.get("date")));
			slotInfo.setTotalSpace((BigDecimal) resultMap.get("total_space"));
			slotInfo.setAvailableSpace((BigDecimal) resultMap.get("available_space"));
			String val = (String) resultMap.get("is_available");
			slotInfo.setAvailable(val.equals("Y") ? true : false);
			listSlotInfo.add(slotInfo);
		}
		return listSlotInfo;
	}

	/**
	 * The method is used to create slot info entries in the db
	 * for a particular date
	 */
	public void insertSlotInfo(String date) throws ServiceException{
		JdbcTemplate template = new JdbcTemplate(datasource);
		for(int i = 1; i<=4 ; i++){
			for(int j = 1 ; j<=4 ; j++){
				for(int k=1; k<=20; k++){
					StringBuilder sql = new StringBuilder();
					sql.append("insert into slot_info(slot_id,van_id,carton_id,date,total_space,available_space,is_available)");
					sql.append("values("+i+","+j+","+k+",'"+date+"',6750,6750,'Y')");
					try{
						template.update(sql.toString());
					}catch(DataAccessException ex){
						throw new ServiceException("",ex);
					}
					
				}				
			}			
		}
	}

	/**
	 * The method is used to retrieve all item details
	 */
	public Map<Integer, ItemDetails> getItemdetails(List<Item> items) throws ServiceException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<Map<String, Object>> resultList = null;
		Map<Integer, ItemDetails> itemMap = null;
		StringBuilder sql = new StringBuilder();
		String itemIds = getItemIds(items);
		sql.append("select * from item_details where item_id in("+itemIds+")");
		try{
			resultList = template.queryForList(sql.toString());
		}catch(DataAccessException ex){
			throw new ServiceException("Exception in Slot Info retrieval"+ex);
		}		
		if(resultList != null){
			itemMap = populateItemDetails(resultList);
		}
		return itemMap;
	}
	
	/**
	 * The method is used to update a slot info in the db after alloting
	 * space to any order
	 */
	public void updateBookDetails(SlotInfo slotInfo, BigDecimal bookedVolume) throws ServiceException{
		JdbcTemplate template = new JdbcTemplate(datasource);
		BigDecimal changedAvailableSpace = slotInfo.getAvailableSpace().subtract(bookedVolume);
		boolean isAvailable = true;
		if(changedAvailableSpace.intValue() == 0){
			isAvailable = false;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("update slot_info set available_space = "+changedAvailableSpace);
		if(!isAvailable){
			sql.append(",is_available = 'N'");
		}
		sql.append("where slot_id = "+slotInfo.getSlotId()+" AND van_id = "+slotInfo.getVanId()+" AND carton_id = "+slotInfo.getCartonId()+" AND date = '"+slotInfo.getDate()+"'");
		try{
			int i = template.update(sql.toString());
			System.out.println(i);
		}catch(DataAccessException ex){
			throw new ServiceException("Exception in Slot Info retrieval"+ex);
		}				
	}


	private Map<Integer, ItemDetails> populateItemDetails(
			List<Map<String, Object>> resultList) {
		Map<Integer, ItemDetails> itemMap = new HashMap<Integer, ItemDetails>();
		ItemDetails itemDetails = null;
		for(Map<String, Object> resultMap : resultList){
			itemDetails = new ItemDetails();
			itemDetails.setId((Integer) resultMap.get("item_id"));
			itemDetails.setName((String) resultMap.get("name"));
			itemDetails.setBreadth((BigDecimal) resultMap.get("breadth"));
			itemDetails.setHeight((BigDecimal) resultMap.get("height"));
			itemDetails.setWidth((BigDecimal) resultMap.get("width"));
			itemDetails.setVolume((BigDecimal) resultMap.get("volume"));
			itemMap.put(itemDetails.getId(), itemDetails);
		}
		return itemMap;
	}

	private String getItemIds(List<Item> items) {
		StringBuilder itemIds = new StringBuilder();
		int count = 0;
		for(Item item : items){
			if(count > 0){
				itemIds.append(",");
			}
			itemIds.append(item.getId().toString());
			count++;
		}
		return itemIds.toString();
	}
	
	private String getStringDate(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = format.format(date);
		return formattedDate;
	}

	

}
