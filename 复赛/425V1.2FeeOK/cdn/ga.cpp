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

     while(iter++ < n ){
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
     }
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
    if(ran < 0.5) {
        int curCost=g.cost;
        int deletedId = ranDelete(g);
        if(g.cost<curCost)
            type1++;
    }
    else if(ran < 1){
        //如果是移动
        int curCost=g.cost;
        ranMove(g,graph);
        if(g.cost<curCost)
            type2++;
    }
    // else if(ran < 1){
    //     // 如果是添加
    //  int addedId = ranAdd(g,graph);
    //     type3++;
    // }

    g.updateCost(graph);

}

void GA::change2(GT &g,Graph &graph)
{
    double ran = (double)(rand() % 100) / 100;

    int a = rand() % 3 + 1;
    int t = rand() % a + 1;
    while(t--)
    {
        if(ran < 0.0) {
            int curCost=g.cost;
            int deletedId = ranDelete(g);
            if(g.cost<curCost)
                type1++;
        }
        else if(ran < 1){
            //如果是移动
            int curCost=g.cost;
            ranMove(g,graph);
            if(g.cost<curCost)
                type2++;
        }
        else if(ran < 1){
            // 如果是添加
            int curCost=g.cost;
            int addedId = ranAdd(g,graph);
            if(g.cost<curCost)
                type3++;
        }
    }


    g.updateCost(graph);

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

    int count = 0;
    // 交换从indexLow 到 indexHigh的基因序列
    for(int i = 0; i < size; i++) {

        if(rand() % 15 != 0 || gene1[i] == gene2[i]) continue;

        if(gene1[i] >0 && gene2[i] == 0) {
            serverIds1.erase(i);
            serverIds2.insert(i);
        }
        if(gene1[i] == 0 && gene2[i] > 0) {
            serverIds2.erase(i);
            serverIds1.insert(i);
        }

        gene1[i] ^= gene2[i];gene2[i] ^= gene1[i]; gene1[i] ^= gene2[i];
        count++;
        if(count == g1.serverIds.size() / 10) break;
    }

    GT child1(serverIds1, gene1,graph);
    GT child2(serverIds2, gene2,graph);

    vector<GT> res;

    //    res.push_back(child1);
    //    res.push_back(child2);
    res.push_back(g1);
    if(child1.cost > 0 && child1.cost< res[0].cost)
        res[0] = (child1);

    if(child2.cost > 0 && child2.cost< res[0].cost)
        res[0] = (child2);

    //    cout << g1.cost <<" x "<< g2.cost<<endl;
    //    cout<<child1.cost << " . "<<child2.cost << endl;


    return res;
}

std::vector<GT> GA::crossXXX(GT &g1, GT &g2,Graph &graph)
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
        int ran = (rand() % 100 )*1.0 /100;
        //均匀交叉
        if(ran < 0.5){
            if(gene1[i] >0 && gene2[i] == 0) {
                serverIds1.erase(i);
                serverIds2.insert(i);
            }
            if(gene1[i] == 0 && gene2[i] > 0) {
                serverIds2.erase(i);
                serverIds1.insert(i);
            }

            int tmp1 = gene1[i],tmp2 = gene2[i];
            if((1.5*tmp1 -0.5*tmp2) > 0 && (1.5*tmp1 -0.5*tmp2)<250){
                gene1[i] = 1.5*tmp1 -0.5*tmp2;
            }
            if((1.5*tmp1 -0.5*tmp2) <= 0 && tmp1 == 0){
                gene1[i] = 0.5*tmp2;
            }
            if((-0.5*tmp1 + 1.5*tmp2) > 0 && (-0.5*tmp1 + 1.5*tmp2)<250){
                gene2[i] = -0.5*tmp1 + 1.5*tmp2;
            }
            if((-0.5*tmp1 + 1.5*tmp2) <= 0 && tmp2 == 0){
                gene2[i] = 0.5*tmp1;
            }
//            cout<<"!!!!!!!!!!!!!!!!!:"<<gene1[i]<<","<<gene2[i]<<endl;
//	        gene1[i] ^= gene2[i];gene2[i] ^= gene1[i]; gene1[i] ^= gene2[i];
        }
    }

    GT child1(serverIds1, gene1,graph);
    GT child2(serverIds2, gene2,graph);

    vector<GT> res;
    // res.push_back(g1);
    // res.push_back(g2);

    int min = g1.cost <= g2.cost ? g1.cost:g2.cost;
    if(child1.cost >0){
        if(child1.cost < g1.cost || child1.cost < g2.cost){
          //  cout<<"++++++++++++++++++cross better "<<child1.cost<<","<<g1.cost<<","<<g2.cost<<endl;
            res.push_back(child1);
        }
    }
    if(child2.cost >0){
        if(child2.cost < g1.cost || child2.cost < g2.cost){
           // cout<<"++++++++++++++++++cross better "<<child2.cost<<","<<g1.cost<<","<<g2.cost<<endl;
            res.push_back(child2);
        }

    }

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


