package com.lab603.module;

import java.io.Serializable;

public class CostNode implements Serializable{
	
	int id;
	int linkedNodeId;
	int requestValue;
	public CostNode(int id, int linkedNodeId, int requestValue) {
		super();
		this.id = id;
		this.linkedNodeId = linkedNodeId;
		this.requestValue = requestValue;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLinkedNodeId() {
		return linkedNodeId;
	}
	public void setLinkedNodeId(int linkedNodeId) {
		this.linkedNodeId = linkedNodeId;
	}
	public int getRequestValue() {
		return requestValue;
	}
	public void setRequestValue(int requestValue) {
		this.requestValue = requestValue;
	}
	
	
}
