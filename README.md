华为2017软件精英挑战赛:
---20170309---
TODO1：
添加多项式类 -> 计算某个网络节点的最低分流成本值，
e.g. node1的带宽需求为65，与node1邻接的顶点node2，3，4的网络链路的最大带宽分别为45，30，20，价格分别为1,2,3
貌似按单位价格从低到高一次满足就可以了...
策略1：当requireBand过大时，优先选择价格较低的
策略2：当可以满足requireband时，选择链路带宽大的

当有了候选节点集合(CandidateSet)时，剩下的就是遍历出从消费节点到候选节点的路径
思路1:拓扑排序
从消费节点对应的网络节点node1出发，初始化最短路径点集合，初始化队列Queue
   1.选择与该网络节点node1相连接的顶点集合NodeSet1，边集合EdgeSet1
                           在选择顶点时，不能选择存在于ShortestSet中的点，自然就不能选择node1与该点相连的边
                           在选择边时，如果该边的可通过带宽已经用完，也不能选择
   2.当node1带宽需求过大时(大于每一个与其相邻接的边的带宽)，优先选择那些带宽价格低的边；否则，优先选择带宽值大的边
   3.经过Step2得到集合NodeSet2,遍历NodeSet2
         for nodetmp in NodeSet2
            if nodetmp in CandidateSet
                                             该条路径结束，输出
            else 
                                             将nodetmp加入Queue
                                             更新nodetmp的带宽需求，根据node1的带宽需求而定
                                             更新node1与nodetmp相连的边的可通过带宽
               如果NodeSet2的为空证明此路不通，然后。。。
   4.同样的方式(Step1,2,3)应用在Queue中的节点