package com.lab603.jun.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lab603.jun.util.dj.MinStep;
import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.module.Node;

public class ServerNodeSet {
	protected int[] imp;
	protected int count;
	protected Net net;
	protected List<Node> nodes;
	protected ArrayList<Integer> mustBeServerNodes;
	protected int yuzhi;
	public ServerNodeSet(Net net) {
		this.net = net;
		nodes = net.getNodes();
		mustBeServerNodes = new ArrayList<>();
		// must be
		setMustBe(mustBeServerNodes, net.getCostNodes(), nodes);
		
		// imp
//		imp = nodesImpfrom(net);
		count = net.getCostNodes().size() - 1;
//		if (net.getNodes().size() >= 788) {
//			yuzhi = 300;
//		} else if (net.getNodes().size() >= 288) {
//			yuzhi = 135;
//		} else if (net.getNodes().size() >= 150) {
//			yuzhi = 75;
//		}
	}
	
	public List<Integer> randomServerByImp(Net net) {
		if (mustBeServerNodes.size() == net.getCostNodes().size()) { 
			return mustBeServerNodes;
		}
		
		setCount();
		Set<Integer> set =  new HashSet<Integer>();//get mustBe && set imp
		set.addAll(mustBeServerNodes);
		// from int[] to arraylist?????????????????????????????????????????
		double sum = 0;
		for (int i = 0; i < net.getNodes().size(); i++) {
			sum += 1 + 1;
		}
		
		
		while(true) {
			ArrayList<Double> impList = new ArrayList<Double>();
			for (int i = 0; i < net.getNodes().size(); i++) {
				impList.add((1 + 1) / sum);
			}
			int randomNodeId = randomPoints(impList);
			set.add(randomNodeId);
			if (set.size() >= count) {
				break;
			}
		}
		if (!isSatisfy(set)) {
			return randomServerByImp(net);
		}
		
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		res.addAll(set);
		return res;
	}
	

	private boolean isSatisfy(Set<Integer> res) {
		int sum = 0;
		for (Integer id : res) {
			sum += net.getNodes().get(id).getThroughput();
		}
		return sum >= net.getTotalRequestValue();
	}

	private void setCount() {
		count = 74;
//				(count - 1) % net.getCostNodes().size(); 
//		if (count <= yuzhi) {
//			count = net.getCostNodes().size() - 1;
//			
//		}
		
	}

	// random some nodes
	private void setServerNodes(Net net, List<Node> res, List<Node> sortedNodes) {
		int totalResquestValue = net.getTotalRequestValue();
		int sum = 0;
		
		for (Node node : sortedNodes) {
			res.add(node);
			sum += node.getThroughput();
			if (sum >= totalResquestValue) {
				return;
			}
		}
	}

	public static Integer randomPoints(ArrayList<Double> rdm) {
		ArrayList<Double> cdm = rdm;
		for (int i = 1; i < cdm.size() - 1; i++) {
			cdm.set(i, cdm.get(i) + cdm.get(i - 1));
		}
		cdm.set(cdm.size() - 1, 1.0);
		double randomNumber;
		randomNumber = Math.random();
		for (int i = 0; i < cdm.size(); i++) {
			if (randomNumber < cdm.get(i)) {
				return i;
			}
		}
		return -1; 

	}
	private void setMustBe(List<Integer> res, List<CostNode> costNodes, List<Node> nodes) {
		for (CostNode costNode : costNodes) {
			Node thisNode = nodes.get(costNode.getLinkedNodeId());
			if (thisNode.getThroughput() < costNode.getRequestValue()) {
				res.add(thisNode.getId());
			}
		}
	}

	private static List<Node> sortNodesByImp(List<Node> nodes, int[] imp) {
		for(int i = 0; i < nodes.size(); i++) {
			nodes.get(i).setImp(imp[i]);
		}
		Collections.sort(nodes);
		return nodes;
	}
	
	public int[] nodesImpfrom(Net net) {
//		ArrayList<Integer> serverNodeIds = new ArrayList<>();
		MinPath minPath = new MinPath(net);
		int[] nodeCount = new int[net.getAllNodeSize()];
		for (CostNode start : net.getCostNodes()) {
			int minLenth = Integer.MAX_VALUE;
			MinStep thisPath = null;
			CostNode end = net.getCostNodes().get(net.getCostNodes().size() - 1);
			if (end == start) {
				end = net.getCostNodes().get(0);
			}
			MinStep minStep = minPath.getMinSetp(start.getId(), end.getId());
			for (CostNode toCostNode : net.getCostNodes()) {
				
				if (start != toCostNode) {
					minLenth = Math.min(minLenth, minStep.getMinStep());
					thisPath = minStep;
					
				}
			}
			for(Integer i : thisPath.getStep()) {
				nodeCount[i]++;
			}
			
			
		}
		
 		return nodeCount;
	}
	
	
}