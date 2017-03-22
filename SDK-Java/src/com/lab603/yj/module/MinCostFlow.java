package com.lab603.yj.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MinCostFlow {
	
	private int INF = 0x3f3f3f3f;
	
	//最大顶点数
	private static int MAX_V = 2000;
	
	//顶点数
	private int V;
	
	//图的邻接表
	Map<Integer,List<Edge>> G = new HashMap<Integer,List<Edge>>();
	//顶点的势
	int[] h;
	//最短距离
	int[] dist;
	//最短路中的前驱节点和对应的边
	int[] prevv, preve;

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
		List<Edge> edges = new ArrayList<>();
		edges.add(new Edge(to, cap,cost, G.get(to).size()));
		G.put(from, edges);
		edges.clear();
		edges.add(new Edge(from, 0,-cost, G.get(from).size()-1));
		G.put(to, edges);
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
		h = new int[V];
		
		while (f > 0) {
			// Dijkstra更新h
			Queue<Pair> que = new LinkedList<Pair>();
			dist = new int[V];
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
					if (e.getCap() > 0 && (dist[e.getTo()] > (dist[v] + e.getCost() + h[v] - h[e.getTo()]))) {
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
				//return -1;
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
				e.setCap(G.get(v).get(e.getRev()).getCap() + d);
			}

		}
		return res;
	}

	void init() {
		//这里假设节点从0编号
		for (int i = 0; i < V; i++) {
			G.get(i).clear();
		}
		V = 0;
	}
	public MinCostFlow() {
		super();
	}
	public int getV() {
		return V;
	}
	public void setV(int v) {
		V = v;
	}
	
	
}
