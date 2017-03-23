package com.lab603.yj.util;

/***
 * 
 * @author geek
 *	first保存最短路径
 *	second保存顶点编号
 */
public class Pair {

	private int first;
	private int second;

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

	@Override
	public String toString() {
		return "Pair [first=" + first + ", second=" + second + "]";
	}
  
}
