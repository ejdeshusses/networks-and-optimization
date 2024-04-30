package ejd;

import ejd.NetworkGraph.Edge;

/** NetworkMetrics is a class of methods to evaluate the network via various key performance indicators (KPI). 
 * 
 * Indicators may include: 
 *  1. utilization: total flows/capacities (percent), edges used (percent), average flow/capacity of nonzero edges (percent). 
 *  2. symmetry: range of flows (unit), range of utilization (percent), 
 *  3. flow statistics: average flows of nonzero edges (unit), number of bottlenecks
 * 
 */
public class NetworkMetrics {
    Edge[][] graph;
    int total_nodes; 
    int total_edges; 

    // Might need these later 
    int source; 
    int sink; 
    
    int total_cap; 
    int total_flow; 
    int max_flow; 
    int zero_flow; 

    
    public NetworkMetrics(Edge[][] graph, int source, int sink){
        this.graph = graph; 
        this.source = source; 
        this.sink = sink; 
        this.zero_flow = 0; 

        this.total_nodes = this.graph.length; 
        this.total_edges = 0;

        initializeTotals();
        initializeMaxFlow(); 

    }

    private int initializeMaxFlow(){
        this.max_flow = 0; 
        int check = 0; 
        for(int v = 0; v < this.graph[this.source].length; v++) {
            this.max_flow += this.graph[this.source][v].flow; 
            check += this.graph[this.source][v].capacity; 
        }
        System.out.println("Graph has max flow: ["+this.max_flow+"] and capacity at the source is ["+check+"] . ");
        
        return this.max_flow; 
    }



    private void initializeTotals(){
        this.total_cap = 0; 
        this.total_flow = 0; 

                
        // Iterate throught the capacities and flows of all edges in the network graph
        for (int i = 0; i < this.graph.length; i++) {
            for (int j = 0; j < this.graph[i].length; j++) {
                this.total_cap += this.graph[i][j].capacity; 
                this.total_flow += this.graph[i][j].flow; 
                
                if( this.graph[i][j].capacity > 0) {
                    this.total_edges += 1; 

                    if(this.graph[i][j].flow <= 0){
                        this.zero_flow += 1; 
                    }
                }
            }
        }
        System.out.print("Initialize totals got : total flow ["+this.total_flow+"] and cap ["+this.total_cap+"] .\n" );
    }

    public boolean sink_eq_source(){

        // Calculate the sum of row ==> source out flow
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
        return s_out == t_in;
    }

    public int getMaxFlow(){
        return this.max_flow; 
    }
    public int getTotalCapacity(){
        return this.total_cap; 
    }
    public int getTotalFlow(){
        return this.total_flow; 
    }

    public float totalUtilization(){
        float u = (float) this.total_flow / (float) this.total_cap; 
        // System.out.println("Total utilization over capacity: "+ u +". ");
        return u; 
    }
    
    public float averageFlow(){
        float x = (float) this.total_flow / (float) this.total_edges; 
        // System.out.println("edges counted:  "+ this.total_edges +". ");
        // System.out.println("Average flow per edge: "+ x +". ");
        return x; 
    }




}






    

