package com.lab603.jun.util.dj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

public class DistanceDijkstraImpl implements Distance{  
    //key1�ڵ��ţ�key2�ڵ��ţ�valueΪkey1��key2�Ĳ���  
    private HashMap<Integer, HashMap<Integer, Integer>> stepLength;  
    //�Ƕ����ڵ����  
    private int nodeNum;  
    //�Ƴ��ڵ�  
    private HashSet<Integer> outNode;  
    //��㵽����Ĳ�����keyΪĿ�Ľڵ㣬valueΪ��Ŀ�Ľڵ�Ĳ���  
    private HashMap<Integer, PreNode> nodeStep;  
    //��һ�μ���Ľڵ�  
    private LinkedList<Integer> nextNode;  
    //��㡢�յ�  
    private int startNode;  
    private int endNode;  
      
    /** 
     * @param start 
     * @param end 
     * @param stepLength 
     * @return 
     * @Author:lulei   
     * @Description: start �� end ����̾��� 
     */  
    public MinStep getMinStep(int start, int end, 
    		final HashMap<Integer, HashMap<Integer, Integer>> stepLength) {  
        this.stepLength = stepLength;  
        this.nodeNum = this.stepLength != null ? this.stepLength.size() : 0;  
        //��㡢�յ㲻��Ŀ��ڵ��ڣ����ز��ɴ�  
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
     * ��ȡstart�����пɴ������·�� by jun
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
     * ���ص�end����̾����Լ�·�� 
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
     * @Description: ��ʼ������ 
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
        //��ȡ��һ������ڵ�  
        int start = nextNode.removeFirst();  
        //����ýڵ����С����  
        int step = 0;  
        if (nodeStep.containsKey(start)) {  
            step = nodeStep.get(start).getNodeStep();  
        }  
        //��ȡ�ýڵ�ɴ�ڵ�  
        HashMap<Integer,Integer> nextStep = stepLength.get(start);  
        Iterator<Entry<Integer, Integer>> iter = nextStep.entrySet().iterator();  
        while (iter.hasNext()) {  
            Entry<Integer, Integer> entry = iter.next();  
            Integer key = entry.getKey();  
            //�������㵽��㣬������֮��Ĳ���  
            if (key == startNode) {  
                continue;  
            }  
            //��㵽�ɴ�ڵ�ľ���  
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
        //���ýڵ��Ƴ�  
        outNode.add(start);  
        //������һ���ڵ�  
        step();  
    }  
}  