package com.huawei.graph;

import java.util.HashMap;

public class Edge {
	public Vertex iVertex,jVertex;
	public Edge iEdge,jEdge;
	public long totalBand;
	public long price;
	public HashMap<String,Integer> usage = null;
	
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
	
	@Override
	public String toString(){
		return this.iVertex.id + " " + this.jVertex.id + " " + this.totalBand + " " + this.price;
	}
	
	//得到同一条边上的另一个节点
	public Vertex getAdjNode(Vertex v){
		if(v != null){
			if(this.iVertex.equals(v)){
				return this.jVertex;
			}else if(this.jVertex.equals(v)){
				return this.iVertex;
			}
		}
		return null;
	}
	
	//获得节点v的下一条邻接边
	public Edge getNextEdge(Vertex v){
		if(v != null){
			if(this.iVertex.equals(v)){
				return this.iEdge;
			}else if(this.jVertex.equals(v)){
				return this.jEdge;
			}
		}
		return null;
	}
}
