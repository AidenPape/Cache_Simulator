import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/*
 * All of the comments for the operations of these methods in this class
 * are the same as the spell sorter, so all of the comments are in the spell sorter
 * class, the read me, and the word class. The only difference in this class is
 * you can choose to just replace on instance of the word
 */

public class SpellCheckerExtra {

	static QuadraticProbingHashTable<String> dictionary = new QuadraticProbingHashTable<String>();
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		ArrayList<Word>[] misspelled = new ArrayList[52];
		
		for(int i = 0; i < misspelled.length; i++) {

			misspelled[i] = new ArrayList<Word>();
		}
		
		Scanner scanner = new Scanner(System.in);
		
		String dict = args[0];
		
		BufferedWriter out = null;
		
		BufferedWriter out1 = null;
		
		String name;
		
		hashDict(dict);
		
		int count = 1;
		
		outer: while(true) {
			
			System.out.println("What document would you like to spell check? (q to quit)");
			
			String choice = scanner.nextLine();
			
			if(choice.equals("q"))
				break outer;
			
			name = choice.replaceAll(".txt", "");
			
			out = new BufferedWriter(new FileWriter(name+"_corrected.txt"));
			
			out1 = new BufferedWriter(new FileWriter(name+"_order.txt"));
			
			Scanner scan = new Scanner(new File(choice));
			
			System.out.println("Print words (p), enter new file(f), or quit (q) ?");
			
			String choice2 = scanner.nextLine();
			
			if(choice2.equals("p")) {
					
				middle: while(scan.hasNextLine()) {
				
					String line = scan.nextLine();
					String[] words = line.split("\\s");
					
					for(int o = 0; o < words.length; o++) {
						
						String word = words[o];
						
						String punct = (",.!?:;");
						
						String replaced = null;
						
						int r_index = -1;
						
						for(int p = 0; p < word.length(); p ++) {
							
							char pt = word.charAt(p);
							
							if(punct.contains(Character.toString(pt))){
								
								replaced = Character.toString(pt);
								r_index = p;
							}
						}
						
						String word1 = word.replaceAll("\\p{Punct}", "");
						
						Word wr = new Word(word1);
											
						if(dictionary.contains(wr.toString()) != true){
							
							out1.write(wr.getText()+" "+count+"\n");
							
							char ch = wr.toString().charAt(0);
							
							int int_ch = (int) ch;
							
							int index = 0;
							
							if(Character.isUpperCase(ch))
								index = (int_ch - 65);
							
							else if(Character.isLowerCase(ch))
								index = (int_ch - 71);
							
							Word cont = null;
							
							for(int j = 0; j < misspelled[index].size(); j++) {
							
								Word com = misspelled[index].get(j);
								
								if(com.isReplaced() || com.isIgnored()) {
								
									cont = misspelled[index].get(j);
									
									break;
									
								}
				
							}
							
							if(cont == null || (cont.isReplaced() != true && cont.isIgnored() != true)) {
								
								System.out.println(wr.getText());
								
								if(misspelled[index].contains(cont) != true)
									
									misspelled[index].add(wr);	
								
								System.out.println("What would you like to do?\ni, r1 (replace 1), r2 (replace all), n, q");
								
								String c = scanner.nextLine();
								
								if(c.equals("i")) {
									
									wr.setIgnored(true);
									
									out.write(wr.getText()+" ");
									
									out1.write(wr.getText());
									
								}
								
								//if the choice is r1 then just replace one instance of the replaced word
								else if(c.equals("r1")) {
									
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
										
										if(rp.equals("n")) {
											
											out.write(wr.getText()+" ");
											
										}
										
										else {
							
											wr.setReplacement(rp);
											
											out.write(wr.getReplacement()+" ");
											
											System.out.println("Replaced: "+wr.getReplacement());
										
										}
									}
								}
								
								//if choice is r2 then replace all instances of the word
								else if(c.equals("r2")) {
									
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
										
										if(rp.equals("n")) {
											
											out.write(wr.getText()+" ");
											
										}
										
										else {
							
											wr.setReplacement(rp);
											
											wr.setReplaced(true);
											
											out.write(wr.getReplacement()+" ");
											
											System.out.println("Replaced: "+wr.getReplacement());
										
										}
									}
									
								}
								
								else if(c.equals("n")) {
									
									if (replaced != null) 
										
										out.write(wr.getText()+replaced+" ");
									
									else
										out.write(wr.getText()+" ");
									
								}
								
								else if(c.equals("q"))
									
									break middle;

							}
							else {
								
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
	
	scanner.close();
}
	
	public static void hashDict(String d) throws IOException {
		
		Scanner s = new Scanner(new File(d));
		
		while(s.hasNextLine()) {
			
			String line = s.nextLine();
			
			dictionary.insert(line);
		}	

	}
	
	public static ArrayList<String> suggestions(Word word) {
	
		ArrayList<String> suggest = new ArrayList<String>();
		
		replace(suggest, word);
		
		delete(suggest, word);
		
		swap(suggest, word);
		
		insert(suggest, word);
		
		split(suggest, word);
		
		return suggest;
		
	}
	
	
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
}

	
