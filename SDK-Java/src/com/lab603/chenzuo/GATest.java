package com.lab603.chenzuo;

import java.io.IOException;

import com.lab603.chenzuo.module.GeneticAlgorithm;

public class GATest {

	public static void main(String[] args) throws IOException {
		GeneticAlgorithm ga = new GeneticAlgorithm(30, 1000, 0.8f, 0.9f);
		ga.init("c://data.txt");
		ga.evolve();
	}
}
