package ejd;

import java.io.StringWriter;

import org.graphstream.graph.implementations.SingleGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.nio.dot.DOTExporter;

import ejd.NetworkGraph.Edge;


// import org.jgrapht.Graph.*;
// import org.graphstream.graph.implementations.*;

public class DrawGraph {
    int[][] data;
    Graph<Integer, DefaultWeightedEdge> graphD ; 
     
    public DrawGraph(int[][] graph_input){
        this.data = graph_input;        
        // this.data = temp_data();        
        createSimpleDirectedWeightedGraph(data); 
        
    }

    
    public int[][] temp_data(){

        int[][] temp__data = {
            // {0, 16, 13, 0, 0, 0}, //0
        //   s, A, B, C, D, t 
            {0, 10, 10, 0, 0, 0}, //s
            {0, 0, 10, 10, 1, 0}, //A
            {0, 0, 0, 0, 4, 0}, //B
            {0, 0, 0, 0, 3, 2}, //C
            {0, 0, 0, 0, 0, 10}, //D
            {0, 0, 0, 0, 0, 0}  //t
        };
        return temp__data; 
    }

    // public boolean set_graph(){
    //     return true; 
    // }

    
    public void drawGraph() {

        // int[][] adjacencyMatrix = temp_data();
        Graph<Integer, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        // DirectedGraph<Integer, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Add vertices
        for (int i = 0; i < this.data.length; i++) {
            graph.addVertex(i);
        }

        // Add edges
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[i].length; j++) {
                if (this.data[i][j] != 0) {
                    DefaultWeightedEdge edge = graph.addEdge(i, j);
                    graph.setEdgeWeight(edge, this.data[i][j]);
                }
            }
        }
        
        // Export as DOT format
        DOTExporter<Integer, DefaultWeightedEdge> exporter = new DOTExporter<>();
        StringWriter writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        System.out.println(writer.toString());

    }



    public void drawFlowGraph(Edge[][] flow_graph) {

        Graph<Integer, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        // DirectedGraph<Integer, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Add vertices
        for (int i = 0; i < this.data.length; i++) {
            graph.addVertex(i);
        }

        // Add edges
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[i].length; j++) {
                if (flow_graph[i][j].flow != 0) {
                    DefaultWeightedEdge edge = graph.addEdge(i, j);
                    graph.setEdgeWeight(edge, flow_graph[i][j].flow);
                }
            }
        }
        
        // Export as DOT format
        DOTExporter<Integer, DefaultWeightedEdge> exporter = new DOTExporter<>();
        StringWriter writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        System.out.println(writer.toString());

    }

    public Graph<Integer, DefaultWeightedEdge> createSimpleDirectedWeightedGraph(int[][] adjacencyMatrix) {
        Graph<Integer, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Add vertices
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            graph.addVertex(i);
        }

        // Add edges
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                if (adjacencyMatrix[i][j] != 0) {
                    DefaultWeightedEdge edge = graph.addEdge(i, j);
                    graph.setEdgeWeight(edge, adjacencyMatrix[i][j]);
                }
            }
        }
        this.graphD = graph; 

        return graph; 

        // DrawGraph draw = new DrawGraph();

        // draw.visualizeGraph(graph);         

    }
    
    
    
    // public void visualizeGraph(Graph<Integer, DefaultWeightedEdge> graph) {
    public void visualizeGraph() {
        // System.setProperty("org.graphstream.ui", "org.graphstream.ui.swing.util.Display");
        Graph<Integer, DefaultWeightedEdge> graph = this.graphD; 
        System.setProperty("org.graphstream.ui", "swing"); // Use Swing
        org.graphstream.graph.Graph gsGraph = new SingleGraph("Network Graph");
    
        // Add nodes
        for (Integer node : graph.vertexSet()) {
            gsGraph.addNode(node.toString()).setAttribute("ui.label", node.toString());
        }
        
        // Add edges
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            String edgeId = graph.getEdgeSource(edge) + "-" + graph.getEdgeTarget(edge);
            gsGraph.addEdge(edgeId, graph.getEdgeSource(edge).toString(), graph.getEdgeTarget(edge).toString(), true)
            .setAttribute("ui.label", graph.getEdgeWeight(edge));
        }
    
        // Display the graph
        gsGraph.display();
    }
    

    
    
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    
        // DrawGraph parsed = new DrawGraph(); 
        
    
        // int[][] data = parsed.temp_data(); 

        // parsed.createSimpleDirectedWeightedGraph(data);
        int[][] temp__data = {
            // {0, 16, 13, 0, 0, 0}, //0
        //   s, A, B, C, D, t 
            {0, 10, 10, 0, 0, 0}, //s
            {0, 0, 10, 10, 1, 0}, //A
            {0, 0, 0, 0, 4, 0}, //B
            {0, 0, 0, 0, 3, 2}, //C
            {0, 0, 0, 0, 0, 10}, //D
            {0, 0, 0, 0, 0, 0}  //t
        };

        
        DrawGraph draw = new DrawGraph(temp__data);

        draw.visualizeGraph();     
        draw.drawGraph(); 

        // parsed.drawGraph(data);

        // DrawGraph draw = new DrawGraph(); 


        System.out.println("\n -- parsed instance --");
            
    
    }
    
}
