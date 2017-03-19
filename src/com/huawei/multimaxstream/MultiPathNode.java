package com.huawei.multimaxstream;

import java.util.ArrayList;

import com.huawei.graph.PathNode;

public class MultiPathNode {
	public int parent_node;
	public int self;
	public int dataflow;
	public int price;
	public ArrayList<MultiPathNode> son;
	
	public MultiPathNode(){
		parent_node = 0;
		self = -1;
		dataflow = 0;
		price = 0;
		son = new ArrayList<MultiPathNode>();
	}

	public MultiPathNode(int parent_node, int self, int dataflow, int price) {
		this.parent_node = parent_node;
		this.self = self;
		this.dataflow = dataflow;
		this.price = price;
		son = new ArrayList<MultiPathNode>();
	}
	
	// 输出所有根节点到叶子节点的路径
	public static int getAllPath(MultiPathNode node, StringBuilder str) {
		int cost = 0;
		str.append(node.self + " ");
		if (node.son.size() == 0) {
			str.append(node.dataflow + ",");
		}else{
			String strtmp = str.toString();
			for (MultiPathNode tmp : node.son) {
				cost += tmp.dataflow * tmp.price;
				getAllPath(tmp, str);
				str.append(strtmp);
			}
			str = str.delete(str.length() - strtmp.length(), str.length());
		}
		return cost;
	}
	
	// 输出所有根节点到叶子节点的路径
	public static int getAllPath(MultiPathNode node) {
		int cost = 0;
		if (node.son.size() != 0){
			for (MultiPathNode tmp : node.son) {
				cost += tmp.dataflow * tmp.price;
				getAllPath(tmp);
			}
		}
		return cost;
	}
}
