/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Random;
import java.util.Comparator;
import java.util.Arrays;
/**
 *  The <tt>Graph</tt> class represents an undirected graph of vertices
 *  named 0 through <em>V</em> - 1.
 *  It supports the following two primary operations: add an edge to the graph,
 *  iterate over all of the vertices adjacent to a vertex. It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed array of {@link Bag} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the vertices adjacent to a given vertex, which takes
 *  time proportional to the number of such vertices.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/41undirected">Section 4.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class Graph {
    private int V;
    private int MxNId; // maximum node id
    private int E;
    private Bag<Integer>[] adj;
    private int S;   // sample size
    private int sampleNumber; // used to store id of random node which is used as starting node
    private int degInS [];
    private boolean inS [];
    private boolean inF [];
    private int countS ;
    
    /**
     * Initializes an empty graph with <tt>V</tt> vertices and 0 edges.
     * param V the number of vertices
     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
     */
    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[MxNId];
        for (int v = 0; v < adj.length; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    /**
     * Initializes a new graph that is a deep copy of <tt>G</tt>.
     * @param G the graph to copy
     */
    public Graph(Graph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.MxNId(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<Integer> reverse = new Stack<Integer>();
            for (int w : G.adj[v]) {
                reverse.push(w);
            }
            for (int w : reverse) {
                adj[v].add(w);
            }
        }
    }
    
    public Graph(String filename,  int sampleNumber){
        Set<Integer> vertexSet = new HashSet<Integer>();
        ArrayList<String> edgeList= new ArrayList<String>();
        this.V = 0;
        this.E = 0;
        this.sampleNumber = sampleNumber;
        this.MxNId = 0;
        this.S = 0;
        //Graph G = new Graph(1);
        try{
            FileReader filein = new FileReader(filename);
            BufferedReader graphFile = new BufferedReader(filein);
            String inLine;
            while( (inLine = graphFile.readLine()) != null){
                String[] parts = inLine.split("\\s");
                try{
                    if(parts.length < 2){
                        System.err.println("Line Format is wrong: Skipping " + inLine);
                        continue;
                        }
                    for (String part : parts){
                        vertexSet.add(Integer.parseInt(parts[0]));
                        vertexSet.add(Integer.parseInt(parts[1]));                    
                    }
                    edgeList.add(parts[0]+" "+parts[1]);   
                    if( this.MxNId < Integer.parseInt(parts[0]))
                    	this.MxNId = Integer.parseInt(parts[0]);
                    if( this.MxNId < Integer.parseInt(parts[1]))
                       	this.MxNId = Integer.parseInt(parts[1]);
                                
                }
                catch( NumberFormatException e ){
                    System.err.println("Line Format is wrong: Skipping " + inLine); 
                }
            }
            System.out.println("Number of vertices is " + vertexSet.size());
            this.V = vertexSet.size();
            this.E = edgeList.size();
            this.MxNId ++;
            //this.gPrime = new boolean[MxNId];
            this.inF = new boolean[MxNId];
            this.inS = new boolean[MxNId];
            this.degInS = new int[MxNId];
            
            this.adj = (Bag<Integer>[]) new Bag[this.MxNId+1];
            for (int v : vertexSet) {
                adj[v] = new Bag<Integer>();
            }
            for(String ed : edgeList){
                String[] parts = ed.split("\\s");
                try{
                    this.addEdge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                }
                catch( NumberFormatException e ){
                    System.err.println("Line Format is wrong: Skipping " + inLine); 
                }
            }
            //G = Gtemp;
           // System.out.println("Num Vertices and Edges in G = " + this.V() + "  " + this.E() );
        }
        catch( IOException e ){
            System.err.println( e );
        }
        
    }

    /**
     * Returns the number of vertices in the graph.
     * @return the number of vertices in the graph
     */
	 
    public int V() {
        return V;
    }

    /**
     * Returns the max Node Id of graph.
     * @return the max Node Id of graph
     */
	 
    public int MxNId() {
        return MxNId;
    }
    /**
     * Returns the number of edges in the graph.
     * @return the number of edges in the graph
     */
    public int E() {
        return E;
    }

    /**
     * Adds the undirected edge v-w to the graph.
     * @param v one vertex in the edge
     * @param w the other vertex in the edge
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= v < V and 0 <= w < V
     */
    public void addEdge(int v, int w) {
        if (v < 0 ) throw new IndexOutOfBoundsException();
        if (w < 0 ) throw new IndexOutOfBoundsException();
        E++;
        adj[v].add(w);
        adj[w].add(v);
    }

    /**
     * Returns the vertices adjacent to vertex <tt>v</tt>.
     * @return the vertices adjacent to vertex <tt>v</tt> as an Iterable
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Integer> adj(int v) {
        if (v < 0 || v >= MxNId) throw new IndexOutOfBoundsException();
        return adj[v];
    }

    /**
     * Returns a string representation of the graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *    followed by the <em>V</em> adjacency lists
     */
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < MxNId; v++) {
        	if(adj[v] == null)
        		continue;
            s.append(v + ": ");
            for (int w : adj[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * input parameter S is maximum sample size
     */
    
    public void reset( int S){
        this.S = S;
    }
    
    public int [] initializeS(int option ,int size){
    	/*
    	 * this function will assign some nodes to empty set S
    	 * so that we proceed in Sample selection
    	 * */
    	Random rand = new Random();
    	int [] a = new int[size];
    	for( int i = 0 ; i < size ; i++){
    		a[i] = rand.nextInt(MxNId);
    	}
    	
		int currentMax = 0;
		int degree = 0;
		for( int i = 0 ; i < size ; i ++){
			if(adj[a[i]] == null)
				continue;
			if( adj[a[i]].size() > degree){
				currentMax = a[i];
				degree = adj[a[i]].size();	
			}
		}
		
    	a[0] = currentMax;
    	
    	return(a);
    }
    
    public void expand(int v){
    	inS[v] = true;
    	for(int u : adj[v]){
    		degInS[u] ++;
    		
    		if( !inS[u] && !inF[u]){
    			inF[u] = true;
    		}
    		
    	}
    	inF[v] = false;
       	countS ++;
    	
    }
    
    public void greedy(){
	   	countS = 0;
    	int [] initial = initializeS(0,5000);
    	for( int i = 0 ; i < 1 ; i++){
    		expand(initial[i]);
    	}

    	long startTime = System.currentTimeMillis();

    	while( countS < S){
    		int v = findBest();
    		expand(v);
		
	    	int divisor = 0;
			if(countS < 1000)
				divisor = 100;
			else
				divisor = 1000;
	
			if( (countS - Integer.parseInt(Test.bottomSize)) % divisor  == 0){
				System.out.println(countS);
				writeTime(startTime);
				createEdgeList(inS,"dirty");
			}
		
    	}
    }
    
    /**
     * it inputs node id
     * returns score
     */
    private long findScore(int u){
    	
    	long scoreU = 0;
    		scoreU += 2*degInS[u]*degInS[u];
    		long a = 0;
    		for( int w : adj[u])
    			
    			if(inS[w])
    				a += degInS[w];
    		scoreU += 4 * a * a;
    	
    	return scoreU;
    }
    
    
    
    private int findBest(){
    	
    	int currentBest = 0;
    	long currentMaxScore = 0;
    	for( int u = 0 ; u < inF.length ; u++){
    		if( !inF[u])
    			continue;
    		long scoreU = findScore(u);
    		
    		if(scoreU > currentMaxScore){
    			currentMaxScore = scoreU;
    			currentBest = u;
    		
    		}
    		// score of two nodes is same then we will use degree is highest best
    		else if(scoreU == currentMaxScore){
				if(adj[u].size() > adj[currentBest].size() ){
					currentMaxScore = scoreU;
					currentBest = u;
				}
    		}
    	}
	
    	return currentBest;
    }
    
    public void createEdgeList(boolean [] barray,String name){
	
    	
    	HashMap<Integer,Integer> index = new HashMap<Integer,Integer>();
    	int counter = 0;
    	for( int i = 0 ; i < MxNId ; i ++){
    		if(barray[i])
    			index.put(i, counter ++);
    	}
    	
    	try {
        	//BufferedWriter br = new BufferedWriter(new FileWriter("Sample1-14000.csv"));
        	FileWriter bw = new FileWriter(Test.pathOfCW+Test.str+"/edgelist/"+name+"-"+sampleNumber+"-"+countS+"-edgeList.csv");
            
            for( int i = 0 ; i < MxNId ; i ++){
            	if(barray[i]){
            		
            		for( int nbr : adj[i]){
            			if(barray[nbr] ){//&& index.get(nbr) > index.get(i)){
            				bw.write(index.get(i) + "\t" + index.get(nbr) + "\n");
            			}
            		}
            	}
            }
			bw.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    }
    
    public void writeTime(long startTime){
    	try {
        	//BufferedWriter br = new BufferedWriter(new FileWriter("Sample1-14000.csv"));
        	FileWriter b = new FileWriter(Test.pathOfCW+Test.str+"/time/"+sampleNumber+"-time.txt",true);
        	//b.write("Time in Minutes\n");
        	
        	
        	b.write("time(min) in sampling " +sampleNumber + " "+countS+" "+(System.currentTimeMillis() - startTime)/60000.0 +" \n");
         	b.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}


