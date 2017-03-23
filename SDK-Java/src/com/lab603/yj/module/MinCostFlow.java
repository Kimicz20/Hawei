package com.lab603.yj.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import com.lab603.module.CostNode;
import com.lab603.module.Net;
import com.lab603.module.ResultPathsAndCost;
import com.lab603.module.Tran;
import com.lab603.yj.util.MyPriorityQueue;
import com.lab603.yj.util.Pair;

public class MinCostFlow {

	private Net net;
	
	private int INF = 0x3f3f3f3f;

	private int totalFlow = 0; 	
	
	// MAX node number
	private static int MAX_V = 2000;

	// Node number
	private int nodeNum;
	
	int superSource, superSink;
	
	int netStates, netRoutes, consumeStates;
	
	// Graph array
	List<List<Edge>> G;

	// Node h
	int[] h = new int[MAX_V];

	// Short distance
	int[] dist = new int[MAX_V];

	// pre node and eadge of short distance
	int[] prevv = new int[MAX_V];
	int[] preve = new int[MAX_V];

	List<List<Integer>> paths = new ArrayList<List<Integer>>();

	/***
	 * add eage to Graph array
	 * 
	 * @param from
	 * 
	 * @param to
	 * 
	 * @param cap
	 * 
	 * @param cost
	 * 
	 */
	public void add_edge(int from, int to, int cap, int cost) {
		G.get(from).add(new Edge(to, cap, cost, G.get(to).size()));
		G.get(to).add(new Edge(from, 0, -cost, G.get(from).size() - 1));
	}

	public void TransNet2Flow() {
		
		netStates = net.getNodes().size();

		netRoutes = net.getTrans().size();

		consumeStates = net.getCostNodes().size();

		init(netStates + consumeStates + 2);

		for (Tran t : net.getTrans()) {
			int start = t.getFromNodeID();
			int end = t.getToNodeID();
			int cap = t.getMaxValue();
			int cost = t.getCostValue();

			add_edge(start, end, cap, cost);
			add_edge(end, start, cap, cost);
		}

		for (CostNode c : net.getCostNodes()) {
			int start = c.getId();
			int end = c.getLinkedNodeId();
			int cap = c.getRequestValue();
			totalFlow += cap;
			add_edge(start, end, cap, 0);
			add_edge(end, start, cap, 0);
		}
	}
	
	public void setServer(List<Integer> serverIndex) {
		
		superSource = netStates + consumeStates;
		superSink = superSource + 1;
		
		for (int i = 0; i < serverIndex.size(); i++) {
			add_edge(superSource, serverIndex.get(i), 600, 0);
			add_edge(serverIndex.get(i), superSource, 600, 0);
		}
		for (int i = netStates; i < netStates + consumeStates; i++) {
			add_edge(superSink, i, 600, 0);
			add_edge(i, superSink, 600, 0);
		}
	}
	
	public ResultPathsAndCost min_cost_flow() {
		paths.clear();
		int res = 0;

		for (int i = 0; i < nodeNum; i++) {
			h[i] = 0;
		}
		while (totalFlow > 0) {
			// Dijkstra update h
			Queue<Pair> que = MyPriorityQueue.getPriorityQueue();
			for (int i = 0; i < nodeNum; i++) {
				dist[i] = INF;
			}
			dist[superSource] = 0;
			que.add(new Pair(0, superSource));

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

			// extended end
			if (dist[superSink] == INF) {
				return new ResultPathsAndCost(paths,-totalFlow);
			}
			for (int v = 0; v < nodeNum; v++) {
				h[v] += dist[v];
			}

			// The shortest path from s to t is augmented
			int d = totalFlow;

			List<Integer> shortIndex = new ArrayList<>();
			/* Compared with the minimum capacity of the residual demand flow
			 and the shortest path, the minimum is the augmented flow*/
			for (int v = superSink; v != superSource; v = prevv[v]) {
				if (d > G.get(prevv[v]).get(preve[v]).getCap())
					d = G.get(prevv[v]).get(preve[v]).getCap();
				// save shortest path
				if (v != superSink) {
					shortIndex.add(v);
				}
			}
			shortIndex.set(0, shortIndex.get(0)-netStates);
			Collections.reverse(shortIndex);
			shortIndex.add(d);
			paths.add(shortIndex);

			totalFlow -= d;
			res += d * h[superSink];
			for (int v = superSink; v != superSource; v = prevv[v]) {
				Edge e = G.get(prevv[v]).get(preve[v]);
				e.setCap(e.getCap() - d);
				G.get(v).get(e.getRev()).setCap(G.get(v).get(e.getRev()).getCap() + d);
			}

		}
		return new ResultPathsAndCost(paths,res);
	}

	public MinCostFlow(Net net) {
		this.net = net;
	}

	public void init(int v) {
		nodeNum = v;
		G = new ArrayList<List<Edge>>();
		for (int i = 0; i < nodeNum; i++) {
			G.add(new ArrayList<Edge>());
		}
	}

}
