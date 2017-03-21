package com.lab603.chenzuo.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.lab603.chenzuo.module.Chromosome;
import com.lab603.chenzuo.module.Net;

public class GAUtil {
	
	/**
	 * 根据文件名称读取图
	 * @param fileName
	 * @return
	 */
	public static String[] readFile(String fileName){
		String[] strbuff = null;
		String s;
		int i=0;
		BufferedReader data = null;
		 try {
			data = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			while ((s = data.readLine()) != null) {
				strbuff[i++] = s;
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strbuff;
	}
	/**
	 * 构建距离矩阵
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public static int[][] distanceBuild(String[] strbuff){
		int cityNum = strbuff.length;
		// 距离矩阵
		int[][] distance; 
		// 读取数据
		int[] x, y;
		
		distance = new int[cityNum][cityNum];
		x = new int[cityNum];
		y = new int[cityNum];
		for (int i = 0; i < cityNum; i++) {
			// 字符分割
			String[] strcol = strbuff[i].split(" ");
			x[i] = Integer.valueOf(strcol[1]);// x坐标
			y[i] = Integer.valueOf(strcol[2]);// y坐标
		}
		/*
		 *  计算距离矩阵:针对具体问题，距离计算方法也不一样
		 */
		for (int i = 0; i < cityNum - 1; i++) {
			distance[i][i] = 0; // 对角线为0
			for (int j = i + 1; j < cityNum; j++) {
				double rij = Math.sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j])) / 10.0);
				// 四舍五入，取整
				int tij = (int) Math.round(rij);
				if (tij < rij) {
					distance[i][j] = tij + 1;
					} else {
						distance[i][j] = tij;
						}
				distance[j][i] = distance[i][j];
				}
			}
		distance[cityNum - 1][cityNum - 1] = 0;
		return distance;
	}

	/**
	 * 计算种群适应度
	 * @param scale
	 * @param net
	 * @param oldPopulation
	 * @return
	 */
	public static int[] evaluateFitness(int scale ,Net net, List<Chromosome> oldPopulation){
		int cityNum = net.getCityNum();
		int[][] distance = net.getDistance();
		
		int[] fitness = new int[scale];
		for (int k = 0; k < scale; k++) {
			// 1.获取当前种群个体
			Chromosome chromosome = oldPopulation.get(k);
			int[] gene = chromosome.getGene();
			// 2.计算个体适应度
			int len = 0;
			for (int i = 1; i < cityNum; i++) {
				len += distance[gene[i - 1]][gene[i]];
			}
			len += distance[gene[cityNum - 1]][gene[0]];
			chromosome.setScore(len);
			fitness[k] = len;
		}
		return fitness;
	}
	
	/**
	 * @Author:kimi
	 * @Description: 计算种群中各个个体的累积概率， 前提是已经计算出各个个体的适应度fitness[max]
	 *               作为赌轮选择策略一部分，Pi[max]
	 */
	public static float[] countRate(int scale ,int[] fitness, List<Chromosome> oldPopulation) {
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
