package com.lab603.jun.util;

import java.util.List;

import com.lab603.module.Net;
import com.lab603.util.getResult;

public class ServerNodeSetV2 extends ServerNodeSet{

	public ServerNodeSetV2(Net net) {
		super(net);
		// TODO Auto-generated constructor stub
	}
	
	
	public List<Integer> randomServer(List<Integer> linkedIds) {
		linkedIds = getResult.allLinkedNodesIds(net);
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
	
}
