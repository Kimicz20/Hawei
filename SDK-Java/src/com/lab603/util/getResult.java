package com.lab603.util;

import java.util.ArrayList;
import java.util.List;

import com.lab603.jun.util.ServerNodeSet;
import com.lab603.module.Net;
import com.lab603.module.ResultPathsAndCost;
import com.lab603.yj.module.MinCostFlow;

public class getResult {
	// main algorithm
	public static String[] formNet(Net net) {
		
		//2.get important Point
		int imp[] = ServerNodeSet.nodesImpfrom(net);
    	
		//3. select
		List<Integer> ids = new ArrayList<>();
		ids.add(0);
		ids.add(1);
		ids.add(24);
		//4.set
		MinCostFlow m = new MinCostFlow(net);
		m.TransNet2Flow();
		m.setServer(ids);
		System.out.println(m.min_cost_flow()); 
		
		ResultPathsAndCost resultPathsAndCost = new ResultPathsAndCost(null, null);
		return transReultToStrings(resultPathsAndCost);
		
	}

	private static String[] transReultToStrings(ResultPathsAndCost resultPathsAndCost) {
		return null;
	}
}
