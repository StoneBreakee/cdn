package com.huawei.graph;

import java.lang.reflect.Array;
import java.util.*;

public  class Graph {
	public ArrayList<Vertex> networknodeCollection = new ArrayList<Vertex>();
	public ArrayList<Consumer> consumernodeCollection = new ArrayList<Consumer>();
	public long networknodenum, consumernodenum, networkedgenum,servercost;

    //获取v节点的第一个邻接点
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

    //获取v节点想对于w的下一个邻接点
    public Vertex NextAdjVex(Vertex v,Vertex w){
    	if(v != null && w != null){
    		Edge e = this.networknodeCollection.get((int)v.id).firstEdge;
    		while(e != null){
    			Vertex i = e.iVertex,j = e.jVertex;
    			if(i.id == v.id){
    				if(j.id == w.id){
    					i = e.iEdge.iVertex;
    					j = e.iEdge.jVertex;
                        if(i != null && j!= null){
                            return i.id == v.id?j:i;
                        }
    				}
    				e = e.iEdge;
    			}else if(j.id == v.id){
    				if(i.id == w.id){
    					i = e.jEdge.iVertex;
    					j = e.jEdge.jVertex;
                        if(i != null && j!= null) {
                            return i.id == v.id ? j : i;
                        }
    				}
                    e = e.jEdge;
    			}else{
    				break;
    			}
    		}
    		return null;
    	}
    	return null;
    }

    public int[]  shortestPath_DIJ(Vertex v,long[] d){
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
            if(etmp.iVertex.equals(v)){
                d[(int)etmp.jVertex.id] = etmp.price;
                path[(int)etmp.jVertex.id] = (int)v.id;
                etmp = etmp.iEdge;
            }else if(etmp.jVertex.equals(v)){
                d[(int)etmp.iVertex.id] = etmp.price;
                path[(int)etmp.iVertex.id] = (int)v.id;
                etmp = etmp.jEdge;
            }
        }

