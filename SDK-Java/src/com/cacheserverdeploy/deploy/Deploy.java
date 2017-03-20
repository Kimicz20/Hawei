package com.cacheserverdeploy.deploy;

import java.util.ArrayList;
import java.util.List;

import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.module.ResultPathsAndCost;
import com.lab603.util.TransStringToModule;
import com.lab603.util.getResult;


public class Deploy
{
  
    public static String[] deployServer(String[] graphContent)
    {
    	

    	Net net = TransStringToModule.fromStrings(graphContent);
    	ResultPathsAndCost resultPathsAndCost = getResult.formNet(net);
    	String[] outputs = new String[resultPathsAndCost.getCosts().size() + 2];
		//1
    	outputs[0] = (""+ resultPathsAndCost.getCosts().size());
		// null line
		outputs[1] = "";
		
		for(int i = 0; i < resultPathsAndCost.getCosts().size(); i++) {
			List<Integer> path = resultPathsAndCost.getPaths().get(i);
			int cost = resultPathsAndCost.getCosts().get(i);
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < path.size(); i++) {
				sb.append(path.get(j)).append(" ");//0 1 2 3 path
			}
			sb.append("" + cost);// cost
			outputs[i] = sb.toString();
		}
		
    	
        return outputs;
    }

}
