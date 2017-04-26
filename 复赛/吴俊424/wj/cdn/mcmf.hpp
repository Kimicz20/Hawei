#ifndef _MCMF_H_
#define _MCMF_H_

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
#include "Graph.hpp"
using namespace std;

#define MIN(A,B) ((A) <= (B) ? (A) : (B))
#define MAX(A,B) ((A) >= (B) ? (A) : (B))
#define ABS(A) ((A) > 0 ? (A) : (-A))

class MCMF_YCW
{

public:
    struct Node;
    //弧定义
    struct Edge {
    public:
        long wjCapacityARCVar, rezCapacityEdgeVar; // 剩余容量
        long long int wjcostARCVar,costEdgeVar;       // 费用
        Node *wjheadARCVar,*headEdgeVar;
        Edge *mirrorEdgeVar,*wjMirrorEdgeVar;      // 相对弧d
        void setCAP( long a) { rezCapacityEdgeVar = a; }
        Edge *getWjMirrorEdgeVar() const;
        void setWjMirrorEdgeVar(Edge *value);
        void removeCAP( long a) { rezCapacityEdgeVar = rezCapacityEdgeVar-a; }
        void setWjheadARCVar(Node *value);
        void addCAP( long a) { rezCapacityEdgeVar = rezCapacityEdgeVar + a; }
        Node *getWjheadARCVar() const;
        void setCost(long long int a) { costEdgeVar = a; }
        void multiCost(long long int a) { costEdgeVar = costEdgeVar * a; }
        void setHead( Node *a) { headEdgeVar = a; }
        void setWjcostARCVar(long long value);
        void setMirror( Edge *a) { mirrorEdgeVar = a; }
        long long getWjcostARCVar() const;
        long getCAP() { return rezCapacityEdgeVar; }
        long long int getCost() { return costEdgeVar; }
        Node *getHead() { return headEdgeVar; }
        Edge *getMirror() { return mirrorEdgeVar; }
        long getWjCapacityARCVar() const;
        void setWjCapacityARCVar(long value);


    };

    vector<MCMF_YCW::Edge*> edgePath;//存放一条路径上的边
    vector<int> path[10000];
    vector<int> pathFlow;
    int pathNum = 0;
    //服务器_档次map 用于生成路径
    map<int,int> server_dc_map;


    //节点
    struct Node {
    public:
        int nodeCountX,nodeCoutY,nodeLinked;
        long long int wjexnodevar,excessNodeVar,wjpricanodevar,priceNodeVar,rankNodeVar, inpNodeVar;
        Edge *firstNodeVar,*wjfirstnodevar,*currentNodeVar,*suspendedNodeVar;
        int getNodeCountX() const;
        Node *qNextNodeVar,*bNextNodeVar,*bPrevNodeVar,*wjnextbnodevar;
        void setExcess(  long long int a) { excessNodeVar = a; }
        void setNodeCountX(int value);
        void removeEXC( long a) { excessNodeVar = excessNodeVar - a; }
        Edge *getWjfirstnodevar() const;
        void addEXC( long a) { excessNodeVar = excessNodeVar +a; }
        void setPrice(  long long int a) { priceNodeVar = a; }
        void removePrice( long a) { priceNodeVar =priceNodeVar - a; }
        void setWjfirstnodevar(Edge *value);
        void setFirst( Edge *a) { firstNodeVar = a; }
        int getNodeLinked() const;
        void setCurrent( Edge *a) { currentNodeVar = a; }
        void addOneCurrent() { currentNodeVar += 1; }
        void setSuspended( Edge *a) { suspendedNodeVar = a; }
        void setQNext( Node *a) { qNextNodeVar = a; }
        long long getWjpricanodevar() const;
        void setBNext( Node *a) { bNextNodeVar = a; }
        void setBPrev( Node *a) { bPrevNodeVar = a; }
        void setRank( long a) { rankNodeVar = a; }
        void setWjpricanodevar(long long value);
        void setInp( long a) { inpNodeVar = a; }
        long long int getExcess() { return excessNodeVar; }
        void setNodeLinked(int value);
        long long int getPrice() { return priceNodeVar; }
        Edge *getFirst() { return firstNodeVar; }
        Node *getWjnextbnodevar() const;
        void removeOneFirst() { firstNodeVar -= 1;}
        void addOneFirst() { firstNodeVar += 1; }
        Edge *getCurrent() { return currentNodeVar; }
        void setWjnextbnodevar(Node *value);
        Edge *getSuspended() { return suspendedNodeVar; }
        Node *getQNext() { return qNextNodeVar; }
        Node *getBNext() { return bNextNodeVar; }
        int getNodeCoutY() const;
        Node *getBprev() { return bPrevNodeVar; }
        long getRank() { return rankNodeVar; }
        void setNodeCoutY(int value);
        long getInp() { return inpNodeVar; }

    };

    struct NodeCost {
        int id;
        int costOfBord;
        vector<int> linkedNode;


    public:
        int getId() const;
        void setId(int value);
        int getCostOfBord() const;
        void setCostOfBord(int value);
        vector<int> getLinkedNode() const;
        void setLinkedNode(const vector<int> &value);
    };


    // fin1
    //桶
    struct Tong {
    private:
        Node *wjfiistBvar,*pFirstBucketVar,*thelastBucketVar;
    public:
        void setThelastBucketVar(Node *value);
        Tong( Node *a) {
            pFirstBucketVar = a;
        }
        Node *getThelastBucketVar() {
            return thelastBucketVar;
        }

        Tong() {}
        void set_p_first( Node *a) {
            pFirstBucketVar = a;
        }
        Node *getWjfiistBvar() {
            return wjfiistBvar;
        }
        Node *p_first() {
            return pFirstBucketVar;
        }

        void setWjfiistBvar(Node *value);


    };
    // fin2

    Graph G;
    //计算总费用需要的属性
    int netStates,consumeStates,totalNeed;
    map<int,pair<int,int> > serverLevel;//保存服务器档次信息
    map<int, int> serverAllPrice;//所有服务器用的架构费用
    map<int, int> getServerAllPrice() {
        return this->serverAllPrice;
    }
    void setRealServerById(map<int, int>);// 根据虚拟点找到服务器的真实id
    map<int, int> realServerById;
    vector<int> netStateDeployPrice;//网络节点部署成本
    bool isOutputResult = false;
    set<int> itServers;
    long nodeNumYCWVar,arcNumYCWVar,*capArrayYCWVar;   // 容量数组
    Node *nodesArrayYCWVar,*sentinelNode,*excqFirst,*excqLast,*iNode,*jNode,dOneNode,*_dummy_node,*dTwoNode;
    Edge *arcsArrayYCWVar,*sentinelArc,dArcYCWVar,*arcCurrent,*arcNew,*arcTmp;

    Tong *bucketsArrayYCWVar,*lBucket;
    long _linf;
    int timeForPriceIn;

    long long int epsilonYCWVar,_dn,priceMinYCWVar,_mmc;
    double fScaleYCWVar,cutOffFactor,cutOn,cutOff;
    long long int totalExcess;

    int flagPrice,flagUpdt,sncMaxYCWVar;


    long nRelYCWVar,nRefYCWVar,nSrcYCWVar,nPushYCWVar,nRelabelYCWVar,nDischargeYCWVar,nRefineYCWVar,nUpdateYCWVar,nScanYCWVar,nPrscanYCWVar,nPrscanOneYCWVar;
    long nPrscanTwoYCWVar,nBadPricein,nBadRelabelYCWVar,nPrefineYCWVar;

    bool noZeroCycles,checkSolution,compDualsYCWVar,costRestartYCWVar,printAnsYCWVar;
    long long int *nodeBalanceYCWVar;

    long nodeMinYCWVar,nodeMaxYCWVar,*arcFirstYCWVar,*arcTailYCWVar,posCurrent;
    long long int maxCost;
    long long int totalPYCWVar,totalNYCWVar;



    void PT(int i){
        //        cout << i<< endl;
    }

