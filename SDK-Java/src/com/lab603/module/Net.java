package com.lab603.module;

import java.util.List;

public class Net {
	boolean[][] conected;
	List<Node> nodes;
	List<Tran> trans;
	List<CostNode> costNodes;
	int serverCost;
	int allNodeSize;
	public Net(boolean[][] conected, List<Node> nodes, List<Tran> trans, List<CostNode> costNodes, int serverCost) {
		super();
		this.conected = conected;
		this.nodes = nodes;
		this.trans = trans;
		this.costNodes = costNodes;
		this.serverCost = serverCost;
		this.allNodeSize = nodes.size() + costNodes.size();
	}
	
	public int getAllNodeSize() {
		return allNodeSize;
	}

	public void setAllNodeSize(int allNodeSize) {
		this.allNodeSize = allNodeSize;
	}

	public boolean[][] getConected() {
		return conected;
	}
	public void setConected(boolean[][] conected) {
		this.conected = conected;
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
