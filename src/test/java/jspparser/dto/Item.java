package jspparser.dto;

import java.util.ArrayList;
import java.util.List;

public class Item {
	
	private String id;
	private List<Item> items=new ArrayList<Item>();
	private Item parent;
	
	public Item getParent() {
		return parent;
	}
	public void setParent(Item parent) {
		this.parent = parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}

}
