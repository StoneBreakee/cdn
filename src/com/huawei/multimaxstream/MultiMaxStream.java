package com.huawei.multimaxstream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.filetool.util.FileUtil;

import static java.lang.System.out;

/**
 * Created by hadoop on 18/03/17. 多源点多汇点的最大流网络 为多个源点建立一个超级源点，超级源点连接到每个源点上
 * 为多个汇点建立一个超级汇点，每个汇点连接到超级汇点上
 * <p>
 * 在这个赛题中，超级源点连接到每个源点的权重为原源点的网络需求 各个汇点连接到超级汇点的权重为无穷大
 */
public class MultiMaxStream {
	private static final int WHITE = 0, GRAY = 1;

	private int[] parent;
	private int[] color;
	private int[] min_capacity;

	private Queue<Integer> queue;

	private int source, sink;
    private int vertex_size;
	
	public int[][] capacity;
	public int[][] res_capacity;
	public int[][] flow;
	public int[] sinks;
	public int max_flow;
	public int totalbandrequire;
	public HashMap<String, Integer> edgePriceMap = new HashMap<String, Integer>();
	public HashMap<Integer, Integer> netnodeToConsumer = new HashMap<Integer, Integer>();
	public int servercost;

	public void readFile(String[] contents) {
		servercost = Integer.parseInt(contents[2]);
		String[] graphInfos = contents[0].split(" ");
		vertex_size = Integer.parseInt(graphInfos[0]);
		int edges_size = Integer.parseInt(graphInfos[1]);
		int consumer_size = Integer.parseInt(graphInfos[2]);

		capacity = new int[vertex_size + 2][vertex_size + 2];
		res_capacity = new int[vertex_size + 2][vertex_size + 2];
		flow = new int[vertex_size + 2][vertex_size + 2];

		for (int i = 4; i < edges_size + 4; i++) {
			String[] edgeInfo = contents[i].split(" ");
			int startVertex = Integer.parseInt(edgeInfo[0]);
			int endVertex = Integer.parseInt(edgeInfo[1]);
			int dataflow = Integer.parseInt(edgeInfo[2]);
			int price = Integer.parseInt(edgeInfo[3]);
			capacity[startVertex + 1][endVertex + 1] = res_capacity[startVertex + 1][endVertex + 1] = dataflow;
			capacity[endVertex + 1][startVertex + 1] = res_capacity[endVertex + 1][startVertex + 1] = dataflow;

			edgePriceMap.put(startVertex + "-" + endVertex, price);
		}

		for (int i = 5 + edges_size; i < 5 + edges_size + consumer_size; i++) {
			String[] consumerInfo = contents[i].split(" ");
			int consumerNode = Integer.parseInt(consumerInfo[0]);
			int networkNode = Integer.parseInt(consumerInfo[1]);
			int bandrequire = Integer.parseInt(consumerInfo[2]);
			boolean flag = false;
			for(int j = 0;j < sinks.length && !flag;j++){
				if(networkNode == sinks[j]){
					flag = true;
				}
			}
			if(!flag){
				capacity[0][networkNode + 1] = res_capacity[0][networkNode + 1] = bandrequire;
				totalbandrequire += bandrequire;
			}
			
			netnodeToConsumer.put(networkNode, consumerNode);
		}

		for (int i = 0; i < sinks.length; i++) {
			int tmp_sink_total = 0;
			for(int j = 1;j < vertex_size + 1;j++){
				if(capacity[sinks[i] + 1][j] > 0){
					tmp_sink_total += capacity[sinks[i] + 1][j];
				}
			}
			capacity[sinks[i] + 1][vertex_size + 1] = res_capacity[sinks[i] + 1][vertex_size + 1] = Integer.MAX_VALUE;
		}

		source = 0;
		sink = vertex_size + 1;
		min_capacity = new int[vertex_size + 2];
		parent = new int[vertex_size + 2];
		color = new int[vertex_size + 2];
		max_flow = 0;
		queue = new LinkedList<Integer>();
	}

	public int maxStream() {
		while (BreadFirstSearch(source)) {
			max_flow += min_capacity[sink];
			int v = sink, u;
			while (v != source) {
				u = parent[v];
				flow[u][v] += min_capacity[sink];
				flow[v][u] -= min_capacity[sink];
				res_capacity[u][v] -= min_capacity[sink];
				res_capacity[v][u] += min_capacity[sink];
				v = u;
			}
		}

		out.println("max_flow is " + max_flow);
		return max_flow;
	}

