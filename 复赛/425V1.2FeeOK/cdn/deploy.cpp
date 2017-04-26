#include "deploy.h"
#include "ga.h"

using namespace std;
void test(char **topo,char * filename){
    Graph G;
    set<int> itServers; 
    G.init(topo);
    
    for (int i=0;i<G.conAndnetArcs.size();i++)
    {
        itServers.insert(G.conAndnetArcs[i]->to);
    }
    cout<<itServers.size()<<endl;
    std::vector<int> v;
    for (int i=0;i<G.netNodesNum;i++)
    {
        v.push_back(150);
    }
    MCMF_YCW mcmf(G,itServers);
    // mcmf.isOutputResult = true;
    // cout<< mcmf.greenTea() <<endl;

    // string result("");
    // mcmf.print_solution(result);
    // //需要输出的内容
    // const char * topo_file = result.c_str();
    
    // // 直接调用输出文件的方法输出到指定文件中(ps请注意格式的正确性，如果有解，第一行只有一个数据；第二行为空；第三行开始才是具体的数据，数据之间用一个空格分隔开)
    // write_result(topo_file, filename);
}
void testGA(char **topo,char * filename){

    clock_t start_time = clock();
    alarm(88);
	//初始化图
    Graph g;
    g.init(topo);

    //GA大法 ！！！！
    GA ga(g);
    if(g.netNodesNum < 1000)
        ga.init(10);
    else {
         ga.init(1);
    }
    const char * topo_file = ga.xjbs(start_time).c_str();
    cout<<"time:"<<(clock() - start_time)/CLOCKS_PER_SEC<<endl;
    // 直接调用输出文件的方法输出到指定文件中(ps请注意格式的正确性，如果有解，第一行只有一个数据；第二行为空；第三行开始才是具体的数据，数据之间用一个空格分隔开)
    write_result(topo_file, filename);

}
//你要完成的功能总入口
void deploy_server(char * topo[MAX_EDGE_NUM], int line_num,char * filename)
{
    srand( (unsigned)time(NULL) );//初始化时间种子
    // test(topo,filename);
    testGA(topo,filename);
}
