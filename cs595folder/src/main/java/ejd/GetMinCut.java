package ejd;

import java.util.ArrayList;
import java.util.List;

public class GetMinCut extends FordFulkerson{

    Edge[][] max_graph;
    int source; 
    int sink; 
    // Edge[] maxflow; 
    List<Edge> maxflow; 

    //// edges_itter to remove edges to/from the source/sink

    /** Constructor GetMinCut with super FordFulkerson 
     * ** should change to be able to take in any max flow graph 
    */
    public GetMinCut(String filename){
        super(filename);
        
        // Instantiate a new Ford Fulkerson instance for the graph         
        // FordFulkerson ff = new FordFulkerson(input.graph);
        FordFulkerson ff = new FordFulkerson(filename);
        // Run Ford Fulkerson for Max Flow
        ff.runMaxFlowFF();
        // System.out.println("Instantiated FF. Graph source--> sink: ");
        // System.out.println(ff.getFFString());

        this.max_graph = ff.graph;
        this.source = ff.source; 
        this.sink = ff.sink; 

        // get_max_cap();
        this.maxflow = getMaxFlowEdges(this.max_graph);
        
    }

    public static List<Edge> getMaxFlowEdges(Edge[][] max_graph) {
        List<Edge> maxFlowEdges = new ArrayList<>();
        for (Edge[] edges : max_graph) {
            for (Edge edge : edges) {
                if (edge.flow == edge.capacity) {
                    if (edge.flow > 0){
                        maxFlowEdges.add(edge);
                        System.out.println("max flow edge: " + edge);
                    }
                }
            }
        }
        return maxFlowEdges;
    }

    /** getSCut: get min cut max flow of nearest max capacity edges to source node S 
     * --> return the list of edges? 
     */
    public void getSCut(){
        
    }
    /** getTCut: get min cut max flow of nearest max capacity edges to the target node T 
     * --> return the list of edges? 
     */
    public void getTCut(){
        
    }





    public boolean hasMaxNeighbor(Edge edge){
        // is edge neighors with (other) max flow edges

        return false; 
    }

    public void interdictionPath(Edge edge){
        // update max impact of interdiction path 

    }





    public static void main(String[] args){

        String fileName = "cs595folder/src/main/java/ejd/data_graphs/sample_1.txt"; 
        GetMinCut cut = new GetMinCut(fileName);

        
    }
        


    
}
