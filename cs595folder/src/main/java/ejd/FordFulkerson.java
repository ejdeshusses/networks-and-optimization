package ejd;

import java.util.Stack;
import java.util.Arrays;


/** 
 * Algorithm to solve for the max flow of a network graph. 
 * @author Elise
 * 
 */

/** 
 * Ford Fulkerson: Augmenting path algorithm using DPS to find max flow solutions. 
 * 
 * @param graph is the network graph adjacency matrix of type Edge[][].
 * @param source is the int index of the source node in the graph.
 * @param sink is the int index of the sink node in the graph.
 * @param explored is the int counter of iterations/paths explored.
 * @param path is the VertexInfo[] of vertices in the current path being explored.
 * 
 */
public class FordFulkerson extends NetworkGraph { 
    Edge[][] graph;
    int source; 
    int sink; 
    int explored; 
    VertexInfo[] path; 
    int[][] input;
    
    /** 
     * @param input is the 2-d int array of the graph data 
     */
    // public FordFulkerson(int[][] input){
    public FordFulkerson(String file){
        // Envoke the super constructer 
        super(file);
        
        // Get data and parse it into the network graph format 
        // NetworkGraph net_graph = new NetworkGraph(file);
        NetworkGraph net_graph = new NetworkGraph(file);

        this.input = net_graph.graph; 
        
        // Initialize graph matrix 
        this.graph = net_graph.capacityMatrix; 

        this.source = net_graph.source; 
        this.sink = net_graph.sink; 
        this.explored = 0; 
        this.path = new VertexInfo[this.graph.length];
        System.out.println(" GOT PATH len: "+ this.graph.length);
        
        System.out.println("Constructed Ford Fulkerson.\n"); 
    }

    /** getSourceIndex 
     * @return the int index of the source in the graph matrix 
     */
    public int getSourceIndex(){
        return this.source; 
    }
    /** getSinkIndex 
     * @return the int index of the sink in the graph matrix 
     */
    public int getSinkIndex(){
        return this.sink; 
    }

    public String getFFString(){
        return this.source + " to "+ this.sink; 
    }

    /** printGraph : For printing the graph in a matrix format. Flow moves from row index to column index. */
    public void printGraph(){
        System.out.print("\n" );
        
        // Print the capacities and flows of the edges in the network graph
        for (int i = 0; i < this.graph.length; i++) {
            System.out.print(i + ": [ " );
            for (int j = 0; j < this.graph[i].length; j++) {
                System.out.print(" " + this.graph[i][j].flow +"/"+ this.graph[i][j].capacity + ", ");
                // System.out.print("Capacity: " + capacityMatrix[i][j].capacity);
            }
            System.out.print("] /\n" );
        }
    }


    public void analyzeGraph(){
        NetworkMetrics metrics = new NetworkMetrics(this.graph, this.source, this.sink);

        System.out.println("Metrics of Network Graph \n -------------------------\n");
        int a = metrics.getMaxFlow();
        int b = metrics.getTotalFlow();
        int c = metrics.getTotalCapacity();
        float d = metrics.averageFlow();
        float e = metrics.totalUtilization(); 
        System.out.println("Max flow: "+a+"\nTotal Flow: "+b+" \nTotal Capacity: "+c+"\nAverage Flow: "+d+"\nTotal Utilization: "+e+" . \n");
    }
    
    
    /** VertexInfo class  
     * @param previous is the index of the last visited node
     * @param forward is the direction of the edge to the previous node
     */
    class VertexInfo {
        int previous; 
        boolean forward; 
        
        public VertexInfo(int previous, boolean forward){
            this.previous = previous; 
            this.forward = forward; 
        }
    }
    