	private boolean BreadFirstSearch(int source) {
		for (int i = 0; i < color.length; i++) {
			color[i] = WHITE;
			parent[i] = Integer.MAX_VALUE;
			min_capacity[i] = Integer.MAX_VALUE;
		}

		queue.add(source);
		color[source] = GRAY;
		parent[source] = -1;

		while (!queue.isEmpty()) {
			int v = queue.poll();
			for (int u = 0; u < min_capacity.length; u++) {
				if (color[u] == WHITE && res_capacity[v][u] > 0) {
					min_capacity[u] = Math.min(min_capacity[v], res_capacity[v][u]);
					parent[u] = v;
					color[u] = GRAY;
					if (u != sink) {
						queue.add(u);
					} else {
						queue.clear();
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void main(String[] args){
		MultiMaxStream mms = new MultiMaxStream();
		mms.sinks = new int[]{0,3,22};
		mms.readFile(FileUtil.read("./graphfile/case5.txt", null));
		mms.maxStream();
		for(int i = 1;i < mms.flow.length -1;i++){
			for(int j = 1;j < mms.flow[i].length - 1;j++){
				if(mms.flow[i][j] > 0 && mms.capacity[i][j] < mms.flow[i][j]){
					System.out.println((i - 1) + " , " + (j - 1));
				}
			}
		}
	}
	
	public void dijistra(int source){
		boolean[] path = new boolean[vertex_size + 2];
		int[] visite = new int[vertex_size + 2];
		int[] distance = new int[vertex_size + 2];
		for(int i = 0;i < vertex_size + 2;i++){
			visite[i] = Integer.MAX_VALUE;
			distance[i] = Integer.MAX_VALUE;
			color[i] = WHITE;
			path[i] = false;
			if(capacity[source][i]> 0){
				distance[i] = capacity[source][i];
			}
		}
		path[source] = true;
		distance[source] = 0;
		visite[source] = -1;
		int v = -1,w = source;
		
		for(int i = 0;i < vertex_size + 2;i++){
			int min = Integer.MAX_VALUE;
			for(int j = 0;j < vertex_size + 2;j++){
				if(!path[j]){
					if(distance[j] < min){
						v = j;
						min = distance[j];
					}
				}
			}
			path[v] = true;
			for(int k = 0;k < vertex_size + 2;k++){
				if(!path[k] && capacity[v][k] > 0 && min + capacity[v][k]< distance[k]){
					distance[k] = min + capacity[v][k];
					visite[k] = v;
				}
			}
		}
		int i = vertex_size - 1;
		System.out.print(i + " -> ");
		while(i != -1){
			System.out.print(visite[i] - 1 + " -> ");
			i = visite[i];
		}
		
		System.out.println();
	}
	
	public void display() {
		for (int i = 0; i < flow[0].length; i++) {
			if (flow[0][i] > 0) {
				int start = i;
				Queue<Integer> qtmp = new LinkedList<Integer>();
				qtmp.add(start);
				while (!qtmp.isEmpty()) {
					int v = qtmp.poll();
					for (int j = 1; j < flow[v].length - 1; j++) {
						if (flow[v][j] > 0) {
							System.out.println((v - 1) + " -> " + (j - 1) + " , flow : " + flow[v][j]);
							qtmp.add(j);
						}
					}
				}
			}
		}
	}

	public ArrayList<MultiPathNode> getPathTree() {
		ArrayList<MultiPathNode> totalPathTree = new ArrayList<MultiPathNode>();
		for (int i = 0; i < flow[0].length; i++) {
			if (flow[0][i] > 0) {
				MultiPathNode startNode = new MultiPathNode(netnodeToConsumer.get(i - 1), i - 1, flow[0][i], 0);
				totalPathTree.add(startNode);
				Queue<MultiPathNode> qtmp = new LinkedList<MultiPathNode>();
				qtmp.add(startNode);
				while (!qtmp.isEmpty()) {
					MultiPathNode vNode= qtmp.poll();
					ArrayList<MultiPathNode> son = vNode.son;
					int dataflow = vNode.dataflow;
					int v = vNode.self + 1;
					for (int j = 1; j < flow[v].length - 1; j++) {
						if (flow[v][j] > 0 && dataflow > 0) {
							int dataflowmin =  Math.min(dataflow, flow[v][j]);
							dataflowmin = Math.min(dataflowmin, capacity[v][j]);
							flow[v][j] -= dataflowmin;
							dataflow -= dataflowmin;
							MultiPathNode tmpNode = new MultiPathNode(v-1,j-1,dataflowmin,getLinePrice(v-1,j-1));
							son.add(tmpNode);
							System.out.println((v - 1) + " -> " + (j - 1) + " , flow : " + dataflowmin);
							qtmp.add(tmpNode);
						}
					}
				}
			}
		}
		return totalPathTree;
	}

	private int getLinePrice(int start,int end){
		Integer price = edgePriceMap.get(start+"-"+end);
		if(price == null){
			price = edgePriceMap.get(end+"-"+start);
		}
		return price;
	}

}