    void PT(long i){
        //        cout << i<< endl;
    }
    void PT(double  i){
        //        cout << i<< endl;
    }
    void PT(float i){
        //        cout << i<< endl;
    }
    void PT(long long int i){
        //        cout << i<< endl;
    }
    // fin3
    int PTJ = 0;
    std::vector<int> serverMaxOutPutLimit;
    void setServerMaxOutPutLimit(std::vector<int> gene){
        this->serverMaxOutPutLimit = gene;
    }
    //需要计算总费用的构造函数
    MCMF_YCW(Graph &G,set<int> &itServers,std::vector<int> gene = vector<int>(2000,150))
        :flagPrice(0),flagUpdt(0),nPushYCWVar(0),nRelabelYCWVar(0),nDischargeYCWVar(0),nRefineYCWVar(0),nUpdateYCWVar(0),nScanYCWVar(0),nPrscanYCWVar(0),
          nPrscanOneYCWVar(0),nPrscanTwoYCWVar(0),nBadPricein(0),nBadRelabelYCWVar(0),nPrefineYCWVar(0),noZeroCycles(false),checkSolution(false),compDualsYCWVar(false),
          costRestartYCWVar(false),printAnsYCWVar(true){

        this->G = G;
        this->itServers = itServers;
        this->nodeNumYCWVar = G.netNodesNum+G.consumeNodesNum+1;
        this->arcNumYCWVar = 2*G.arcNum+G.consumeNodesNum+itServers.size();
        this->netStates = G.netNodesNum;
        this->consumeStates = G.consumeNodesNum;
        this->totalNeed = G.totalFlow;
        this->serverMaxOutPutLimit = gene;
        //服务器档次和网络节点部署费用
        this->serverLevel = G.serverLevel;
        this->netStateDeployPrice = G.netStateDeployPrice;

        allocate_arrays();
        convert2MCMF();
    }

    struct NodeServer {
        int id;
        int costOfBord;
        vector<int> linkedNode;


    public:
        int getId() const;
        void setId(int value);
        int getCostOfBord() const;
        void setCostOfBord(int value);
        vector<int> getLinkedNode() const;
        void setLinkedNode(const vector<int> &value);
    };

    ~MCMF_YCW() {}

    void restartInit() {
        //        initArrays();
    }

    void allocate_arrays() {

        Node *nodesArrayYCWVarA;
        Edge *arcsArrayYCWVarA;
        nodesArrayYCWVar = (Node*) calloc ( nodeNumYCWVar+2,   sizeof(Node) );
        arcsArrayYCWVar  = (Edge*)  calloc ( 2*arcNumYCWVar+1, sizeof(Edge) );
        capArrayYCWVar  = (long*) calloc ( 2*arcNumYCWVar,   sizeof(long) );
        if(capArrayYCWVar == NULL) {
            restartInit();
        }
        arcTailYCWVar  = (long*) calloc ( 2*arcNumYCWVar,   sizeof(long) );
        if(arcTailYCWVar == NULL) {
            restartInit();
        }
        arcFirstYCWVar  = (long*) calloc ( nodeNumYCWVar+2,   sizeof(long) );

        if(arcsArrayYCWVar == NULL) {
            arcsArrayYCWVar = arcsArrayYCWVarA;
        }

        for ( Node *in = nodesArrayYCWVar; in <= nodesArrayYCWVar + nodeNumYCWVar; in ++ ) {
            in->setExcess( 0);
        }
        if(nodesArrayYCWVar == NULL) {
            nodesArrayYCWVar = nodesArrayYCWVarA;
        }
        posCurrent = maxCost = nodeMaxYCWVar = totalPYCWVar = totalNYCWVar = 0;
        arcCurrent = arcsArrayYCWVar;
        nodeMinYCWVar = nodeNumYCWVar;


    }

    struct EdgeLinked {
    public:
        int id;
        long linkedEd;
        vector<int> linkedNode;
    };
    // fin4
    void deallocate_arrays() {


        if(arcsArrayYCWVar || dTwoNode) {
            if(arcsArrayYCWVar) {
                free ( arcsArrayYCWVar );
            }

            if (dTwoNode){
                delete dTwoNode;
            }
        }

        if(capArrayYCWVar || bucketsArrayYCWVar || checkSolution || nodesArrayYCWVar) {
            if ( capArrayYCWVar) {
                free ( capArrayYCWVar );
            }
            if ( bucketsArrayYCWVar) {
                free ( bucketsArrayYCWVar );
            }
            if ( checkSolution) {
                free ( nodeBalanceYCWVar );
            }
            if ( nodesArrayYCWVar) {
                nodesArrayYCWVar = nodesArrayYCWVar - nodeMinYCWVar;
                free ( nodesArrayYCWVar );
            }
        }

    }


    int lowB;
    int lowCost;
    //    int highB;
    //    int highCost;
    void set_arc( long from, long to,
                  long limitLow, long limitHight,
                  long long int cost) {
        long wjarcTailYCWVar;
        limitHight  = limitHight < 0 ? 2147483647 : limitHight;

        long l = limitLow;
        long h = limitHight;
        arcFirstYCWVar[++from] ++; from --;
        arcFirstYCWVar[++to] ++; to--;

        iNode = nodesArrayYCWVar + from;
        jNode = nodesArrayYCWVar + to;

        // 存储edge信息
        arcTailYCWVar[posCurrent]   = from;
        wjarcTailYCWVar = arcTailYCWVar[posCurrent];
        if(wjarcTailYCWVar == -1) {
            //            cout<< "this tail is -1"<< endl;
        }
        arcTailYCWVar[posCurrent+1] = to;
        if(arcTailYCWVar[posCurrent+1] == wjarcTailYCWVar) {
            //            cout << "self tran" << endl;
        }
        arcCurrent->setHead( jNode );
        arcCurrent->setCAP( h - l );
        capArrayYCWVar[posCurrent] = h;
        arcCurrent->setCost( cost );

        arcCurrent->setMirror( arcCurrent + 1 );
        ( arcCurrent + 1 )->setHead( nodesArrayYCWVar + from );
        ( arcCurrent + 1 )->setCAP( 0 );
        lowB = MIN(lowB, l);
        lowCost = MIN(cost, lowCost);
        capArrayYCWVar[posCurrent+1] = 0;
        ( arcCurrent + 1 )->setCost( -cost );
        ( arcCurrent + 1 )->setMirror( arcCurrent );

        iNode->removeEXC( limitLow );
        jNode->addEXC( limitLow );

        nodeMinYCWVar = MIN(nodeMinYCWVar, to);
        nodeMinYCWVar = MIN(nodeMinYCWVar, from);
        nodeMaxYCWVar = MAX(nodeMaxYCWVar, from);
        nodeMaxYCWVar = MAX(nodeMaxYCWVar, to);

        cost = ABS(cost);
        if(h > 0)
            maxCost = MAX(maxCost, cost);
        arcCurrent += 2;
        posCurrent += 2;
    }

    // fin5

    void set_supply_demand_of_node( long id, long excess) {
        (nodesArrayYCWVar + id)->setExcess( excess);

        if(excess <= 0 ) {
            totalNYCWVar += ABS(excess);
        } else {
            totalPYCWVar += excess;
        }

    }