int a = 0;

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
        //        if(r1<0.5){
        //            res[*it] = graph.serverDC[r] < 100 ? graph.serverDC[r]+100:graph.serverDC[r];
        //        }else{
        res[*it] = graph.serverDC[graph.serverDC.size() - a - 1];
        //        }
    }
    a++;
    return res;
}

bool Comp(const GT &a,const GT &b)
{
    return a.cost < b.cost;
}
//signal handle
bool timeIsOK = true;
void handle(int n);
// finall GT
GT finallGT;
int finish = false;
string GA::xjbsMid(clock_t start_time) {
    int zqsize = 10;
    int size = gts.size(),gener=0;
    std::vector<GT> gt_v;
    while(timeIsOK){
//        while((clock()-start_time)/CLOCKS_PER_SEC< 88){
            gt_v.clear();
            while(!gts.empty()){
                GT g0 = gts.top();
                gt_v.push_back(g0);
                gts.pop();
            }//end while
            int curTotalCost=0;
            //cout<<"第"<<++gener<<"代 ++++";
            //sort(gt_v.begin(),gt_v.end(),Comp);

            /*for(int i = 0; i < gt_v.size(); i++) {
                cout <<gt_v[i].cost;
                curTotalCost+=gt_v[i].cost;

                int xx[] = {0,0,0,0,0,0,0,0,0,0,0};
                for(set<int>::iterator it = gt_v[i].serverIds.begin();
                    it != gt_v[i].serverIds.end(); it++) {
                    int serverId = *it;
                    int ge = gt_v[i].gene[serverId];


                    xx[ge / 25]++;
                }


                int max = 0;
                int p = 0;

                for(int i = 0; i <= 10; i++) {
                    //                cout << xx[i] << " ";
                    if(max < xx[i]) {
                        max = xx[i];
                        p = i;
                    }

                }
                cout <<"("<<p - 1<<") ";
            }// end for


            cout << endl;*/
            //cout<<"第几代:"<<++gener<< "fuwuqigeshu:"<<gt_v[gt_v.size()-1].serverIds.size()<<"+++++++"<<gt_v[gt_v.size()-1].cost<<endl;
            for (int i = 0; i < gt_v.size(); i++)
            {

                int f = 0;
                double r = (double) (rand() % 100) /100;
                // 旧的备份
                //            GT oldGt = gt_v[i];
                //gts.push(oldGt);


                GT gti =  gt_v[i];
                if(rand() % 2 == 0) {
                    //变异
                    if(r < 0.9){
                        if((clock()-start_time)/CLOCKS_PER_SEC < 10){
                            change(gti,graph);
                        }
                        else {
                            change2(gti,graph);
                        }


                        if(gti.cost > 0 && gti.cost < gt_v[i].cost) {
                            gts.push(gti);
                        } else {
                            gts.push(gt_v[i]);
                        }
                        f ++;
                    }
                } else {

                    double r2 = (double) (rand() % 100) /100;
                    //交叉
                    if(r2 < 0.2 && (clock()-start_time)/CLOCKS_PER_SEC > 40){

                        int index2 ;
                        vector<GT> crossGts;


                        do{
                            index2= rand() % gt_v.size();
                        }while(index2 == i);
                        crossGts = cross(gti,gt_v[index2],graph);
                        if(crossGts[0].cost > 0 && crossGts[0].cost < gti.cost) {
                            //cout << "better1" << endl;
                            gts.push(crossGts[0]);
                            f ++;

                        }

                        //                     if(crossGts[1].cost > 0 && crossGts[1].cost < gt_v[index2].cost) {
                        //                         cout << "better2" << endl;
                        //                         gt_v[index2] = crossGts[1];
                        //                     }

                    }
                }

                if(f == 0)     gts.push(gt_v[i]);


            }

            while(gts.size() > zqsize){ //size + (clock()-start_time)/CLOCKS_PER_SEC/ 10){
                gts.pop();
            }

            //store best GT
            finallGT = gt_v[gt_v.size()-1];
        }//end while time

    GT gtFnial = gt_v[gt_v.size()-1];
    cout<<"min Cost"<<gtFnial.cost<<endl;
    /*cout<<"删除:"<<type1<<endl;
    cout<<"移动:"<<type2<<endl;
    cout<<"添加:"<<type3<<endl;*/
    return print_result(gtFnial);
}

