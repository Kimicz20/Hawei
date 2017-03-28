package com.lab603.chenzuo.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.rmi.CORBA.Util;

import com.lab603.chenzuo.util.GACriterion;
import com.lab603.chenzuo.util.GAUtil;
import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.module.ResultPathsAndCost;

public class GeneticAlgorithm {

	GACriterion criterion;

	static int bestT; // best generation
	static int nodesNum; // nodes Num
	static int[] fitness; // fitness function
	static float[] Pi; // cumulative probability
	static int t; // now generation

	Random random = null;

	private Net net;

	private List<Chromosome> oldPopulation = null;
	private List<Chromosome> newPopulation = null;

	public GeneticAlgorithm(Net net, GACriterion criterion) {
		this.net = net;
		this.criterion = criterion;
		init();
	}

	public void init() {

		nodesNum = net.getNodes().size();
		bestT = 0;
		t = 0;
		random = new Random(System.currentTimeMillis());
		oldPopulation = new ArrayList<Chromosome>();
		newPopulation = new ArrayList<Chromosome>();
		Pi = new float[criterion.getSCALE()];
		fitness = new int[criterion.getSCALE()];
	}

	private void initPopulation() {
		for (int i = 0; i < criterion.getSCALE(); i++) {
			List<Integer> ids = new ArrayList<Integer>();
			// first put nodesNum
			Chromosome chro = null;
			if (i == 0) {
				ids.add(0);
				ids.add(nodesNum);
				for (CostNode costNode : net.getCostNodes()) {
					ids.add(costNode.getLinkedNodeId());
				}
				chro = new Chromosome(ids);
			} else {
				chro = new Chromosome(nodesNum);
				 while(true){
					 chro = new Chromosome(nodesNum);
					 //get available chromesomes
					 if(GAUtil.evaluateOneFitness(net, chro).getCosts() > 0)
						 break;
				 }
			}
			oldPopulation.add(chro);
		}
	}

	/**
	 * @Author:kimi
	 * @Description:population evolve
	 */
	public ResultPathsAndCost evolve() {

		// 1.intit population
		initPopulation();

		System.err.println("init ok");

		// 2.evaluate fitness
		fitness = GAUtil.evaluateFitness(net, oldPopulation);

		// 3.evaluate rate
		Pi = GAUtil.evaluateRate(fitness, oldPopulation);
		long startTime = System.currentTimeMillis(), endTime = 0l;

		// 4.genetic iteration

		for (t = 0; t < criterion.getMAX_GEN(); t++) {
			if ((endTime - startTime) < criterion.getMAX_TIME_GEN()){
				System.err.println("generate population , t:" + t);

				// 4.1 generate next population
				evolution();
				oldPopulation.clear();
				// 4.2 change
				for (Chromosome s : newPopulation) {
					oldPopulation.add(s);
				}
				newPopulation.clear();
				// 4.3 evaluate fitness
				fitness = GAUtil.evaluateFitness(net, oldPopulation);
				// 4.4 evaluate rate
				Pi = GAUtil.evaluateRate(fitness, oldPopulation);
				endTime = System.currentTimeMillis();
			}
		}
		return oldPopulation.get(0).getPathsAndCost();
	}

	/**
	 * @return
	 * @Author:kimi
	 * @Description: Roulette choice
	 */
	private void evolution() {
		int k;
		// choice the highest fitness individual
		selectBest();

		// Roulette choice next generation
		select();

		float r;

		for (k = 1; k + 1 < criterion.getSCALE() / 2; k = k + 2) {
			r = random.nextFloat();
			if (r < criterion.getPc()) {
				// cross
				OXCross(k, k + 1);
			} else {
				r = random.nextFloat();
				// mutation
				if (r < criterion.getPm()) {
					OnCVariation(k);
				}
				r = random.nextFloat();
				// mutation
				if (r < criterion.getPm()) {
					OnCVariation(k + 1);
				}
			}
		}
		// L-1 chromos not cross
		if (k == criterion.getSCALE() / 2 - 1) {
			r = random.nextFloat();
			if (r < criterion.getPm()) {
				OnCVariation(k);
			}
		}
	}

	// choice best fitness chromosome of T-gen population and copy it to chilld
	public void selectBest() {
		int k, bestId = 0, bestEvaluation;

		bestEvaluation = oldPopulation.get(0).getPathsAndCost().getCosts();
		for (k = 1; k < criterion.getSCALE(); k++) {
			if (bestEvaluation > oldPopulation.get(k).getPathsAndCost().getCosts()
					&& oldPopulation.get(k).getPathsAndCost().getCosts() > 0) {
				bestEvaluation = oldPopulation.get(k).getPathsAndCost().getCosts();
				bestId = k;
			}
		}
		// choice smallest fitness gen into new population
		newPopulation.add(0, oldPopulation.get(bestId));
	}

	// Roulette choice next generation
	public void select() {
		int k, i, selectId;
		float ran1;
		for (k = 1; k < criterion.getSCALE(); k++) {
			ran1 = (float) (random.nextInt(65535) % 1000 / 1000.0);
			for (i = 0; i < criterion.getSCALE(); i++) {
				if (ran1 <= Pi[i] && oldPopulation.get(i).getPathsAndCost().getCosts() > 0) {
					break;
				}
			}
			selectId = i;
			newPopulation.add(k, oldPopulation.get(selectId));
		}
	}

	// cross generate diffence chromesome
	public void OXCross(int k1, int k2) {

		int j = 0;
		Chromosome p1 = newPopulation.get(k1);
		Chromosome p2 = newPopulation.get(k2);
		if (!p1.equals(p2)) {
			List<Chromosome> children = Chromosome.genetic(p1, p2);
			// 重新计算
			for (Chromosome chromosome : children) {
				chromosome.setPathsAndCost(GAUtil.evaluateOneFitness(net, chromosome));
				if (chromosome.getPathsAndCost().getCosts() > 0) {
					newPopulation.add(chromosome);
					j++;
				}
			}

			// 选择交叉后优秀的加入
			Collections.sort(newPopulation, new Comparator<Chromosome>() {

				@Override
				public int compare(Chromosome o1, Chromosome o2) {
					return o1.getPathsAndCost().getCosts() - o2.getPathsAndCost().getCosts();
				}
			});
			for (int i = 0; i < children.size() - j; i++)
				newPopulation.remove(newPopulation.size() - 1 - i);
		}
	}

	// N-times change mutation operator
	public void OnCVariation(int k) {
		Chromosome c1 = newPopulation.get(k);
		Chromosome c2 = Chromosome.clone(c1);
		c2.mutation();
		ResultPathsAndCost nowCost = GAUtil.evaluateOneFitness(net, c2);
		c2.setPathsAndCost(nowCost);
//		System.err.println(nowCost.getCosts() + "," + c1.getPathsAndCost().getCosts());
		if (nowCost.getCosts() > 0 && nowCost.getCosts() < c1.getPathsAndCost().getCosts()) {
			System.out.println("变异之前:" + c1);
			newPopulation.set(k, c2);
			System.out.println("变异之后:" + newPopulation.get(k));
		}
	}
}