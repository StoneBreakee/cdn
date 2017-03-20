package com.huawei.multimaxstream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

public class MaxStreamServerLocate {
	
	public static ArrayList<String> allPath = new ArrayList<String>();
	public static HashMap<Integer,ArrayList<MultiPathNode>> totalAvailPath = new HashMap<Integer,ArrayList<MultiPathNode>>();
	public static ArrayList<Integer[]> costlist = new ArrayList<Integer[]>();
	
	public static String[] displayPath(String[] contents){
		long start = System.currentTimeMillis();
		MultiMaxStream mms1 = new MultiMaxStream();
		mms1.sinks = new int[]{};
		mms1.readFile(contents);
	    ArrayList<Integer> serverlist = new ArrayList<Integer>();
	    for(Integer inte:mms1.netnodeToConsumer.keySet()){
	    	serverlist.add(inte);
		}
		
	    //遍历当前servernode集合的cost最小值，如果存在，则继续遍历servernode少一的每种情况
	    while(getAvailPathSet(serverlist,contents)){
	    	int cutnode = costlist.get(0)[0];
	    	costlist.clear();
	    	for(int i = 0;i < serverlist.size();i++){
	    		if(serverlist.get(i) == cutnode){
	    			serverlist.remove(i);
	    			break;
	    		}
	    	}
	    	long end = System.currentTimeMillis();
	    	if(end - start > 70 * 1000){
	    		break;
	    	}
	    }
		
	    int min = Integer.MAX_VALUE;
	    ArrayList<MultiPathNode> minlist = new ArrayList<MultiPathNode>();
	    for(Entry<Integer, ArrayList<MultiPathNode>> en:totalAvailPath.entrySet()){
	    	if(min > en.getKey()){
	    		min = en.getKey();
	    		minlist = en.getValue();
	    	}
	    }
	    
	    System.out.println(min);
	    //This is the final path
	    StringBuilder totalPath = new StringBuilder("");
	    for(MultiPathNode node:minlist){
	    	StringBuilder nodepath = new StringBuilder("");
	    	MultiPathNode.getAllPath(node, nodepath);
	    	totalPath.append(nodepath);
	    }
	    
	    String[] pathArray = totalPath.substring(0, totalPath.length() - 1).toString().split(",");
	    String[] pathOutput = new String[contents.length];
	    pathOutput[0] = ""+pathArray.length;
	    pathOutput[1] = "";
//	    pathOutput[pathOutput.length - 1] = "";
	    
	    int start_index = 2;
	    for(String path:pathArray){
	    	String[] tmp = path.split(" ");
	    	String dataflow = tmp[tmp.length - 1];
	    	String str = "";
	    	for(int m= tmp.length - 2;m >= 0;m--){
	    		str += tmp[m] + " ";
	    	}
	    	str += dataflow;
	    	pathOutput[start_index++] = str;
	    }
		return pathOutput;
	}
	
	public static boolean getAvailPathSet(ArrayList<Integer> serverlist,String[] contents){
		MultiMaxStream mms = null;
		int min = Integer.MAX_VALUE;
		ArrayList<MultiPathNode> minPath = new ArrayList<MultiPathNode>();
		for(int i = 0;i < serverlist.size();i++){
			mms = new MultiMaxStream();
			ArrayList<Integer> listtmp = new ArrayList<Integer>();
			for(Integer inte:serverlist){
				listtmp.add(inte);
			}
			int cutnode = listtmp.remove(i);
			
			int increment = 0;
			int[] servers = new int[listtmp.size()];
			for(Integer inte:listtmp){
				servers[increment++] = inte;
			}
			
			mms.sinks = servers;
			mms.readFile(contents);
			
			if(mms.maxStream() == mms.totalbandrequire){
				//System.out.println("i = " + i);
				ArrayList<MultiPathNode> list = mms.getPathTree();
				int totalCost = 0;
				for(MultiPathNode tmp:list){
					totalCost += MultiPathNode.getAllPath(tmp);
				}
				totalCost += mms.servercost * mms.sinks.length;
				System.out.println(totalCost);
				if(totalCost < min){
					minPath = list;
				}
				costlist.add(new Integer[]{cutnode,totalCost});
			}else{
				costlist.add(new Integer[]{cutnode,Integer.MAX_VALUE});
			}
		}
		Collections.sort(costlist, new Comparator<Integer[]>() {

			@Override
			public int compare(Integer[] o1, Integer[] o2) {
				// TODO Auto-generated method stub
				return o1[1] - o2[1];
			}
		});

		if(costlist.get(0)[1] < Integer.MAX_VALUE){
			totalAvailPath.put(costlist.get(0)[1], minPath);
			return true;
		}
		return false;
	}
	
}
