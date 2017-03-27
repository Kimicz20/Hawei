package com.lab603.chenzuo.module;

import java.util.Arrays;
import java.util.List;

import com.lab603.module.ResultPathsAndCost;

public class Chromosome {
	
	private int[] gene;
	
	ResultPathsAndCost pathsAndCost;
	
	List<Integer> serversId;
	
	double x = 0;
	
	double T;
	
	double P;
	
	public Chromosome() {

	}
	
	public Chromosome(List<Integer> id, double x, double t) {
		this.serversId = id;
		this.x = x;
		T = t;
		P = Math.exp(-x/T);
	}
	
	public Chromosome(List<Integer> ids) {
		if(ids.get(0) == 0){
			ids.remove(0);
			gene = new int[ids.get(0)];
			for(int i=1;i<ids.size();i++)
				gene[ids.get(i)]=1;
		}else{
			gene = new int[ids.get(0)];
			for(int i=1;i<ids.get(0);i++)
				gene[i]= Math.random() > 0.5 ? 1:0;
		}
		
	}
	
	public ResultPathsAndCost getPathsAndCost() {
		return pathsAndCost;
	}

	public void setPathsAndCost(ResultPathsAndCost pathsAndCost) {
		this.pathsAndCost = pathsAndCost;
	}

	public int[] getGene() {
		return gene;
	}
	public void setGene(int index,int value) {
		gene[index] = value;
	}
	
	
	
	@Override
	public String toString() {
		return "Chromosome [gene=" + Arrays.toString(gene) + ", pathsAndCost=" + pathsAndCost + "]";
	}
}