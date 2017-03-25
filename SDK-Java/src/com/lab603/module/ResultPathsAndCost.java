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

	public String[] result(){
		
		String[] result = new String[paths.size()+2];
		result[0] = paths.size()+"";
		result[1] = "";
		for(int j=2;j<result.length;j++){
			StringBuffer sb = new StringBuffer();
			List<Integer> s = paths.get(j-2);
			for(int i=0;i<s.size()-1;i++)
				sb.append(s.get(i)+" ");
			sb.append(s.get(s.size()-1));
			result[j] = sb.toString();
		}
		return result;
	}
	@Override
	public String toString() {
		
		String tmp = "";
		for(int j=0;j<paths.size();j++){
			List<Integer> s = paths.get(j);
			for(int i=0;i<s.size()-1;i++)
				tmp+=s.get(i)+" ";
			if(j == paths.size()-1)
				tmp+=s.get(s.size()-1);
			else
				tmp+=s.get(s.size()-1)+"\n";
		}
		return tmp;
	}
	
	
}
