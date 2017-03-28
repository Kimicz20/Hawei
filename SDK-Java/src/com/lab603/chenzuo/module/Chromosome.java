package com.lab603.chenzuo.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
		P = Math.exp(-x / T);
	}

	public Chromosome(List<Integer> ids) {
		if (ids.get(0) == 0) {
			ids.remove(0);
			gene = new int[ids.get(0)];
			for (int i = 1; i < ids.size(); i++)
				gene[ids.get(i)] = 1;
		} else {
			gene = new int[ids.get(0)];
			for (int i = 1; i < ids.get(0); i++)
				gene[i] = Math.random() > 0.5 ? 1 : 0;
		}
		serversId = new ArrayList<Integer>();
		setServersId(gene2server());
	}

	public Chromosome(int size) {
		initGeneSize(size);
		for (int i = 1; i < size; i++)
			gene[i] = Math.random() > 0.5 ? 1 : 0;
		serversId = new ArrayList<Integer>();
		setServersId(gene2server());
	}

	public List<Integer> getServersId() {
		return serversId;
	}

	public void setServersId(List<Integer> serversId) {
		this.serversId = serversId;
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

	public void setGene(int index, int value) {
		gene[index] = value;
	}

	@Override
	public String toString() {
		// return "Chromosome [gene=" + Arrays.toString(gene) + ",
		// pathsAndCost=" + pathsAndCost.getCosts() + "]";
		StringBuilder sb = new StringBuilder();
		for (int i : serversId) {
			sb.append(i+" ");
		}
		return Integer.toString(pathsAndCost.getCosts());
	}

	public void mutation() {
		int ran1, ran2, temp;
		int size = gene.length;
		Random random = new Random(System.currentTimeMillis());
		// change count
		int count;

		count = random.nextInt(65535) % size;

		for (int i = 0; i < count; i++) {

			ran1 = random.nextInt(65535) % size;
			ran2 = random.nextInt(65535) % size;
			while (ran1 == ran2) {
				ran2 = random.nextInt(65535) % size;
			}
			temp = gene[ran1];
			gene[ran1] = gene[ran2];
			gene[ran2] = temp;
		}
		setServersId(gene2server());
	}

	public List<Integer> gene2server(){
		List<Integer> ids = new ArrayList<Integer>();
		for(int i=0;i< gene.length;i++){
			if(gene[i] == 1)
				ids.add(i);
		}	
		return ids;
	}
	
	public static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {
		if (p1 == null || p2 == null) {
			return null;
		}
		if (p1.gene == null || p2.gene == null) {
			return null;
		}
		if (p1.gene.length != p2.gene.length) {
			return null;
		}
		Chromosome c1 = clone(p1);
		Chromosome c2 = clone(p2);
		int size = c1.gene.length;
		int a = ((int) (Math.random() * size)) % size;
		int b = ((int) (Math.random() * size)) % size;
		int min = a > b ? b : a;
		int max = a > b ? a : b;
		for (int i = min; i <= max; i++) {
			int t = c1.gene[i];
			c1.gene[i] = c2.gene[i];
			c2.gene[i] = t;
		}
			
		c1.setServersId(c1.gene2server());
		c2.setServersId(c2.gene2server());
		List<Chromosome> list = new ArrayList<Chromosome>();
		list.add(c1);
		list.add(c2);
		return list;
	}

	public static Chromosome clone(final Chromosome c) {
		if (c == null || c.gene == null) {
			return null;
		}
		Chromosome copy = new Chromosome();
		copy.initGeneSize(c.gene.length);
		for (int i = 0; i < c.gene.length; i++) {
			copy.gene[i] = c.gene[i];
		}
		copy.pathsAndCost = c.pathsAndCost;
		copy.serversId = c.serversId;
		return copy;
	}

	private void initGeneSize(int size) {
		if (size <= 0) {
			return;
		}
		gene = new int[size];
	}

	public boolean equals(Chromosome chro) {
		if (Arrays.equals(gene, chro.getGene())) {
			return true;
		}
		return false;

	}

}