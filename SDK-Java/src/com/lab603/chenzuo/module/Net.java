package com.lab603.chenzuo.module;

import java.io.IOException;
import java.util.List;

import com.lab603.chenzuo.util.GAUtil;

public class Net {

	private int bestLength = Integer.MAX_VALUE; // 最佳长度
	private int[] bestTour; // 最佳路径
	private int[][] distance; // 距离矩阵
	private int cityNum; // 城市数量，染色体长度
	
	public Net() {
	}

	public int getBestLength() {
		return bestLength;
	}

	public void setBestLength(int bestLength) {
		this.bestLength = bestLength;
	}

	public int[] getBestTour() {
		return bestTour;
	}

	public void setBestTourAtIndex(int index,int tmp) {
		bestTour[index] = tmp;
	}

	public int[][] getDistance() {
		return distance;
	}

	public int getCityNum() {
		return cityNum;
	}

	public void init(String fileName) {
		List<String> tmp = GAUtil.readFile(fileName);
		this.cityNum = tmp.size();
		this.bestTour = new int[tmp.size() + 1];
		this.distance = GAUtil.distanceBuild(tmp);
	}
	
	
}
