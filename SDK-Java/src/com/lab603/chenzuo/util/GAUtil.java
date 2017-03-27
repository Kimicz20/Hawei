package com.lab603.chenzuo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lab603.chenzuo.module.Chromosome;
import com.lab603.module.Net;
import com.lab603.module.ResultPathsAndCost;
import com.lab603.yj.module.MinCostFlow;

public class GAUtil {
	
	
	public static int[] evaluateFitness(Net net, List<Chromosome> oldPopulation){
		
		int[] fitness = new int[oldPopulation.size()];
		int k=0;
		for (Chromosome chromosome : oldPopulation) {
			// 1.get now Population
			int[] gene = chromosome.getGene();
			
			//2.put servers
			List<Integer> ids = new ArrayList<Integer>();
			for(int i=0;i<gene.length;i++)
				if(gene[i] == 1)
					ids.add(i);
			MinCostFlow m = new MinCostFlow(net);
			m.TransNet2Flow();
			m.setServer(ids);
			
			//3.cacluate min_cost_flow
			ResultPathsAndCost tempResult = m.min_cost_flow();
			chromosome.setPathsAndCost(tempResult);
			fitness[k++] = tempResult.getCosts();
		}
		
		return fitness;
	}
	
	
	public static float[] evaluateRate(int[] fitness, List<Chromosome> oldPopulation) {
		
		int k,scale=oldPopulation.size();
		double sumFitness = 0;
		float[] Pi = new float[scale];
		double[] tempf = new double[scale];

		for (k = 0; k < scale; k++) {
			tempf[k] = 10.0 / fitness[k];
			sumFitness += tempf[k];
		}

		Pi[0] = (float) (tempf[0] / sumFitness);
		for (k = 1; k < scale; k++) {
			Pi[k] = (float) (tempf[k] / sumFitness + Pi[k - 1]);
		}
		return Pi;
	}
}
