package com.lab603.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.module.ResultPathsAndCost;
import com.lab603.module.Tran;

public class VERI {

	static Net NET;
	static HashMap<Integer, HashMap<Integer, Tran>> tranHash;
	public static boolean verification(ResultPathsAndCost resultPathsAndCost, Net net) {
		NET = net;
		int liu = NET.getTotalRequestValue();
		setTranHash();
		List<List<Integer>> paths = resultPathsAndCost.getPaths();
		for (List<Integer> path : paths) {
			for(int i = 1; i < path.size() - 1; i++) {
				if (i == path.size() - 2) {
					if (!goTran(path.get(i-1), path.get(i)+NET.getNodes().size(), path.get(path.size() - 1))) {
						return false;
					}            
				} else {
					if (!goTran(path.get(i-1), path.get(i), path.get(path.size() - 1))) {
						return false;
					} 
				}
				
			}
			
		}
		
		int sum = 0;
		for (List<Integer> path : paths) {
			sum += path.get(path.size() - 1);
		}
		if (sum != liu) {
			System.out.println("======总流不够========");
			return false;
		}
		
		
		return true;
	}

	private static void setTranHash() {
		tranHash = new HashMap<>();
		ArrayList<Tran> newTs = new ArrayList<>();
		for (Tran tran : NET.getTrans()) {// go back tran
			Tran newT = new Tran(tran.getToNodeID(), tran.getFromNodeID(), tran.getMaxValue(), tran.getCostValue());
			newTs.add(newT);
		}
		NET.getTrans().addAll(newTs);
		for (CostNode costNode : NET.getCostNodes()) {
			int to = costNode.getId();
			int from = costNode.getLinkedNodeId();
			int max = costNode.getRequestValue();
			int cost = 0;
			Tran tran1 = new Tran(from, to, max, cost);
			NET.getTrans().add(tran1);
		}
		
		for (Tran tran : NET.getTrans()) { // set tran hash
			int fromId = tran.getFromNodeID();
			int toId = tran.getToNodeID();
			HashMap<Integer, Tran> fromMap = 
					tranHash.containsKey(fromId) ? tranHash.get(fromId) : new HashMap<Integer, Tran>();
			fromMap.put(toId, tran);
			tranHash.put(fromId, fromMap);
		}
		
		
	}

	private static boolean goTran(Integer from, Integer to, Integer cost) {
		if (tranHash.containsKey(from)) {
			if (tranHash.get(from).containsKey(to)) {
				// exist
				Tran tran = tranHash.get(from).get(to);
				if (tran.getMaxValue() - cost < 0) {
					System.out.println("================"+from + " " + to + "流不足");
					return false;
				} else {
					tran.setMaxValue(tran.getMaxValue() - cost);
					return true;
				}
			}
		}
		System.out.println("================"+from + " " + to + "路径不存在");
		return false;
	}
}
