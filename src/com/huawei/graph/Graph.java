package com.huawei.graph;

import java.util.*;

public  class Graph {
	public ArrayList<Vertex> networknodeCollection = new ArrayList<Vertex>();
	public HashMap<Integer,Integer> consumernodeCollection = new HashMap<Integer,Integer>();
	public long networknodenum, consumernodenum, networkedgenum,servercost;
	
	public Vertex FirstAdjRex(Vertex v){
    	if(v != null){
    		Edge e = this.networknodeCollection.get((int)v.id).firstEdge;
    		if(e != null){
    			if(e.iVertex.equals(v)){
    				return e.jVertex;
    			}else if(e.jVertex.equals(v)){
    				return e.iVertex;
    			}
    		}
    		return null;
    	}
    	return null;
    }
	
    public Vertex NextAdjVex(Vertex v,Vertex w){
    	if(v != null && w != null){
    		Edge e = this.networknodeCollection.get((int)v.id).firstEdge;
    		while(e != null){
    			Vertex i = e.iVertex,j = e.jVertex;
    			if(i.id == v.id){
    				if(j.id == w.id){
    					i = e.iEdge.iVertex;
    					j = e.iEdge.jVertex;
    					return i.id == v.id?j:i;
    				}
    				e = e.iEdge;
    			}else if(j.id == v.id){
    				if(i.id == w.id){
    					i = e.jEdge.iVertex;
    					j = e.jEdge.jVertex;
    					return i.id == v.id?j:i;
    				}
    			}else{
    				break;
    			}
    		}
    		return null;
    	}
    	return null;
    }
    
    public int[]  shortestPath_DIJ(Vertex v,double[] d){
    	long bandrequire = v.bandRequire;
    	Set<Vertex> min_set = new HashSet<Vertex>();
    	Set<Vertex> unknown_set = new HashSet<Vertex>();
    	int[] path = new int[d.length];
    	for(int i = 0;i < d.length;i++){
    		d[i] = Long.MAX_VALUE; 
    		path[i] = -1;
    		unknown_set.add(this.networknodeCollection.get(i));
    	}
    	min_set.add(v);
    	unknown_set.remove(v);
    	path[(int)v.id] = (int)v.id;
    	d[(int)v.id] = 0;
    	Edge e = this.networknodeCollection.get((int)v.id).firstEdge;
    	Edge etmp = e;
    	while(etmp != null){
    		//在这条边上的另一个节点
    		Vertex adjNode = etmp.getAdjNode(v);
    		//v到adjNode的最短
    		d[(int)adjNode.id] = etmp.price/etmp.totalBand;
    		path[(int)adjNode.id] = (int)v.id;
    		etmp = etmp.getNextEdge(v);
    	}
    	
    	for(int i = 0;i < d.length;i++){
    		int index_min = 0;
        	double min = Double.MAX_VALUE;
        	for(int j = 0;j < d.length;j++){
        		if(d[j] < min && !min_set.contains(new Vertex(j))){
        			min = d[j];
        			index_min = j;
        		}
        	}
        	Vertex vtmp = this.networknodeCollection.get(index_min);
        	min_set.add(vtmp);
        	unknown_set.remove(vtmp);
        	//System.out.println(index_min + " : " + min);
            Edge vtmp_edge = vtmp.firstEdge;
            Edge vtmp_tmpedge = vtmp_edge;
            while(vtmp_tmpedge != null){
            	Edge tmp = vtmp_tmpedge;
            	Vertex jVertex = vtmp_tmpedge.jVertex;
            	if(vtmp_tmpedge.jVertex.equals(vtmp)){
            		jVertex = vtmp_tmpedge.iVertex;
                	vtmp_tmpedge = vtmp_tmpedge.jEdge;
            	}else{
            		vtmp_tmpedge = vtmp_tmpedge.iEdge;
            	}
            	if(!min_set.contains(jVertex)){
            		double price = tmp.price/tmp.totalBand + d[(int)vtmp.id];
            		double pre_price = d[(int)jVertex.id];
            		if(price < pre_price){
            			d[(int)jVertex.id] = price;
            			path[(int)jVertex.id] = index_min;
            		}
            	}
            }
    	}
    	return path;
    }
}
