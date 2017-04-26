#ifndef _GRAPH_H_
#define _GRAPH_H_

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <assert.h>
#include <utility>
#include <string.h>
#include <iostream>
#include <algorithm>
#include <string>
#include <sstream>
#include <set>
#include <vector>
#include <stack>
#include <map>
#include <queue>
#include <deque>
#include <cstdlib>
#include <cstdio>
#include <cstring>
#include <cmath>
#include <ctime>
#include <sys/types.h>
#include <unistd.h>
#include <functional>
#include <fstream>
#include <cassert>

using namespace std;

class Graph
{
public:
    class Arc;
    //节点
    class Node {
    public:
        int nodeID;
        std::vector<Arc *> arcs;          
        std::vector<Node *> linkNodes;    
        int netDeployPrice;
        Node();
        Node(int nodeID){
            this->nodeID = nodeID;
        }
        Node(int nodeID,int netDeployPrice){
            this->nodeID = nodeID;
            this->netDeployPrice = netDeployPrice;
        }

        void setNetDeployPrice(int netDeployPrice){
            this->netDeployPrice = netDeployPrice;
        }
    };

    //弧定义
    class Arc
    {   
        public:
            int from,to,cap,cost;
            Arc();
            Arc(int from,int to,int cap,int cost){
                this->from = from;
                this->to = to;
                this->cap = cap;
                this->cost = cost;
            } 
            Arc(int from,int to,int cap){
                this->from = from;
                this->to = to;
                this->cap = cap;
            }  
    };

    int superSource,superSink;                  //超级源 超级汇
    int totalFlow=0;                            //总流量
    int netNodesNum,arcNum,consumeNodesNum;
    int n;  //总结点数
    int minCost =0;
    int maxServerOutput=0;                      //服务器的最大输出能力
    int consumeMaxFlow=0;                       //消费节点的最大流
    std::vector<Node *> allNetNodes;
    std::vector<Node *> conNodes;
    std::vector<Arc *> allNetArcs;
    std::vector<Arc *> conAndnetArcs;
    std::map<int,pair<int,int>> serverLevel;   //保存服务器档次信息
    std::vector<int> serverDC;
    std::map<int, int> serverAllPrice;         //所有服务器用的架构费用
    std::vector<int> netStateDeployPrice;       //网络节点部署成本
    std::vector<int> serverMaxOutPutLimit;

    void setServerMaxOutPutLimit(std::vector<int> _serverMaxOutPutLimit){
        this->serverMaxOutPutLimit = _serverMaxOutPutLimit;
    }

    void init(char **topo){

        int topoCnt = 0; //用来读topo中的每行

        //网络节点数量 网络链路数量 消费节点数量
        sscanf(topo[topoCnt++], "%d%d%d", &netNodesNum, &arcNum, &consumeNodesNum);
        topoCnt++;

        //服务器硬件档次ID 输出能力 硬件成本

        int serverLevelID, serverCap, serverCost;
        while (true){
            sscanf(topo[topoCnt++], "%d%d%d", &serverLevelID, &serverCap, &serverCost);
            
            if(serverCap>maxServerOutput){
                maxServerOutput = serverCap;
            }
            serverDC.push_back(serverCap);
            serverLevel.insert(make_pair(serverCap, make_pair(serverLevelID, serverCost)));
            if (topo[topoCnt][0] != '0' + serverLevelID + 1)
                break;
        }
        topoCnt++;

        cout<<"服务器最大输出能力"<<maxServerOutput<<endl;

        //打印服务器档次信息
        map<int, pair<int,int> >::iterator iter;
        
        for(iter = serverLevel.begin(); iter != serverLevel.end(); iter++){
            cout<<iter->first<<"---"<<iter->second.first<<"---"<<iter->second.second<<endl;
        }
        
        cout<<"读取网络节点部署"<<endl;

        int netStateId,deployPrice,n=netNodesNum;
        while (n--){
            sscanf(topo[topoCnt++], "%d%d", &netStateId, &deployPrice);
            allNetNodes.push_back(new Node(netStateId,deployPrice));
            netStateDeployPrice.push_back(deployPrice);
        }
        topoCnt++;

        cout<<"节点数："<<allNetNodes.size()<<endl;

        cout<<"取网络链路信息"<<endl;
        
        int from, to, cap, cost,cnt=arcNum;
        //初始化m个网络链路
        while (cnt--){
            sscanf(topo[topoCnt++], "%d%d%d%d", &from, &to, &cap, &cost);

            Arc *arc = new Arc(from,to,cap,cost);
            allNetArcs.push_back(arc);

            allNetNodes[from]->arcs.push_back(arc);
            allNetNodes[from]->linkNodes.push_back(allNetNodes[to]);
            allNetNodes[to]->arcs.push_back(arc);
            allNetNodes[to]->linkNodes.push_back(allNetNodes[from]);
        }

       topoCnt++;
       cout<<"读取剩下的消费节点信息行"<<endl;
      

        int consumeID, netStateID, need;
        //初始化 消费节点位置
        for (int i = 0; i<consumeNodesNum; i++){
            sscanf(topo[topoCnt++], "%d%d%d", &consumeID, &netStateID, &need);
            if(need > consumeMaxFlow){
                consumeMaxFlow = need;
            }

            int xx =consumeID+netNodesNum;
            Node *consumeNode = new Node(xx);
            Arc *conAndNetArc = new Arc(xx,netStateID,need);
            conAndnetArcs.push_back(conAndNetArc);

            consumeNode->linkNodes.push_back(allNetNodes[netStateID]);
            consumeNode->arcs.push_back(conAndNetArc);
            conNodes.push_back(consumeNode);
            //总费用
            totalFlow += need;
            
        }

        //计算添加的超级源点和超级汇点的索引
        superSource = netNodesNum+consumeNodesNum;
        // superSink = superSource+1;

        //默认档次 
        this->serverMaxOutPutLimit = vector<int>(netNodesNum,125);

        cout<<"totalFlow:"<<totalFlow<<" gouzao OK!"<<endl; 
    }     
};
#endif