package com.lab603.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lab603.jun.util.ServerNodeSet;
import com.lab603.module.Net;
import com.lab603.module.Node;
import com.lab603.module.ResultPathsAndCost;
import com.lab603.yj.module.MinCostFlow;

public class getResult {

	// main algorithm
	public static String[] formNet(Net net) {
		MinCostFlow m = new MinCostFlow(net);
		// 2.get server nodes
		List<Node> serverNodes = ServerNodeSet.selectServerfrom(net);
		// 3. select
		List<Integer> ids = addIdsFromServerNodes(serverNodes);
		// 4.set
		m.TransNet2Flow();
		m.setServer(ids);
		return transReultToStrings(m.min_cost_flow());

	}

	private static List<Integer> addIdsFromServerNodes(List<Node> serverNodes) {
		List<Integer> res = new ArrayList<>();
		for (Node node : serverNodes) {
			res.add(node.getId());
		}
		return res;
	}

	private static String[] transReultToStrings(ResultPathsAndCost resultPathsAndCost) {
		System.out.println(resultPathsAndCost);
		return new String[2];
	}
}
