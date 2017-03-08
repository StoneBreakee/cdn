package com.huawei.graph;

import java.io.Serializable;

public class Vertex implements Serializable,Comparable<Vertex>{
	private static final long serialVersionUID = 1L;
	public long id;
	public Edge firstEdge;
	public long kind;
	public long bandRequire;
	
	public Vertex() {
		this.id = 0;
		this.firstEdge = null;
		this.kind = 0;
		this.bandRequire = 0;
	}
	
	//netowrknode
	public Vertex(long id) {
		this.id = id;
		this.firstEdge = null;
		this.kind = 0;
		this.bandRequire = 0;
	}

	//consumernode
	public Vertex(long id,long bandRequire){
		this.id = id;
		this.firstEdge = null;
		this.kind = 1;
		this.bandRequire = bandRequire;
	}
	
	@Override
	public boolean equals(Object v){
		if(v instanceof Vertex){
			Vertex x = (Vertex)v;
			return x.id == this.id && x.kind == this.kind;
		}
		return false;
	}
	
	public int hashCode(){
		return (id + "" + kind).hashCode();
	}
	
	public String toString(){
		return id + "";
	}

	@Override
	public int compareTo(Vertex o) {
		Vertex v = (Vertex)o;
		return (int)(this.id - v.id);
	}
}