    /** findAugmentingPath DFS for a path
     * @param vertices is the VertexInfo[] array of vertices and edge directions of the path 
     * @return boolean - true if there exists an augmenting path, false otherwise
     */
    public boolean findAugmentingPath(VertexInfo[] vertices) {
        // If print vertices is not null, Error: persisting data in augmenting path.

        // Starting potential augmenting path: Initialize vertices array at the source node
        vertices[this.source] = new VertexInfo(-1, true);

        // Starting potential augmenting path: Initialize path with the first item source
        Stack<Integer> path = new Stack<Integer>(); 
        path.push(this.source); 
        
        while( !path.isEmpty() ){
            int current_node = path.pop(); 
            
            // At node current, iterate through FORWARD edges from current_node
            for (int v = 0; v < this.graph[current_node].length; v++) {
                Edge e = this.graph[current_node][v];
                // check if edge e exists (not null) 
                if (e == null) {
                    continue; 
                }
                // check of i is already visited/in the path 
                if( vertices[v] != null){
                    continue;
                }
                
                // if capacity is not met, there is an edge to augment
                if (e.flow < e.capacity ) {
                    // add the node/edge to the path
                    vertices[v] = new VertexInfo(current_node, true);

                    // check if v is sink and path is complete 
                    if( v == this.sink){
                        return true; 
                    }
                    // if v is not the sink, continue to the next node in the iteration
                    path.push(v); 
                } 
            }
            // if it has not yet returned true, then there is no possible forward edge
        
            // If there is no forward edge, check for backwards edges
            for (int v = 0; v < this.graph[current_node].length; v++) {
                Edge e = this.graph[v][current_node];
                // check if edge e exists (not null) 
                if (e == null) {
                    continue; 
                }
                // check of i is already visited/in the path 
                if( vertices[v] != null){
                    continue;
                }
                
                // if capacity is not met, there is an edge to augment
                if (e.flow > 0 ) {
                    // add the node/edge to the path and bool for BACKWARDS is false
                    vertices[v] = new VertexInfo(current_node, false);

                    // if v is not the sink, continue to the next node in the iteration
                    path.push(v); 
                } 
            }
        }
        // If we get here, there is no possible augment! So return false, and end FF
        return false; 
    }
                
    /** processPath takes the augmenting path and augments it by the max possible amount (int)
     * @param vertices is the VertexInfo array of the vertices and edge directions in the augmenting path
     */
    public void processPath(VertexInfo[] vertices){

        int max_augment = Integer.MAX_VALUE; 
        int v = this.sink; 

        // Step 1: find max vale for min augment (bottleneck) 
        while(v != this.source){
            int u = vertices[v].previous;
            
            // if forwards edge: 
            if(vertices[v].forward){
                if((this.graph[u][v].capacity - this.graph[u][v].flow) < max_augment){
                    max_augment = (this.graph[u][v].capacity - this.graph[u][v].flow);
                }  
            }
            // if backwards edge: 
            else {
                if(( this.graph[v][u].flow) < max_augment){
                    max_augment = (this.graph[v][u].flow);
                }
            }
            // update the new v node 
            v = u; 
        }
        
        v = this.sink; 
        // augment the path -- for each edge: update_flow(max_augment)
        while(v != this.source){
            int u = vertices[v].previous;
            
            // if forwards edge: 
            if(vertices[v].forward){
                // increase flow for forwards edge
                this.graph[u][v].flow += max_augment; 
            } 
            else {
                // decrease flow for backwards edge
                this.graph[v][u].flow -= max_augment; 
            }
            v=u;  
        }
        this.explored +=1 ;
        // clearing old data for the next iterations/steps 
        Arrays.fill(vertices, null);

    }

    /** printPath :  print out vertexInfo[previous vertex, edge direction] 
     * @param vertices is the VertexInfo array of vertices and edge directions in the augmenting path
    */
    public void printPath(VertexInfo[] vertices){
        String response; 
        response = ""; 
        for (int i = 0; i < vertices.length; i++) {
            if( vertices[i] != null){
                response = response + vertices[i].previous + vertices[i].forward + "  " ;
            }       
        }
        if(response == ""){
            response = " none. ";
        }
    }

    /** runMaxFlowFF - the traditional, simplest form of Ford Fulkerson on a directed graph with positive integer weights. 
     * 
     */
    public int runMaxFlowFF(){
        
        // initialize null array vertices
        VertexInfo[] vertices = new VertexInfo[this.graph.length]; 
        System.out.println("runMaxFlowFF>> Instantiated vertex info. "+ vertices.length + ". ");

        // Iterate through augmentations until further augment is not possible. 
        while (findAugmentingPath(vertices)) {
            printPath(vertices);
            processPath(vertices);
            // System.out.println("(loop) Augmented path. " + this.explored +". ");

            // Safety catch, max at 500 iterations
            if(this.explored >= 500){
                System.out.println("\n\n Safety BREAK enacted. ");
                break; 
            }
               
        }

        System.out.println("\nrunMaxFlowFF>> ENDED AUGMENTATION. ");
        // printGraph();

        // double check conservation of flow for source and sink 
        int s_out = 0;
        for (int col = 0; col < this.graph[this.source].length; col++) {
            s_out += this.graph[this.source][col].flow;
        }

        // Calculate the sum of column ==> sink in flow
        int t_in = 0;
        for (int row = 0; row < this.graph.length; row++) {
            t_in += this.graph[row][this.sink].flow;
        }

        // Compare the sums
        if(s_out != t_in){
            System.err.println("Err: Ford Fulkerson has failed. Source and Sink do not have the same flow.");
            return -1; 
        };

        return s_out; 
    }


}    
    
