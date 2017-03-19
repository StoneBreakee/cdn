package com.huawei.graph;

import java.util.*;

public class Graph {
	public ArrayList<Vertex> networknodeCollection = new ArrayList<Vertex>();
	public ArrayList<Consumer> consumernodeCollection = new ArrayList<Consumer>();
	public long networknodenum, consumernodenum, networkedgenum, servercost;
	public int totalbandrequire;
	
	// 获取v节点的第一个邻接点
	public Vertex FirstAdjRex(Vertex v) {
		if (v != null) {
			Edge e = this.networknodeCollection.get((int) v.id).firstEdge;
			if (e != null) {
				if (e.iVertex.equals(v)) {
					return e.jVertex;
				} else if (e.jVertex.equals(v)) {
					return e.iVertex;
				}
			}
			return null;
		}
		return null;
	}

	// 获取v节点想对于w的下一个邻接点
	public Vertex NextAdjVex(Vertex v, Vertex w) {
		if (v != null && w != null) {
			Edge e = this.networknodeCollection.get((int) v.id).firstEdge;
			while (e != null) {
				Vertex i = e.iVertex, j = e.jVertex;
				if (i.id == v.id) {
					if (j.id == w.id) {
						i = e.iEdge.iVertex;
						j = e.iEdge.jVertex;
						if (i != null && j != null) {
							return i.id == v.id ? j : i;
						}
					}
					e = e.iEdge;
				} else if (j.id == v.id) {
					if (i.id == w.id) {
						i = e.jEdge.iVertex;
						j = e.jEdge.jVertex;
						if (i != null && j != null) {
							return i.id == v.id ? j : i;
						}
					}
					e = e.jEdge;
				} else {
					break;
				}
			}
			return null;
		}
		return null;
	}

	public int[] shortestPath_DIJ(Vertex v, long[] d) {
		// long bandrequire = v.bandRequire;
		Set<Vertex> min_set = new HashSet<Vertex>();
		Set<Vertex> unknown_set = new HashSet<Vertex>();
		int[] path = new int[d.length];
		for (int i = 0; i < d.length; i++) {
			d[i] = Long.MAX_VALUE;
			path[i] = -1;
			unknown_set.add(this.networknodeCollection.get(i));
		}
		min_set.add(v);
		unknown_set.remove(v);
		path[(int) v.id] = (int) v.id;
		d[(int) v.id] = 0;
		Edge e = this.networknodeCollection.get((int) v.id).firstEdge;
		Edge etmp = e;
		while (etmp != null) {
			if (etmp.iVertex.equals(v)) {
				d[(int) etmp.jVertex.id] = etmp.price;
				path[(int) etmp.jVertex.id] = (int) v.id;
				etmp = etmp.iEdge;
			} else if (etmp.jVertex.equals(v)) {
				d[(int) etmp.iVertex.id] = etmp.price;
				path[(int) etmp.iVertex.id] = (int) v.id;
				etmp = etmp.jEdge;
			}
		}

		for (int i = 0; i < d.length; i++) {
			int index_min = 0;
			long min = Long.MAX_VALUE;
			for (int j = 0; j < d.length; j++) {
				if (d[j] < min && !min_set.contains(new Vertex(j))) {
					min = d[j];
					index_min = j;
				}
			}
			Vertex vtmp = this.networknodeCollection.get(index_min);
			min_set.add(vtmp);
			unknown_set.remove(vtmp);
			// System.out.println(index_min + " : " + min);
			Edge vtmp_edge = vtmp.firstEdge;
			Edge vtmp_tmpedge = vtmp_edge;
			while (vtmp_tmpedge != null) {
				Edge tmp = vtmp_tmpedge;
				Vertex jVertex = vtmp_tmpedge.jVertex;
				if (vtmp_tmpedge.jVertex.equals(vtmp)) {
					jVertex = vtmp_tmpedge.iVertex;
					vtmp_tmpedge = vtmp_tmpedge.jEdge;
				} else {
					vtmp_tmpedge = vtmp_tmpedge.iEdge;
				}
				if (!min_set.contains(jVertex)) {
					long price = tmp.price + d[(int) vtmp.id];
					long pre_price = d[(int) jVertex.id];
					if (price < pre_price) {
						d[(int) jVertex.id] = price;
						path[(int) jVertex.id] = index_min;
					}
				}
			}
		}
		return path;
	}

	// 获得v节点的所有邻接边
	public ArrayList<Edge> getAllEdgeOfNode(Vertex v) {
		Edge e = this.networknodeCollection.get((int) v.id).firstEdge;
		Edge etmp = e;
		ArrayList<Edge> result = new ArrayList<Edge>();
		while (etmp != null) {
			result.add(etmp);
			if (etmp.jVertex.equals(v)) {
				etmp = etmp.jEdge;
			} else {
				etmp = etmp.iEdge;
			}
		}
		Collections.sort(result, new Comparator<Edge>() {
			@Override
			public int compare(Edge e1, Edge e2) {
				return (int) (e1.price - e2.price);
			}
		});
		return result;
	}

