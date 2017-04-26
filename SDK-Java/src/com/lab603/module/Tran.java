package com.lab603.module;

import java.io.Serializable;

public class Tran implements Serializable{
	int fromNodeID;
	int toNodeID;
	int maxValue;
	int costValue;
	
	public Tran(int fromNodeID, int toNodeID, int maxValue, int costValue) {
		super();
		this.fromNodeID = fromNodeID;
		this.toNodeID = toNodeID;
		this.maxValue = maxValue;
		this.costValue = costValue;
	}

	public int getFromNodeID() {
		return fromNodeID;
	}

	public void setFromNodeID(int fromNodeID) {
		this.fromNodeID = fromNodeID;
	}

	public int getToNodeID() {
		return toNodeID;
	}

	public void setToNodeID(int toNodeID) {
		this.toNodeID = toNodeID;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getCostValue() {
		return costValue;
	}

	public void setCostValue(int costValue) {
		this.costValue = costValue;
	}
}
