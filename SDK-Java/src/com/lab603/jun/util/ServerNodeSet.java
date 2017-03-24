package com.lab603.jun.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lab603.jun.util.dj.MinStep;
import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.module.Node;

public class ServerNodeSet {
	
	
	public static List<Node> selectServerfrom(Net net) {
		List<Node> nodes = net.getNodes();
		List<Node> res = new ArrayList<>();
		// must be
		setMustBe(res, net.getCostNodes(), nodes);
		
		// imp
		int imp[] = ServerNodeSet.nodesImpfrom(net);
		List<Node> sortedNodes = sortNodesByImp(net.getNodes(), imp);
		
		// get some serverNodes
    	setServerNodes(net, res, sortedNodes);
    	
		return res;
	}
	
	private static void setServerNodes(Net net, List<Node> res, List<Node> sortedNodes) {
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

	private static void setMustBe(List<Node> res, List<CostNode> costNodes, List<Node> nodes) {
		for (CostNode costNode : costNodes) {
			Node thisNode = nodes.get(costNode.getLinkedNodeId());
			if (thisNode.getThroughput() < costNode.getRequestValue()) {
				res.add(thisNode);
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
	
	public static int[] nodesImpfrom(Net net) {
		MinPath minPath = new MinPath(net);
		int[] nodeCount = new int[net.getAllNodeSize()];
		for (CostNode costNode1 : net.getCostNodes()) {
			int minLenth = Integer.MAX_VALUE;
			MinStep thisPath = null;
			for (CostNode costNode2 : net.getCostNodes()) {
				if (costNode1 != costNode2) {
					MinStep minStep = minPath.getMinSetp(costNode1.getId(), costNode2.getId());
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
