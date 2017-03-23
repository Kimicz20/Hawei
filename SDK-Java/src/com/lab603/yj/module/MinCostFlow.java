package com.lab603.yj.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import com.lab603.yj.util.MyPriorityQueue;
import com.lab603.yj.util.Pair;

public class MinCostFlow {
	
	private int INF = 0x3f3f3f3f;
	
	//最大顶点数
	private static int MAX_V = 2000;
	
	//顶点数
	private int V;
	
	//图的邻接表
	List<List<Edge>> G ;
	//顶点的势
	int[] h = new int[MAX_V];
	//最短距离
	int[] dist = new int[MAX_V];
	//最短路中的前驱节点和对应的边
	int[] prevv = new int[MAX_V];
	int[] preve = new int[MAX_V]; 

	/***
	 * 向邻接表中添加 边
	 * @param from
	 * 		起点
	 * @param to
	 * 		终点
	 * @param cap
	 * 		容量
	 * @param cost
	 * 		费用
	 */
	public void add_edge(int from, int to, int cap, int cost) {
		G.get(from).add(new Edge(to, cap,cost, G.get(to).size()));
		G.get(to).add(new Edge(from, 0,-cost, G.get(from).size()-1));
	}
	/**
	 * 最小费用流算法
	 * @param s
	 * @param t
	 * @param f
	 * @return
	 */
	int min_cost_flow(int s, int t, int f) {
		int res = 0;
		//初始化h
		
		for(int i=0;i<V;i++){
			h[i]=0;
		}
		while (f > 0) {
			// Dijkstra更新h
			Queue<Pair> que = MyPriorityQueue.getPriorityQueue();
			for(int i=0;i<V;i++){
				dist[i]=INF;
			}
			dist[s] = 0;
			que.add(new Pair(0,s));
			
			while (!que.isEmpty()) {
				Pair p = que.peek();
				que.poll();
				int v = p.getSecond();
				if (dist[v] < p.getFirst())
					continue;
				
				for (int i = 0; i < G.get(v).size(); i++) {
					Edge e = G.get(v).get(i);
					if (e.getCap() > 0 && dist[e.getTo()] > dist[v] + e.getCost() + h[v] - h[e.getTo()]) {
						dist[e.getTo()] = dist[v] + e.getCost() + h[v] - h[e.getTo()];
						prevv[e.getTo()] = v;
						preve[e.getTo()] = i;
						que.add(new Pair(dist[e.getTo()], e.getTo()));
					}
				}
			}

			//不能再增广了
			if (dist[t] == INF) {
				//更改
				return -f;
			}
			for (int v = 0; v < V; v++) {
				h[v] +=  dist[v];
			}
			
			//从s到t的最短路尽量增广
			int d = f;
			for (int v = t; v != s; v = prevv[v]) {
				if(d > G.get(prevv[v]).get(preve[v]).getCap())
					d = G.get(prevv[v]).get(preve[v]).getCap();
			}
			f -= d;
			res += d * h[t];
			for (int v = t; v != s; v = prevv[v]) {
				Edge e = G.get(prevv[v]).get(preve[v]);
				e.setCap(e.getCap() - d);
				G.get(v).get(e.getRev()).setCap(G.get(v).get(e.getRev()).getCap() + d);
			}

		}
		return res;
	}

	public MinCostFlow() {
		super();
	}
	public int getV() {
		return V;
	}
	public void setV(int v) {
		V = v;
		init();
	}
	public void init(){
		G = new ArrayList<List<Edge>>();
		for(int i=0;i<V;i++){
			G.add(new ArrayList<Edge>());
		}
	}
	public void printf(){
		for(int i=0;i<V;i++){
			List<Edge> l = G.get(i);
			for(Edge e:l){
				System.out.println(e);
			}
		}
	}
}
