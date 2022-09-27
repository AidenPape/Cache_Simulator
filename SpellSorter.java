import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class SpellSorter {

	static QuadraticProbingHashTable<String> dictionary = new QuadraticProbingHashTable<String>();
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		//Array containing arraylists to hold each misspelled word by index
		ArrayList<Word>[] misspelled = new ArrayList[52];
		
		for(int i = 0; i < misspelled.length; i++) {

			misspelled[i] = new ArrayList<Word>();
		}
		
		/*
		 * this array list holds all of the reappearing misspelled words to add what line they 
		 * appear on. makes the process simpler
		 */
		ArrayList<Word> stored = new ArrayList<Word>();
		
		Scanner scanner = new Scanner(System.in);
		
		String dict = args[0];
		
		//creates the buffered writer for the corrected file
		BufferedWriter out = null;
		
		//creates the buffered writer for the order file
		BufferedWriter out1 = null;
		
		//holds name of file for later
		String name = null;
		
		hashDict(dict);
		
		int count = 1;
		
		//outer loop to keep the program running smoothly; broken when user quits
		outer: while(true) {
			
			System.out.println("What document would you like to spell check? (q to quit)");
			
			String choice = scanner.nextLine();
			
			//option to quit
			if(choice.equals("q"))
				break outer;
		
			/*
			 * these assign the names of the buffered writer output files based on
			 * the input file that the user gives
			 */
			name = choice.replaceAll(".txt", "");
			
			out = new BufferedWriter(new FileWriter(name+"_corrected.txt"));
			
			out1 = new BufferedWriter(new FileWriter(name+"_order.txt"));
			
			Scanner scan = new Scanner(new File(choice));
			
			//p prints all the misspelled words in order of appearance until user quits or program ends
			System.out.println("Print words (p), enter new file(f), or quit (q) ?");
			
			String choice2 = scanner.nextLine();
			
			if(choice2.equals("p")) {
					
				//continual loop that runs until the input file does not have any more lines left
				middle: while(scan.hasNextLine()) {
				
					/*
					 * this creates a list of the words that are within the line so that they can be printed line by line
					 * rather than word by word; stores current line
					 */
					String line = scan.nextLine();
					String[] words = line.split("\\s");
					
					//for loop for all of the words within the current line
					for(int o = 0; o < words.length; o++) {
						
						String word = words[o];
						
						String punct = (",.!?:;");
						
						String replaced = null;
						
						/*
						 * this loops stores any of the punctuation within a word for later, so that it can be 
						 * added back into the output file; must create each word object without the punctuation so it
						 * is accurately stored
						 */
						
					
							
						char pt = word.charAt(word.length()-1);
							
						if(punct.contains(Character.toString(pt))){
								
						replaced = Character.toString(pt);
							
						}
						
						//replaces the punctuation
						String word1 = word.replaceAll("\\p{Punct}", "");
						
						Word wr = new Word(word1);
											
						//if the word is misspelled i.e. it is not within the given dictionary
						if(dictionary.contains(wr.toString()) != true){
							
							//this writes each misspelled word that appears into the non sorted output file
							out1.write(wr.getText()+" "+count+"\n");
							
							/*
							 * this next piece of code is how the misspelled word is indexed into the "hash" like table 
							 * misspelled. it takes the first letter and its numerical value to hash it 
							 */
							char ch = wr.toString().charAt(0);
							
							int int_ch = (int) ch;
							
							int index = 0;
							
							if(Character.isUpperCase(ch))
								index = (int_ch - 65);
							
							else if(Character.isLowerCase(ch))
								index = (int_ch - 71);
							
							
							Word cont = null;
							
							/*
							 *this checks to see if the word exists in the misspelled list already
							 */
							for(int j = 0; j < misspelled[index].size(); j++) {
							
								 Word com = misspelled[index].get(j);
								
								 if(wr.compareTo(com) == 0)
									 cont = com;
				
							}
							
							//this checks to see if the word was replaced or ignored or has not been added to the misspelled list yet
							if(cont == null || (cont.isReplaced() != true && cont.isIgnored() != true)) {
								
								System.out.println(wr.getText());
								
								/*
								 * if misspelled does not already contain the word, add it to the misspelled array list at index
								 */
								
								if(misspelled[index].contains(cont) != true)
									
									misspelled[index].add(wr);
								
								/*
								 * if the stored array list already has the word in it add the line that the new appearance occurred on
								 * else it adds the line and stores that word in the list
								 */
								if(stored.contains(cont) )
									
									cont.getLine().add(String.valueOf(count));
								
								else {
									
									wr.getLine().add(String.valueOf(count));
									stored.add(wr);
								}
								
								System.out.println("What would you like to do?\ni, r, n, q");
								
								String c = scanner.nextLine();
								
								//if the user chooses to ignore, set the word to be ignored for future reference
								if(c.equals("i")) {
									
									wr.setIgnored(true);
									
									out.write(wr.getText()+" ");
									
									out1.write(wr.getText());
									
								}
								
								//if the user chooses are, the program attempts to find the best replacement
								else if(c.equals("r")) {
									
									//array list that holds the suggested words
									ArrayList<String> suggest2 = suggestions(wr);
									
									if(suggest2.size()==0) {
										
										System.out.println("No suggestions");
										out.write(wr.getText()+" ");
									}
									else {
										
										System.out.println("Suggestions:");
										
										for(int i = 0; i < suggest2.size(); i++)
											
											System.out.print(suggest2.get(i)+" ");
										
										System.out.println();
										
										System.out.println("Enter which word you would like to replace with (Enter n for none)");
										
										String rp = scanner.nextLine();
										
										//if the user decides not to replace
										if(rp.equals("n")) {
											
											out.write(wr.getText()+" ");
											
										}
										
										//if the user decides to replace than set replacement 
										else {
							
											wr.setReplacement(rp);
											
											wr.setReplaced(true);
											
											out.write(wr.getReplacement()+" ");
											
											System.out.println("Replaced: "+wr.getReplacement());
										
										}
									}
									
								}
								
								//user decides to go to next misspelled word
								else if(c.equals("n")) {
									
									if (replaced != null) 
										
										out.write(wr.getText()+replaced+" ");
									
									else
										out.write(wr.getText()+" ");
									
								}
								
								else if(c.equals("q"))
									
									break middle;

							}
							
							//this section occurs only if the word is already in misspelled and is replaced or ignored
							else {
								
								if(stored.contains(cont)) {
									
									cont.getLine().add(String.valueOf(count));
								}
								
								else {
									
									cont.getLine().add(String.valueOf(count));
									stored.add(cont);
								}
								
								if(cont.isReplaced()) {
									
									if (replaced != null) 
										
										out.write(cont.getReplacement()+replaced+" ");
									
									else
										out.write(cont.getReplacement()+" ");
									}
								else {
									
									if (replaced != null)
										out.write(wr.getText()+replaced+" ");
									else
										out.write(wr.getText()+" ");
								}
							}
							
						}
						
						//if the word is in the dictionary already just write it to the out file with any replaced punctuation
						else {
							
							if (replaced != null) 
								
								out.write(wr.getText()+replaced+" ");
							
							else
								out.write(wr.getText()+" ");
						}
					}
					out.write("\n");
					
					count++;
				}
				
			}
			else if(choice.equals("q"))
						
				break outer;
			
			
		}
		
	if(out != null)
		out.close();
		
	if(out1 != null)
		out1.close();
	
	//new buffered writer to output the sorted misspelled words with their lines
	BufferedWriter sort = new BufferedWriter(new FileWriter(name+"_sorted.txt"));
	
	ArrayList<Word>[] outfile = bucketSort(misspelled);
	
	
	for(int l = 0; l < outfile.length; l++) {
		
		for(int y = 0; y < outfile[l].size(); y++) {
			
			String now = (outfile[l].get(y).getText()+" "+outfile[l].get(y).getLine()+"\n");
			
			sort.write(now);
		}
	}
	
	sort.close();
	
	scanner.close();
	
}
	
	//this method hashes the dictionary into a hash table
	public static void hashDict(String d) throws IOException {
		
		Scanner s = new Scanner(new File(d));
		
		while(s.hasNextLine()) {
			
			String line = s.nextLine();
			
			dictionary.insert(line);
		}	

	}
	
	
	//this is the main method to generate the suggestion and returns an array list of suggestions
	public static ArrayList<String> suggestions(Word word) {
	
		ArrayList<String> suggest = new ArrayList<String>();
		
		replace(suggest, word);
		
		delete(suggest, word);
		
		swap(suggest, word);
		
		insert(suggest, word);
		
		split(suggest, word);
		
		return suggest;
		
	}
	
	/*
	 * 1 of 5 ways to generate suggestions
	 * this works by inserting a character from capital letter to lower case letter at each index in the word
	 * if the new word is in dictionary, put it in the suggestions; else remove the inserted letter and continue
	 */
	public static void insert(ArrayList<String> suggest, Word word) {
		
		String tmp = word.toString();
		
		StringBuilder str = new StringBuilder(tmp);
		
		for(int i = 0; i < tmp.length(); i++) {
			
			for(int j = 65; j < 123; j++) {
				
				char cr = (char) j;
				
				str.insert(i, cr);
				
				String tmp2 = str.toString();
				
				if(dictionary.contains(tmp2)  && suggest.contains(tmp2) != true) 
					suggest.add(tmp2);
				else
					str.deleteCharAt(i);
			}
		}
	}
	
	/*
	 * 2 of 5 methods
	 * this replaces each letter in the word with a letter from upper case to lower case
	 * if the word matches in dictionary then it adds to suggestion, otherwise if replaces that letter with the previous one
	 */
	public static void replace(ArrayList<String> suggest, Word word) {
		
		String tmp = word.toString();
		
		StringBuilder str = new StringBuilder(tmp);
		
		for(int i = 0; i < tmp.length(); i++) {
			
				for(int j = 65; j < 123; j++) {
				
					char t = str.charAt(i);
					
					char cr = (char) j;
					
					str.setCharAt(i, cr);
					
					String tmp2 = str.toString();
					
					if(dictionary.contains(tmp2)&& suggest.contains(tmp2) != true) 
						suggest.add(tmp2);
				
					else
						str.setCharAt(i, t);
			}
		}
	}
	
	/*
	 * 3 of 5 (Extra credit)
	 * this method deletes characters index by index. if the word is in the dictionary
	 * it adds it and if not it replaces that deleted character
	 * it also deletes from the front and back to see if the word matches in dictionary
	 * if it does it adds it to the list and if not it moves on
	 * Math.abs ensures that the words are of similar size so that a long word doesn't get a suggestion of a short word
	 */
	public static void delete(ArrayList<String> suggest, Word word) {

		String tmp = word.toString();
		
		StringBuilder str = new StringBuilder(tmp);
		
		for(int i = 0; i < tmp.length()-1; i++) {
			
			char t = str.charAt(i);
			
			str.deleteCharAt(i);
			
			String tmp2 = str.toString();
			
			if(dictionary.contains(tmp2) && suggest.contains(tmp2) != true) 
				suggest.add(tmp2);
			else
				str.insert(i, t);
		}
		
		String tmp1 = word.toString();
		
		StringBuilder str1 = new StringBuilder(tmp1);
		
		for(int i = 0; i < tmp1.length(); i++) {
			
			str1.deleteCharAt(0);
			
			String tmp2 = str.toString();
			
			if(dictionary.contains(tmp2) && Math.abs(tmp2.length()-word.getText().length()) < 3 && suggest.contains(tmp2) != true)
				suggest.add(tmp2);
		}
		
		String tmp3 = word.toString();
		
		StringBuilder str2 = new StringBuilder(tmp3);
		
		for(int i = 0; i < tmp.length(); i++) {
			
			str2.deleteCharAt(str2.length()-1);
			
			String tmp4= str2.toString();
			
			if(dictionary.contains(tmp4) && Math.abs(tmp4.length()-word.getText().length()) < 3 && suggest.contains(tmp4) != true) 
				suggest.add(tmp4);
		}

	}
	/*
	 * 4 o 5 methods (Extra Credit)
	 * this methods swaps each index with the adjacent character and if it is in the dictionary it adds it to the 
	 * list and if not it swaps them back and moves to the next index
	 */
	public static void swap(ArrayList<String> suggest, Word word) {
		
		String tmp = word.toString();
		
		StringBuilder str = new StringBuilder(tmp);
		
		for(int i = 0; i < tmp.length()-1; i++) {
			
			char a = str.charAt(i);
			
			str.setCharAt(i, str.charAt(i+1));
			
			str.setCharAt(i+1, a);
			
			String tmp2 = str.toString();
			
			if(dictionary.contains(tmp2) && suggest.contains(tmp2) != true) 
				suggest.add(tmp2);
			
			else {
				
				str.setCharAt(i+1, str.charAt(i));
				
				str.setCharAt(i, a);
			}
		}
	}
	
	/*
	 * 5 of 5 methods (Extra Credit): this splits at each index the word into 
	 * 2 words and if both are in dictionary add them to the suggestions
	 */
	public static void split(ArrayList<String> suggest, Word word) {
		
		String tmp = word.toString();
		
		for(int i = 0; i < tmp.length(); i++) {
			
			String first = tmp.substring(i, i+1);
			
			String second = tmp.substring(i+1, tmp.length());
			
			if(dictionary.contains(first) && dictionary.contains(second)) {
				if(suggest.contains(first) != true)
					suggest.add(first);
				if(suggest.contains(second) != false)
					suggest.add(second);	
			}
			
		}
		
	}
	
	
	//main method for the bucket sort; sorts each bucket within the misspelled array
	public static ArrayList<Word>[] bucketSort(ArrayList<Word>[] misspelled) {
		
		for(int i = 0; i < misspelled.length; i ++) {
			
			if(misspelled[i].size() > 1) {
				
				Word first = misspelled[i].get(0);
				Word last = misspelled[i].get(misspelled[i].size()-1);
				
				quickSort(misspelled[i], first, last);
			}
		}
		
		return misspelled;
		
	}
	
	/*
	 * this is the partition method for the quick sort method
	 * it takes in the first last and pivot and partitions the list around it
	 * the code here is very similar to that in the lectures but the syntax has been changed to work with word
	 * objects and their given string values
	 * this method uses the word objects compareTo method to see if the words are alphabetically and lexographically correct
	 */
	public static int partition(ArrayList<Word> m, Word first, Word last, Word pivot) {
		
		int o = m.indexOf(first);
		
		Collections.swap(m, m.indexOf(last), m.indexOf(pivot));
		
		int i = o;
		
		int j = m.indexOf(pivot) - 1;
		
		boolean loop = true;
		
		while(loop == true) {
			
			while(m.get(i).compareTo(pivot) > 0 && i+1 < m.indexOf(pivot)-o+1)
				i++;
			while(m.get(j).compareTo(pivot) < 0 && j-1 > -1)
				j--;
			if(i < j)
				Collections.swap(m, i, j);
			else
				loop = false;
		}
		
		Collections.swap(m, i, m.indexOf(pivot));
		
		return i;
	}
	
	/*
	 * the quick sort method
	 * if the list is of size 3 or less do an insertion sort
	 * otherwise do another quick sort as long as the index of split point is still viable 
	 */
	public static void quickSort(ArrayList<Word> m, Word first, Word last) {
		
		int c = m.indexOf(last) - m.indexOf(first);
		
		if(c < 3) {
			
			if(c == 1) {	
				
				if(last.compareTo(first) == 1)
					Collections.swap(m, m.indexOf(last), m.indexOf(first));
			}
				
			else if (c > 1) {
			
				Word mid = m.get(m.indexOf(last)-1);
			
				if(last.compareTo(mid) == 1)
					Collections.swap(m, m.indexOf(mid), m.indexOf(last));
				if(last.compareTo(first) == 1)
					Collections.swap(m, m.indexOf(first), m.indexOf(last));
				if(first.compareTo(mid) == -1)
					Collections.swap(m, m.indexOf(mid), m.indexOf(first));
			}
		}
		
		else {
			
			Word pivot = pivot(m, first, last);
			
			int split_point = partition(m, first, last, pivot);
			
			if(split_point-1 > -1 && split_point+1 < m.size()) {
					
				quickSort(m, m.get(0), m.get(split_point-1));
				
				quickSort(m, m.get(split_point+1), m.get(m.size()-1));
			}
		}
	}

	//this chooses the pivot through the median of 3 using compareTo to see which word is alphabetically the median
	private static Word pivot(ArrayList<Word> m, Word first, Word last) {
		
		Word mid;
		
		int sub = (m.indexOf(last)-m.indexOf(first));
		
		if(sub % 2 == 0)
			
			mid = m.get(m.indexOf(first)+(sub/2));
		
		else
			
			mid =  m.get(m.indexOf(first)+(sub/2)+1);
		
		
		ArrayList<Word> piv = new ArrayList<Word>();
		
		piv.add(first);
		piv.add(mid);
		piv.add(last);
		
		if(last.compareTo(mid) == 1)
			Collections.swap(piv, piv.indexOf(last), piv.indexOf(mid));
		if(last.compareTo(first) == 1)
			Collections.swap(piv, piv.indexOf(last), piv.indexOf(first));
		if(mid.compareTo(first) == 1)
			Collections.swap(piv, piv.indexOf(mid), piv.indexOf(first));
		
		
		return piv.get(1);
	}
	
	
}
	