string GA::xjbs(clock_t start_time){
    signal(SIGALRM,handle);
    if(graph.netNodesNum < 1000) return xjbsMid(start_time);
    int size = gts.size(),gener=0;
    std::vector<GT> gt_v;
    if(graph.netNodesNum<1000){
        TIME_OUT = 85;
    }
    while(timeIsOK){
//        while((clock()-start_time)/CLOCKS_PER_SEC< 88){
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
                if((clock()-start_time)/CLOCKS_PER_SEC < 25)
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
             if(r2<  (clock()-start_time)*1.0/CLOCKS_PER_SEC / 660 && (clock()-start_time)/CLOCKS_PER_SEC >80){
//            if(r2<  0){
                double r4 =(double) (rand() % 100)*1.0 /100;
                int r3 ;
                vector<GT> gt930;
                // if(r4 < (clock()-start_time)*1.0 /CLOCKS_PER_SEC / 160){
                if(r4 < 1){
                    do{
                        r3= rand() % gt_v.size();
                    }while(r3 == i);
                    // sort(gt_v.begin(),gt_v.end(),Comp);
                    gt930 = crossXXX(gti,gt_v[r3],graph);
                }
                //          else{
                // 	do{
                //   			r3= rand() % badGts.size();
                // 	}while(r3 != i);
                // 	gt930 = cross(gti,badGts[r3],graph);
                // }
                for (int j = 0; j < gt930.size(); ++j)
                {
                   // cout << "jiaocha  ::"<<gt930[j].cost<< endl;
                    gts.push(gt930[j]);
                }

            }
        }//end for


        if((clock()-start_time) * 1000 /CLOCKS_PER_SEC % 1000 < 100 && finish == false) {
            finish = true;
            for(int d = 0; d < 10; d++) {
                GT minGt = gt_v[gt_v.size()-1];// 复制最小的个体
                for(int i = 0; i < minGt.gene.size(); i++) {
                    if(minGt.gene[i] > 0) {
                        minGt.gene[i] = graph.serverDC[d];
                    }
                }
                minGt.updateCost(graph);
                //cout << minGt.cost <<  "  ***  ";
                if(minGt.cost > 0 && minGt.cost < gt_v[gt_v.size()-1].cost) {
                    gts.push(minGt);
                    //cout << d <<"()";
                }
            }
           //cout << endl;
        }
        if((clock()-start_time) * 1000 /CLOCKS_PER_SEC % 1000 > 100) finish = false;


        while(gts.size() > size + (clock()-start_time)/CLOCKS_PER_SEC/ 10){
            // badGts.push_back(gts.top());
            gts.pop();
        }
        //cout<<"badGts Size:"<<badGts.size()<<endl;
        //store best GT
        finallGT = gt_v[gt_v.size()-1];
    }//end while time
    GT gtFnial = gt_v[gt_v.size()-1];
    cout<<"min Cost"<<gtFnial.cost<<endl;
    /*cout<<"删除:"<<type1<<endl;
    cout<<"移动:"<<type2<<endl;
    cout<<"添加:"<<type3<<endl;*/
    return print_result(gtFnial);
}

string GA::print_result(GT gt){
     /* for(set<int>::iterator it = gt.serverIds.begin();
          it != gt.serverIds.end(); it++) {
          int serverId = *it;
	    cout<<*it<<" ("<<gt.gene[serverId]<<") ,";
        }
	cout<<endl;*/
    MCMF_YCW mcmf(graph,gt.serverIds,gt.gene);

    mcmf.isOutputResult = true;
    cout <<"check:"<< mcmf.greenTea()<<endl;
	
    string result("");
    mcmf.print_solution(result);
    return result;
}

void handle(int n) {
    timeIsOK = false;
}



