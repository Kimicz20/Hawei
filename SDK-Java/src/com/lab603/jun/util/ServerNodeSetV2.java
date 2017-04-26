package com.lab603.jun.util;

import java.util.ArrayList;
import java.util.List;

import com.lab603.module.CostNode;
import com.lab603.module.Net;

public class ServerNodeSetV2 extends ServerNodeSet{

	public ServerNodeSetV2(Net net) {
		super(net);
		// TODO Auto-generated constructor stub
	}
	
	public List<Integer> allLinkedNodesIds(Net net) {
		
		List<Integer> res = new ArrayList<>();
		for (CostNode costNode : net.getCostNodes()) {
			res.add(costNode.getLinkedNodeId());
		}
		return res;
	}
	
	public List<Integer> randomServer(List<Integer> linkedIds) {
		linkedIds = allLinkedNodesIds(net);
		int random1 = (int) (Math.random() * linkedIds.size());
		linkedIds.remove(random1);
		if (Math.random() > 0.5) {
			int random2 = (int) (Math.random() * linkedIds.size());
			linkedIds.remove(random2);
		}
		
		int random3 = (int) (Math.random() * net.getNodes().size()); 
		linkedIds.add(random3);
		return linkedIds;
	}
	
	public List<Integer> randomChooseServer(List<Integer> linkedIds) {
		
		int random;
		
		random = (int) (Math.random() * linkedIds.size());
		linkedIds.remove(random);
		
		for(int i = 0;i<2;i++){
			if (Math.random() > 0.5) {
				random = (int) (Math.random() * linkedIds.size());
				linkedIds.remove(random);
			}
		}
		if (Math.random() > 0.5) {
			random = (int) (Math.random() * net.getNodes().size()); 
			linkedIds.add(random);
		}
		return linkedIds;
	}
	
}
