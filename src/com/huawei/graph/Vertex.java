package com.huawei.graph;

import java.io.Serializable;
import java.util.ArrayList;

public class Vertex implements Serializable,Comparable<Vertex>{
	private static final long serialVersionUID = 1L;
	public long id;
	public Edge firstEdge;
	public long kind;
	public long bandRequire;
	//该点的邻接边的个数
	public long edges;
	
	public Vertex() {
		this.id = 0;
		this.firstEdge = null;
		this.kind = 0;
		this.bandRequire = 0;
		this.edges = 0;
	}
	
	//netowrknode
	public Vertex(long id) {
		this.id = id;
		this.firstEdge = null;
		this.kind = 0;
		this.bandRequire = 0;
		this.edges = 0;
	}

	//consumernode
	public Vertex(long id,long bandRequire){
		this.id = id;
		this.firstEdge = null;
		this.kind = 1;
		this.bandRequire = bandRequire;
		this.edges = 0;
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

	//获得该点的所有邻接点
	public ArrayList<Vertex> getAllAdjNodes(Vertex v){
        ArrayList<Vertex> list = null;
        if(v.firstEdge != null){
            list = new ArrayList<Vertex>();
            Edge e = v.firstEdge;
            while(e != null){
                if(e.iVertex.equals(v)){
                    list.add(e.jVertex);
                    e = e.iEdge;
                }else{
                    list.add(e.iVertex);
                    e = e.jEdge;
                }
            }
        }
        return list;
    }
	
	@Override
	public Object clone(){
		Vertex v = null;
		try{
			v = (Vertex)super.clone();
			v.firstEdge = firstEdge;
			return v;
		}catch(CloneNotSupportedException e){
			
		}
		return null;
	}
}
