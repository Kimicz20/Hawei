package com.lab603.chenzuo.util;

import java.util.Arrays;
import java.util.List;
import com.lab603.chenzuo.module.Chromosome;
import com.lab603.module.Net;
import com.lab603.module.ResultPathsAndCost;
import com.lab603.yj.module.MinCostFlow;

public class GAUtil {
	
	
	/**
	 * 计算种群适应度
	 * @param scale
	 * @param net
	 * @param oldPopulation
	 * @return
	 */
	public static int[] evaluateFitness(Net net, List<Chromosome> oldPopulation){
		
		int[] fitness = new int[oldPopulation.size()];
		int k=0;
		for (Chromosome chromosome : oldPopulation) {
			// 1.get now Population
			int[] gene = chromosome.getGene();
			
			//2.put servers
			List ids = Arrays.asList(gene);
			MinCostFlow m = new MinCostFlow(net);
			m.TransNet2Flow();
			m.setServer(ids);
			
			//3.cacluate min_cost_flow
			ResultPathsAndCost tempResult = m.min_cost_flow();
			chromosome.setScore(tempResult.getCosts());
			fitness[k++] = tempResult.getCosts();
		}
		return fitness;
	}
	
	/**
	 * @Author:kimi
	 * @Description: 计算种群中各个个体的累积概率， 前提是已经计算出各个个体的适应度fitness[max]
	 *               作为赌轮选择策略一部分，Pi[max]
	 */
	public static float[] evaluateRate(int scale ,int[] fitness, List<Chromosome> oldPopulation) {
		int k;
		double sumFitness = 0;// 适应度总和
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
