package com.huawei.utils;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import com.huawei.graph.Consumer;
import com.huawei.graph.Graph;
import com.huawei.graph.Vertex;

public class ServerLocate {
	public static void displayPath(String[] contents){
		long start = System.currentTimeMillis();
		Graph g = GraphUtils.getGraphByFile(contents);
		
		double gradient_total = 2;
		double[] scores = new double[(int) g.networknodenum];
		for(Consumer c:g.consumernodeCollection){
			System.out.println("-----begin-----");
			int networkId = (int) c.networkid;
			Vertex vBand = g.networknodeCollection.get(networkId);
			double weight = gradient_total + (double)vBand.bandRequire / (double)500 ;
			gradient_total = gradient_total - 0.1;
//			scores[networkId] = weight;
			
			double[] vertexes = new double[(int)g.networknodenum];
			int[] path = g.shortestPath_DIJ1(vBand, vertexes);
			for(Consumer ctmp:g.consumernodeCollection){
				int value = (int) ctmp.networkid;
				if(value != networkId){
					System.out.print("node " + value + " --> ");
					int vertex_index = networkId;
					double gradient = 0.1;
					while(path[value] != vertex_index){
						scores[path[value]] = gradient * weight;
						gradient = gradient + 0.05;
						System.out.print(path[value] + " --> ");
						value = path[value];
					}
					System.out.println(networkId);
				}
			}
			System.out.println("------end------");
		}
		
		int[] nodecross = new int[(int)g.networknodenum];
		for(int i = 0;i < nodecross.length;i++){
			nodecross[i] = i;
		}
		
		for(int i = 0;i < scores.length;i++){
			int index_tmp = 0;
			int tmp = i;
			for(int j = i+1;j < scores.length;j++){
				if(scores[tmp] < scores[j]){
					tmp = j;
				}
			}
			if(i != tmp){
				int change = nodecross[i];
				nodecross[i] = nodecross[tmp];
				nodecross[tmp] = change;
				
				double double_tmp = scores[tmp];
				scores[tmp] = scores[i];
				scores[i] = double_tmp;
			}
		}

		for(int i = 0;i < scores.length;i++){
			System.out.print(scores[i] + " ");
		}
		System.out.println();
		for(int i = 0;i < nodecross.length;i++){
			System.out.print(nodecross[i] + " ");
		}
		
		ArrayList<Integer> candidateIndex  = new ArrayList<Integer>();
		Set<Integer> candidateNode = new TreeSet<Integer>();
		for(int i = 0;i <scores.length  && scores[i] > 0.0;i++){
			candidateIndex.add(nodecross[i]);
			System.out.print(nodecross[i] + " ");
		}
		for(int i = 0;i < candidateIndex.size() - 6;i++){
			candidateNode.add(candidateIndex.get(i));
		}
		System.out.println("\nServer Num:" + candidateNode.size());
		
		System.out.println();
		for(Consumer c:g.consumernodeCollection){
			System.out.println("--path begin--");
			g.consumerToCandidateNode(g.networknodeCollection.get((int)c.networkid),candidateNode);
			System.out.println("--path over--");
		}
		long end = System.currentTimeMillis();
		System.out.println("\n" + (end - start));
	}
}
