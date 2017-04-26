package com.lab603.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.lab603.chenzuo.module.GeneticAlgorithm;
import com.lab603.chenzuo.util.GACriterion;
import com.lab603.jun.util.ServerNodeSetV2;
import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.module.Node;
import com.lab603.module.Population;
import com.lab603.module.ResultPathsAndCost;
import com.lab603.yj.module.MinCostFlow;

/**
 * 
 * @author geek
 *
 */
public class getResult {
	
	// main algorithm
//	public static String[] formNet(Net net) {
//		
//		
//		int best = Integer.MAX_VALUE;
//		ServerNodeSetV2 serverNodeSet = new ServerNodeSetV2(net);
//		
//		long time0 = System.currentTimeMillis();
//		ResultPathsAndCost resultPathsAndCost = null;
//		boolean first = true;
//		List<Integer> lastIds = null;
//		List<Integer> initIDs = allLinkedNodesIds(net);
//		int count = 100;
//		while(true) {
//			List<Integer> ids = new ArrayList<>();
//			if (first) {
//				first = false;
//				ids.addAll(initIDs);
//				lastIds = ids;
//			} else {
//				ids = serverNodeSet.randomServer(lastIds);
//				if (count-- == 0) {
//					count = 100;
//					ids = new ArrayList<>();
//					ids.addAll(initIDs);
//				}
//				
//			}
//			 
//			// 4.set
//			MinCostFlow m = new MinCostFlow(net);
//			m.TransNet2Flow();
//			m.setServer(ids);
//			
//           	ResultPathsAndCost tempResult = m.min_cost_flow();
//  			if (tempResult.getCosts() >= 0 && tempResult.getCosts() < best) {
//  				lastIds = ids;
//  				best = tempResult.getCosts();
////				System.err.println(best);
//   				resultPathsAndCost = tempResult;
//			}
//			long time2 = System.currentTimeMillis();
//			if (time2 - time0 > 80000) {
//				break;
//			}
//			
//		}
//		// verification
//		if (resultPathsAndCost.getCosts()>0 && VERI.verification(resultPathsAndCost, net) == false) {
//			System.out.println(1);
//		}
//		System.err.println("***" + VERI.verification(resultPathsAndCost, net));
//		return transReultToStrings(resultPathsAndCost);
//	}


	//GA 
	public static String[] formNetWithGA(Net net) {
		
		//make criterion 
		GeneticAlgorithm GA = new GeneticAlgorithm(net,new GACriterion(40, 0.6f,0.3f, 10000, 100));
		
		//GA 
		ResultPathsAndCost bestGroupInTime = GA.evolve();
		
		return transReultToStrings(bestGroupInTime);
	}

	
	//SA 
	public static String[] formNetWithSA(Net net) {
		
		ServerNodeSetV2 serverNodeSet = new ServerNodeSetV2(net);
		
		//init population
		List<Integer> serverIds = CopyUtil.deepCopyList(serverNodeSet.allLinkedNodesIds(net));
//		Population initPopulation = new Population(initIds, 0, 0);
		
		int minCost = Integer.MAX_VALUE;
		
		MinCostFlow initFlow = new MinCostFlow(net);
		initFlow.TransNet2Flow();
		
		ResultPathsAndCost resultPathsAndCost = null;
		long starTime = System.currentTimeMillis();
		
		
		while(true) {
			
			List<Integer> tmpIds = CopyUtil.deepCopyList(serverIds);
			//choseServer
			serverIds = serverNodeSet.randomChooseServer(serverIds);
			// set
			MinCostFlow m = CopyUtil.clone(initFlow);
			m.setServer(serverIds);
			resultPathsAndCost = m.min_cost_flow();
			int currCost = resultPathsAndCost.getCosts();
			if(currCost>0&&(currCost+net.getServerCost()*serverIds.size())<minCost) {
				
				minCost = currCost+net.getServerCost()*serverIds.size();
				System.err.println(currCost+","+minCost);
//				initPopulation.setServersId(ids);
			}else{
				serverIds = tmpIds;
			}
			
			long endTime = System.currentTimeMillis();
			if (endTime - starTime > 80000) {
				break;
			}
			
		}
		// verification
		System.err.println(VERI.verification(resultPathsAndCost, net));
		return transReultToStrings(resultPathsAndCost);
	}
	
	

	private static List<Integer> addIdsFromServerNodes(List<Node> serverNodes) {
		List<Integer> res = new ArrayList<Integer>();
		for (Node node : serverNodes) {
			res.add(node.getId());
		}
		return res;
	}

	private static String[] transReultToStrings(ResultPathsAndCost resultPathsAndCost) {
		return resultPathsAndCost.result();
	}
}
