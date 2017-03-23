package com.lab603.jun.util.dj;

import java.util.HashMap;

public interface Distance {  
    public static final MinStep UNREACHABLE = new MinStep(false, -1);  
    /** 
     * @param start 
     * @param end 
     * @param stepLength 
     * @return 
     * @Author:lulei   
     * @Description: 起点到终点的最短路径 
     */  
    public MinStep getMinStep(int start, int end, 
    		final HashMap<Integer, HashMap<Integer, Integer>> stepLength);  
}  