        for(int i = 0;i < d.length;i++){
            int index_min = 0;
            long min = Long.MAX_VALUE;
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
                    long price = tmp.price + d[(int)vtmp.id];
                    long pre_price = d[(int)jVertex.id];
                    if(price < pre_price){
                        d[(int)jVertex.id] = price;
                        path[(int)jVertex.id] = index_min;
                    }
                }
            }
        }
        return path;
    }

    //获得v节点的所有邻接边
    public ArrayList<Edge> getAllEdgeOfNode(Vertex v){
        Edge e = this.networknodeCollection.get((int)v.id).firstEdge;
        Edge etmp = e;
        ArrayList<Edge> result = new ArrayList<Edge>();
        while (etmp != null){
            result.add(etmp);
            if(etmp.jVertex.equals(v)){
            	etmp = etmp.jEdge;
            }else {
            	etmp = etmp.iEdge;
            }
        }
        Collections.sort(result, new Comparator<Edge>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                return (int)(e1.price - e2.price);
            }
        });
        return result;
    }

    //获取服务器选址的备选节点集合
    public int[]  shortestPath_DIJ1(Vertex v,double[] d){
        Set<Vertex> min_set = new HashSet<Vertex>();
        Set<Vertex> unknown_set = new HashSet<Vertex>();
        int[] path = new int[d.length];
        for(int i = 0;i < d.length;i++){
            d[i] = Double.MAX_VALUE;
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
            if(etmp.iVertex.equals(v)){
                    d[(int)etmp.jVertex.id] = etmp.price / etmp.totalBand;
                    path[(int)etmp.jVertex.id] = (int)v.id;
                etmp = etmp.iEdge;
            }else if(etmp.jVertex.equals(v)){
                    d[(int)etmp.iVertex.id] = etmp.price / etmp.totalBand;
                    path[(int)etmp.iVertex.id] = (int)v.id;

                etmp = etmp.jEdge;
            }
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
                    double price = tmp.price / tmp.totalBand + d[(int)vtmp.id];
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
    
    //从备选节点获取方案经费消耗
    //v 消费节点对应的网络节点
    public void consumerToCandidateNode(final Vertex vertex,Set<Integer> candidatenode){
    	Queue<Vertex> queue = new LinkedList<Vertex>();
    	queue.add(vertex);
    	while(!queue.isEmpty()){
    		final Vertex v = queue.poll();
    		if(candidatenode.contains((int)v.id)){
    			System.out.println(v+" --  ");
    			continue;
    		}
    		long bandrequire = v.bandRequire;
        	ArrayList<Edge> eList = this.getAllEdgeOfNode(v);
        	ArrayList<Vertex> vList = v.getAllAdjNodes(v);
        	
        	//当需求带宽过大时(超过每个邻接带宽吞吐)，按照带宽价格从小到大依次选取;否则，选择带宽带宽吞吐最接近的价格最便宜的路径
        	long maxedgeband = 0;
        	Iterator<Edge> it = eList.iterator();
        	while(it.hasNext()){
        		Edge e = it.next();
        		long totalBand = e.totalBand;
        		long usage = e.usage.get(v + "->" + e.getAdjVertex(v));
        		long avail = totalBand - usage;
        		if(avail <= 0.0){
        			it.remove();
        			vList.remove(e.getAdjVertex(v));
        			continue;
        		}
        		if(maxedgeband < avail){
        			maxedgeband = avail;
        		}
        	}
        	if(maxedgeband < bandrequire){
        		Collections.sort(eList,new Comparator<Edge>() {
    				@Override
    				public int compare(Edge e1, Edge e2) {
    					// TODO Auto-generated method stub
    					Vertex v1 = e1.getAdjVertex(v);
    					long band1 = e1.usage.get(v+"->"+v1);
    					long avail1 = e1.totalBand - band1;
    					Vertex v2 = e2.getAdjVertex(v);
    					long band2 = e2.usage.get(v+"->"+v2);
    					long avail2 = e2.totalBand - band2;
    					return (int)(avail2 - avail1);
    				}
    			});
        		long bandrequiretmp = bandrequire;
        		for(Edge e:eList){
        			long totalBand = e.totalBand;
            		long usage = e.usage.get(v + "->" + e.getAdjVertex(v));
            		long avail = totalBand - usage;
        			if(bandrequiretmp > 0){
                		if(bandrequiretmp < avail){
                			e.usage.put(v + "->" + e.getAdjVertex(v),(int) (usage + bandrequiretmp));
                			e.getAdjVertex(v).bandRequire = bandrequiretmp;
                		}else{
                			//更新路径带宽吞吐
                			e.usage.put(v + "->" + e.getAdjVertex(v),(int) totalBand);
                			//将带宽需求传递下去
                			e.getAdjVertex(v).bandRequire = avail;
                		}
                		System.out.println(v +" --> " + e.getAdjVertex(v));
                		queue.add(e.getAdjVertex(v));
        			}else{
        				break;
        			}
        			bandrequiretmp = bandrequiretmp - avail;
        		}
        	}else{
        		Iterator<Edge> eit = eList.iterator();
        		while(eit.hasNext()){
        			Edge e = eit.next();
            		long totalBand = e.totalBand;
            		long usage = e.usage.get(v + "->" + e.getAdjVertex(v));
            		long avail = totalBand - usage;
        			if(avail < bandrequire){
        				eit.remove();
        			}
        		}
        		Collections.sort(eList,new Comparator<Edge>() {

					@Override
					public int compare(Edge e1, Edge e2) {
						// TODO Auto-generated method stub
						Vertex v1 = e1.getAdjVertex(v);
    					long band1 = e1.usage.get(v+"->"+v1);
    					long avail1 = e1.totalBand - band1;
    					Vertex v2 = e2.getAdjVertex(v);
    					long band2 = e2.usage.get(v+"->"+v2);
    					long avail2 = e2.totalBand - band2;
    					return (int)(avail1 - avail2);
					}
				});
        		boolean flag = false;
        		for(Edge e:eList){
        			if(candidatenode.contains((int)(e.getAdjVertex(v).id))){
        				queue.add(e.getAdjVertex(v));
        				long usage = e.usage.get(v + "->" + e.getAdjVertex(v));
        				e.usage.put(v + "->" + e.getAdjVertex(v),(int)(usage + bandrequire));
        				flag = true;
        				break;
        			}
        		}
        		if(flag){
        			continue;
        		}else{
        			Edge e = eList.get(0);
        			queue.add(e.getAdjVertex(v));
        			System.out.println(v +" --> " + e.getAdjVertex(v));
    				long usage = e.usage.get(v + "->" + e.getAdjVertex(v));
    				e.usage.put(v + "->" + e.getAdjVertex(v),(int)(usage + bandrequire));
    				e.getAdjVertex(v).bandRequire = bandrequire;
        		}
        	}
    	}
    	
    }

	
}