	// 获取服务器选址的备选节点集合
	public int[] shortestPath_DIJ1(Vertex v, double[] d) {
		Set<Vertex> min_set = new HashSet<Vertex>();
		Set<Vertex> unknown_set = new HashSet<Vertex>();
		int[] path = new int[d.length];
		for (int i = 0; i < d.length; i++) {
			d[i] = Double.MAX_VALUE;
			path[i] = -1;
			unknown_set.add(this.networknodeCollection.get(i));
		}
		min_set.add(v);
		unknown_set.remove(v);
		path[(int) v.id] = (int) v.id;
		d[(int) v.id] = 0;
		Edge e = this.networknodeCollection.get((int) v.id).firstEdge;
		Edge etmp = e;
		while (etmp != null) {
			if (etmp.iVertex.equals(v)) {
				d[(int) etmp.jVertex.id] = etmp.price / etmp.totalBand;
				path[(int) etmp.jVertex.id] = (int) v.id;
				etmp = etmp.iEdge;
			} else if (etmp.jVertex.equals(v)) {
				d[(int) etmp.iVertex.id] = etmp.price / etmp.totalBand;
				path[(int) etmp.iVertex.id] = (int) v.id;

				etmp = etmp.jEdge;
			}
		}

		for (int i = 0; i < d.length; i++) {
			int index_min = 0;
			double min = Double.MAX_VALUE;
			for (int j = 0; j < d.length; j++) {
				if (d[j] < min && !min_set.contains(new Vertex(j))) {
					min = d[j];
					index_min = j;
				}
			}
			Vertex vtmp = this.networknodeCollection.get(index_min);
			min_set.add(vtmp);
			unknown_set.remove(vtmp);
			// System.out.println(index_min + " : " + min);
			Edge vtmp_edge = vtmp.firstEdge;
			Edge vtmp_tmpedge = vtmp_edge;
			while (vtmp_tmpedge != null) {
				Edge tmp = vtmp_tmpedge;
				Vertex jVertex = vtmp_tmpedge.jVertex;
				if (vtmp_tmpedge.jVertex.equals(vtmp)) {
					jVertex = vtmp_tmpedge.iVertex;
					vtmp_tmpedge = vtmp_tmpedge.jEdge;
				} else {
					vtmp_tmpedge = vtmp_tmpedge.iEdge;
				}
				if (!min_set.contains(jVertex)) {
					double price = tmp.price / tmp.totalBand + d[(int) vtmp.id];
					double pre_price = d[(int) jVertex.id];
					if (price < pre_price) {
						d[(int) jVertex.id] = price;
						path[(int) jVertex.id] = index_min;
					}
				}
			}
		}
		return path;
	}

