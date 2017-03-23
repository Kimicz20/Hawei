package com.lab603.util;

import com.lab603.jun.util.ServerNodeSet;
import com.lab603.module.Net;
import com.lab603.module.ResultPathsAndCost;

public class getResult {
	// main algorithm
	public static String[] formNet(Net net) {
		
		
		int imp[] = ServerNodeSet.nodesImpfrom(net);//get important Point
    	
		// continue 
		
		
		ResultPathsAndCost resultPathsAndCost = new ResultPathsAndCost(null, null);
		return transReultToStrings(resultPathsAndCost);
		
	}

	private static String[] transReultToStrings(ResultPathsAndCost resultPathsAndCost) {
		return null;
	}
}
