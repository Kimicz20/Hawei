package com.lab603.module;

import java.util.List;

public class ResultPathsAndCost {
	
	List<List<Integer>> paths;
	List<Integer> costs;
	public ResultPathsAndCost(List<List<Integer>> paths, List<Integer> costs) {
		this.paths = paths;
		this.costs = costs;
	}
	public List<List<Integer>> getPaths() {
		return paths;
	}
	public void setPaths(List<List<Integer>> paths) {
		this.paths = paths;
	}
	public List<Integer> getCosts() {
		return costs;
	}
	public void setCosts(List<Integer> costs) {
		this.costs = costs;
	}
	
}