    // fin6
    void pre_processing() {
        // called after the arcs were just added and before run_cs2();
        // ordering arcs - linear time algorithm;
        long i;
        long last, arc_num, arc_new_num;;
        PT(i);
        long tail_node_id;
        Node *head_p;
        Edge *arc_new, *arc_tmp;
        long up_bound;
        long long int cost;
        PT(cost);
        long long int cap_out;
        PT(cap_out);
        long long int cap_in;

        ( nodesArrayYCWVar + nodeMinYCWVar )->setFirst( arcsArrayYCWVar );
        PT(PTJ);
        for ( i = nodeMinYCWVar + 1; i <= nodeMaxYCWVar + 1; i ++ ) {
            arcFirstYCWVar[i] += arcFirstYCWVar[i-1];
            PT(arcFirstYCWVar[i-1]);
            ( nodesArrayYCWVar + i )->setFirst( arcsArrayYCWVar + arcFirstYCWVar[i] );
        }

        // scanning all the nodes except the last
        for ( i = nodeMinYCWVar; i < nodeMaxYCWVar; i ++ ) {
            PT(PTJ);
            last = ( ( nodesArrayYCWVar + i + 1 )->getFirst() ) - arcsArrayYCWVar;
            PT(PTJ);
            for ( arc_num = arcFirstYCWVar[i]; arc_num < last; arc_num ++ ) {
                tail_node_id = arcTailYCWVar[arc_num];
                PT(PTJ);
                while ( tail_node_id != i ) {
                    PT(PTJ);
                    arc_new_num = arcFirstYCWVar[tail_node_id];
                    arcCurrent = arcsArrayYCWVar + arc_num;PT(PTJ);
                    arc_new = arcsArrayYCWVar + arc_new_num;
                    PT(PTJ);
                    head_p = arc_new->getHead();
                    arc_new->setHead( arcCurrent->getHead() );
                    arcCurrent->setHead( head_p );
                    PT(PTJ);
                    up_bound          = capArrayYCWVar[arc_new_num];
                    capArrayYCWVar[arc_new_num] = capArrayYCWVar[arc_num];PT(PTJ);
                    capArrayYCWVar[arc_num]     = up_bound;

                    up_bound = arc_new->getCAP();PT(PTJ);
                    arc_new->setCAP( arcCurrent->getCAP() );
                    arcCurrent->setCAP( up_bound) ;
                    PT(PTJ);
                    cost = arc_new->getCost();
                    arc_new->setCost( arcCurrent->getCost() );
                    arcCurrent->setCost( cost );
                    PT(PTJ);
                    if ( arc_new != arcCurrent->getMirror() ) {
                        arc_tmp = arc_new->getMirror();
                        arc_new->setMirror( arcCurrent->getMirror() );PT(PTJ);
                        arcCurrent->setMirror( arc_tmp );
                        PT(PTJ);
                        arcCurrent->getMirror()->setMirror( arcCurrent );
                        arc_new->getMirror()->setMirror( arc_new );
                    }

                    arcTailYCWVar[arc_num] = arcTailYCWVar[arc_new_num];PT(PTJ);
                    arcTailYCWVar[arc_new_num] = tail_node_id;

                    arcFirstYCWVar[tail_node_id] ++ ;

                    tail_node_id = arcTailYCWVar[arc_num];
                }
            }
        }

        for ( Node *ndp = nodesArrayYCWVar + nodeMinYCWVar; ndp <= nodesArrayYCWVar + nodeMaxYCWVar; ndp ++ ) {
            cap_in  =   ( ndp->getExcess() );
            cap_out = - ( ndp->getExcess() );PT(PTJ);
            for ( arcCurrent = ndp->getFirst(); arcCurrent != (ndp+1)->getFirst();
                  arcCurrent ++ ) {PT(PTJ);
                arc_num = arcCurrent - arcsArrayYCWVar;
                if ( capArrayYCWVar[arc_num] > 0 ) cap_out += capArrayYCWVar[arc_num];
                if ( capArrayYCWVar[arc_num] == 0 )
                    cap_in += capArrayYCWVar[ arcCurrent->getMirror() - arcsArrayYCWVar ];
            }PT(PTJ);
        }

        nodeNumYCWVar = nodeMaxYCWVar - nodeMinYCWVar + 1;PT(PTJ);
        nodesArrayYCWVar = nodesArrayYCWVar + nodeMinYCWVar;

        free ( arcFirstYCWVar );PT(PTJ);
        free ( arcTailYCWVar );
    }

