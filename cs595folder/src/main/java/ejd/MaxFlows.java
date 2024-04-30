package ejd;

import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import ejd.FordFulkerson;
import ejd.NetworkGraph.Edge;




public class MaxFlows {
    private List<Run> runs;
    private List<Integer> nodes;
    private List<Integer> maxf;

    public MaxFlows() {
        this.runs = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.maxf = new ArrayList<>();
    }

    public void add(int node, int flow) {
        this.nodes.add(node);
        this.maxf.add(flow);
    }


    /** addRun to List<Run> of MaxFlows instance. 
     * @param graph_filepath is the folder file for graph text file 
     * @param num_n is the n values for creating the graph 
     * @param density is the float density value 
     * @param seed is the random graph gen seed 
     * @param graph_type is the name of the graph 
     */
    public void addRun(String graph_filepath, int num_n, int density, int seed, String graph_type) {
        // Run r = new Run(num_n, maxFlow, density, seed, graph_type);
        Run r = new Run(graph_filepath, num_n, density, seed, graph_type);
        this.runs.add(r); 
        // this.nodes.add(node);
        // this.maxf.add(flow);
    }

    public int getRunLen(){
        return this.runs.size(); 
    }

    public Run getRun(int ind){
        // Run rr = new Run(null, ind, ind, null)
        return this.runs.get(ind);
    }

    public class Run {
        String graphPath ; 
        int num_n ; 
        int maxFlow ; 

        int density;
        int seed; 
        String graph_type; 
        
        /** Constructor to initialize the parameters and info of the FF run. 
         * 
         */ 
        Run(String graph_filepath, int num_n, int density, int seed, String graph_type){
            this.graphPath = graph_filepath; 
            this.num_n =num_n;
            // this.maxFlow = maxFlow; 
            this.density = density;
            this.seed = seed; 
            this.graph_type = graph_type; 
        }

        public void add( int maxFlow){
        // public void add(int num_n, int maxFlow, int density, int seed, String graph_type){
            // this.num_n = num_n;
            this.maxFlow = maxFlow; 
            // this.density = density;
            // this.seed = seed; 
            // this.graph_type = graph_type; 
        }

        @Override
        public String toString(){
            return "n("+ this.num_n + ")-d("+this.density+")-gr("+this.graph_type+")-sd("+this.seed+") --> mf("+this.maxFlow+"). "; 
        } 

        public String dataString(){
            return "["+ this.num_n + ", "+this.density+", "+this.maxFlow+"], "; 
        } 

    }

    public void printNodes() {
        System.out.println(this.nodes);
    }
    public void printMaxFlows() {
        System.out.println(this.maxf);
    }


    public void printRuns() {
        // this.runs.listIterator().toString();
        ListIterator<Run> allRuns = this.runs.listIterator();
        // Run rr = allRuns; 
        while( allRuns.hasNext()){
            System.out.println(allRuns.next().toString());
        }
    }

    public void printDatas() {
        // this.runs.listIterator().toString();
        ListIterator<Run> allRuns = this.runs.listIterator();
        // Run rr = allRuns; 
        while( allRuns.hasNext()){
            System.out.println(allRuns.next().dataString());
        }
    }

    

    /** Create folder matrix for input folder paths  */
    public static String[][] createFolderMatrix(String filePath) {
        File topFolder = new File(filePath);
        System.out.println(">> CREATE MATRIX: topfolder "+topFolder.toString());
        // System.out.println(">> CREATE MATRIX: list-- "+topFolder.listFiles());
    
        File[] folders = topFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
    
        String[][] graphs_one = new String[folders.length][];
    
        for (int i = 0; i < folders.length; i++) {
            System.out.println(">> CREATE MATRIX: folders-- "+folders[i].toString());
    
            File[] files = folders[i].listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isFile();
                }
            });
            graphs_one[i] = new String[files.length];
    
            for (int j = 0; j < files.length; j++) {
                graphs_one[i][j] = folders[i].getName() + "/" + files[j].getName();
            }
        }
    
        return graphs_one;
    }

    
    /** Get run /configuration info from file name and path */
    public int get_density_info(String foldername){
        // get number between density and _
        String[] info = foldername.split("density");
        String density = info[1];
        int den = Integer.parseInt(density.split("_")[0]);

        // for each r
        return den;
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
    
    


    public static void main(String[] args){

        // Get input data in the input format for FF.
        System.out.println("Starting main. \n");

        // keeps track of the max flows of the generated graphs 
        MaxFlows mf = new MaxFlows();

        // Data graphs folder path for all generated graphs 
        String folder = "cs595folder/src/main/java/ejd/data_graphs/sample_graphs/"; 

        // Set to grid or to netgen
        // String graphFolder = "grid_graph/";
        String graphFolder = "net_graph/";

        String[][] testing_folder = createFolderMatrix((folder+graphFolder));
        // System.out.println("\n====TESTING FOLDER : \n"+testing_folder+"\n ... + [0][0] "+testing_folder[0][0]);
                
        for( int i = 0; i < testing_folder.length ; i ++ ){
            for( int j = 0; j < testing_folder[i].length ; j ++){
                // get number between density and _
                String[] info = testing_folder[i][j].split("density");
                String density = info[1];
                int den = Integer.parseInt(density.split("_")[0]);

                // for each run, add the file path, num nodes, seed, and graph type 
                mf.addRun((folder+graphFolder+testing_folder[i][j]), (i+2), den, (j+1), graphFolder);
            }
        }

        // File top_dir = new File(folder);
        // String[] file_list = top_dir.list();
        // System.out.println("GOT FILE LIST: "+file_list);

        for(int r = 0; r < mf.getRunLen() ; r++ ){
            Run temp = mf.getRun(r); 
            // System.out.println(">> got temp: "+ temp.toString());
            // System.out.println(">> got temp graph : "+ temp.graphPath);
            
            // Instantiate a new Ford Fulkerson instance for the graph         
            // FordFulkerson ff = new FordFulkerson(input.graph);
            FordFulkerson ff = new FordFulkerson(temp.graphPath);
            System.out.println(ff.getFFString());
            
            // Run Ford Fulkerson for Max Flow
            int maxFlow = ff.runMaxFlowFF();
            mf.add(ff.sink, maxFlow);

            // INTERDICTION 
            List<Edge> allMaxflow = mf.getMaxFlowEdges(ff.capacityMatrix);
            // random pick edge from allMaxflow, set edge capacity to 0

            mf.getRun(r).add(maxFlow);

            // NetworkMetrics mets = new NetworkMetrics(ff.graph, ff.source, ff.sink);
            // System.out.println(" Network metrics: conservation of flow--- "+ mets.sink_eq_source());
            // System.out.println(" Network metrics: total capacity--- "+ mets.total_cap );

            // DrawGraph draw = new DrawGraph(ff.input);
            // draw.drawFlowGraph(ff.graph);
            // System.out.println("  post flow graph xxxX ");
            // // draw.drawGraph();
            // draw.visualizeGraph();

            ff = null;
        }

        // For moving data to other places -- [n, density, mf]
        mf.printRuns();
        mf.printDatas();

    }
}
