 /**  
 *@Description:     
 */ 
package huawei.genetic.algorithm;  

import java.util.ArrayList;
import java.util.List;

  
public abstract class GeneticAlgorithm {
	private List<Chromosome> population = new ArrayList<Chromosome>();
	private int popSize = 100;//��Ⱥ����
	private int geneSize;//������󳤶�
	private int maxIterNum = 500;//����������
	private double mutationRate = 0.01;//�������ĸ���
	private int maxMutationNum = 3;//�����첽��
	
	private int generation = 1;//��ǰ�Ŵ����ڼ���
	
	private double bestScore;//��õ÷�
	private double worstScore;//��÷�
	private double totalScore;//�ܵ÷�
	private double averageScore;//ƽ���÷�
	
	private double x; //��¼��ʷ��Ⱥ����õ�Xֵ
	private double y; //��¼��ʷ��Ⱥ����õ�Yֵ
	private int geneI;//x y���ڴ���
	
	public GeneticAlgorithm(int geneSize) {
		this.geneSize = geneSize;
	}
	
	public void caculte() {
		//��ʼ����Ⱥ
		generation = 1;
		init();
		while (generation < maxIterNum) {
			//��Ⱥ�Ŵ�
			evolve();
			print();
			generation++;
		}
	}
	
	/**
	 * @Author:lulei  
	 * @Description: ������
	 */
	private void print() {
		System.out.println("--------------------------------");
		System.out.println("the generation is:" + generation);
		System.out.println("the best y is:" + bestScore);
		System.out.println("the worst fitness is:" + worstScore);
		System.out.println("the average fitness is:" + averageScore);
		System.out.println("the total fitness is:" + totalScore);
		System.out.println("geneI:" + geneI + "\tx:" + x + "\ty:" + y);
	}
	
	
	/**
	 * @Author:lulei  
	 * @Description: ��ʼ����Ⱥ
	 */
	private void init() {
		for (int i = 0; i < popSize; i++) {
			population = new ArrayList<Chromosome>();
			Chromosome chro = new Chromosome(geneSize);
			population.add(chro);
		}
		caculteScore();
	}
	
	/**
	 * @Author:lulei  
	 * @Description:��Ⱥ�����Ŵ�
	 */
	private void evolve() {
		List<Chromosome> childPopulation = new ArrayList<Chromosome>();
		//������һ����Ⱥ
		while (childPopulation.size() < popSize) {
			Chromosome p1 = getParentChromosome();
			Chromosome p2 = getParentChromosome();
			List<Chromosome> children = Chromosome.genetic(p1, p2);
			if (children != null) {
				for (Chromosome chro : children) {
					childPopulation.add(chro);
				}
			} 
		}
		//����Ⱥ�滻����Ⱥ
		List<Chromosome> t = population;
		population = childPopulation;
		t.clear();
		t = null;
		//����ͻ��
		mutation();
		//��������Ⱥ����Ӧ��
		caculteScore();
	}
	
	/**
	 * @return
	 * @Author:lulei  
	 * @Description: ���̶ķ�ѡ������Ŵ���һ����Ⱦɫ��
	 */
	private Chromosome getParentChromosome (){
		double slice = Math.random() * totalScore;
		double sum = 0;
		for (Chromosome chro : population) {
			sum += chro.getScore();
			if (sum > slice && chro.getScore() >= averageScore) {
				return chro;
			}
		}
		return null;
	}
	
	/**
	 * @Author:lulei  
	 * @Description: ������Ⱥ��Ӧ��
	 */
	private void caculteScore() {
		setChromosomeScore(population.get(0));
		bestScore = population.get(0).getScore();
		worstScore = population.get(0).getScore();
		totalScore = 0;
		for (Chromosome chro : population) {
			setChromosomeScore(chro);
			if (chro.getScore() > bestScore) { //������û���ֵ
				bestScore = chro.getScore();
				if (y < bestScore) {
					x = changeX(chro);
					y = bestScore;
					geneI = generation;
				}
			}
			if (chro.getScore() < worstScore) { //���������ֵ
				worstScore = chro.getScore();
			}
			totalScore += chro.getScore();
		}
		averageScore = totalScore / popSize;
		//��Ϊ�������⵼�µ�ƽ��ֵ�������ֵ����ƽ��ֵ���ó����ֵ
		averageScore = averageScore > bestScore ? bestScore : averageScore;
	}
	
	/**
	 * ����ͻ��
	 */
	private void mutation() {
		for (Chromosome chro : population) {
			if (Math.random() < mutationRate) { //��������ͻ��
				int mutationNum = (int) (Math.random() * maxMutationNum);
				chro.mutation(mutationNum);
			}
		}
	}
	
	/**
	 * @param chro
	 * @Author:lulei  
	 * @Description: ����Ⱦɫ��÷�
	 */
	private void setChromosomeScore(Chromosome chro) {
		if (chro == null) {
			return;
		}
		double x = changeX(chro);
		double y = caculateY(x);
		chro.setScore(y);

	}
	
	/**
	 * @param chro
	 * @return
	 * @Author:lulei  
	 * @Description: ��������ת��Ϊ��Ӧ��X
	 */
	public abstract double changeX(Chromosome chro);
	
	
	/**
	 * @param x
	 * @return
	 * @Author:lulei  
	 * @Description: ����X����Yֵ Y=F(X)
	 */
	public abstract double caculateY(double x);

	public void setPopulation(List<Chromosome> population) {
		this.population = population;
	}

	public void setPopSize(int popSize) {
		this.popSize = popSize;
	}

	public void setGeneSize(int geneSize) {
		this.geneSize = geneSize;
	}

	public void setMaxIterNum(int maxIterNum) {
		this.maxIterNum = maxIterNum;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public void setMaxMutationNum(int maxMutationNum) {
		this.maxMutationNum = maxMutationNum;
	}

	public double getBestScore() {
		return bestScore;
	}

	public double getWorstScore() {
		return worstScore;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public double getAverageScore() {
		return averageScore;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
