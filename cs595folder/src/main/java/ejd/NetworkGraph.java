package ejd;

import java.nio.file.*;
// import java.io.File;
import java.io.*;
import java.util.*;

/** For reading and creating a directed graph from the given txt file. 
 * The input file format is : i1, j1, capacity1 // i2, j2, capacity2 // ...
 * 
 * @author elise
 * 
 */

 public class NetworkGraph {

    int source; 
    int sink; 
    int[][] graph;
    
    public Edge[][] capacityMatrix;
    
    
    
    /** NetworkGraph Constructor 
     * @param data is the 2-d int array representation of capacity flows in the network graph 
     * 
     */
    public NetworkGraph(String fileName) {

        try {
            System.out.println("DATA FILE name: "+ fileName);
            int[][] data = get_data_from_file(fileName); 

            this.graph = data; 
            
            
            Edge[][] capacities = new Edge[data.length][data[0].length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    // if(data[i][j] != 0){
                        capacities[i][j] = new Edge(data[i][j]);
                        // }
                    }
            }
            this.capacityMatrix = capacities; 
            // this.source = 1; 
            // this.sink = this.capacityMatrix.length ; // check if length -1
        }
        catch(IOException e) {
            System.err.println(e);
        }
        
        this.source = 0; 
        this.sink = this.capacityMatrix.length -1; // check if length -1 
    }
    
    /** Getter for the flow capacities of the network graph */
    public Edge[][] getCapacityMatrix() {
        return this.capacityMatrix;
    }

    /** For defining an edge as having flow and capacity 
     * @default value of flow is 0.
    */ 
    public class Edge {
        int flow = 0 ;
        int capacity ; 

        /** Constructor to initialize the capacity of the edge. 
         * @param capacity is the flow capacity of the edge.
         */ 
        Edge(int capacity){
            this.capacity = capacity;
            this.flow = 0; 
        }    
        
        /** For updating the flow of the edge 
         * @param delta is the int change in flow, positive or negative
         * @return updated flow, or -1 on error
         */ 
        public int update_flow(int delta) {
            if( (this.flow + delta <= this.capacity) && (this.flow +delta >= 0) ){
                this.flow += delta; 
                return this.flow;
            } else {
                // If flow + delta is greater than capacity or negative, this update is illegal 
                return -1; 
            }     
        }    

        /** toString to get the Edge flow/capacity.  */
        @Override
        public String toString(){
            return this.flow + "/" +this.capacity; 
        }    

    }    

    /** For creating a new edge instance of given capacity. 
     * @return the new edge instance
    */ 
    public Edge addEdge(int cap){
        Edge e = new Edge(cap);
        return e; 
    }    

    /** For printing the  graph in a matrix format. Flow moves from row index to column index. */
    public void print_network(){
        System.out.print("\n" );
        
        // Print the capacities and flows of the edges in the network graph
        for (int i = 0; i < capacityMatrix.length; i++) {
            System.out.print(i + ": [ " );
            for (int j = 0; j < capacityMatrix[i].length; j++) {
                System.out.print(" " + capacityMatrix[i][j].flow +"/"+ capacityMatrix[i][j].capacity + ", ");
                // System.out.print("Capacity: " + capacityMatrix[i][j].capacity);
            }
            System.out.print("] /\n" );
        }
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static int[][] get_data_from_file(String filePath) throws IOException {
        int[][] matrix = readData(filePath);

        // PRINT Matrix if needed
        // for (int[] row : matrix) {
        //     System.out.println(Arrays.toString(row));
        // }
        return matrix;
    }

    public static int[][] readData(String filename) throws IOException {
 
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        Map<String, Integer> nodeIndices = new HashMap<>();
        List<String[]> lines = new ArrayList<>();

        String source = reader.readLine();
        String sink = reader.readLine();
        System.out.println("Source: " + source);
        System.out.println("Sink: " + sink);


        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(", ");
            lines.add(parts);
            if (!nodeIndices.containsKey(parts[0])) {
                nodeIndices.put(parts[0], nodeIndices.size());
            }
            if (!nodeIndices.containsKey(parts[1])) {
                nodeIndices.put(parts[1], nodeIndices.size());
            }
        }
        reader.close();

        int size = nodeIndices.size();
        int[][] data = new int[size][size];
        for (String[] parts : lines) {
            int i = nodeIndices.get(parts[0]);
            int j = nodeIndices.get(parts[1]);
            int capacity = Integer.parseInt(parts[2]);
            data[i][j] = capacity;
        }

        return data;
 
 
        // BufferedReader reader = new BufferedReader(new FileReader(filename));
        // Map<String, Integer> nodeIndices = new HashMap<>();
        // List<String[]> lines = new ArrayList<>();

        // String line;
        // while ((line = reader.readLine()) != null) {
        //     String[] parts = line.split(", ");
        //     lines.add(parts);
        //     if (!nodeIndices.containsKey(parts[0])) {
        //         nodeIndices.put(parts[0], nodeIndices.size());
        //     }
        //     if (!nodeIndices.containsKey(parts[1])) {
        //         nodeIndices.put(parts[1], nodeIndices.size());
        //     }
        // }
        // reader.close();

        // int size = nodeIndices.size();
        // int[][] data = new int[size][size];
        // for (String[] parts : lines) {
        //     int i = nodeIndices.get(parts[0]);
        //     int j = nodeIndices.get(parts[1]);
        //     int capacity = Integer.parseInt(parts[2]);
        //     data[i][j] = capacity;
        // }

        // return data;
    }

    /////////////////////////////////////////////////////

    // public static int[][] read_file(String fileName) {

    //     if(fileName.contains("temp")){
    //         return temp_data(); 
    //     }

    //     // String fileName = "example.txt"; // replace with your file name
    //     try {
    //         String content = new String(Files.readAllBytes(Paths.get(fileName)));
    //         // System.out.println("try: pre print post read. \n");
    //         System.out.println(content);
    //     } catch (IOException e) {
    //         System.out.println("An error occurred while reading the file.");
    //         e.printStackTrace();
    //     }
    //     int[][] data = { {0} }; 
    //     return data; 
    //     // return true; 
    // }


    // public static int[][] temp_data(){

    //     int[][] temp__data = {
    //         // {0, 16, 13, 0, 0, 0}, //0
    //     //   s, A, B, C, D, t 
    //         {0, 10, 10, 0, 0, 0}, //s
    //         {0, 0, 10, 10, 1, 0}, //A
    //         {0, 0, 0, 0, 4, 0}, //B
    //         {0, 0, 0, 0, 3, 2}, //C
    //         {0, 0, 0, 0, 0, 10}, //D
    //         {0, 0, 0, 0, 0, 0}  //t
    //     };
    //     return temp__data; 
    // }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.graph.length; i++) {
            for (int j = 0; j < this.graph[i].length; j++) {
                sb.append(this.graph[i][j]);
                if (j < this.graph[i].length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }



    
    
    public static void main(String[] args) {
        // String file = "temp"; 
        // String file = "data_graphs/sample_1.txt"; 
        String file = "cs595folder/src/main/java/ejd/data_graphs/sample_1.txt"; 

        NetworkGraph net = new NetworkGraph(file); 
        net.print_network();
         
        // net.drawGraph();
        
        // String fileName = "graph_01.txt";
        // boolean status = read_file(file); 
        // System.out.println(status);

    }

}
