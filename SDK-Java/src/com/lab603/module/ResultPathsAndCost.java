package com.lab603.module;

import java.util.List;

public class ResultPathsAndCost {
	
	List<List<Integer>> paths;
	int costs;
	
	public ResultPathsAndCost() {
	}
	
	public ResultPathsAndCost(List<List<Integer>> paths, int costs) {
		super();
		this.paths = paths;
		this.costs = costs;
	}
	public List<List<Integer>> getPaths() {
		return paths;
	}
	public void setPaths(List<List<Integer>> paths) {
		this.paths = paths;
	}
	public int getCosts() {
		return costs;
	}
	public void setCosts(int costs) {
		this.costs = costs;
	}

	@Override
	public String toString() {
		String tmp = "总花费: "+costs+"\n";
		for(List<Integer> s:paths){
			for(int i:s)
				tmp+=i+" ";
			tmp+="\n";
		}
		return tmp;
	}
	
	
}
