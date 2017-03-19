package com.huawei.utils;

import com.filetool.util.FileUtil;
import com.sun.org.apache.bcel.internal.generic.INEG;

import java.util.LinkedList;
import java.util.Queue;

import static java.lang.System.out;

/**
 * Created by hadoop on 18/03/17.
 * 多源点多汇点的最大流网络
 * 为多个源点建立一个超级源点，超级源点连接到每个源点上
 * 为多个汇点建立一个超级汇点，每个汇点连接到超级汇点上
 * <p>
 * 在这个赛题中，超级源点连接到每个源点的权重为原源点的网络需求
 * 各个汇点连接到超级汇点的权重为无穷大
 */
public class MultiMaxStream {
    private static final int WHITE = 0, GRAY = 1;

    private int[][] capacity;
    private int[][] res_capacity;
    public int[][] flow;

    private int[] parent;
    private int[] color;

    private int max_flow;
    private int[] min_capacity;

    private Queue<Integer> queue;

    private int source, sink;

    public void readFile(int[] sinks) {
        String[] contents = FileUtil.read("./graphfile/case5.txt", null);

        String[] graphInfos = contents[0].split(" ");
        int vertex_size = Integer.parseInt(graphInfos[0]);
        int edges_size = Integer.parseInt(graphInfos[1]);
        int consumer_size = Integer.parseInt(graphInfos[2]);

        capacity = new int[vertex_size + 2][vertex_size + 2];
        res_capacity = new int[vertex_size + 2][vertex_size + 2];
        flow = new int[vertex_size + 2][vertex_size + 2];

        for (int i = 4; i < edges_size + 4; i++) {
            String[] edgeInfo = contents[i].split(" ");
            int startVertex = Integer.parseInt(edgeInfo[0]);
            int endVertex = Integer.parseInt(edgeInfo[1]);
            int dataflow = Integer.parseInt(edgeInfo[2]);
            int price = Integer.parseInt(edgeInfo[3]);
            capacity[startVertex + 1][endVertex + 1] = res_capacity[startVertex + 1][endVertex + 1] = dataflow;
            capacity[endVertex + 1][startVertex + 1] = res_capacity[endVertex + 1][startVertex + 1] = dataflow;
        }

        for (int i = 5 + edges_size; i < 5 + edges_size + consumer_size; i++) {
            String[] consumerInfo = contents[i].split(" ");
            int consumerNode = Integer.parseInt(consumerInfo[0]);
            int networkNode = Integer.parseInt(consumerInfo[1]);
            int weight = Integer.parseInt(consumerInfo[2]);
            capacity[0][networkNode + 1] = res_capacity[0][networkNode + 1] = weight;
        }

        for (int i = 0; i < sinks.length; i++) {
            capacity[sinks[i] + 1][vertex_size + 1] = res_capacity[sinks[i] + 1][vertex_size + 1] = Integer.MAX_VALUE;
        }

        source = 0;
        sink = vertex_size + 1;
        min_capacity = new int[vertex_size + 2];
        parent = new int[vertex_size + 2];
        color = new int[vertex_size + 2];
        max_flow = 0;
        queue = new LinkedList<Integer>();
    }

    public void maxStream() {
        int[] sinks = new int[]{0, 3, 22};
        readFile(sinks);

        while (BreadFirstSearch(source)) {
            max_flow += min_capacity[sink];
            int v = sink, u;
            while (v != source) {
                u = parent[v];
                try {
                    flow[u][v] += min_capacity[sink];
                    flow[v][u] -= min_capacity[sink];
                    res_capacity[u][v] -= min_capacity[sink];
                    res_capacity[v][u] += min_capacity[sink];
                    v = u;
                } catch (ArrayIndexOutOfBoundsException e) {
                    out.println(u + " " + v);
                }
            }
        }

        out.println("max_flow is " + max_flow);
        display();
    }

    private boolean BreadFirstSearch(int source) {
        for (int i = 0; i < color.length; i++) {
            color[i] = WHITE;
            parent[i] = Integer.MAX_VALUE;
            min_capacity[i] = Integer.MAX_VALUE;
        }

        queue.add(source);
        color[source] = GRAY;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int u = 0; u < min_capacity.length; u++) {
                if (color[u] == WHITE && res_capacity[v][u] > 0) {
                    min_capacity[u] = Math.min(min_capacity[v], res_capacity[v][u]);
                    parent[u] = v;
                    color[u] = GRAY;
                    if (u != sink) {
                        queue.add(u);
                    } else {
                        queue.clear();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void display(){
        for(int i = 0;i < flow[0].length;i++){
            if(flow[0][i] > 0){
                int start = i;
                Queue<Integer> qtmp = new LinkedList<Integer>();
                qtmp.add(start);
                while(!qtmp.isEmpty()){
                    int v = qtmp.poll();
                    for(int j = 1;j < flow[v].length - 1;j++){
                        if(flow[v][j] > 0){
                            System.out.println((v-1) + " -> " + (j-1) + " , flow : " + flow[v][j]);
                            qtmp.add(j);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        MultiMaxStream mms = new MultiMaxStream();
        mms.maxStream();
    }
}
