package com.services.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.services.exception.ServiceException;
import com.services.manager.BookingManager;
import com.services.model.Item;
import com.services.model.Order;
import com.services.model.Response;
import com.services.model.Slot;
import com.services.model.SlotInfo;

/**
 * This class acts as the central layer for all rest exposed methods
 * 
 * @author Shahbaz.Alam
 */
@RestController
public class RestLayer {
	@Autowired
	private BookingManager manager;
		
	/**
	 * This is a Post Service which takes the Order object and pass it 
	 * to the manager. This needs a UI interface for sending the request
	 * 
	 * @return Response
	 * 
	 */	
	@RequestMapping(value = "/bookSlot",
			method = RequestMethod.POST)
	public ResponseEntity<Object> bookSlot(@RequestBody Order order){
		SlotInfo info = null;
		try{
			info = manager.bookSlot(order);
		}catch(ServiceException ex){
			return new ResponseEntity<Object>("Exception Caught in executing the Service", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Response response = createResponse(info);
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	/**
	 * This is a Get Service which takes the item id and units as path params.
	 * It creates an order object and passes on to the manager. This doesn't need
	 * any UI and can be invoked directly through browser
	 * 
	 * @return Response
	 * 
	 */	
	@RequestMapping(value = "/bookSlot/{itemId}/{units}",
			method = RequestMethod.GET)
	public ResponseEntity<Object> bookSlot(@PathVariable String itemId, @PathVariable String units){
		Order order = new Order();
		List<Item> items = new ArrayList<Item>();
		Item item = new Item();
		item.setId(Integer.parseInt(itemId));
		item.setNoOfUnits(Integer.parseInt(units));
		items.add(item);
		order.setItems(items);
		SlotInfo info = null;		
		try{
			info = manager.bookSlot(order);
		}catch(ServiceException ex){
			return new ResponseEntity<Object>("Exception Caught in executing the Service", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Response response = createResponse(info);
		
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	}

	/**
	 * The method populates the response object
	 * 
	 * @param info
	 * @return
	 */
	private Response createResponse(SlotInfo info) {
		Response response = new Response();
		switch(info.getSlotId()){
		case 1 : response.setSlotHours(
				Slot.Slot1.getStartHours().toString() + "AM - " + Slot.Slot1.getEndHours().toString() + "AM");
				response.setDate(info.getDate());
				return response;
		
		case 2 : response.setSlotHours(
				Slot.Slot2.getStartHours().toString() + "AM - " + Slot.Slot2.getEndHours().toString() + "PM");
				response.setDate(info.getDate());
				return response;
				
		case 3 : response.setSlotHours(
				Slot.Slot3.getStartHours().toString() + "PM - " + Slot.Slot3.getEndHours().toString() + "PM");
				response.setDate(info.getDate());
				return response;
				
		case 4 : response.setSlotHours(
				Slot.Slot4.getStartHours().toString() + "PM - " + Slot.Slot4.getEndHours().toString() + "PM");
				response.setDate(info.getDate());
				return response;
		default : return response;
		}
	}
}
