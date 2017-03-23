package com.lab603.jun.util.dj;

import java.util.HashMap;

public class DistanceTest {  
	  
    public static void main(String[] args) {  
        // TODO Auto-generated method stub    
        HashMap<Integer, HashMap<Integer,Integer>> stepLength = new HashMap<Integer, HashMap<Integer,Integer>>();  
        HashMap<Integer,Integer> step1 = new HashMap<Integer, Integer>();  
        stepLength.put(1, step1);  
        step1.put(6, 14);  
        step1.put(3, 9);  
        step1.put(2, 7);  
          
        HashMap<Integer,Integer> step2 = new HashMap<Integer, Integer>();  
        stepLength.put(2, step2);  
        step2.put(1, 7);  
        step2.put(3, 10);  
        step2.put(4, 15);  
          
        HashMap<Integer,Integer> step3 = new HashMap<Integer, Integer>();  
        stepLength.put(3, step3);  
        step3.put(1, 9);  
        step3.put(2, 10);  
        step3.put(4, 11);  
        step3.put(6, 2);  
          
        HashMap<Integer,Integer> step4 = new HashMap<Integer, Integer>();  
        stepLength.put(4, step4);  
        step4.put(2, 15);  
        step4.put(5, 5);  
        step4.put(3, 11);  
          
        HashMap<Integer,Integer> step5 = new HashMap<Integer, Integer>();  
        stepLength.put(5, step5);  
        step5.put(6, 9);  
        step5.put(4, 5);  
          
        HashMap<Integer,Integer> step6 = new HashMap<Integer, Integer>();  
        stepLength.put(6, step6);  
        step6.put(1, 14);  
        step6.put(5, 9);  
        step6.put(3, 2);  
          
        Distance distance = new DistanceDijkstraImpl();  
        MinStep step = distance.getMinStep(1, 5, stepLength);  
        System.out.println(step.getStep());  
    }  
  
}  
