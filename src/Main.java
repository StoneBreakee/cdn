import com.huawei.graph.Graph;
import com.huawei.graph.Vertex;
import com.huawei.initialgraph.GraphUtils;

public class Main {
	public static void main(String[] args){
		long start = System.currentTimeMillis();
		Graph g = GraphUtils.getGraphByFile();
		Vertex v = new Vertex(22);
		Vertex u = g.FirstAdjRex(v);
		System.out.println("--" + u);
		Vertex x = g.NextAdjVex(v, u);
		System.out.println("--" + x);
		
		double gradient_total = 2;
		double[] scores = new double[(int) g.networknodenum];
		for(Integer key:g.consumernodeCollection.keySet()){
			System.out.println("-----begin-----");
			int networkId = g.consumernodeCollection.get(key);
			Vertex vBand = g.networknodeCollection.get(networkId);
			double weight = gradient_total + (double)vBand.bandRequire / (double)500 ;
			gradient_total = gradient_total - 0.1;
//			scores[networkId] = weight;
			
			double[] vertexes = new double[(int)g.networknodenum];
			int[] path = g.shortestPath_DIJ(new Vertex(networkId), vertexes);
			for(Integer i:g.consumernodeCollection.keySet()){
				int value = g.consumernodeCollection.get(i);
				if(value != networkId){
					System.out.print("node " + value + " --> ");
					int vertex_index = networkId;
					double gradient = 0.1;
					while(path[value] != vertex_index){
						scores[path[value]] = gradient * weight;
						gradient = gradient + 0.05;
						System.out.print(path[value] + " --> ");
						value = path[value];
					}
					System.out.println(networkId);
				}
			}
			System.out.println("------end------");
		}
		
		for(int i = 0;i < scores.length;i++){
			System.out.println(scores[i]);
		}
		
		int[] nodecross = new int[(int)g.networknodenum];
		for(int i = 0;i < nodecross.length;i++){
			nodecross[i] = i;
		}
		
		for(int i = 0;i < scores.length;i++){
			int index_tmp = 0;
			int tmp = i;
			for(int j = i+1;j < scores.length;j++){
				if(scores[tmp] < scores[j]){
					tmp = j;
				}
			}
			if(i != tmp){
				int change = nodecross[i];
				nodecross[i] = nodecross[tmp];
				nodecross[tmp] = change;
				
				double double_tmp = scores[tmp];
				scores[tmp] = scores[i];
				scores[i] = double_tmp;
			}
		}

		for(int i = 0;i < nodecross.length;i++){
			System.out.println(nodecross[i]);
		}
		long end = System.currentTimeMillis();
		System.out.println("\n" + (end - start));
	}
}
