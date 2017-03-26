package com.lab603.chenzuo.util;

public class GACriterion {

	private int SCALE; 			// 种群规模，种群规模是指任意一代中的个体总数
	private float Pc;			// 交叉概率
	private float Pm;			// 变异概率
	private long MAX_TIME_GEN; 	// 种群代数执行时间
	private long MAX_GEN; 		// 最大执行代数
	
	public GACriterion(int sCALE, float pc, float pm, long mAX_TIME_GEN, long mAX_GEN) {
		SCALE = sCALE;
		Pc = pc;
		Pm = pm;
		MAX_TIME_GEN = mAX_TIME_GEN;
		MAX_GEN = mAX_GEN;
	}

	public int getSCALE() {
		return SCALE;
	}

	public void setSCALE(int sCALE) {
		SCALE = sCALE;
	}

	public float getPc() {
		return Pc;
	}

	public void setPc(float pc) {
		Pc = pc;
	}

	public float getPm() {
		return Pm;
	}

	public void setPm(float pm) {
		Pm = pm;
	}

	public long getMAX_TIME_GEN() {
		return MAX_TIME_GEN;
	}

	public void setMAX_TIME_GEN(long mAX_TIME_GEN) {
		MAX_TIME_GEN = mAX_TIME_GEN;
	}

	public long getMAX_GEN() {
		return MAX_GEN;
	}

	public void setMAX_GEN(long mAX_GEN) {
		MAX_GEN = mAX_GEN;
	}

	
}
