package com.lab603.chenzuo.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lab603.chenzuo.util.GACriterion;
import com.lab603.chenzuo.util.GAUtil;
import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.module.ResultPathsAndCost;

public class GeneticAlgorithm {

	GACriterion criterion;

	static int bestT; 			// best generation
	static int nodesNum;		// nodes Num
	static int[] fitness;		// fitness function
	static float[] Pi;			// cumulative probability
	static int t;				// now generation
	
	Random random = null;
	
	private Net net;
	
	private List<Chromosome> oldPopulation = null;
	private List<Chromosome> newPopulation = null;


	public GeneticAlgorithm(Net net,GACriterion criterion) {
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
			//first put nodesNum
			if(i ==0 )
				ids.add(0);
			ids.add(nodesNum);
			for (CostNode costNode : net.getCostNodes()) {
				ids.add(costNode.getLinkedNodeId());
			}
			Chromosome chro = new Chromosome(ids);
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
		showList(true);
		// 2.evaluate fitness
		fitness = GAUtil.evaluateFitness(net, oldPopulation);
		
		// 3.evaluate rate
		Pi = GAUtil.evaluateRate(fitness, oldPopulation);
		
		long startTime = System.currentTimeMillis(),endTime = 0l;
		// 4.genetic iteration
		if(endTime - startTime < criterion.getMAX_TIME_GEN())
			for (t = 0; t < criterion.getMAX_GEN(); t++) {
				// 4.1  generate next population
				evolution();
	//			showList();
				oldPopulation.clear();
				// 4.2 change
				for(Chromosome s : newPopulation){
					oldPopulation.add(s);
				}
				newPopulation.clear();
				// 4.3 evaluate fitness
				fitness = GAUtil.evaluateFitness(net, oldPopulation);
				// 4.4 evaluate rate
				Pi = GAUtil.evaluateRate(fitness, oldPopulation);
				endTime =System.currentTimeMillis();
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
				//cross
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
		if (k == criterion.getSCALE() / 2 - 1)
		{
			r = random.nextFloat();
			if (r < criterion.getPm()) {
				OnCVariation(k);
			}
		}
	}

	 // choice best fitness chromosome of T-gen population and copy it to chilld
	public void selectBest() {
		int k, i, bestId=0, bestEvaluation;

		bestEvaluation = oldPopulation.get(0).getPathsAndCost().getCosts();
		for (k = 1; k < criterion.getSCALE(); k++) {
			if (bestEvaluation > oldPopulation.get(k).getPathsAndCost().getCosts()) {
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
				if (ran1 <= Pi[i]) {
					break;
				}
			}
			selectId = i;
			newPopulation.add(k, oldPopulation.get(selectId));
		}
	}

	// cross generate diffence chromesome
	public void OXCross(int k1, int k2) {
		int i, j, k, flag;
		int ran1, ran2, temp;
		int[] Gh1 = new int[nodesNum];
		int[] Gh2 = new int[nodesNum];

		ran1 = random.nextInt(65535) % nodesNum;
		ran2 = random.nextInt(65535) % nodesNum;
		while (ran1 == ran2) {
			ran2 = random.nextInt(65535) % nodesNum;
		}

		if (ran1 > ran2)
		{
			temp = ran1;
			ran1 = ran2;
			ran2 = temp;
		}

		for (i = 0, j = ran2; j < nodesNum; i++, j++) {
			Gh2[i] = newPopulation.get(k1).getGene()[j];
		}

		flag = i;

		for (k = 0, j = flag; j < nodesNum;)
		{
			System.err.println("k:"+k+" ,j:"+j);
			Gh2[j] = newPopulation.get(k2).getGene()[k++];
			for (i = 0; i < flag; i++) {
				if (Gh2[i] == Gh2[j]) {
					break;
				}
			}
			if (i == flag) {
				j++;
			}
		}

		flag = ran1;
		for (k = 0, j = 0; k < nodesNum;)
		{
			Gh1[j] = newPopulation.get(k1).getGene()[k++];
			for (i = 0; i < flag; i++) {
				if (newPopulation.get(k2).getGene()[i] == Gh1[j]) {
					break;
				}
			}
			if (i == flag) {
				j++;
			}
		}

		flag = nodesNum - ran1;

		for (i = 0, j = flag; j < nodesNum; j++, i++) {
			Gh1[j] = newPopulation.get(k2).getGene()[i];
		}

		//put into population
		for (i = 0; i < nodesNum; i++) {
			newPopulation.get(k1).setGene(i, Gh1[i]);
			newPopulation.get(k2).setGene(i, Gh2[i]);
		}
	}

	// N-times change mutation operator
	public void OnCVariation(int k) {
		int ran1, ran2, temp;
		//change count
		int count;

		count = random.nextInt(65535) % nodesNum;

		for (int i = 0; i < count; i++) {

			ran1 = random.nextInt(65535) % nodesNum;
			ran2 = random.nextInt(65535) % nodesNum;
			while (ran1 == ran2) {
				ran2 = random.nextInt(65535) % nodesNum;
			}
			temp = newPopulation.get(k).getGene()[ran1];
			newPopulation.get(k).setGene(ran1,newPopulation.get(k).getGene()[ran2]);
			newPopulation.get(k).setGene(ran2,temp);
		}
	}

	private void showList(boolean flag) {
		if(flag){
			System.out.println("...原种群...");
			for(Chromosome chromosome :oldPopulation){
				System.out.println(chromosome);
			}
		}else{
			System.out.println("...新种群...");
			for(Chromosome chromosome : newPopulation){
				System.out.println(chromosome);
			}
		}
	}
}