/*
 * Salesman.java 
 * by Melvyn Ian Drag
 * drag.2@osu.edu
 * 
 * My first Java program!
 * 
 */


/*
 * Steps:
 *	 Read in data from the input file.
 */
 
 /*
  * Important Variables:
  * 1. n - number of vertices
  * 2. dist - distance between vert i and vert j
  * 3. length - length of the path from starting at v which passes through W and arrives at s. 
  */
  
  
/*
 * I am using strings for all the numerical values because I dont 
 * understand why Java has ints an Integers!
 */

import java.io.*;
import java.util.*; 													// This is bad practice. Ill clean it up if there is time.

public class Salesman {
	public static void main(String args[]) {
		
		/*
		 * Read in the input file, store information in the 
		 * dist() table.
		 */
		 
		int n = 0;
		Vector<String> dist = new Vector<String>(); 					// Vector() is deprecated. If there is time, change this.
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
		 * Make sure I read in the file correctly.
		 */
		System.out.println();
		System.out.println("These are the weights in the input file.:");
		System.out.println();
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				System.out.print( dist.get(i*n + j) );
				System.out.print(" ");
			}
			System.out.println();
		}
		
		/* 
		 * Construct the set V
		 */
		 
 		Set<String> V = new HashSet<String>();
		for(int i = 0; i < n; i++){
			V.add(Integer.toString(i));	
		}
		
		/*
		 * Construct the power set of V - {s}. 
		 * These will be the Ws we use.
		 */
		 
		Set<String>Vprime = new HashSet<String>();
		Vprime.addAll(V);
		Vprime.remove(0);
		List<Set<String>> powerSet = new ArrayList<Set<String>>();
		for(String addToSets:Vprime) {
			List<Set<String>> newSets = new ArrayList<Set<String>>();
			for(Set<String> curSet:powerSet) {
				Set<String> copyPlusNew = new HashSet<String>();
				copyPlusNew.addAll(curSet);
				copyPlusNew.add(addToSets);
				newSets.add(copyPlusNew);
			}
			Set<String> newVSet = new HashSet<String>();
			newVSet.add(addToSets);
			newSets.add(newVSet);
			powerSet.addAll(newSets);
		}
		
		/*
		 * Have a look at the generated subsets
		 */
		System.out.println();
		System.out.println("This is the power set:");
		System.out.println();
		for(Set<String> set:powerSet) {
			for(String setEntry:set) {
				System.out.print(setEntry + " ");
			}
			System.out.println();
		}
		
		/*
		 * Set the length of each vertex to s = 0 to be
		 * dist (i*n), since s is vert 0.
		 */
		 
		HashMap<String, HashMap<HashSet<String>, String>> length = new HashMap <String, HashMap<HashSet<String>, String>>(); // NO RAW TYPES
		for (int vertex = 0; vertex < n; vertex++){
			HashMap <HashSet<String>, String> MapofSets = new HashMap<HashSet<String>, String> ();
			MapofSets.put(new HashSet<String>(), dist.get(vertex*n));
			length.put(Integer.toString(vertex), MapofSets);                  // I guess java doesnt like you to use primitives, so Ill cast it as an Integer.

		}
		
		/* 
		 * This is the important triple for loop in the algorithm.
		 */
		
		for(int i = 1; i <= n - 1; i++){
			for(Set<String> W : powerSet){
				if(W.size() == i){
					Set<String> VminusW = new HashSet<String>();
					VminusW.addAll(V);
					VminusW.removeAll(W);
					double minimum = 0.0;
					for (String v : VminusW){
						// Here we update length
						// This first value will be v. 
						// The second value is a hash map, which we have to make.
						// This hash map takes a HashSet and a String as a parameter
						// Where the string is the length of the path 
						// from v to s through the vertices in the HashSet.
						for(String v1 : W){
							HashMap <HashSet, String> MapOfSets = new HashMap<HashSet, String>(length.get(v1));
							Set <String> Wminusv1 = new HashSet<String>();
							Wminusv1.addAll(W);
							Wminusv1.remove(v1);
							
							int I = Integer.parseInt(v);
							int J = Integer.parseInt(v1);
						    double value = Double.parseDouble( dist.get(I*n + J) ) +  Double.parseDouble( MapOfSets.get(Wminusv1) );
							System.out.println("Value in MapOfSets: ");
							System.out.println(MapOfSets.get(Wminusv1));
						}
					}
				}
			}
		}
		
		
		Set entries = length.entrySet();
		Iterator i = entries.iterator();
		while(i.hasNext()){
			
			Map.Entry me = (Map.Entry)i.next();
			System.out.print(me.getKey() + ": ");
			System.out.println(me.getValue());
		}
		
		if(length.containsKey(0)){
			System.out.println("integers and ints are sometimes interchangeable!");
		}
		



	}
}
