package com.lab603.module;

import java.io.Serializable;

public class Node implements Comparable<Node>,Serializable{
	int id;
	int throughput;
	int imp;
	boolean mustBeServer;
	
	public boolean isMustBeServer() {
		return mustBeServer;
	}


	public void setMustBeServer(boolean mustBeServer) {
		this.mustBeServer = mustBeServer;
	}


	public Node(int id) {
		super();
		this.id = id;
		this.throughput = 0;
	}

	
	public int getImp() {
		return imp;
	}


	public void setImp(int imp) {
		this.imp = imp;
	}


	public void addThroughput(int x) {
		throughput += x;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getThroughput() {
		return throughput;
	}

	public void setThroughput(int throughput) {
		this.throughput = throughput;
	}


	@Override
	public int compareTo(Node o) {
		return o.imp - this.imp;
	}
	
	
}
