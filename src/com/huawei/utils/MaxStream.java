package com.huawei.utils;

import java.util.HashSet;
import java.util.Set;

import com.filetool.util.FileUtil;
import com.huawei.graph.Graph;
import com.huawei.graph.Vertex;

/**
 * 最大流的算法 1.initial foreach edge(u,v) in E f(u,v) = 0,f(v,u) = 0
 * //也就能推出residual(u,v) = c(u,v) maxflow = 0 2.while there exists augment path p
 * in the residual network Gf cfp = min{f(u,v):(u,v) in p} maxflow += cfp
 * foreach edge(u,v) in path p f(u,v) = f(u,v) + cfp f(v,u) = f(v,u) - cfp
 * residual(u,v) = c(u,v) - f(u,v) //当residual(u,v)为0时，表明在寻找增广路径时,这条边不通
 * residual(v,u) = c(v,u) - f(v,u) //当residual(u,v)为0时，表明在寻找增广路径时,这条边不通
 * 
 */
public class MaxStream {
	private static final int WHITE = 0, GRAY = 1;
	private int[][] capacity;
	private int[][] res_capacity;
	private int[][] flow;
	private int max_flow;
	private int size;
	private int con_size;
	private int[] consumernodes;
	private int[] networknodes;
	private int source, sink;
	private int first, last;
	private int[] min_capacity;
	private int[] parent;
	private int[] color;
	private int[] queue;

	// 阶段1:暂不考虑价格问题，最考虑流问题
	public void readFile() {
		String[] contents = FileUtil.read("./graphfile/case5.txt", null);
		String[] numOfnodeedgeconsumer = contents[0].split(" ");
		size = Integer.parseInt(numOfnodeedgeconsumer[0]);
		int edge_size = Integer.parseInt(numOfnodeedgeconsumer[1]);
		con_size = Integer.parseInt(numOfnodeedgeconsumer[2]);
		capacity = new int[size][size];
		res_capacity = new int[size][size];
		flow = new int[size][size];
		consumernodes = new int[con_size];
		networknodes = new int[size];

		for (int i = 4; !contents[i].equals(""); i++) {
			String[] edgeInfo = contents[i].split(" ");
			int iVertex = Integer.parseInt(edgeInfo[0]);
			int jVertex = Integer.parseInt(edgeInfo[1]);
			int edgeCol = Integer.parseInt(edgeInfo[2]);

			capacity[iVertex][jVertex] = capacity[jVertex][iVertex] = edgeCol;
			res_capacity[iVertex][jVertex] = res_capacity[jVertex][iVertex] = edgeCol;
		}

		source = 22;
		sink = 0;
		min_capacity = new int[size];
		parent = new int[size];
		color = new int[size];
		queue = new int[size];
	}
//
//	public void maxstream() {
//		while (BreadFirstSearch(source)) {
//			max_flow += min_capacity[sink];
//			int v = sink, u;
//			while (v != source) {
//				u = parent[v];
//				flow[u][v] += min_capacity[sink];
//				flow[v][u] -= min_capacity[sink];
//				res_capacity[u][v] -= min_capacity[sink];
//				res_capacity[v][u] += min_capacity[sink];
//				v = u;
//			}
//		}
//		displayMaxStream(source);
//	}

	private boolean BreadFirstSearch(int source) {
		for (int i = 0; i < size; i++) {
			color[i] = WHITE;
			min_capacity[i] = Integer.MAX_VALUE;
			queue[i] = -1;
			parent[i] = -1;
		}
		first = last = 0;

		color[source] = GRAY;
		queue[last++] = source;
		while (first != last) {
			int v = queue[first++];
			for (int u = 0; u < size; u++) {
				if (color[u] == WHITE && res_capacity[v][u] > 0) {
					min_capacity[u] = Math.min(min_capacity[v], res_capacity[v][u]);
					color[u] = GRAY;
					parent[u] = v;
					if (u == sink) {
						return true;
					} else {
						queue[last++] = u;
					}
				}
			}
		}

		return false;
	}

	public void displayMaxStream(int source) {
		System.out.println("max_flow : " + max_flow);
		first = last = 0;

		queue[last++] = source;
		while (first != last) {
			int u = queue[first++];
			for (int v = 0; v < size; v++) {
				if(flow[u][v] > 0){
					System.out.println(u + " -> " + v + " , flow : " + flow[u][v]);
					queue[last++] = v; 
				}
			}
		}
	}
	
	public static void main(String[] args){
		MaxStream ma = new MaxStream();
		ma.readFile();
//		ma.maxstream();
	}
}
