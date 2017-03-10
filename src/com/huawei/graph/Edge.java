package com.huawei.graph;

import java.util.HashMap;

public class Edge {
	public Vertex iVertex,jVertex;
	public Edge iEdge,jEdge;
	public long totalBand;
	public long price;
	public HashMap<String,Integer> usage = null;
	public boolean visited = false;
	
	public Edge() {
		super();
		this.iVertex = null;
		this.jVertex = null;
		this.iEdge = null;
		this.jEdge = null;
		this.totalBand = 0;
		this.price = 0;
		this.usage = null;
	}
	
	public Edge(Vertex iVertex, Vertex jVertex, Edge iEdge, Edge jEdge, long totalBand, long price) {
		super();
		this.iVertex = iVertex;
		this.jVertex = jVertex;
		this.iEdge = iEdge;
		this.jEdge = jEdge;
		this.totalBand = totalBand;
		this.price = price;
		this.usage = new HashMap<String,Integer>();
		usage.put(iVertex+"->"+jVertex, 0);
		usage.put(jVertex+"->"+iVertex, 0);
	}
	
	//Edge得到v的邻接顶点
	public Vertex getAdjVertex(Vertex v){
		if(this.iVertex.equals(v)){
			return this.jVertex;
		}else if(this.jVertex.equals(v)){
			return this.iVertex;
		}else{
			return null;
		}
	}
	
	@Override
	public String toString(){
		return this.iVertex.id + " " + this.jVertex.id + " " + this.totalBand + " " + this.price;
	}
	
	@Override
	public Object clone(){
		Edge e = null;
		try{
			e = (Edge)super.clone();
			e.iVertex.firstEdge = iVertex.firstEdge;
			e.jVertex.firstEdge = jVertex.firstEdge;
			return e;
		}catch(CloneNotSupportedException ex){
			
		}
		return null;
	}
	
}
