#include "ga.h"
#include <algorithm>
int TIME_OUT = 88;
/**
 * @brief GA::GA 构造
 * @param _bord
 */
GA::GA(Graph &_graph)
{
    graph = _graph;
    srand( (unsigned)time(NULL) );//初始化时间种子
}


/**
 * @brief GA::init 初始化n个可行的解
 * @param n
 */
void GA::init(int n)
{
    int iter =0;
    std::set<int> serverList;
    
  //   int stardFlow = 0.1 * graph.consumeMaxFlow;

    // while(iter++ < n ){
	 //    int nowCap = stardFlow + iter;
		// int cntBig = 0;

		// //将于消费节点直连且能提供费用的节点 基因序号置为1
		// for (int i = 0; i<graph.consumeNodesNum; i++){

		// 	Graph::Arc *e = graph.conAndnetArcs[i];
		// 	if (e->cap >= nowCap)
		// 	{
		// 		serverList.insert(e->to);
		// 		cntBig++;
		// 	}
		// }
		

		// int ranCount = rand() % (graph.consumeNodesNum - cntBig);
		// for (int i = 0; i<ranCount; i++){
		// 	int index = rand() % graph.allNetNodes.size();
		// 	serverList.insert(graph.allNetNodes[index]->nodeID);
		// }

		// cout<<"大于 "<<nowCap<<" 的容量有 "<<cntBig<<" 个,补全个数:"<<ranCount<<" 服务器补全后有: "<<serverList.size()<<" 个。"<<endl;
		//随机档次
        // std::vector<int> ranDC;
        // for (int i=0;i<graph.netNodesNum;i++)
        // {
        //     int r = rand() % graph.serverDC.size();
        //     ranDC.push_back(graph.serverDC[r]);
        // }

        // graph.setServerMaxOutPutLimit(ranDC);
 //        GT gene(serverList,graph);
	// 	// cout<<"是否可行："<<gene.cost<<endl;
	// 	if(gene.cost >0){
	// 		gts.push(gene);
	// 	}
	// 	serverList.clear();
	// }

	// cout<<"size :"<<gts.size()<<endl;
	// //种群个数太少，变异其中某几个
	// if(gts.size() == 0){
	    for (int i=0;i<graph.conAndnetArcs.size();i++)
	    {
	        serverList.insert(graph.conAndnetArcs[i]->to);
	    }
        // std::vector<int> ranDC;
        // for (int i=0;i<graph.netNodesNum;i++)
        // {
        //     int r = rand() % graph.serverDC.size();
        //     ranDC.push_back((graph.serverDC[r]+100)%250);
        // }

        // graph.setServerMaxOutPutLimit(ranDC);
	    GT gene(serverList,graph);
        if(gene.cost >0){
            gts.push(gene);
        }
	// }
	cout<<"populaction init ok"<<endl;
}

/**
 * @brief GA::change 变异
 * @param g 个体
 * @param graph 图
 */
 int type1=0,type2=0,type3=0;
void GA::change(GT &g,Graph &graph)
{
    double ran = (double)(rand() % 100) / 100;
    
    // 删:加:移 = 1:1:1
    if(ran < 0.66) {
       int curCost=g.cost;
	   int deletedId = ranDelete(g);
       g.updateCost(graph);
       if(g.cost<curCost)
        type1++;
    }  
    else if(ran < 1){
        //如果是移动
        int curCost=g.cost;
	    ranMove(g,graph);
        g.updateCost(graph);
        if(g.cost<curCost)
	       type2++;
    } 
    // else if(ran < 1){
    //     // 如果是添加
	   //  int addedId = ranAdd(g,graph);
    //     type3++;
    // }
    
}

void GA::change2(GT &g,Graph &graph)
{
    double ran = (double)(rand() % 100) / 100;
    
    if(ran < 0.1) {
	   int curCost=g.cost;
       int deletedId = ranDelete(g);
       g.updateCost(graph);
       if(g.cost<curCost)
        type1++;
    }  
    else if(ran < 0.55){
        //如果是移动
        int curCost=g.cost;
        ranMove(g,graph);
        g.updateCost(graph);
        if(g.cost<curCost)
           type2++;
    } 
    else if(ran < 1){
        // 如果是添加
        int curCost=g.cost;
        int addedId = ranAdd(g,graph);
        g.updateCost(graph);
        if(g.cost<curCost)
            type3++;
    }
}
/**
 * @brief GA::cross 交叉
 * @param g1 个体1
 * @param g2 个体2
 * @return 原来的两个个体+新的两个个体
 */
