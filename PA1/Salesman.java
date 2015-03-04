/*
 * Salesman.java 
 * by Melvyn Ian Drag
 * drag.2@osu.edu
 * 
 * My first Java program!
 * 
 */
  
/*
 * I am using strings for all the numerical values because I dont 
 * understand why Java has ints an Integers!
 */

// Dont import the whole library . . .
import java.io.*;
import java.util.*; 													

public class Salesman {
	public static void main(String args[]) {
		
		/*
		 * Read in the input file, store information in the 
		 * dist() table.
		 */
		 
		int n = 0;
		Vector<String> dist = new Vector<String>(); 
		// Vector() is deprecated. If there is time, change this.					
		try{															
			File file = new File("input.txt");
			Scanner sc = new Scanner (file);
			String file_line = sc.nextLine();
			n = Integer.parseInt(file_line);
			for (int i = 0; i < n; i++){
				file_line = sc.nextLine();
				String[] weights = file_line.split(" ");
				for(int j = 0; j < n; j++){
					dist.add( weights[j] );
				}
			}
		}catch (IOException e) {
				e.printStackTrace();
		}
		
		/* 
		 * Construct the set V
		 */
		 
 		HashSet<String> V = new HashSet<String>();
		for(int i = 0; i < n; i++){
			V.add(Integer.toString(i));	
		}
		
		/*
		 * Construct the power set of V - {s}. 
		 * These will be the Ws we use.
		 * Thank you to this webpage:
		 * stackoverflow.com/questions/1670862/obtaining-a-powerset-of-a-set-in-java
		 */
		 
		HashSet<String>Vprime = new HashSet<String>();
		Vprime.addAll(V);
		Vprime.remove(Integer.toString(0));
		List<HashSet<String>> powerSet = new ArrayList<HashSet<String>>();
		for(String addToHashSets:Vprime) {
			List<HashSet<String>> newHashSets = new ArrayList<HashSet<String>>();
			for(HashSet<String> curHashSet:powerSet) {
				HashSet<String> copyPlusNew = new HashSet<String>();
				copyPlusNew.addAll(curHashSet);
				copyPlusNew.add(addToHashSets);
				newHashSets.add(copyPlusNew);
			}
			HashSet<String> newVHashSet = new HashSet<String>();
			newVHashSet.add(addToHashSets);
			newHashSets.add(newVHashSet);
			powerSet.addAll(newHashSets);
		}
		
		/*
		 * Set the length of each vertex to s = 0 to be
		 * dist (i*n), since s is vert 0.
		 */
		 
		HashMap<String, HashMap<HashSet<String>, String>> length = 
			new HashMap <String, HashMap<HashSet<String>, String>>(); // NO RAW TYPES
		for (int vertex = 0; vertex < n; vertex++){
			HashMap <HashSet<String>, String> MapofSets = new HashMap<HashSet<String>, String> ();
			MapofSets.put(new HashSet<String>(), dist.get(vertex*n));
			length.put(Integer.toString(vertex), MapofSets);                  
			// I guess java doesnt like you to use primitives, so Ill cast it as an Integer.

		}
		
		/* 
		 * This is the important triple for loop in the algorithm.
		 */
		
		for(int i = 1; i <= n - 1; i++){
			for(HashSet<String> W : powerSet){
				if(W.size() == i){
					HashSet<String> VminusW = new HashSet<String>();
					VminusW.addAll(V);
					VminusW.removeAll(W);
					double minimum = Double.POSITIVE_INFINITY;
					for (String v : VminusW){
						for(String v1 : W){
							
							HashMap <HashSet<String>, String> MapOfSets =
								new HashMap<HashSet<String>, String>(length.get(v1));
							HashSet <String> Wminusv1 = new HashSet<String>();
							Wminusv1.addAll(W);
							Wminusv1.remove(v1);
							
							int I = Integer.parseInt(v);
							int J = Integer.parseInt(v1);
						    double value = Double.parseDouble( dist.get(I*n + J) ) +
								Double.parseDouble( MapOfSets.get(Wminusv1) );
						    if (value < minimum){
								minimum = value;
							}
						}
						HashMap <HashSet<String>, String> NewMapEntry = 
							new HashMap<HashSet<String>, String>(length.get(v));
						NewMapEntry.put(W, Double.toString(minimum));
						length.put(v, NewMapEntry);
					}
				}
			}
		}
		
		HashMap <HashSet<String>, String> TST =
			new HashMap<HashSet<String>, String>(length.get(Integer.toString(0)));
		String shortestPath = TST.get(Vprime);

		/*
		 * Write to the output file.
		 */
		 PrintWriter out = null;
		  try {
				out = new PrintWriter(new FileWriter("output.txt"));
				out.println(shortestPath);
			} catch (IndexOutOfBoundsException e) {
				System.err.println("Caught IndexOutOfBoundsException: "
								   +  e.getMessage());
										 
			} catch (IOException e) {
				System.err.println("Caught IOException: " +  e.getMessage());
										 
			} finally {
				if (out != null) {
					out.close();
				} 
				else {
					System.out.println("PrintWriter not open");
				}
			}
	}
}
