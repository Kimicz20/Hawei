package com.lab603.module;

import java.io.Serializable;
import java.util.List;

import com.lab603.util.CopyUtil;

@SuppressWarnings("serial")
public class Population implements Serializable{

	ResultPathsAndCost pathsAndCost;

	List<Integer> serversId;

	double x = 0;

	double T;

	double P;

	public Population() {

	}

	public Population(List<Integer> id, double x, double t) {
		this.serversId = id;
		this.x = x;
		T = t;
		P = Math.exp(-x / T);
	}

	public void mutation() {
	}

	
	public static Population clone(final Population c) {
		return CopyUtil.clone(c);
	}
	
	
	public List<Integer> getServersId() {
		return serversId;
	}

	public void setServersId(List<Integer> serversId) {
		this.serversId = serversId;
	}

	public ResultPathsAndCost getPathsAndCost() {
		return pathsAndCost;
	}

	public void setPathsAndCost(ResultPathsAndCost pathsAndCost) {
		this.pathsAndCost = pathsAndCost;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i : serversId) {
			sb.append(i+" ");
		}
		return Integer.toString(pathsAndCost.getCosts());
	}
}