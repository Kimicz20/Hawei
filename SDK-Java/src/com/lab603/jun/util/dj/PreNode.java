package com.lab603.jun.util.dj;

public class PreNode {  
    private int preNodeNum;// ���� ǰһ���ڵ�  
    private int nodeStep;// ��С����  
  
    public PreNode(int preNodeNum, int nodeStep) {  
        this.preNodeNum = preNodeNum;  
        this.nodeStep = nodeStep;  
    }  
  
    public int getPreNodeNum() {  
        return preNodeNum;  
    }  
    public void setPreNodeNum(int preNodeNum) {  
        this.preNodeNum = preNodeNum;  
    }  
    public int getNodeStep() {  
        return nodeStep;  
    }  
    public void setNodeStep(int nodeStep) {  
        this.nodeStep = nodeStep;  
    }  
}  
