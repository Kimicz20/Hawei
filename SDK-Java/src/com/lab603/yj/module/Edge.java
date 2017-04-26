package com.lab603.yj.module;

import java.io.Serializable;

/**
 * 
 * @author geek
 *	终点、容量、费用、反向边
 */
public class Edge implements Serializable{
	private int	to ;
	private int	cap ;
	private int	cost ;
	private int	rev ;
	
	public Edge( int to, int cap, int cost, int rev) {
		super();
		this.to = to;
		this.cap = cap;
		this.cost = cost;
		this.rev = rev;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getCap() {
		return cap;
	}

	public void setCap(int cap) {
		this.cap = cap;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getRev() {
		return rev;
	}

	public void setRev(int rev) {
		this.rev = rev;
	}

	@Override
	public String toString() {
		return "edge [" + to + "," + cap + "," + cost + "," + rev + "]";
	}
}