    void cs2_initialize() {
        Node *i;
        Edge *a;
        Edge *a_stop;
        Tong *b;
        long df;

        fScaleYCWVar = (long) 12.0;PT(PTJ);
        sentinelNode = nodesArrayYCWVar + nodeNumYCWVar;
        sentinelArc  = arcsArrayYCWVar + arcNumYCWVar;
        PT(PTJ);
        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++ ) {
            i->setPrice( 0);PT(PTJ);
            i->setSuspended( i->getFirst());

            i->setQNext( sentinelNode);
        }
        PT(PTJ);
        sentinelNode->setFirst( sentinelArc);
        sentinelNode->setSuspended( sentinelArc);


        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++ ) {PT(PTJ);
            for ( a = i->getFirst(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++ ) {
                if ( a->getCost() < 0) {
                    if ( ( df = a->getCAP()) > 0) {
                        increase_flow( i, a->getHead(), a, df);
                    }
                }
            }
        }

        _dn = nodeNumYCWVar + 1;
        if ( noZeroCycles == true) { // NO_ZERO_CYCLES
            _dn = 2 * _dn;PT(PTJ);
        }
        PT(PTJ);
        for ( a = arcsArrayYCWVar; a != sentinelArc; a ++ ) {
            a->multiCost( _dn);PT(PTJ);
        }

        if ( noZeroCycles == true) { // NO_ZERO_CYCLES
            for ( a = arcsArrayYCWVar; a != sentinelArc; a ++ ) {
                if ((a->getCost() == 0) && (a->getMirror()->getCost() == 0)) {
                    a->setCost( 1);PT(PTJ);
                    a->getMirror()->setCost( -1);
                }
            }
        }
        PT(PTJ);
        _mmc = maxCost * _dn;

        _linf = (long) (_dn * ceil(fScaleYCWVar) + 2);

        bucketsArrayYCWVar = (Tong*) calloc ( _linf, sizeof(Tong));

        PT(PTJ);
        lBucket = bucketsArrayYCWVar + _linf;

        dTwoNode = new Node; // used as reference;

        for ( b = bucketsArrayYCWVar; b != lBucket; b ++ ) {
            reset_bucket( b);PT(PTJ);
        }

        epsilonYCWVar = _mmc;
        if ( epsilonYCWVar < 1) {
            epsilonYCWVar = 1;PT(PTJ);
        }

        priceMinYCWVar = -(0x7fffffffffffffffLL);
        PT(PTJ);
        cutOffFactor = 1.5 * pow( (double)nodeNumYCWVar, 0.44);

        cutOffFactor = cutOffFactor > 12  ?  cutOffFactor : 12;
        PT(PTJ);
        nRefYCWVar = 0;

        flagPrice = 0;

        _dummy_node = &dOneNode;PT(PTJ);

        excqFirst = NULL;

    }

    void up_node_scan( Node *i) {
        Node *j; // opposite node
        Edge *a; // (i, j)
        Edge *a_stop; // first arc from the next node
        Edge *ra; // (j, i)
        Tong *b_old; // old bucket contained j
        Tong *b_new; // new bucket for j
        long i_rank;
        long j_rank; // ranks of nodes
        long j_new_rank;
        long long int rc; // reduced cost of (j, i)
        long long int dr; // rank difference

        nScanYCWVar ++;

        i_rank = i->getRank();
        PT(PTJ);
        // scanning arcs;
        for ( a = i->getFirst(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++ ) {

            ra = a->getMirror();
            PT(PTJ);
            if ( ra->getCAP() > 0 ) {
                j = a->getHead();
                j_rank = j->getRank();
                PT(PTJ);
                if ( j_rank > i_rank ) {
                    if ( ( rc = j->getPrice() + ra->getCost() - i->getPrice() ) < 0 ) {
                        j_new_rank = i_rank;
                    } else {
                        dr = rc / epsilonYCWVar;
                        j_new_rank = ( dr < _linf ) ? i_rank + (long)dr + 1 : _linf;
                    }
                    PT(PTJ);
                    if ( j_rank > j_new_rank ) {
                        j->setRank( j_new_rank);
                        j->setCurrent( ra);

                        if ( j_rank < _linf ) {
                            b_old = bucketsArrayYCWVar + j_rank;
                            // REMOVE_FROM_BUCKET( j, b_old );
                            if ( j == ( b_old -> p_first() ) )
                                b_old ->set_p_first( j -> getBNext() );
                            else
                            {
                                ( j -> getBprev() )->setBNext( j -> getBNext() );
                                ( j -> getBNext() )->setBPrev( j -> getBprev() );
                            }
                        }
                        PT(PTJ);
                        b_new = bucketsArrayYCWVar + j_new_rank;
                        insert_to_bucket( j, b_new );
                    }
                }
            }
        }
        PT(PTJ);
        i->removePrice( i_rank * epsilonYCWVar);
        PT(PTJ);
        i->setRank( -1);
    }
    void price_update() {
        register Node *i;
        long long int remain;
        Tong *b;
        long long int dp;
        PT(PTJ);
        nUpdateYCWVar ++;

        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++ ) {
            if ( i->getExcess() < 0 ) {PT(PTJ);
                insert_to_bucket( i, bucketsArrayYCWVar );
                i->setRank( 0);
            } else {
                i->setRank( _linf);
            }
        }

        remain = totalExcess;
        if ( remain < 0.5 ) return;

        for ( b = bucketsArrayYCWVar; b != lBucket; b ++ ) {

            while ( nonempty_bucket( b) ) {
                i=(b -> p_first() );PT(PTJ);
                b ->set_p_first( i -> getBNext() );
                up_node_scan( i );

                if ( i ->getExcess() > 0 ) {
                    remain -= ( i->getExcess());
                    if ( remain <= 0 ) break;
                }
            }
            if ( remain <= 0 ) break;PT(PTJ);
        }

        if ( remain > 0.5 ) flagUpdt = 1;

        dp = ( b - bucketsArrayYCWVar ) * epsilonYCWVar;
        PT(PTJ);
        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++ ) {

            if ( i->getRank() >= 0 ) {
                if ( i->getRank() < _linf ) {PT(PTJ);
                    if ( i == ( ( bucketsArrayYCWVar + i->getRank()) -> p_first() ) )
                        ( bucketsArrayYCWVar + i->getRank()) ->set_p_first( i -> getBNext() );
                    else
                    {PT(PTJ);
                        ( i -> getBprev() )->setBNext( i -> getBNext() );
                        ( i -> getBNext() )->setBPrev( i -> getBprev() );
                    }
                }
                if ( i->getPrice() > priceMinYCWVar ) {
                    i->removePrice( dp);
                }
            }
        }
    }

    int relabel( Node *i) {
        register Edge *a;
        register Edge *a_stop;
        register Edge *a_max;
        register  long long int p_max;
        register  long long int i_price;
        register  long long int dp;
        PT(PTJ);
        p_max = priceMinYCWVar;
        i_price = i->getPrice();

        a_max = NULL;

        for ( a = i->getCurrent() + 1, a_stop = (i + 1)->getSuspended(); a != a_stop; a ++ ) {
            PT(PTJ);
            if ( (a->getCAP() > 0) && ( (dp = (a->getHead()->getPrice() - a->getCost())) > p_max ) ) {
                if ( i_price < dp ) {
                    i->setCurrent( a);
                    return ( 1);
                }PT(PTJ);
                p_max = dp;
                a_max = a;
            }
        }

        for ( a = i->getFirst(), a_stop = i->getCurrent() + 1; a != a_stop; a ++ ) {
            if ( (a->getCAP() > 0) && ( (dp = (a->getHead()->getPrice() - a->getCost())) > p_max ) ) {
                if ( i_price < dp ) {
                    i->setCurrent( a);PT(PTJ);
                    return ( 1);
                }
                p_max = dp;
                a_max = a;
            }
        }

        if ( p_max != priceMinYCWVar ) {
            i->setPrice( p_max - epsilonYCWVar);
            i->setCurrent( a_max);
        } else {
            if ( i->getSuspended() == i->getFirst() ) {
                if ( i->getExcess() == 0 ) {
                    i->setPrice( priceMinYCWVar);
                } else {
                    if ( nRefYCWVar == 1 ) {PT(PTJ);
                        return -1;
                    } else {
                        return -1;
                    }
                }
            } else {
                flagPrice = 1;
            }
        }
        PT(PTJ);
        nRelabelYCWVar ++;
        PT(PTJ);
        nRelYCWVar ++;
        return 0;
    }

    void discharge( Node *i) {
        register Edge *a;// an arc from i
        register Node *j; // head of a
        register long df; // amoumt of flow to be pushed through a
        long long int j_exc; // former excess of j

        nDischargeYCWVar ++;
        PT(PTJ);
        a = i->getCurrent();
        j = a->getHead();

        if ( !(a->getCAP() > 0 && (i->getPrice() + a->getCost() < j->getPrice())) ) {
            relabel( i );
            a = i->getCurrent();
            j = a->getHead();
        }

        PT(PTJ);        while ( 1 ) {

            j_exc = j->getExcess();
            if ( j_exc >= 0 ) {

                df =  i->getExcess() < a->getCAP()?i->getExcess():a->getCAP();
                if ( j_exc == 0) nSrcYCWVar++;PT(PTJ);
                increase_flow( i, j, a, df ); // INCREASE_FLOW
                nPushYCWVar ++;

                if ( out_of_excess_q( j ) ) {
                    insert_to_excess_q( j );
                }
            } else { // j_exc < 0;
                PT(PTJ);
                df = i->getExcess() < a->getCAP()?i->getExcess():a->getCAP();
                increase_flow( i, j, a, df ); // INCREASE_FLOW
                nPushYCWVar ++;

                if ( j->getExcess() >= 0 ) {
                    if ( j->getExcess() > 0 ) {
                        nSrcYCWVar ++;PT(PTJ);
                        relabel( j );
                        insert_to_excess_q( j );
                    }
                    totalExcess += j_exc;
                } else {
                    totalExcess -= df;
                }
            }

            if ( i->getExcess() <= 0) nSrcYCWVar --;
            if ( i->getExcess() <= 0 || flagPrice ) break;
            PT(PTJ);
            relabel( i );

            a = i->getCurrent();
            j = a->getHead();
        }

        i->setCurrent( a);
    }

    int price_in() {
        Node *i; // current node
        Node *j;
        Edge *a; // current arc from i
        Edge *a_stop; // first arc from the next node
        Edge *b; // arc to be exchanged with suspended
        Edge *ra; // opposite to a
        Edge *rb; // opposite to b
        long long int rc; // reduced cost
        int n_in_bad; // number of priced_in arcs with negative reduced cost
        int bad_found; // if 1 we are at the second scan if 0 we are at the first scan
        long long int i_exc; // excess of i
        long long int df; // an amount to increase flow

        PT(PTJ);
        bad_found = 0;
        n_in_bad = 0;

restart:
        PT(PTJ);
        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++ ) {

            for ( a = i->getFirst() - 1, a_stop = i->getSuspended() - 1; a != a_stop; a -- ) {
                PT(PTJ);
                rc = i->getPrice() + a->getCost() - a->getHead()->getPrice();
                if ( ( rc < 0) && ( a->getCAP() > 0) ) { // bad case;
                    if ( bad_found == 0 ) {
                        bad_found = 1;
                        update_cut_off();
                        goto restart;
                    }PT(PTJ);
                    df = a->getCAP();
                    increase_flow( i, a->getHead(), a, df );

                    ra = a->getMirror();
                    j  = a->getHead();
                    PT(PTJ);
                    i->removeOneFirst();
                    b = i->getFirst();
                    exchange( a, b );

                    if ( a < j->getFirst() ) {
                        j->removeOneFirst();
                        rb = j->getFirst();
                        exchange( ra, rb );
                    }
                    PT(PTJ);
                    n_in_bad ++;
                } else {
                    if ( ( rc < cutOn ) && ( rc > -cutOn ) ) {
                        i->removeOneFirst();
                        b = i->getFirst();PT(PTJ);
                        exchange( a, b );
                    }
                }
            }
        }


        if ( n_in_bad != 0 ) {
            PT(PTJ);
            nBadPricein ++;

            // recalculating excess queue;
            totalExcess = 0;
            nSrcYCWVar = 0;
            reset_excess_q();
            PT(PTJ);
            for ( i = nodesArrayYCWVar; i != sentinelNode; i ++ ) {
                i->setCurrent( i->getFirst());
                i_exc = i->getExcess();
                if ( i_exc > 0 ) { // i is a source;
                    totalExcess += i_exc;
                    nSrcYCWVar ++;PT(PTJ);
                    insert_to_excess_q( i );
                }
            }

            insert_to_excess_q( _dummy_node );
        }
        PT(PTJ);
        if ( timeForPriceIn == 4)
            timeForPriceIn = 6;
        PT(PTJ);
        if ( timeForPriceIn == 2)
            timeForPriceIn = 4;

        return ( n_in_bad);
    }
    int refine() {
        Node *i; // current node
        long long int i_exc; // excess of i
        long np, nr, ns; // variables for additional print
        int pr_in_int; // current number of updates between price_in
        PT(PTJ);
        np = nPushYCWVar;
        nr = nRelabelYCWVar;
        ns = nScanYCWVar;

        nRefineYCWVar ++;
        nRefYCWVar ++;
        nRelYCWVar = 0;
        pr_in_int = 0;
        PT(PTJ);
        // initialize;
        totalExcess = 0;
        nSrcYCWVar = 0;
        reset_excess_q();
        PT(PTJ);
        timeForPriceIn = 2;

        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++ ) {
            i->setCurrent( i->getFirst());
            i_exc = i->getExcess();
            if ( i_exc > 0 ) { // i  is a source
                totalExcess += i_exc;
                nSrcYCWVar++;
                insert_to_excess_q( i );
            }
        }
        PT(PTJ);
        if ( totalExcess <= 0 ) return -2;;

        // (2) main loop

        while ( 1 ) {

            if ( empty_excess_q() ) {
                if ( nRefYCWVar > 1 ) {
                    pr_in_int = 0;
                    price_in();
                }
                PT(PTJ);
                if ( empty_excess_q() ) break;
            }

            i = excqFirst;
            excqFirst = i -> getQNext();
            i ->setQNext( sentinelNode );
            PT(PTJ);
            if ( i->getExcess() > 0 ) {
                discharge( i );

                if ( time_for_update() || flagPrice ) {
                    if ( i->getExcess() > 0 ) {
                        insert_to_excess_q( i );
                    }
                    PT(PTJ);
                    if ( flagPrice && ( nRefYCWVar > 1 ) ) {
                        pr_in_int = 0;
                        price_in();
                        flagPrice = 0;
                    }

                    price_update();
                    PT(PTJ);
                    while ( flagUpdt ) {
                        if ( nRefYCWVar == 1 ) {
                            return -1;
                        } else {
                            flagUpdt = 0;
                            update_cut_off();
                            nBadRelabelYCWVar ++;
                            pr_in_int = 0;
                            price_in();
                            price_update();
                        }
                    }
                    nRelYCWVar = 0;
                    PT(PTJ);
                    if ( nRefYCWVar > 1 && (pr_in_int ++ > timeForPriceIn) ) {
                        pr_in_int = 0;
                        price_in();PT(PTJ);
                    }
                }
            }
        }

        return -2;
    }

    int price_refine() {
        Node *i;
        Node *j;
        Node *ir;
        PT(PTJ);
        Node *is;
        Edge *a;
        Edge *a_stop;
        Edge *ar;
        long bmax;
        long i_rank;
        PT(PTJ);
        long j_rank;
        long j_new_rank;
        Tong *b;
        Tong *b_old;
        Tong *b_new;
        PT(PTJ);
        long long int rc = 0;
        long long int dr;
        long long int dp;
        PT(PTJ);
        int cc;

        long df;
        int nnc;
        int snc;
        PT(PTJ);
        nPrefineYCWVar ++;

        cc = 1;
        snc = 0;

        sncMaxYCWVar = 0;
        PT(PTJ);

        // (1) main loop
        // while negative cycle is found or eps-optimal solution is constructed
        while ( 1 ) {
            PT(PTJ);
            nnc = 0;
            for ( i = nodesArrayYCWVar; i != sentinelNode; i ++ ) {
                i->setRank( 0);
                i->setInp( 0);
                i->setCurrent( i->getFirst());PT(PTJ);
            }
            reset_stackq();

            for ( i = nodesArrayYCWVar; i != sentinelNode; i ++ ) {
                if ( i->getInp() == 2 ) continue;

                i->setBNext( NULL);PT(PTJ);

                // deapth first search
                while ( 1 ) {
                    i->setInp(1);


                    for ( a = i->getCurrent(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++) {
                        if ( (a->getCAP() > 0) ) {
                            j = a->getHead();
                            if ( i->getPrice() + a->getCost() - j->getPrice() < 0 ) {
                                if ( j->getInp() == 0 ) {
                                    i->setCurrent( a);
                                    j->setBNext( i);PT(PTJ);
                                    i = j;
                                    a = j->getCurrent();
                                    a_stop = (j+1)->getSuspended();
                                    break;
                                }
                                PT(PTJ);
                                if ( j->getInp() == 1 ) {
                                    cc = 0;
                                    nnc ++;
                                    i->setCurrent( a);
                                    is = ir = i;
                                    df = 0x7fffffff;

                                    while ( 1 ) {
                                        ar = ir->getCurrent();
                                        if ( ar->getCAP() <= df ) {
                                            df = ar->getCAP();
                                            is = ir;
                                        }
                                        if ( ir == j ) break;
                                        ir = ir->getBNext();
                                    }

                                    ir = i;
                                    PT(PTJ);
                                    while ( 1 ) {
                                        ar = ir->getCurrent();
                                        increase_flow( ir, ar->getHead(), ar, df);
                                        if ( ir == j ) break;
                                        ir = ir->getBNext();
                                    }

                                    if ( is != i ) {
                                        for ( ir = i; ir != is; ir = ir->getBNext() ) {
                                            ir->setInp( 0);
                                        }
                                        i = is;
                                        a = is->getCurrent() + 1;
                                        a_stop = (is+1)->getSuspended();
                                        break;
                                    }
                                }
                            }PT(PTJ);

                        }
                    }
                    PT(PTJ);
                    if ( a == a_stop ) {

                        i->setInp( 2);
                        nPrscanOneYCWVar ++;
                        j = i->getBNext();
                        stackq_push( i );
                        if ( j == NULL ) break;
                        i = j;
                        i->addOneCurrent();
                    }

                }
            } PT(PTJ);

            PT(PTJ);

            snc += nnc;
            if ( snc < sncMaxYCWVar ) cc = 1;
            if ( cc == 0 ) break;
            bmax = 0;

            while ( nonempty_stackq() ) {

                nPrscanTwoYCWVar ++;
                PT(PTJ);
                i = excqFirst;
                excqFirst = i -> getQNext();
                i ->setQNext( sentinelNode );
                i_rank = i->getRank();
                for ( a = i->getFirst(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++) {

                    if (a->getCAP() > 0) {
                        j  = a->getHead();
                        rc = i->getPrice() + a->getCost() - j->getPrice();
                        PT(PTJ);
                        if ( rc < 0 ) {
                            dr = ( long long int) (( - rc - 0.5 ) / epsilonYCWVar);
                            if (( j_rank = dr + i_rank ) < _linf ) {
                                if ( j_rank > j->getRank() )
                                    j->setRank( j_rank);
                            }
                        }
                    }
                } // all arcs from i are scanned
                PT(PTJ);
                if ( i_rank > 0 ) {
                    if ( i_rank > bmax ) bmax = i_rank;
                    b = bucketsArrayYCWVar + i_rank;
                    insert_to_bucket( i, b );
                }
            }


            if ( bmax == 0 ) {
                break;
            }
            PT(PTJ);

            for ( b = bucketsArrayYCWVar + bmax; b != bucketsArrayYCWVar; b -- ) {
                i_rank = b - bucketsArrayYCWVar;
                dp = i_rank * epsilonYCWVar;

                while ( nonempty_bucket( b) ) {
                    PT(PTJ);
                    i=(b -> p_first() );
                    b ->set_p_first( i -> getBNext() );
                    nPrscanYCWVar ++;

                    for ( a = i->getFirst(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++) {
                        if (a->getCAP() > 0) {
                            j = a->getHead();
                            PT(PTJ);
                            j_rank = j->getRank();
                            if ( j_rank < i_rank ) {
                                rc = i->getPrice() + a->getCost() - j->getPrice();
                                if ( rc < 0 ) {
                                    j_new_rank = i_rank;
                                } else {
                                    dr = rc / epsilonYCWVar;
                                    PT(PTJ);
                                    j_new_rank = ( dr < _linf ) ? i_rank - ( (long)dr + 1 ) : 0;
                                }
                                if ( j_rank < j_new_rank ) {
                                    if ( cc == 1 ) {
                                        j->setRank( j_new_rank);
                                        if ( j_rank > 0 ) {
                                            b_old = bucketsArrayYCWVar + j_rank;
                                            PT(PTJ);
                                            if ( j == ( b_old -> p_first() ) )
                                                b_old ->set_p_first( j -> getBNext() );
                                            else
                                            {
                                                ( j -> getBprev() )->setBNext( j -> getBNext() );
                                                ( j -> getBNext() )->setBPrev( j -> getBprev() );
                                            }
                                        }
                                        b_new = bucketsArrayYCWVar + j_new_rank;
                                        insert_to_bucket( j, b_new );
                                        PT(PTJ);
                                    } else {
                                        df = a->getCAP();
                                        PT(PTJ);
                                        increase_flow( i, j, a, df );
                                    }
                                }
                            }
                        } PT(PTJ);
                    } PT(PTJ);

                    i->removePrice( dp);

                } PT(PTJ);
            } PT(PTJ);

            if ( cc == 0 ) break;

        } PT(PTJ);



        PT(PTJ);
        if ( cc == 0 ) {
            for ( i = nodesArrayYCWVar; i != sentinelNode; i ++) {
                for ( a = i->getFirst(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++) {
                    PT(PTJ);
                    if ( i->getPrice() + a->getCost() - a->getHead()->getPrice() < - epsilonYCWVar ) {
                        if ( ( df = a->getCAP() ) > 0 ) {
                            increase_flow( i, a->getHead(), a, df );
                        }
                    }
                }
            }
        }

        return ( cc );
    }
    void compute_prices() {
        Node *i;
        Node *j;
        Edge *a;
        Edge *a_stop;
        long bmax;
        long i_rank;
        PT(PTJ);
        long j_rank;
        long j_new_rank;
        Tong *b;
        Tong *b_old;
        Tong *b_new;
        PT(PTJ);
        long long int rc;
        long long int dr;
        long long int dp;
        PT(PTJ);
        int cc;

        nPrefineYCWVar ++;
        cc = 1;

        // (1) main loop
        // while negative cycle is found or eps-optimal solution is constructed
        while ( 1 ) {

            for ( i = nodesArrayYCWVar; i != sentinelNode; i ++) {
                i->setRank( 0);
                i->setInp( 0);
                i->setCurrent( i->getFirst());
                PT(PTJ);
            }
            reset_stackq();

            for ( i = nodesArrayYCWVar; i != sentinelNode; i ++ ) {
                if ( i->getInp() == 2 ) continue;
                PT(PTJ);
                i->setBNext( NULL);
                // depth first search
                while ( 1 ) {
                    i->setInp( 1);
                    PT(PTJ);
                    for ( a = i->getSuspended(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++) {
                        if (a->getCAP() > 0) {
                            j = a->getHead();
                            if ( i->getPrice() + a->getCost() - j->getPrice() < 0 ) {
                                if ( j->getInp() == 0 ) {
                                    i->setCurrent( a);
                                    j->setBNext( i);
                                    i = j;
                                    a = j->getCurrent();
                                    a_stop = (j+1)->getSuspended();
                                    break;
                                }
                                PT(PTJ);
                                if ( j->getInp() == 1 ) {
                                    cc = 0;
                                }
                            }
                            PT(PTJ);
                        }
                    } PT(PTJ);

                    if ( a == a_stop ) {
                        PT(PTJ);                        i->setInp( 2);
                        nPrscanOneYCWVar ++;
                        j = i->getBNext();
                        stackq_push( i );
                        if ( j == NULL ) break;
                        i = j;
                        i->addOneCurrent();
                    }

                } PT(PTJ);
            } PT(PTJ);


            if ( cc == 0 ) break;
            bmax = 0;

            while ( nonempty_stackq() ) {
                nPrscanTwoYCWVar ++;
                i = excqFirst;
                excqFirst = i -> getQNext();PT(PTJ);
                i ->setQNext( sentinelNode );
                i_rank = i->getRank();
                for ( a = i->getSuspended(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++) {
                    if (a->getCAP() > 0) {
                        j  = a->getHead();PT(PTJ);
                        rc = i->getPrice() + a->getCost() - j->getPrice();


                        if ( rc < 0 ) {
                            dr = - rc;
                            if (( j_rank = dr + i_rank ) < _linf ) {
                                if ( j_rank > j->getRank() )
                                    j->setRank( j_rank);
                                PT(PTJ);
                            }
                        }
                    }
                }

                if ( i_rank > 0 ) {
                    if ( i_rank > bmax ) bmax = i_rank;
                    b = bucketsArrayYCWVar + i_rank;
                    insert_to_bucket( i, b );
                    PT(PTJ);
                }
            }

            if ( bmax == 0 ) {
                PT(PTJ);
                break;

            }

            for ( b = bucketsArrayYCWVar + bmax; b != bucketsArrayYCWVar; b -- ) {
                i_rank = b - bucketsArrayYCWVar;
                dp = i_rank;
                PT(PTJ);
                while ( nonempty_bucket( b) ) {
                    i=(b -> p_first() );
                    b ->set_p_first( i -> getBNext() );
                    nPrscanYCWVar ++;
                    PT(PTJ);
                    for ( a = i->getSuspended(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++) {
                        if (a->getCAP() > 0) {
                            j = a->getHead();
                            j_rank = j->getRank();
                            if ( j_rank < i_rank ) {
                                rc = i->getPrice() + a->getCost() - j->getPrice();
                                PT(PTJ);
                                if ( rc < 0 ) {
                                    j_new_rank = i_rank;
                                } else {
                                    dr = rc;
                                    j_new_rank = ( dr < _linf ) ? i_rank - ( (long)dr + 1 ) : 0;
                                }
                                if ( j_rank < j_new_rank ) {
                                    if ( cc == 1 ) {
                                        PT(PTJ);
                                        j->setRank( j_new_rank);
                                        if ( j_rank > 0 ) {
                                            b_old = bucketsArrayYCWVar + j_rank;
                                            if ( j == ( b_old -> p_first() ) )
                                                b_old ->set_p_first( j -> getBNext() );
                                            else
                                            {
                                                ( j -> getBprev() )->setBNext( j -> getBNext() );
                                                ( j -> getBNext() )->setBPrev( j -> getBprev() );
                                                PT(PTJ);
                                            }
                                        }
                                        b_new = bucketsArrayYCWVar + j_new_rank;
                                        insert_to_bucket( j, b_new );
                                    }
                                }
                            }
                        } PT(PTJ);
                    }

                    i->removePrice( dp);

                }
            }
            PT(PTJ);
            if ( cc == 0 ) break;

        } PT(PTJ);
    }
    void price_out() {
        Node *i;
        Edge *a;
        PT(PTJ);
        Edge *a_stop;
        Edge *b;
        double n_cut_off;
        double rc;

        n_cut_off = - cutOff;
        PT(PTJ);
        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++) {
            for ( a = i->getFirst(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++) {
                PT(PTJ);
                rc = i->getPrice() + a->getCost() - a->getHead()->getPrice();
                if ( ( rc > cutOff && a->getMirror()->getCAP() <= 0 ) ||
                     ( rc < n_cut_off && a->getCAP() <= 0 ) ) {
                    PT(PTJ);
                    b = i->getFirst();
                    i->addOneFirst();
                    exchange( a, b );
                }
            }
        }
    }
    int update_epsilon() {
        PT(PTJ);
        if ( epsilonYCWVar <= 1 ) return ( 1 );

        epsilonYCWVar = ( long long int) (ceil ( (double) epsilonYCWVar / fScaleYCWVar ));
        cutOff = cutOffFactor * epsilonYCWVar;
        cutOn = cutOff * 0.8;
        PT(PTJ);
        return ( 0 );
    }
    int check_feas();
    int check_cs();
    int check_eps_opt() {
        Node *i;
        Edge *a, *a_stop;
        PT(PTJ);
        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++) {
            for ( a = i->getSuspended(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++) {
                PT(PTJ);
                if ( (a->getCAP() > 0) && ((i->getPrice() + a->getCost() - a->getHead()->getPrice()) < - epsilonYCWVar) ) {
                    return ( 0);
                }
            }
        }
        PT(PTJ);
        return 1;
    }

    void init_solution() {
        Edge *a;
        Node *i;
        Node *j;
        PT(PTJ);
        long df;

        for ( a = arcsArrayYCWVar; a != sentinelArc; a ++ ) {
            if ( a->getCAP() > 0 && a->getCost() < 0 ) {
                df = a->getCAP();
                i  = a->getMirror()->getHead();
                PT(PTJ);
                j  = a->getHead();
                increase_flow( i, j, a, df );
            }
        }
    }
    void cs_cost_reinit() {
        if ( costRestartYCWVar == false)
            return;
        PT(PTJ);
        Node *i;
        Edge *a;
        PT(PTJ);
        Edge *a_stop;
        Tong *b;
        long long int rc, minc, sum;
        PT(PTJ);

        for ( b = bucketsArrayYCWVar; b != lBucket; b ++) {
            reset_bucket( b);PT(PTJ);
        }

        rc = 0;
        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++) {
            rc = rc < i->getPrice()?rc:i->getPrice();
            i->setFirst( i->getSuspended());
            PT(PTJ);
            i->setCurrent( i->getFirst());
            i->setQNext( sentinelNode);
        }

        // make prices nonnegative and multiply
        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++) {
            i->setPrice( (i->getPrice() - rc) * _dn);
            PT(PTJ);
        }

        // multiply arc costs
        for (a = arcsArrayYCWVar; a != sentinelArc; a ++) {
            a->multiCost( _dn);
            PT(PTJ);
        }

        sum = 0;
        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++) {
            minc = 0;
            for ( a = i->getFirst(), a_stop = (i + 1)->getSuspended(); a != a_stop; a ++) {
                if ( ((a->getCAP() > 0) && ((rc = i->getPrice() + a->getCost() - a->getHead()->getPrice()) < 0)) )
                    minc =  epsilonYCWVar > -rc?epsilonYCWVar:-rc;
                PT(PTJ);
            }
            sum += minc;
        }

        epsilonYCWVar = ceil(sum / _dn);
        PT(PTJ);
        cutOffFactor = 1.5 * pow((double)nodeNumYCWVar, 0.44);

        cutOffFactor =  cutOffFactor > 12 ? cutOffFactor:12;

        nRefYCWVar = 0;
        PT(PTJ);
        nRefineYCWVar = nDischargeYCWVar = nPushYCWVar = nRelabelYCWVar = 0;
        nUpdateYCWVar = nScanYCWVar = nPrefineYCWVar = nPrscanYCWVar = nPrscanOneYCWVar =
                nBadPricein = nBadRelabelYCWVar = 0;

        flagPrice = 0;
        PT(PTJ);
        excqFirst = NULL;
    }
    void cs2_cost_restart( double *objective_cost) {
        // restart after a cost update;
        if ( costRestartYCWVar == false)
            return;
        PT(PTJ);
        int cc; // for storing return code;

        cs_cost_reinit();
        PT(PTJ);
        cc = update_epsilon();

        if (cc == 0) {
            do { // scaling loop
                while ( 1 ) {
                    if ( ! price_refine() )
                        break;
                    PT(PTJ);
                    if ( nRefYCWVar >= 1 ) {
                        if ( price_in() )
                            break;
                    }
                    if ((cc = update_epsilon ()))
                        break;
                }
                if (cc) break;
                refine();
                if ( nRefYCWVar >= 1 ) {
                    price_out();
                }
                PT(PTJ);
                if ( update_epsilon() )
                    break;
            } while ( cc == 0 );
        }
        PT(PTJ);
        finishup( objective_cost );
    }
    void print_solution(string& result) {
        bool  flag = false;
        for(int k=0;k<10000;k++){
            path[k].clear();
        }
        PT(PTJ);
        while(true) {
            Node* u = nodesArrayYCWVar + nodeNumYCWVar - 1;
            long minFlowOfPath = 10000;
            edgePath.clear();
            PT(PTJ);
            while(!((u-nodesArrayYCWVar)>=netStates&&(u-nodesArrayYCWVar)<=(nodeNumYCWVar-2))) {
                path[pathNum].push_back(u-nodesArrayYCWVar);
                Edge* a = u->getSuspended();
                while(a != (u+1)->getSuspended()) {
                    if((capArrayYCWVar[ a == NULL ? -1 : a - arcsArrayYCWVar ] - a->getCAP()) > 0) {
                        minFlowOfPath = minFlowOfPath < (capArrayYCWVar[ a == NULL ? -1 : a - arcsArrayYCWVar ] - a->getCAP()) ? minFlowOfPath:(capArrayYCWVar[ a == NULL ? -1 : a - arcsArrayYCWVar ] - a->getCAP());
                        PT(PTJ);
                        edgePath.push_back(a) ;
                        u = a->getHead();
                        break;
                    }
                    ++a;
                    PT(PTJ);
                }
                if(u == nodesArrayYCWVar + nodeNumYCWVar - 1) {
                    flag = true;
                    break;
                }
                PT(PTJ);
            }
            if(flag) {
                break;
            }
            path[pathNum].push_back(u-nodesArrayYCWVar);
            pathNum++;
            pathFlow.push_back(minFlowOfPath);

            //在路径上删除最小流量
            for(int k=0; k<edgePath.size(); k++) {
                edgePath[k]->rezCapacityEdgeVar += minFlowOfPath;
            }

        }
        //确定当前路径的每个服务器的档次
        storeServerGrade();

        //打印路径

        char c[50];
        sprintf(c, "%d\n\n", pathNum);
        result += c;
        for(int i=0;i<pathNum;i++){
            for(int j=1;j<path[i].size();j++){
                sprintf(c,"%d ",path[i][j]>=netStates?(path[i][j]-netStates):path[i][j]);
                result += c;
            }
            sprintf(c,"%d %d\n",pathFlow[i],server_dc_map[path[i][1]]);
            result += c;
        }
    }
    void print_graph();
    void finishup( double *objective_cost) {
        Edge *a;
        long na;
        double obj_internal = 0;
        PT(PTJ);
        long long int cs;
        long flow;
        Node *i;

        PT(PTJ);

        if ( noZeroCycles == true) {
            for ( a = arcsArrayYCWVar; a != sentinelArc; a ++ ) {
                if ( a->getCost() == 1) {
                    assert( a->getMirror()->getCost() == -1);
                    PT(PTJ);
                    a->setCost( 0);
                    a->getMirror()->setCost( 0);
                }
            }
            PT(PTJ);
        }

        // (2)
        for ( a = arcsArrayYCWVar, na = 0; a != sentinelArc ; a ++, na ++ ) {
            cs = a->getCost() / _dn;
            PT(PTJ);
            if ( capArrayYCWVar[na]  > 0 && (flow = capArrayYCWVar[na] - a->getCAP()) != 0 )
                obj_internal += (double) cs * (double) flow;
            a->setCost( cs);
            PT(PTJ);
        }

        for ( i = nodesArrayYCWVar; i != sentinelNode; i ++) {
            i->setPrice( (i->getPrice() / _dn));
            PT(PTJ);
        }

        // (3) COMP_DUALS?
        if ( compDualsYCWVar == true) {
            compute_prices();
            PT(PTJ);
        }

        *objective_cost = obj_internal;
    }

    void cs2( double *objective_cost) {
        int cc = 0;

        PT(PTJ);

        update_epsilon();


        do {
            PT(PTJ);
            int ref = refine();
            if(ref==-1) {
                *objective_cost = -1;
                return ;
            }

            if ( nRefYCWVar >= 1 )
                price_out();
            PT(PTJ);
            if ( update_epsilon() )
                break;

            while (1) {
                if ( ! price_refine() )
                    break;
                PT(PTJ);
                if ( nRefYCWVar >= 1 ) {
                    if ( price_in() ) break;
                    if ( (cc = update_epsilon()) ) break;
                }
            }
        } while ( cc == 0 );


        PT(PTJ);
        finishup( objective_cost );
    }

    int greenTea() {
        if(!YCMflag){
             return -1;
        }
        double objective_cost;


        // (4) ordering, etc.;

        pre_processing();

        // () CHECK_SOLUTION?
        if ( checkSolution == true) {
            nodeBalanceYCWVar = (long long int *) calloc (nodeNumYCWVar+1, sizeof(long long int));
            for ( Node *i = nodesArrayYCWVar; i < nodesArrayYCWVar + nodeNumYCWVar; i ++ ) {
                nodeBalanceYCWVar[i - nodesArrayYCWVar] = i->getExcess();
            }
        }


        // (5) initializations;
        arcNumYCWVar = 2 * arcNumYCWVar;
        cs2_initialize(); // works already with 2*m;

        // (6) run CS2;
        cs2( &objective_cost );


        if(objective_cost<0){
            if(!isOutputResult){
                deallocate_arrays();
            }


            return -1;
        }else{
            // cout<<"加总费用之前："<<objective_cost<<endl;
            addServerAndDeployPrice(&objective_cost);

            if(!isOutputResult){
                // () cleanup;
                deallocate_arrays();
            }
            return objective_cost;
        }


    }
    int addServerAndDeployPrice(double *cost){
        // cout<<"不加服务器和部署的费用："<<cost<<endl;

        int total = 0;

        Node* srcNode = nodesArrayYCWVar + nodeNumYCWVar - 1;
        Edge* srcArc = srcNode->getSuspended();

        while(srcArc != (srcNode+1)->getSuspended()) {
            if((capArrayYCWVar[ srcArc == NULL ? -1 : srcArc - arcsArrayYCWVar ] - srcArc->getCAP()) > 0) {
                Node* serverNode = srcArc->getHead();
                Edge* a = serverNode->getSuspended();

                int serverOutput = 0;

                while(a != (serverNode+1)->getSuspended()) {

                    if((capArrayYCWVar[ a == NULL ? -1 : a - arcsArrayYCWVar ] - a->getCAP()) > 0) {
                        serverOutput += (capArrayYCWVar[ a == NULL ? -1 : a - arcsArrayYCWVar ] - a->getCAP());

                    }

                    ++a;
                }
                total += serverOutput;

                int serverFee = determineDC(serverOutput).second;
                // if(serverOutput>250)
                // cout<<"有服务器超过了250++++++++++++++++++++++++++++++++"<<endl;
                // cout<<"服务器："<<serverNode-_nodes<<"总输出："<<serverOutput<<"对应的服务器费用："<<serverFee<<endl;
                *cost += (serverFee + netStateDeployPrice[serverNode-nodesArrayYCWVar]);
                //                cout << "^^^" <<serverNode - _nodes << endl;
                serverAllPrice[serverNode-nodesArrayYCWVar] = serverFee + netStateDeployPrice[serverNode-nodesArrayYCWVar];
            }

            srcArc++;
        }

    }
    void storeServerGrade(){

        for(int i=0; i< pathNum ;i++){

            int serverIndex = path[i][1];

            map<int, int>::iterator iter = server_dc_map.find(serverIndex);
            if(iter!=server_dc_map.end()){
                // cout<<"iter->second加之前："<<iter->second;
                iter->second += pathFlow[i];
                // cout<<"iter->second加之后："<<iter->second<<"加了多少："<<outFlow[i]<<endl;
            }else{
                server_dc_map.insert(pair<int,int>(serverIndex,pathFlow[i]));
            }
        }
        map<int, int>::iterator iter;
        int totalFeed = 0;
        for(iter = server_dc_map.begin() ; iter != server_dc_map.end(); iter++){
            totalFeed += iter->second;
            // cout<<iter->first<<"攻击"<<iter->second<<endl;
            pair<int,int> p = determineDC(iter->second);
            iter->second = p.first;
        }
        // cout<<"总供给："<<totalFeed<<endl;
        // cout<<"总需求：k"<<endl;
    }
    pair<int,int> determineDC(int serverOutput){
        map<int, pair<int,int> >::iterator iter;


        for(iter = serverLevel.begin() ; iter != serverLevel.end(); iter++){
            // cout<<"iter->first:"<<iter->first<<endl;
            // cout<<"iter->second价格:"<<iter->second.second<<endl;
            if(serverOutput<=iter->first){
                return iter->second;
            }
        }
        // cout<<"不要走这里啊啊啊啊啊啊啊=================================="<<endl;
        return serverLevel.rbegin()->second;
    }
    //确定存储每个服务器的档次
    void server_dc_mapstoreServerGrade();
    bool YCMflag =false;
    void convert2MCMF(){

        //读取网络链路备份信息
        for(int i=0;i<G.arcNum;i++){
            set_arc(G.allNetArcs[i]->from,G.allNetArcs[i]->to,0,G.allNetArcs[i]->cap,G.allNetArcs[i]->cost);
            set_arc(G.allNetArcs[i]->to,G.allNetArcs[i]->from,0,G.allNetArcs[i]->cap,G.allNetArcs[i]->cost);
        }
        int total = 0;
        //读取消费节点备份信息
        for(int i=0;i<G.consumeNodesNum;i++){
            
            //消费节点编号接着网络节点后面
            set_arc(G.conNodes[i]->linkNodes[0]->nodeID,G.conNodes[i]->nodeID,0,G.conNodes[i]->arcs[0]->cap,0);
            //添加消费节点的需求
            set_supply_demand_of_node( G.conNodes[i]->nodeID, -1*G.conNodes[i]->arcs[0]->cap);
        }

        //添加超源到服务器的边

        for(set<int>::iterator it=itServers.begin();it!=itServers.end();++it){
            total+=serverMaxOutPutLimit[*it];
            set_arc(G.superSource, *it, 0,serverMaxOutPutLimit[*it], 0);// 超级源与服务器点连接
        }
        if( total >= G.totalFlow)
            YCMflag =true;
        set_supply_demand_of_node( G.superSource, G.totalFlow);
    }

    void increase_flow( Node *i, Node *j, Edge *a, long df) {
        i->removeEXC( df);
        j->addEXC( df);
        a->removeCAP( df);
        a->getMirror()->addCAP( df);
    }
    bool time_for_update() {
        return ( nRelYCWVar > nodeNumYCWVar * 0.4 + nSrcYCWVar * 30);
    }
    void reset_excess_q() {
        for ( ; excqFirst != NULL; excqFirst = excqLast ) {
            excqLast = excqFirst->getQNext();
            excqFirst->setQNext( sentinelNode);
        }
    }
    bool out_of_excess_q( Node *i) { return ( i->getQNext() == sentinelNode); }
    bool empty_excess_q() { return ( excqFirst == NULL); }
    bool nonempty_excess_q() { return ( excqFirst != NULL); }
    void insert_to_excess_q( Node *i) {
        if ( nonempty_excess_q() ) {
            excqLast->setQNext( i);
        } else {
            excqFirst = i;
        }
        i->setQNext( NULL);
        excqLast = i;
    }
    void insert_to_front_excess_q( Node *i) {
        if ( empty_excess_q() ) {
            excqLast = i;
        }
        i->setQNext( excqFirst);
        excqFirst = i;
    }
    void remove_from_excess_q( Node *i) {
        i = excqFirst;
        excqFirst = i->getQNext();
        i->setQNext( sentinelNode);
    }
    bool empty_stackq() { return empty_excess_q(); }
    bool nonempty_stackq() { return nonempty_excess_q(); }
    void reset_stackq() { reset_excess_q(); }
    void stackq_push( Node *i) {
        i->setQNext( excqFirst);
        excqFirst = i;
    }
    void stackq_pop( Node *i) {
        remove_from_excess_q( i);
    }
    void reset_bucket( Tong *b) { b->set_p_first( dTwoNode); }
    bool nonempty_bucket( Tong *b) { return ( (b->p_first()) != dTwoNode); }
    void insert_to_bucket( Node *i, Tong *b) {
        i->setBNext( b->p_first() );
        b->p_first()->setBPrev( i);
        b->set_p_first( i);
    }
    void get_from_bucket( Node *i, Tong *b) {
        i = b->p_first();
        b->set_p_first( i->getBNext());
    }
    void remove_from_bucket( Node *i, Tong *b) {
        if ( i == b->p_first() ) {
            b->set_p_first( i->getBNext());
        } else {
            i->getBprev()->setBNext( i->getBNext());
            i->getBNext()->setBPrev( i->getBprev());
        }
    }
    void update_cut_off() {
        if ( nBadPricein + nBadRelabelYCWVar == 0) {
            cutOffFactor = pow( (double)nodeNumYCWVar, 0.75 );
            cutOffFactor = cutOffFactor > 12 ? cutOffFactor:12;
            cutOff = cutOffFactor * epsilonYCWVar;
            cutOn = cutOff * 0.8;
        } else {
            cutOffFactor *= 4;
            cutOff = cutOffFactor * epsilonYCWVar;
            cutOn = cutOff * 0.8;
        }
    }
    void exchange( Edge *a, Edge *b) {
        if ( a != b) {
            Edge *sa = a->getMirror();
            Edge *sb = b->getMirror();
            long d_cap;

            dArcYCWVar.setCAP( a->getCAP());
            dArcYCWVar.setCost( a->getCost());
            dArcYCWVar.setHead( a->getHead());

            a->setCAP( b->getCAP());
            a->setCost( b->getCost());
            a->setHead( b->getHead());

            b->setCAP( dArcYCWVar.getCAP());
            b->setCost( dArcYCWVar.getCost());
            b->setHead( dArcYCWVar.getHead());

            if ( a != sb) {
                b->setMirror( sa);
                a->setMirror( sb);
                sa->setMirror( b);
                sb->setMirror( a);
            }

            d_cap = capArrayYCWVar[ a - arcsArrayYCWVar];
            capArrayYCWVar[ a - arcsArrayYCWVar] = capArrayYCWVar[ b - arcsArrayYCWVar];
            capArrayYCWVar[ b - arcsArrayYCWVar] = d_cap;
        }
    }
};


#endif


