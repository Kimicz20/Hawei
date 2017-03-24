package com.lab603.module;

import java.util.List;

public class Net {
	List<Node> nodes;
	List<Tran> trans;
	List<CostNode> costNodes;
	int serverCost;
	int allNodeSize;
	int totalRequestValue;
	public Net(List<Node> nodes, List<Tran> trans, List<CostNode> costNodes, int serverCost, int totalRequestValue) {
		super();
		this.nodes = nodes;
		this.trans = trans;
		this.costNodes = costNodes;
		this.serverCost = serverCost;
		this.allNodeSize = nodes.size() + costNodes.size();
		this.totalRequestValue = totalRequestValue;
	}
	
	
	public int getTotalRequestValue() {
		return totalRequestValue;
	}


	public void setTotalRequestValue(int totalRequestValue) {
		this.totalRequestValue = totalRequestValue;
	}


	public int getAllNodeSize() {
		return allNodeSize;
	}

	public void setAllNodeSize(int allNodeSize) {
		this.allNodeSize = allNodeSize;
	}

	public List<CostNode> getCostNodes() {
		return costNodes;
	}
	public void setCostNodes(List<CostNode> costNodes) {
		this.costNodes = costNodes;
	}
	public int getServerCost() {
		return serverCost;
	}
	public void setServerCost(int serverCost) {
		this.serverCost = serverCost;
	}
	public List<Node> getNodes() {
		return nodes;
	}
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	public List<Tran> getTrans() {
		return trans;
	}
	public void setTrans(List<Tran> trans) {
		this.trans = trans;
	}
	
}
