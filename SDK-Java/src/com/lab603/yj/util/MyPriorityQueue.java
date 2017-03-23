package com.lab603.yj.util;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MyPriorityQueue{
	
	public static PriorityQueue<Pair> getPriorityQueue(){
		Comparator<Pair> OrderIsdn =  new Comparator<Pair>(){  
			@Override
			public int compare(Pair o1, Pair o2) {
					int first = o1.getFirst() - o2.getFirst();
					int second =  o1.getSecond()-o2.getSecond();
					if(first < 0){
						return -1;
					}else if(first == 0){
						if(second < 0)
							return -1;
						else if(second == 0)
							return 0;
						else
							return 1;
					}else
						return 1;
				}  
        };  
        
       return new PriorityQueue<Pair>(11,OrderIsdn);  
	}
}