	// 从备选节点获取方案经费消耗,使用拓扑排序
	// v 消费节点对应的网络节点
	public void consumerToCandidateNode(final Vertex vertex, Set<Integer> candidatenode) {
		ArrayList<Edge> visitedEdge = new ArrayList<Edge>();
		Queue<Vertex> queue = new LinkedList<Vertex>();
		queue.add(vertex);
		while (!queue.isEmpty()) {
			final Vertex v = queue.poll();
			if (candidatenode.contains((int) v.id)) {
				System.out.println(v + " --  ");
				continue;
			}
			long bandrequire = v.bandRequire;
			ArrayList<Edge> eList = this.getAllEdgeOfNode(v);
			ArrayList<Vertex> vList = v.getAllAdjNodes(v);

			// 当需求带宽过大时(超过每个邻接带宽吞吐)，按照带宽价格从小到大依次选取;否则，选择带宽带宽吞吐最接近的价格最便宜的路径
			long maxedgeband = 0;
			Iterator<Edge> it = eList.iterator();
			while (it.hasNext()) {
				Edge e = it.next();
				long totalBand = e.totalBand;
				long usage = e.usage.get(v + "->" + e.getAdjVertex(v));
				long avail = totalBand - usage;
				if (e.visited) {
					it.remove();
					continue;
				}
				if (avail <= 0.0) {
					it.remove();
					vList.remove(e.getAdjVertex(v));
					continue;
				}
				if (maxedgeband < avail) {
					maxedgeband = avail;
				}
			}
			if (maxedgeband < bandrequire) {
				Collections.sort(eList, new Comparator<Edge>() {
					@Override
					public int compare(Edge e1, Edge e2) {
						// TODO Auto-generated method stub
						Vertex v1 = e1.getAdjVertex(v);
						long band1 = e1.usage.get(v + "->" + v1);
						long avail1 = e1.totalBand - band1;
						Vertex v2 = e2.getAdjVertex(v);
						long band2 = e2.usage.get(v + "->" + v2);
						long avail2 = e2.totalBand - band2;
						return (int) (avail2 - avail1);
					}
				});
				long bandrequiretmp = bandrequire;
				for (Edge e : eList) {
					long totalBand = e.totalBand;
					long usage = e.usage.get(v + "->" + e.getAdjVertex(v));
					long avail = totalBand - usage;
					if (bandrequiretmp > 0) {
						if (bandrequiretmp < avail) {
							e.usage.put(v + "->" + e.getAdjVertex(v), (int) (usage + bandrequiretmp));
							e.getAdjVertex(v).bandRequire = bandrequiretmp;
						} else {
							// 更新路径带宽吞吐
							e.usage.put(v + "->" + e.getAdjVertex(v), (int) totalBand);
							// 将带宽需求传递下去
							e.getAdjVertex(v).bandRequire = avail;
						}
						System.out.println(v + " --> " + e.getAdjVertex(v));
						queue.add(e.getAdjVertex(v));
						e.visited = true;
						visitedEdge.add(e);
					} else {
						break;
					}
					bandrequiretmp = bandrequiretmp - avail;
				}
			} else {
				Iterator<Edge> eit = eList.iterator();
				while (eit.hasNext()) {
					Edge e = eit.next();
					long totalBand = e.totalBand;
					long usage = e.usage.get(v + "->" + e.getAdjVertex(v));
					long avail = totalBand - usage;
					if (avail < bandrequire) {
						eit.remove();
					}
				}
				Collections.sort(eList, new Comparator<Edge>() {

					@Override
					public int compare(Edge e1, Edge e2) {
						// TODO Auto-generated method stub
						Vertex v1 = e1.getAdjVertex(v);
						long band1 = e1.usage.get(v + "->" + v1);
						long avail1 = e1.totalBand - band1;
						Vertex v2 = e2.getAdjVertex(v);
						long band2 = e2.usage.get(v + "->" + v2);
						long avail2 = e2.totalBand - band2;
						return (int) (avail1 - avail2);
					}
				});
				boolean flag = false;
				for (Edge e : eList) {
					if (candidatenode.contains((int) (e.getAdjVertex(v).id))) {
						long usage = e.usage.get(v + "->" + e.getAdjVertex(v));
						if (e.totalBand - usage > v.bandRequire) {
							e.usage.put(v + "->" + e.getAdjVertex(v), (int) (usage + bandrequire));
							flag = true;
							queue.add(e.getAdjVertex(v));
						}
						break;
					}
				}
				if (flag) {
					continue;
				} else {
					Edge e = eList.get(0);
					queue.add(e.getAdjVertex(v));
					e.visited = true;
					System.out.println(v + " --> " + e.getAdjVertex(v));
					long usage = e.usage.get(v + "->" + e.getAdjVertex(v));
					e.usage.put(v + "->" + e.getAdjVertex(v), (int) (usage + bandrequire));
					e.getAdjVertex(v).bandRequire = bandrequire;
				}
			}

		}
		for (Edge e : visitedEdge) {
			e.visited = false;
		}
	}

	// private Vertex unknown(Vertex vertex ,Set<Vertex> minset){
	// ArrayList<Edge> eList = this.getAllEdgeOfNode(vertex);
	// long maxBand = getMaxBand(vertex ,eList);
	// if(maxBand < vertex.bandRequire){
	//
	// }else{
	//
	// }
	// return null;
	// }

	public long getMaxBand(Vertex vertex, ArrayList<Edge> eList) {
		long max = Long.MIN_VALUE;
		for (Edge e : eList) {
			Vertex adj = e.getAdjVertex(vertex);
			long band = e.totalBand;
			long usage = e.usage.get(vertex + "->" + adj);
			if (max < band - usage) {
				max = band - usage;
			}
		}
		return max;
	}

	public long getMaxBand(Vertex vertex, ArrayList<Edge> eList,Set<Vertex> minset) {
		long max = Long.MIN_VALUE;
		for (Edge e : eList) {
			Vertex adj = e.getAdjVertex(vertex);
			if(minset.contains(adj)){
				continue;
			}
			long band = e.totalBand;
			long usage = e.usage.get(vertex + "->" + adj);
			if (max < band - usage) {
				max = band - usage;
			}
		}
		return max;
	}
	
	public long getSumBand(Vertex vertex, ArrayList<Edge> eList, Vertex v1) {
		long sum = 0, exception = 0;
		for (Edge e : eList) {
			Vertex adj = e.getAdjVertex(vertex);
			long band = e.totalBand;
			long usage = e.usage.get(vertex + "->" + adj);
			if (v1 != null && adj.equals(v1)) {
				exception = band - usage;
			}
			sum += band - usage;
		}
		return sum - exception;
	}

