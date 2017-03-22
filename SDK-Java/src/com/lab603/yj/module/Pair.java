package com.lab603.yj.module;

public class Pair {

	private int first;//first保存最短路径
	private int second;//sec保存顶点编号

	public Pair() {

	}

	public Pair(int first, int second) {
		this.first = first;
		this.second = second;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

}
