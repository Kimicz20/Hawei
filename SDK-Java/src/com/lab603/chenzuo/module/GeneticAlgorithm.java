package com.lab603.chenzuo.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.lab603.chenzuo.util.GAUtil;

public class GeneticAlgorithm {

	private int scale; // 种群规模，种群规模是指任意一代中的个体总数
	private int MAX_GEN; // 运行代数
	private int bestT; // 最佳出现代数
	private int cityNum;
	private int[] fitness;// 种群适应度，表示种群中各个个体的适应度
	private float[] Pi;// 种群中各个个体的累计概率
	private float Pc;// 交叉概率
	private float Pm;// 变异概率
	private int t;// 当前代数
	Random random = null;
	
	private Net net = new Net();
	
	// 初始种群
	private List<Chromosome> oldPopulation = null;
	// 新的种群，子代种群
	private List<Chromosome> newPopulation = null;


	public GeneticAlgorithm() {
	}

	/**
	 * constructor of GA
	 * 
	 * @param s
	 *            种群规模
	 * @param g
	 *            运行代数
	 * @param c
	 *            交叉率
	 * @param m
	 *            变异率
	 * 
	 **/
	public GeneticAlgorithm(int s, int g, float c, float m) {
		scale = s;
		MAX_GEN = g;
		Pc = c;
		Pm = m;
	}

	public void init(String filename) throws IOException {
		
		//初始化距离矩阵以及参数信息
		net.init(filename);
		//初始化城市数目
		cityNum = net.getCityNum();
		bestT = 0;
		t = 0;
		random = new Random(System.currentTimeMillis());
		oldPopulation = new ArrayList<Chromosome>();
		newPopulation = new ArrayList<Chromosome>();
		Pi = new float[scale];
		fitness = new int[scale];
	}

	/**
	 * @Author:kimi
	 * @Description: 初始化种群
	 */
	private void initGroup() {
		for (int i = 0; i < scale; i++) {
			Chromosome chro = new Chromosome();
			oldPopulation.add(chro);
		}
	}

	/**
	 * @Author:kimi
	 * @Description:种群进行遗传
	 */
	public void evolve() {
		// 1.初始化种群
		initGroup();
		// 2.计算初始化种群中各个个体的累积概率，Pi[max]
		fitness = GAUtil.evaluateFitness(scale, net, oldPopulation);
		Pi = GAUtil.countRate(scale,fitness, oldPopulation);
		System.out.println("初始种群...");
		// 3.遗传迭代
		for (t = 0; t < MAX_GEN; t++) {
			// 4.1 生成下一代种群
			evolution();
			// 4.2 将新种群newGroup复制到旧种群oldGroup中，准备下一代进化
			oldPopulation.clear();
			oldPopulation = newPopulation;
			newPopulation.clear();
			// 计算种群中各个个体的累积概率
			fitness = GAUtil.evaluateFitness(scale, net, oldPopulation);
		}
		printf();
	}
	
	/**
	 * @return
	 * @Author:kimi
	 * @Description: 轮盘赌法选择可以遗传下一代种群
	 */
	private void evolution() {
		int k;
		// 挑选某代种群中适应度最高的个体
		selectBestGh();

		// 赌轮选择策略挑选scale-1个下一代个体
		select();

		// Random random = new Random(System.currentTimeMillis());
		float r;

		for (k = 1; k + 1 < scale / 2; k = k + 2) {
			r = random.nextFloat();// /产生概率
			if (r < Pc) {
				OXCross1(k, k + 1);// 进行交叉
				// OXCross(k,k+1);//进行交叉
			} else {
				r = random.nextFloat();// /产生概率
				// 变异
				if (r < Pm) {
					OnCVariation(k);
				}
				r = random.nextFloat();// /产生概率
				// 变异
				if (r < Pm) {
					OnCVariation(k + 1);
				}
			}
		}
		if (k == scale / 2 - 1)// 剩最后一个染色体没有交叉L-1
		{
			r = random.nextFloat();// /产生概率
			if (r < Pm) {
				OnCVariation(k);
			}
		}
	}

