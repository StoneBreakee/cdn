package com.huawei.initialgraph;

import java.util.*;

import com.huawei.graph.Consumer;
import com.huawei.graph.Edge;
import com.huawei.graph.Graph;
import com.huawei.graph.Vertex;

public class GraphUtils {
	@SuppressWarnings("unchecked")
	public static Graph getGraphByFile() {
    	Graph g = new Graph();
		String[] lines = FileUtil.read("./graphfile/case0.txt", null);
		String[] arr   = lines[0].split(" ");
		g.networknodenum  = Long.parseLong(arr[0]);
		g.networkedgenum  = Long.parseLong(arr[1]);
		g.consumernodenum = Long.parseLong(arr[2]);
		g.servercost = Long.parseLong(lines[2]);
		
    	int index = 4;
    	//initial network node 
    	Set<Vertex> set = new HashSet<Vertex>();
    	for(;!lines[index].equals("");index++){
    		String[] stmp = lines[index].split(" ");
    		//isart,jend,totalBand,price
    		long[] ltmp = new long[stmp.length];
    		for(int i = 0;i < ltmp.length;i++){
    			ltmp[i] = Long.parseLong(stmp[i]);
    		}
    		
    		Vertex v = new Vertex(ltmp[0]);
    		Vertex u = new Vertex(ltmp[1]);
    		Edge e = new Edge(v,u,null,null,ltmp[2],ltmp[3]);
    			
    		addEdgeOnVertex(g, set, v, e);
    		addEdgeOnVertex(g, set, u, e);
    	}
    	Collections.sort(g.networknodeCollection);
    	g.networkedgenum = (index++) - 4;
    	
    	System.out.println(g.networknodeCollection.size());
    	System.out.println(g.networkedgenum);
    	
    	for(;index < lines.length;index++){
    		String[] stmp = lines[index].split(" ");
    		long[] ltmp = new long[stmp.length];
    		//consumernode networknode bandrequire
    		for(int i = 0;i < ltmp.length;i++){
    			ltmp[i] = Long.parseLong(stmp[i]);
    		}
    		
    		long networkId = (int)ltmp[1];
    		Vertex networknode = g.networknodeCollection.get((int)networkId);
    		networknode.bandRequire = ltmp[2];
			Consumer c = new Consumer(ltmp[0],ltmp[1],ltmp[2]);
			g.consumernodeCollection.add(c);
    	}

    	Collections.sort(g.consumernodeCollection, new Comparator<Consumer>() {
			@Override
			public int compare(Consumer c1, Consumer c2) {
				return (int)(c2.bandrequire - c1.bandrequire);
			}
		});

    	return g;
	}
    
	private static void displayEdges(Graph g) {
		Set<Vertex> set = new HashSet<Vertex>();
    	for(Vertex v:g.networknodeCollection){
    		set.add(v);
    		int[] vertex_visited = new int[(int)g.networknodenum];
    		vertex_visited[(int)v.id] = 1;
    		Edge e = v.firstEdge;
    		while(e != null){
    			Vertex j = null;
    			Edge etmp = null;
    			if(e.iVertex.equals(v)){
    				etmp = e.iEdge;
    				j = e.jVertex;
    			}else if(e.jVertex.equals(v)){
    				etmp = e.jEdge;
    				j = e.iVertex;
    			}else{
    				break;
    			}
    			if(vertex_visited[(int)j.id] == 0){
    				if(!set.contains(j)){
    					System.out.println(e);
    				}
    				vertex_visited[(int)j.id] = 1;
    			}
    			e = etmp;
    		}
    	}
	}
	private static void addEdgeOnVertex(Graph g, Set<Vertex> set, Vertex v, Edge e) {
		if(!set.contains(v)){
			set.add(v);
			g.networknodeCollection.add(v);
			v.firstEdge = e;
		}else{
			//拿到v节点的firstEdge
			Vertex vtmp = null;
			for(Vertex vit:g.networknodeCollection){
				if(vit.equals(v)){
					vtmp = vit;
					break;
				}
			}
			
			if(e.iVertex.equals(vtmp)){
				e.iVertex = vtmp;
			}else if(e.jVertex.equals(vtmp)){
				e.jVertex = vtmp;
			}
			
			//将e边连接到v节点相关的边链表的末尾
			Edge etmp = vtmp.firstEdge , ptmp = vtmp.firstEdge;
			while(etmp != null){
				if(etmp.iVertex.equals(v)){
					ptmp = etmp;
					etmp = etmp.iEdge;
				}else if(etmp.jVertex.equals(v)){
					ptmp = etmp;
					etmp = etmp.jEdge;
				}else{
					break;
				}
			}
			if(ptmp.iVertex.equals(v)){
				ptmp.iEdge = e;
			}else if(ptmp.jVertex.equals(v)){
				ptmp.jEdge = e;
			}
			
		}
	}
}
