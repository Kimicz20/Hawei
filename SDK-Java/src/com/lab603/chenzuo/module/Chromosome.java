package com.lab603.chenzuo.module;

/**   
*	种群个体（这里认为是染色体），在个体中，我们为这个个体添加两个属性:
*	gene :个体的基因
*	score:基因对应的适应度（函数值）
*/

import java.util.Arrays;
import java.util.Random;

public class Chromosome {
	private int[] gene;// 基因序列
	private int score;// 对应的函数得分

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int[] getGene() {
		return gene;
	}

	public void setGene(int[] gene) {
		this.gene = gene;
	}

	/**
	 * @param size
	 *            随机生成基因序列
	 */
	public Chromosome(int size) {
		int i,j;
		Random random = new Random(System.currentTimeMillis());
		if (size <= 0) {
			return;
		}
		initGeneSize(size);

		gene[0] = random.nextInt(65535) % size;
		for (i = 1; i < size;)// 染色体长度
		{
			gene[i] = random.nextInt(65535) % size;
			for (j = 0; j < i; j++) {
				if (gene[i] == gene[j]) {
					break;
				}
			}
			if (j == i) {
				i++;
			}
		}
	}

	/**
	 * 生成一个新基因
	 */
	public Chromosome() {

	}

	/**
	 * @param size
	 * @Author:kimi
	 * @Description: 初始化基因长度
	 */
	private void initGeneSize(int size) {
		if (size <= 0) {
			return;
		}
		gene = new int[size];
	}

	@Override
	public String toString() {
		return "Chromosome [gene=" + Arrays.toString(gene) + ", score=" + score + "]";
	}

}