package com.lab603.jun.util.dj;

import java.util.List;

public class MinStep {  
    private boolean reachable;//是否可达  
    private int minStep;//最短步长  
    private List<Integer> step;//最短路径  
  
    public MinStep() {  
    }  
      
    public MinStep(boolean reachable, int minStep) {  
        this.reachable = reachable;  
        this.minStep = minStep;  
    }  
  
    public boolean isReachable() {  
        return reachable;  
    }  
    public void setReachable(boolean reachable) {  
        this.reachable = reachable;  
    }  
    public int getMinStep() {  
        return minStep;  
    }  
    public void setMinStep(int minStep) {  
        this.minStep = minStep;  
    }  
    public List<Integer> getStep() {  
        return step;  
    }  
    public void setStep(List<Integer> step) {  
        this.step = step;  
    }  
}  
