package com.huawei.graph;

import java.util.ArrayList;

//记录路径
public class PathNode {
	// 本节点的id
	public long id;
	// 记录父节点到本节点的带宽消耗
	public long band;
	public long price;
	// 本节点通往哪些子节点
	public ArrayList<PathNode> son;

	public PathNode(long id, long band, long price) {
		this.id = id;
		this.band = band;
		this.price = price;
		this.son = new ArrayList<PathNode>();
	}

	@Override
	public String toString() {
		return this.id + " " + this.son.size();
	}

	// 计算所有叶子节点的band总和
	public static long[] getSumBandCost(PathNode node) {
		if (node.son.size() == 0) {
			return new long[] { node.band, 0 };
		}
		long cost[] = new long[2];
		for (PathNode tmp : node.son) {
			if (tmp.son.size() > 0) {
				cost[0] += getSumBandCost(tmp)[0];
				cost[1] += getSumBandCost(tmp)[1] + tmp.band * tmp.price;
			} else {
				cost[0] += tmp.band;
				cost[1] += tmp.band * tmp.price;
			}
		}
		return cost;
	}

	// 输出所有根节点到叶子节点的路径
	public static void getAllPath(PathNode node, StringBuilder str) {
		str.append(node.id + " ");
		if (node.son.size() == 0) {
			str.append(node.band + ",");
		}else{
			String strtmp = str.toString();
			for (PathNode tmp : node.son) {
				getAllPath(tmp, str);
				str.append(strtmp);
			}
			str = str.delete(str.length() - strtmp.length(), str.length());
		}
		
	}
}
