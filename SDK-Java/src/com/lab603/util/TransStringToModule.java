package com.lab603.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.module.Node;
import com.lab603.module.Tran;

public class TransStringToModule {
	public static Net fromStrings(String[] input) {
		int index = 0;
		
		String[] countString = input[index++].split(" ");
		int nodeCount = Integer.valueOf(countString[0]);
		int tranCount = Integer.valueOf(countString[1]);
		int costNodeCount = Integer.valueOf(countString[2]);
		
		// set nodes first
		ArrayList<Node> nodes = new ArrayList<Node>(nodeCount);
		for(int i = 0; i < nodeCount; i++) {
			Node node = new Node(i);
			nodes.add(node);
		}
				
		index++;
		int serverCost = Integer.valueOf(input[index++]);
		index++;
		
		ArrayList<Tran> trans = new ArrayList<Tran>(tranCount);
		for(; index < 4 + tranCount; index ++) {
			String[] tranDataString = input[index].split(" ");
			int fromNodeID = Integer.valueOf(tranDataString[0]);
			int toNodeID = Integer.valueOf(tranDataString[1]);
			int maxValue = Integer.valueOf(tranDataString[2]);
			int costValue = Integer.valueOf(tranDataString[3]);
			Tran tran = new Tran(fromNodeID, toNodeID, maxValue, costValue);
			trans.add(tran);
			nodes.get(fromNodeID).addThroughput(maxValue);
			nodes.get(toNodeID).addThroughput(maxValue);
		}
		
		
		int totalResquestValue = 0;
		index++;
		ArrayList<CostNode> costNodes = new ArrayList<CostNode>(costNodeCount);
		for(; index < 5 + tranCount + costNodeCount; index++) {
			String[] costNodeDataString = input[index].split(" ");
			int id = Integer.valueOf(costNodeDataString[0]) + nodeCount;
			int linkedNodeId = Integer.valueOf(costNodeDataString[1]);
			int requestValue = Integer.valueOf(costNodeDataString[2]);
			CostNode costNode = new CostNode(id, linkedNodeId, requestValue);
			costNodes.add(costNode);
			totalResquestValue += requestValue;
		}
		
		
		
		Net net = new Net(nodes, trans, costNodes, serverCost, totalResquestValue);
		return net; 
		
	}
	

}
