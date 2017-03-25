package com.lab603.jun.util;

import java.util.HashMap;
import java.util.List;

import com.lab603.jun.util.dj.DistanceDijkstraImpl;
import com.lab603.jun.util.dj.MinStep;
import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.module.Tran;

public class MinPath {
	static HashMap<Integer, HashMap<Integer, MinStep>> minSetpMap;
	static HashMap<Integer, HashMap<Integer, Integer>> stepLenth;
	static Net net;
	// init 
	public MinPath(Net net) {
		this.net = net;
		minSetpMap = new HashMap<Integer, HashMap<Integer, MinStep>>(net.getAllNodeSize());
		List<Tran> trans = net.getTrans();
		stepLenth = new HashMap<Integer, HashMap<Integer, Integer>>(net.getAllNodeSize());
		
		for (Tran tran : trans) {
			int fromId = tran.getFromNodeID();
			int toId = tran.getToNodeID();
			setFromToLenthMap(fromId, toId, tran.getCostValue());
			setFromToLenthMap(toId, fromId, tran.getCostValue());
		}
		
		for(CostNode costNode : net.getCostNodes()) {
			int fromId = costNode.getId();
			int toId = costNode.getLinkedNodeId();
			setFromToLenthMap(fromId, toId, 0);
			setFromToLenthMap(toId, fromId, 0);
		}
	}
	// set costValue from to
	private void setFromToLenthMap(int fromId, int toId, int costValue) {
		
		HashMap<Integer, Integer> fromMap = 
				stepLenth.containsKey(fromId) ? stepLenth.get(fromId) : new HashMap<Integer, Integer>(net.getNodes().size());
		fromMap.put(toId, costValue);
		stepLenth.put(fromId, fromMap);
	}
	// get minSetp from to
	public static MinStep getMinSetp(int fromNodeId, int toNodeId) {
	
		if (minSetpMap.containsKey(fromNodeId)) {
			if (minSetpMap.get(fromNodeId).containsKey(toNodeId)) {
				return minSetpMap.get(fromNodeId).get(toNodeId);
			}
		}
	
		DistanceDijkstraImpl d = new DistanceDijkstraImpl();
		MinStep res = d.getMinStep(fromNodeId, toNodeId, stepLenth);
		
		// -time save all path
//		ArrayList<MinStep> allminStep = d.getAllMinSetp(net.getNodes().size());
//		for (MinStep minStep : allminStep) {
//			
//		}
		return res;
	}
}