std::vector<GT> GA::cross(GT &g1, GT &g2,Graph &graph)
{
    vector<int> gene1 = g1.gene;
    vector<int> gene2 = g2.gene;
    set<int> serverIds1 = g1.serverIds;
    set<int> serverIds2 = g2.serverIds;

    int size = gene1.size() <= gene2.size() ? gene1.size():gene1.size();

    int indexLow = rand() % size;
    int indexHigh = rand() % size;

    if(indexLow > indexHigh) {
        indexLow^=indexHigh; indexHigh^=indexLow; indexLow^=indexHigh;
    }

    // 交换从indexLow 到 indexHigh的基因序列
    for(int i = indexLow; i <= indexHigh; i++) {
        if(gene1[i] == gene2[i]) continue;
        if(gene1[i] >0 && gene2[i] == 0) {
            serverIds1.erase(i); 
            serverIds2.insert(i);
        }
        if(gene1[i] == 0 && gene2[i] > 0) {
            serverIds2.erase(i); 
            serverIds1.insert(i);
        }

        gene1[i] ^= gene2[i];gene2[i] ^= gene1[i]; gene1[i] ^= gene2[i];

    }

    GT child1(serverIds1, gene1,graph);
    GT child2(serverIds2, gene2,graph);

    vector<GT> res;
    res.push_back(g1);
    res.push_back(g2);
    if(child1.cost >0)
    	res.push_back(child1);
    if(child2.cost >0)
    	res.push_back(child2);

    return res;
}

/**
 * @brief GA::ranDelete 随机删除一个id
 * @param ids
 * @return 删除的那个id
 */
int GA::ranDelete(GT &gt)
{
    int deleteIndex = rand() % gt.serverIds.size();
    int index = 0,deletedId;
    for(std::set<int>::iterator it = gt.serverIds.begin();it != gt.serverIds.end(); it++) {
        if(index++ == deleteIndex) {
            deletedId = *it;
            gt.serverIds.erase(*it);
            break;
        }
    }
    gt.gene[deletedId] = 0;
    return deletedId;
}
/**
 * @brief GA::ranAdd 随机加一个没有的id
 * @param ids
 * @param sizeOfNode 所有的可选节点id 0-sizeOfNode
 * @return 加的id
 */
int GA::ranAdd(GT &gt, Graph &graph)
{
    int sizeOfNode = graph.allNetNodes.size();
    int osize = gt.serverIds.size();
    int addId = 0;
    // do {
        //随机加一个没有的id
        do{
            addId = rand() % sizeOfNode;
        }while(gt.serverIds.find(addId) != gt.serverIds.end());
        gt.serverIds.insert(addId);
        //随机档次
        int r = rand() % graph.serverDC.size();
        int r1 = (rand() % 100) *1.0 / 100;
        // if(r1<0.5){
            gt.gene[addId] = graph.serverDC[r] < 100 ? graph.serverDC[r]+100:graph.serverDC[r];
        // }else{
        //     gt.gene[addId] = graph.serverDC[r];
        // }
    // }while(gt.serverIds.size() == osize);
    return addId;
}
/**
 * @brief GA::ranMove 随机选择一个移动一格
 * @param ids
 * @param graph 图
 * @return pait(删除的id，移动到那个点的id)
 */
std::pair<int,int> GA::ranMove(GT &gt,Graph &graph)
{
    int deleteIndex = rand() % gt.serverIds.size();
    int index = 0;
    int deletedId = 0;
    for(std::set<int>::iterator it = gt.serverIds.begin(); it != gt.serverIds.end(); it++) {
        if(index++ == deleteIndex) {
            deletedId = *it;
        }
    }
    // 先删除
    gt.serverIds.erase(deletedId);


    // 添加
    Graph::Node *selectedNode = graph.allNetNodes[deletedId];
    std::vector<Graph::Node *> linkedNodes = selectedNode->linkNodes;

    int moveToId = 0;
    int moveToIndex = rand() % linkedNodes.size();
    moveToId = linkedNodes[moveToIndex]->nodeID;
    // 再加上
    gt.serverIds.insert(moveToId);

    std::vector<int> gene = gt.gene;
    gene[deletedId] ^= gene[moveToId];
    gene[moveToId] ^= gene[deletedId];
    gene[deletedId] ^= gene[moveToId];
    gt.gene = gene;
    // deletedId -> moveToId
    return make_pair(deletedId,moveToId);
}




