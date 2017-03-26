package huawei.genetic.algorithm;

/**   
*	��Ⱥ���壨������Ϊ��Ⱦɫ�壩���ڸ����У�����Ϊ������������������:
*	gene :����Ļ���
*	score:�����Ӧ����Ӧ�ȣ�����ֵ��
*/      
 
import java.util.ArrayList;  
import java.util.List;  

public class Chromosome {  
   private boolean[] gene;//��������  
   private double score;//��Ӧ�ĺ����÷�  
     
   public double getScore() {  
       return score;  
   }  
 
   public void setScore(double score) {  
       this.score = score;  
   }  
 
   /** 
    * @param size 
    * ������ɻ������� 
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
    * ����һ���»��� 
    */  
   public Chromosome() {  
         
   }  
     
   /** 
    * @param c 
    * @return 
    * @Author:kimi   
    * @Description: ��¡���� 
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
    * @Description: ��ʼ�����򳤶� 
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
    * @Description: �Ŵ�������һ�� 
    */  
   public static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {  
       if (p1 == null || p2 == null) { //Ⱦɫ����һ��Ϊ�գ���������һ��  
           return null;  
       }  
       if (p1.gene == null || p2.gene == null) { //Ⱦɫ����һ��û�л������У���������һ��  
           return null;  
       }  
       if (p1.gene.length != p2.gene.length) { //Ⱦɫ��������г��Ȳ�ͬ����������һ��  
           return null;  
       }  
       Chromosome c1 = clone(p1);  
       Chromosome c2 = clone(p2);  
       //����������滥��λ��  
       int size = c1.gene.length;  
       int a = ((int) (Math.random() * size)) % size;  
       int b = ((int) (Math.random() * size)) % size;  
       int min = a > b ? b : a;  
       int max = a > b ? a : b;  
       //��λ���ϵĻ�����н��滥��  
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
    * @Description: ����num��λ�÷������� 
    */  
   public void mutation(int num) {  
       //�������  
       int size = gene.length;  
       for (int i = 0; i < num; i++) {  
           //Ѱ�ұ���λ��  
           int at = ((int) (Math.random() * size)) % size;  
           //������ֵ  
           boolean bool = !gene[at];  
           gene[at] = bool;  
       }  
   }  
     
   /** 
    * @return 
    * @Author:kimi   
    * @Description: ������ת��Ϊ��Ӧ������ 
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