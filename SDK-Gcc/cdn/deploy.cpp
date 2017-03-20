#include "deploy.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//你要完成的功能总入口
void deploy_server(char * topo[MAX_EDGE_NUM], int line_num,char * filename)
{
	int n = line_num;
	char* num = topo[0];
	char* tmp;
	char t[256];
	char* tmp2;
	char* tmp3;
	tmp = strtok(num," ");
	tmp = strtok(NULL," ");
	tmp = strtok(NULL," ");
	int a  = atoi(tmp);
	char str[256];
	sprintf(str,"%d\n%s",a,"\n");
	for(int i = n-a;i<n;i++){
		tmp = strtok(topo[i]," ");
		tmp2 = strtok(NULL," ");
		tmp3 = strtok(NULL," ");
		if(i == n-1){
			tmp3 = strtok(tmp3,"\n");
		}
		sprintf(t,"%s %s %s",tmp2,tmp,tmp3);
		sprintf(str,"%s%s",str,t);
	}
	//printf("%s",str);
	// 需要输出的内容
	char * topo_file = str;

	// 直接调用输出文件的方法输出到指定文件中(ps请注意格式的正确性，如果有解，第一行只有一个数据；第二行为空；第三行开始才是具体的数据，数据之间用一个空格分隔开)
	write_result(topo_file, filename);

}
