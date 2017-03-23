package com.lab603.jun.util.dj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

public class DistanceDijkstraImpl implements Distance{  
    //key1节点编号，key2节点编号，value为key1到key2的步长  
    private HashMap<Integer, HashMap<Integer, Integer>> stepLength;  
    //非独立节点个数  
    private int nodeNum;  
    //移除节点  
    private HashSet<Integer> outNode;  
    //起点到各点的步长，key为目的节点，value为到目的节点的步长  
    private HashMap<Integer, PreNode> nodeStep;  
    //下一次计算的节点  
    private LinkedList<Integer> nextNode;  
    //起点、终点  
    private int startNode;  
    private int endNode;  
      
    /** 
     * @param start 
     * @param end 
     * @param stepLength 
     * @return 
     * @Author:lulei   
     * @Description: start 到 end 的最短距离 
     */  
    public MinStep getMinStep(int start, int end, 
    		final HashMap<Integer, HashMap<Integer, Integer>> stepLength) {  
        this.stepLength = stepLength;  
        this.nodeNum = this.stepLength != null ? this.stepLength.size() : 0;  
        //起点、终点不在目标节点内，返回不可达  
        if (this.stepLength == null || (!this.stepLength.containsKey(start)) || (!this.stepLength.containsKey(end))) {  
            return UNREACHABLE;  
        }  
        initProperty(start, end);  
        step();  
        if (nodeStep.containsKey(end)) {  
            return changeToMinStep(end);  // g
        }  
        return UNREACHABLE;  
    }  
    
    /**
     * 获取start到所有可达点的最短路径 by jun
     * @param countOfNode
     * @return
     */
    public ArrayList<MinStep> getAllMinSetp(int countOfNode) {
    	ArrayList<MinStep> res = new ArrayList<>();
    	for(int i = 0; i < countOfNode; i++) {
    		 if (nodeStep.containsKey(i)) {
				res.add(changeToMinStep(i));
			}
    	}
		return res;
    	
    }
    /** 
     * 返回到end的最短距离以及路径 
     * @param end 
     */  
    private MinStep changeToMinStep(int end) {  
        MinStep minStep = new MinStep();  
        minStep.setMinStep(nodeStep.get(end).getNodeStep());  
        minStep.setReachable(true);  
        LinkedList<Integer> step = new LinkedList<Integer>();  
        minStep.setStep(step);  
        int nodeNum = end;  
        step.addFirst(nodeNum);  
        while (nodeStep.containsKey(nodeNum)) {  
            int node = nodeStep.get(nodeNum).getPreNodeNum();  
            step.addFirst(node);  
            nodeNum = node;  
        }  
        return minStep;  
    }  
      
    /** 
     * @param start 
     * @Author:lulei   
     * @Description: 初始化属性 
     */  
    private void initProperty(int start, int end) {  
        outNode = new HashSet<Integer>();  
        nodeStep = new HashMap<Integer, PreNode>();  
        nextNode = new LinkedList<Integer>();  
        nextNode.add(start);  
        startNode = start;  
        endNode = end;  
    }  
      
    /** 
     * @param end 
     * @Author:lulei   
     * @Description: 
     */  
    private void step() {  
        if (nextNode == null || nextNode.size() < 1) {  
            return;  
        }  
        if (outNode.size() == nodeNum) {  
            return;  
        }  
        //获取下一个计算节点  
        int start = nextNode.removeFirst();  
        //到达该节点的最小距离  
        int step = 0;  
        if (nodeStep.containsKey(start)) {  
            step = nodeStep.get(start).getNodeStep();  
        }  
        //获取该节点可达节点  
        HashMap<Integer,Integer> nextStep = stepLength.get(start);  
        Iterator<Entry<Integer, Integer>> iter = nextStep.entrySet().iterator();  
        while (iter.hasNext()) {  
            Entry<Integer, Integer> entry = iter.next();  
            Integer key = entry.getKey();  
            //如果是起点到起点，不计算之间的步长  
            if (key == startNode) {  
                continue;  
            }  
            //起点到可达节点的距离  
            Integer value = entry.getValue() + step;  
            if ((!nextNode.contains(key)) && (!outNode.contains(key))) {  
                nextNode.add(key);  
            }  
            if (nodeStep.containsKey(key)) {  
                if (value < nodeStep.get(key).getNodeStep()) {  
                    nodeStep.put(key, new PreNode(start, value));  
                }  
            } else {  
                nodeStep.put(key, new PreNode(start, value));  
            }  
        }  
        //将该节点移除  
        outNode.add(start);  
        //计算下一个节点  
        step();  
    }  
}  