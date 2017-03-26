package com.lab603.chenzuo.module;

import java.util.Arrays;
import java.util.List;

public class Chromosome {
	
	private int[] gene;
	
	private int score;

	public Chromosome() {

	}
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
	public Chromosome(List<Integer> ids) {
		gene = new int[ids.get(0)];
		for(int i=1;i<ids.size();i++)
			if(i == ids.get(i))
				gene[i]=1;
			else
				gene[i]=0;
	}

	@Override
	public String toString() {
		return "Chromosome [gene=" + Arrays.toString(gene) + ", score=" + score + "]";
	}

}