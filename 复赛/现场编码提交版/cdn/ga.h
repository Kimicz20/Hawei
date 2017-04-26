#ifndef GA_H
#define GA_H
#include "mcmf.hpp"

#define GENE_LENGTH 2000;
struct GT;
class GA
{

    // *****************************************************成员
public:
    Graph graph;// 图的信息
    std::priority_queue<GT> gts;// 种群
    std::vector<GT> badGts;
    

     // -----------------------------------------------------方法
public:// 静态方法
    static std::vector<int> getGeneByIds(std::set<int> &ids,Graph &graph);// 根据id获取基因序列010101
    static void change(GT &g,Graph &graph);// 变异
    static void change2(GT &g,Graph &graph);// 变异
    static std::vector<GT> cross(GT &g1, GT &g2,Graph &graph);//交叉
    // 三种变异
    static int ranDelete(GT &gt);
    static int ranAdd(GT &gt,Graph &graph);
    static std::pair<int,int> ranMove(GT &gt,Graph &graph);

public://普通方法
    GA(Graph &_graph);// 构造
    void init(int);//得到n个初始解
    string xjbs();
};


struct GT {//个体
    std::set<int> serverIds;// 服务器id
    std::vector<int> gene;//基因序列
    int cost;
    GT(std::set<int> _serverIds, Graph &graph) {//构造
        serverIds = _serverIds;
        gene = GA::getGeneByIds(_serverIds,graph);
        MCMF_YCW mcmf(graph,serverIds,gene);
        if(_serverIds.size() * 150 <graph.totalFlow)
            cost =-1;
        else
            cost = mcmf.greenTea();
    }


    GT(std::set<int> _serverIds, std::vector<int> _gene, Graph &graph) {//构造
        serverIds = _serverIds;
        gene = _gene; 
        MCMF_YCW mcmf(graph,serverIds,gene);
        if(_serverIds.size() * 150 <graph.totalFlow)
            cost =-1;
        else
            cost = mcmf.greenTea();
    }

	void updateCost(Graph &graph) {
 		MCMF_YCW mcmf(graph,serverIds,gene);
		if(serverIds.size() * 150 <graph.totalFlow)
      	     cost =-1;
      	else
       	     cost = mcmf.greenTea();
	}
    bool operator <(GT a) const  {  return cost < a.cost; }
    bool operator >(GT a) const  {  return cost > a.cost; }

};


#endif // GA_H
