package com.lab603.jun.util;

import java.util.ArrayList;
import java.util.Arrays;

import com.lab603.jun.util.dj.MinStep;
import com.lab603.module.CostNode;
import com.lab603.module.Net;

public class ServerNodeSet {
	public static int[] nodesImpfrom(Net net) {
//		ArrayList<Integer> serverNodeIds = new ArrayList<>();
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
			System.out.println(thisPath.getStep());
			for(Integer i : thisPath.getStep()) {
				nodeCount[i]++;
			}
		}
		
 		return nodeCount;
	}
}
