package com.huawei.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.huawei.graph.Consumer;
import com.huawei.graph.Graph;
import com.huawei.graph.PathNode;
import com.huawei.graph.Vertex;

public class ServerLocate {

	public static int[] candidateServerNode(String[] contents) {
		Graph g = GraphUtils.getGraphByFile(contents);

		double gradient_total = 2;
		// 通过计算每个消费节点到其余消费节点的路径，
		// 为网络节点打分，得分最高就越可能成为服务节放置节点
		double[] scores = new double[(int) g.networknodenum];
		for (Consumer c : g.consumernodeCollection) {
			System.out.println("-----begin-----");
			int networkId = (int) c.networkid;
			Vertex vBand = g.networknodeCollection.get(networkId);
			double weight = (double) vBand.bandRequire / (double) 500;
			scores[networkId] = weight;

			long maxBand = g.getMaxBand(vBand, g.getAllEdgeOfNode(vBand));
			ArrayList<Vertex> vList = new ArrayList<Vertex>();
			vList.add(vBand);
			if (maxBand < vBand.bandRequire) {
				vList.addAll(vBand.getAllAdjNodes(vBand));
			}

			for (Vertex vloop : vList) {
				long networkidInLoop = vloop.id;
				double[] vertexes = new double[(int) g.networknodenum];
				int[] path = g.shortestPath_DIJ1(vloop, vertexes);
				for (Consumer ctmp : g.consumernodeCollection) {
					int value = (int) ctmp.networkid;
					if (value != networkidInLoop) {
						System.out.print("node " + value + " --> ");
						int vertex_index = (int) networkidInLoop;
						double gradient = 0.1;
						while (path[value] != vertex_index) {
							scores[path[value]] = scores[path[value]] + gradient * weight;
							gradient = gradient + 0.05;
							System.out.print(path[value] + " --> ");
							value = path[value];
						}
						System.out.println(networkidInLoop);
					}
					if (networkidInLoop != networkId) {
						scores[(int) networkidInLoop] = scores[(int) networkidInLoop] + 0.1 * weight;
					}
				}
				System.out.println("------end------");
			}
		}

		int[] nodecross = new int[(int) g.networknodenum];
		for (int i = 0; i < nodecross.length; i++) {
			nodecross[i] = i;
		}

		for (int i = 0; i < scores.length; i++) {
			int tmp = i;
			for (int j = i + 1; j < scores.length; j++) {
				if (scores[tmp] < scores[j]) {
					tmp = j;
				}
			}
			if (i != tmp) {
				int change = nodecross[i];
				nodecross[i] = nodecross[tmp];
				nodecross[tmp] = change;

				double double_tmp = scores[tmp];
				scores[tmp] = scores[i];
				scores[i] = double_tmp;
			}
		}

		return nodecross;
	}

	public static String[] displayPath(String[] contents) {
		long start = System.currentTimeMillis();

		ArrayList<Integer> candidateIndex = new ArrayList<Integer>();
		Set<Integer> candidateNode = new TreeSet<Integer>();

		int[] nodecross = candidateServerNode(contents);
		
		for (int i = 0; i < nodecross.length; i++) {
			candidateIndex.add(nodecross[i]);
		}

		HashMap<Integer, ArrayList<PathNode>> resultMap = new HashMap<Integer, ArrayList<PathNode>>();

		Graph g = GraphUtils.getGraphByFile(contents);
		// 依次次增加候选点的个数
		for (int j = 1; j < candidateIndex.size(); j++) {
			for (int i = 0; i < j; i++) {
				candidateNode.add(candidateIndex.get(i));
			}
			System.out.println("\nServer Num:" + candidateNode.size());

			for (Consumer c : g.consumernodeCollection) {
				Vertex vc = g.networknodeCollection.get((int) c.networkid);
				vc.bandRequire = 0;
			}

			System.out.println();
			boolean failed = false;
			ArrayList<PathNode> nodelist = new ArrayList<PathNode>();
			for (Consumer c : g.consumernodeCollection) {
				System.out.println("--path begin--");
				Vertex vc = g.networknodeCollection.get((int) c.networkid);
				vc.bandRequire = c.bandrequire;
				PathNode tmp = g.consumerToCandiateNodeByShortest(vc, candidateNode);
				nodelist.add(tmp);
				if (tmp == null) {
					failed = true;
					break;
				}
				System.out.println("--path over--\n");
			}
			if (failed) {
				System.out.println("第" + j + "次失败");
				continue;
			} else {
				System.out.println("第" + j + "次成功");
				long servercost = j * g.servercost;
				long linkcost = 0;
				for (PathNode pathnodetmp : nodelist) {
					linkcost += PathNode.getSumBandCost(pathnodetmp)[0];
				}
				resultMap.put((int) (servercost + linkcost), nodelist);
				System.out.println(servercost + linkcost);
				break;
			}
		}

		if (resultMap.size() == 0) {
			return new String[] { "NA\r\n" };
		}

		ArrayList<String> results = new ArrayList<String>();
		Integer min = Integer.MAX_VALUE;
		Iterator<Integer> itsort = resultMap.keySet().iterator();
		while (itsort.hasNext()) {
			Integer tmpkey = itsort.next();
			if (min > tmpkey) {
				min = tmpkey;
			}
		}

		StringBuilder sbTotal = new StringBuilder("");
		for (PathNode nodeloop : resultMap.get(min)) {
			StringBuilder sb = new StringBuilder("");
			PathNode.getAllPath(nodeloop, sb);
			sbTotal.append(sb);
		}
		for (String sloop : sbTotal.toString().split(",")) {
			String[] value = sloop.split(" ");
			int band = Integer.parseInt(value[value.length - 1]);
			String[] path = Arrays.copyOfRange(value, 0, value.length - 1);
			String[] tmpArr = new String[path.length + 1];
			for (int i = 0; i < path.length; i++) {
				tmpArr[tmpArr.length - 2 - i] = path[i];
			}
			for (int i = 0; i < g.consumernodenum; i++) {
				Consumer c = g.consumernodeCollection.get(i);
				if (tmpArr[tmpArr.length - 2].equals(c.networkid + "")) {
					tmpArr[tmpArr.length - 1] = c.id + "";
				}
			}
			String pathTmp = "";
			for (int i = 0; i < tmpArr.length; i++) {
				pathTmp += tmpArr[i] + " ";
			}
			pathTmp += band;
			System.out.println(pathTmp);
			results.add(pathTmp);
		}
		results.add(0, "");
		results.add(0, (results.size() - 1) + "");
		long end = System.currentTimeMillis();
		System.out.println("\n" + (end - start));

		String[] resultsArr = new String[results.size()];
		for (int i = 0; i < results.size(); i++) {
			resultsArr[i] = results.get(i);
		}
		return resultsArr;
	}
}