	//通过最短路径计算经费
	public PathNode consumerToCandiateNodeByShortest(Vertex vertex, final Set<Integer> candidatenode) {
		PathNode root = new PathNode(vertex.id,vertex.bandRequire,0);
		Queue<Vertex> queue = new LinkedList<Vertex>();
		Queue<PathNode> queuePath = new LinkedList<PathNode>();
		Set<Vertex> minset = new HashSet<Vertex>();
		long[] distance = new long[(int) this.networkedgenum];
		int[] path = new int[(int) this.networkedgenum];
		for (int i = 0; i < path.length; i++) {
			distance[i] = Long.MAX_VALUE;
			path[i] = -1;
		}
		distance[(int) vertex.id] = 0;
		path[(int) vertex.id] = (int) vertex.id;
		queue.add(vertex);
		queuePath.add(root);
		minset.add(vertex);
		while (!queue.isEmpty()) {
			final Vertex v = queue.poll();
			final PathNode node = queuePath.poll();
			if (candidatenode.contains((int) v.id)) {
				System.out.println(v.id + "--> right");
				continue;
			}
			long maxBand = getMaxBand(v, getAllEdgeOfNode(v),minset);
			long sumBand = getSumBand(v, getAllEdgeOfNode(v), null);
			ArrayList<Edge> eList = getAllEdgeOfNode(v);
			if (v.bandRequire < maxBand) {
				Collections.sort(eList, new Comparator<Edge>() {

					@Override
					public int compare(Edge e1, Edge e2) {
						Vertex v1 = e1.getAdjVertex(v);
						long usage1 = e1.usage.get(v + "->" + v1);

						Vertex v2 = e2.getAdjVertex(v);
						long usage2 = e2.usage.get(v + "->" + v2);
						if (candidatenode.contains((int) v1.id) && !candidatenode.contains((int) v2.id)) {
							return -1;
						} else if (!candidatenode.contains((int) v1.id) && candidatenode.contains((int) v2.id)) {
							return 1;
						} else {
							return (int) ((e1.totalBand - usage1) - (e2.totalBand - usage2));
						}
					}
				});
			}else{
				Collections.sort(eList, new Comparator<Edge>() {

					@Override
					public int compare(Edge e1, Edge e2) {
						Vertex v1 = e1.getAdjVertex(v);
						long usage1 = e1.usage.get(v + "->" + v1);

						Vertex v2 = e2.getAdjVertex(v);
						long usage2 = e2.usage.get(v + "->" + v2);
						if (candidatenode.contains((int) v1.id) && !candidatenode.contains((int) v2.id)) {
							return -1;
						} else if (!candidatenode.contains((int) v1.id) && candidatenode.contains((int) v2.id)) {
							return 1;
						} else {
							return (int) (-(e1.totalBand - usage1) + (e2.totalBand - usage2));
						}
					}
				});
			}
			
			if (v.bandRequire < sumBand) {
				long bandrequire = v.bandRequire;
				for (Edge e : eList) {
					Vertex adjV = e.getAdjVertex(v);
					if (bandrequire > 0) {
						long totalBand = 0;
						long costBand = 0;
						long vertexrequire = adjV.bandRequire;
						if(candidatenode.contains((int)adjV.id)){
							vertexrequire = 0;
						}
						if (bandrequire > e.totalBand) {
							totalBand = vertexrequire + e.totalBand;
							costBand = e.totalBand;
						} else {
							totalBand = bandrequire + vertexrequire;
							costBand = bandrequire;
						}
						boolean walk = false;
						int usagetmp = e.usage.get(v + "->" + adjV);
						if (costBand <= e.totalBand - usagetmp) {
							walk = true;
						}
						if (totalBand <= getSumBand(adjV, getAllEdgeOfNode(adjV), v) && walk) {
							long distancecost = distance[(int) v.id] + totalBand * e.price;
							if (distancecost < distance[(int) adjV.id] || candidatenode.contains((int)adjV.id)) {
								PathNode tmpnode = new PathNode(adjV.id, costBand, e.price);
								node.son.add(tmpnode);
								bandrequire = bandrequire - e.totalBand;
								System.out.println(v.id + "-->" + adjV.id + " weight:" + costBand);
								distance[(int) adjV.id] = distancecost;
								path[(int) adjV.id] = (int) v.id;
								queue.add(adjV);
								queuePath.add(tmpnode);
								int usage = e.usage.get(v + "->" + adjV);
								e.usage.put(v + "->" + adjV, (int) (usage + costBand));
								adjV.bandRequire += costBand;
							}
						}
					}
				}
			}
		}
		long[] cost = PathNode.getSumBandCost(root);
		System.out.println(cost[0] + " , " + cost[1]);
		if(cost[0] == vertex.bandRequire){
			return root;
		}else{
			return null;
		}
	}
}