/**
 * @brief GA::getGeneByIds 根据id构造基因序列
 * @param ids
 * @return 一个基因序列
 */
std::vector<int> GA::getGeneByIds(std::set<int> &ids,Graph &graph)
{
    int size = GENE_LENGTH;
    vector<int> res(size);

    for(set<int>::iterator it = ids.begin();it != ids.end(); it++) {
        int r = rand() % graph.serverDC.size();
        //随机档次
        int r1 = (rand() % 100) *1.0 / 100;
        if(r1<0.5){
            res[*it] = graph.serverDC[r] < 100 ? graph.serverDC[r]+100:graph.serverDC[r];
        }else{
            res[*it] = graph.serverDC[r];
        }
    }

    return res;
}

bool Comp(const GT &a,const GT &b)
{
    return a.cost < b.cost;
}

string GA::xjbs(){
    clock_t start_time = clock();
    int size = gts.size(),gener=0;
    std::vector<GT> gt_v;
    if(graph.netNodesNum<1000){
    	TIME_OUT = 85;
    }
    while((clock()-start_time)/CLOCKS_PER_SEC<TIME_OUT){
    	gt_v.clear();
    	while(!gts.empty()){
    		GT g0 = gts.top();
    		gt_v.push_back(g0);
    		gts.pop();
    	}
        int curTotalCost=0;
	//cout<<"第"<<++gener<<"代 ++++";
    //sort(gt_v.begin(),gt_v.end(),Comp);
	for(int i = 0; i < gt_v.size(); i++) {
        cout <<gt_v[i].cost << " ";
        curTotalCost+=gt_v[i].cost;
    }
	cout << endl; 
    	//cout<<"第几代:"<<++gener<< "fuwuqigeshu:"<<gt_v[gt_v.size()-1].serverIds.size()<<"+++++++"<<gt_v[gt_v.size()-1].cost<<endl;
    	for (int i = 0; i < gt_v.size(); i++)
    	{
		    GT gti =  gt_v[i];
    		double r = (double) (rand() % 100) /100;
    		GT gt303 = gti;
    		gts.push(gt303);
		
    		//变异
    		if(r<1.0 / gt_v.size()){
                if((clock()-start_time)/CLOCKS_PER_SEC < 40)
                    change(gti,graph);
                else {
                    change2(gti,graph);
                }
                //选择加入
                // if(gti.cost > 0 && gti.cost < (curTotalCost / gt_v.size())){
                if(gti.cost > 0  ) {
                    gts.push(gti);
                }
    		} 

    		double r2 = (double) (rand() % 100) /100;
    		//交叉
    		// if(r2<  (clock()-start_time)*1.0/CLOCKS_PER_SEC / 660 && (clock()-start_time)/CLOCKS_PER_SEC >80){
    		if(r2<  0){
            	double r4 =(double) (rand() % 100)*1.0 /100;
    			int r3 ;
    			vector<GT> gt930;
    			// if(r4 < (clock()-start_time)*1.0 /CLOCKS_PER_SEC / 160){
                if(r4 < 1){
    				do{
    	    			r3= rand() % gt_v.size();
    				}while(r3 != i);  
                    // sort(gt_v.begin(),gt_v.end(),Comp);
    				gt930 = cross(gti,gt_v[r3],graph);  	
    			}
       //          else{
    			// 	do{
    	  //   			r3= rand() % badGts.size();
    			// 	}while(r3 != i);  
    			// 	gt930 = cross(gti,badGts[r3],graph);			
    			// }	 
    			for (int j = 1; j < gt930.size(); ++j)
    			{
                    cout << "jiaocha  ::"<<gt930[j].cost<< endl;
    				gts.push(gt930[j]);
    			}

    		}
    	}
		while(gts.size() > size + (clock()-start_time)/CLOCKS_PER_SEC/ 5){
			// badGts.push_back(gts.top());
    		gts.pop();
		}
		//cout<<"badGts Size:"<<badGts.size()<<endl;
    }
    
    cout<<"min Cost"<<gt_v[gt_v.size()-1].cost<<endl;
    cout<<"删除:"<<type1<<endl;
    cout<<"移动:"<<type2<<endl;
    cout<<"添加:"<<type3<<endl;
    MCMF_YCW mcmf(graph,gt_v[gt_v.size()-1].serverIds,gt_v[gt_v.size()-1].gene);

    mcmf.isOutputResult = true;
    cout << mcmf.greenTea()<<endl;

    string result("");
    mcmf.print_solution(result);
    return result;
}
