package com.lab603.yj.module;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Min {
	MinCostFlow mcf = new MinCostFlow();
	int n,m;//节点数，边数
	int serversNum = 3;//服务器个数
	List<Integer> serverIndex;//服务器编号
	int serverPrice; //服务器成本
	int superSource,superSink;
	int totalFlow=0;//总流量
	int netStates,netRoutes,consumeStates;

	public void readTxt(String filename) throws IOException {
		BufferedReader data = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		String[] strcol =  data.readLine().split(" ");
		
		//网络节点个数
		netStates = Integer.valueOf(strcol[0]);
		
		//链路个数
		netRoutes = Integer.valueOf(strcol[1]);
		
		//消费节点个数
		consumeStates = Integer.valueOf(strcol[2]);
		
		m = netStates+consumeStates+2;
		n = netRoutes + 2*consumeStates + serversNum;
		mcf.setV(n);
		//空行读取
		data.readLine();
		
		//部署服务器费用
		serverPrice =  Integer.valueOf(data.readLine());
		
		data.readLine();
		//读取所有节点之间的链路关系
		for (int i = 0; i < netRoutes-consumeStates; i++) {
			/*
			 *  读取一行数据，数据格式
			 *  0 16 8 2  
			 *  链路起始节点为0，链路终止节点为16，总带宽为8，单位网络租用费为2
			 */
			strcol = data.readLine().split(" ");
			int start = Integer.valueOf(strcol[0]);
			int end = Integer.valueOf(strcol[1]);
			int cap = Integer.valueOf(strcol[2]);
			int cost =  Integer.valueOf(strcol[3]);
			
			mcf.add_edge(start, end, cap, cost);
			mcf.add_edge(start, end, cap, cost);
		}
		//空行读取
		data.readLine();
		
		//读取消费节点信息
		for (int i = 0; i < consumeStates; i++) {
			/*
			 *  读取一行数据，数据格式
			 * 	0 8 40 
			 *  消费节点为0，相连网络节点ID为8，视频带宽消耗需求为40
			 */
			strcol = data.readLine().split(" ");
			int start = Integer.valueOf(strcol[0]);
			int end = Integer.valueOf(strcol[1]);
			int cap = Integer.valueOf(strcol[2]);
			totalFlow += cap;
			mcf.add_edge(start+netStates, end, cap, 0);
			mcf.add_edge(start, end+netStates, cap, 0);
		}
		
		//添加超级源点和超级汇点的信息
		superSource = netStates+consumeStates;
		superSink = superSource+1;
		serverIndex.add(7);
		serverIndex.add(13);
		serverIndex.add(15);
		serverIndex.add(22);
		serverIndex.add(37);
		serverIndex.add(38);
		serverIndex.add(43);
		//添加超源到服务器的边
		for(int i=0 ; i<serverIndex.size(); i++) {
			mcf.add_edge(superSource, serverIndex.get(i), 600, 0);
			mcf.add_edge(serverIndex.get(i), superSource, 600, 0);
		}
		//添加超汇到消费点的边
		for(int i=netStates ; i<netStates+consumeStates; i++) {
			mcf.add_edge(superSink, i, 600, 0);
			mcf.add_edge(i, superSink, 600, 0);
		}
	
	}

	public static void main(String[] args) {
		try {
			new Min().readTxt("case0.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
