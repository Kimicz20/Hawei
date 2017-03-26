package huawei.genetic.algorithm;

/**   
*	种群个体（这里认为是染色体），在个体中，我们为这个个体添加两个属性:
*	gene :个体的基因
*	score:基因对应的适应度（函数值）
*/      
 
import java.util.ArrayList;  
import java.util.List;  

public class Chromosome {  
   private boolean[] gene;//基因序列  
   private double score;//对应的函数得分  
     
   public double getScore() {  
       return score;  
   }  
 
   public void setScore(double score) {  
       this.score = score;  
   }  
 
   /** 
    * @param size 
    * 随机生成基因序列 
    */  
   public Chromosome(int size) {  
       if (size <= 0) {  
           return;  
       }  
       initGeneSize(size);  
       for (int i = 0; i < size; i++) {  
           gene[i] = Math.random() >= 0.5;  
       }  
   }  
     
   /** 
    * 生成一个新基因 
    */  
   public Chromosome() {  
         
   }  
     
   /** 
    * @param c 
    * @return 
    * @Author:kimi   
    * @Description: 克隆基因 
    */  
   public static Chromosome clone(final Chromosome c) {  
       if (c == null || c.gene == null) {  
           return null;  
       }  
       Chromosome copy = new Chromosome();  
       copy.initGeneSize(c.gene.length);  
       for (int i = 0; i < c.gene.length; i++) {  
           copy.gene[i] = c.gene[i];  
       }  
       return copy;  
   }  
     
   /** 
    * @param size 
    * @Author:kimi   
    * @Description: 初始化基因长度 
    */  
   private void initGeneSize(int size) {  
       if (size <= 0) {  
           return;  
       }  
       gene = new boolean[size];  
   }  
     
     
   /** 
    * @param c1 
    * @param c2 
    * @Author:kimi   
    * @Description: 遗传产生下一代 
    */  
   public static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {  
       if (p1 == null || p2 == null) { //染色体有一个为空，不产生下一代  
           return null;  
       }  
       if (p1.gene == null || p2.gene == null) { //染色体有一个没有基因序列，不产生下一代  
           return null;  
       }  
       if (p1.gene.length != p2.gene.length) { //染色体基因序列长度不同，不产生下一代  
           return null;  
       }  
       Chromosome c1 = clone(p1);  
       Chromosome c2 = clone(p2);  
       //随机产生交叉互换位置  
       int size = c1.gene.length;  
       int a = ((int) (Math.random() * size)) % size;  
       int b = ((int) (Math.random() * size)) % size;  
       int min = a > b ? b : a;  
       int max = a > b ? a : b;  
       //对位置上的基因进行交叉互换  
       for (int i = min; i <= max; i++) {  
           boolean t = c1.gene[i];  
           c1.gene[i] = c2.gene[i];  
           c2.gene[i] = t;  
       }  
       List<Chromosome> list = new ArrayList<Chromosome>();  
       list.add(c1);  
       list.add(c2);  
       return list;  
   }  
     
   /** 
    * @param num 
    * @Author:kimi   
    * @Description: 基因num个位置发生变异 
    */  
   public void mutation(int num) {  
       //允许变异  
       int size = gene.length;  
       for (int i = 0; i < num; i++) {  
           //寻找变异位置  
           int at = ((int) (Math.random() * size)) % size;  
           //变异后的值  
           boolean bool = !gene[at];  
           gene[at] = bool;  
       }  
   }  
     
   /** 
    * @return 
    * @Author:kimi   
    * @Description: 将基因转化为对应的数字 
    */  
   public int getNum() {  
       if (gene == null) {  
           return 0;  
       }  
       int num = 0;  
       for (boolean bool : gene) {  
           num <<= 1;  
           if (bool) {  
               num += 1;  
           }  
       }  
       return num;  
   }  
}