	/*
	 * 挑选某代种群中适应度最高的个体，直接复制到子代中 前提是已经计算出各个个体的适应度Fitness[max]
	 */
	public void selectBestGh() {
		int k, i, maxid;
		int maxevaluation;

		maxid = 0;
		maxevaluation = oldPopulation.get(0).getScore();
		for (k = 1; k < scale; k++) {
			if (maxevaluation > oldPopulation.get(k).getScore()) {
				maxevaluation = oldPopulation.get(k).getScore();
				maxid = k;
			}
		}

		if (net.getBestLength() > maxevaluation) {
			net.setBestLength(maxevaluation);
			bestT = t; // 最好的染色体出现的代数;
			for (i = 0; i < cityNum; i++) {
				net.setBestTourAtIndex(i, oldPopulation.get(maxid).getGene()[i]);
			}
		}

		// 将当代种群中适应度最高的染色体k复制到新种群中，排在第一位0
		newPopulation.add(0, oldPopulation.get(maxid));
	}

	// 赌轮选择策略挑选
	public void select() {
		int k, i, selectId;
		float ran1;
		for (k = 1; k < scale; k++) {
			ran1 = (float) (random.nextInt(65535) % 1000 / 1000.0);
			// System.out.println("概率"+ran1);
			// 产生方式
			for (i = 0; i < scale; i++) {
				if (ran1 <= Pi[i]) {
					break;
				}
			}
			selectId = i;
			newPopulation.add(k, oldPopulation.get(selectId));
		}
	}

	// 交叉算子,相同染色体交叉产生不同子代染色体
	public void OXCross1(int k1, int k2) {
		int i, j, k, flag;
		int ran1, ran2, temp;
		int[] Gh1 = new int[cityNum];
		int[] Gh2 = new int[cityNum];
		// Random random = new Random(System.currentTimeMillis());

		ran1 = random.nextInt(65535) % cityNum;
		ran2 = random.nextInt(65535) % cityNum;
		while (ran1 == ran2) {
			ran2 = random.nextInt(65535) % cityNum;
		}

		if (ran1 > ran2)// 确保ran1<ran2
		{
			temp = ran1;
			ran1 = ran2;
			ran2 = temp;
		}

		// 将染色体1中的第三部分移到染色体2的首部
		for (i = 0, j = ran2; j < cityNum; i++, j++) {
			Gh2[i] = newPopulation.get(k1).getGene()[j];
		}

		flag = i;// 染色体2原基因开始位置

		for (k = 0, j = flag; j < cityNum;)// 染色体长度
		{
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
		for (k = 0, j = 0; k < cityNum;)// 染色体长度
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

		flag = cityNum - ran1;

		for (i = 0, j = flag; j < cityNum; j++, i++) {
			Gh1[j] = newPopulation.get(k2).getGene()[i];
		}

		for (i = 0; i < cityNum; i++) {
			newPopulation.get(k1).getGene()[i] = Gh1[i];// 交叉完毕放回种群
			newPopulation.get(k2).getGene()[i] = Gh2[i];// 交叉完毕放回种群
		}
	}

	// 多次对换变异算子
	public void OnCVariation(int k) {
		int ran1, ran2, temp;
		int count;// 对换次数

		count = random.nextInt(65535) % cityNum;

		for (int i = 0; i < count; i++) {

			ran1 = random.nextInt(65535) % cityNum;
			ran2 = random.nextInt(65535) % cityNum;
			while (ran1 == ran2) {
				ran2 = random.nextInt(65535) % cityNum;
			}
			temp = newPopulation.get(k).getGene()[ran1];
			newPopulation.get(k).getGene()[ran1] = newPopulation.get(k).getGene()[ran2];
			newPopulation.get(k).getGene()[ran2] = temp;
		}
	}

	/**
	 * 打印
	 */
	private void printf() {
		System.out.println("最后种群...");
		for (Chromosome i : oldPopulation) {
			System.out.println(i);
		}
		System.out.println("最佳长度出现代数：");
		System.out.println(bestT);
		System.out.println("最佳长度");
		System.out.println(net.getBestLength());
		System.out.println("最佳路径：");
		System.out.println(Arrays.toString(net.getBestTour()));
	}
}