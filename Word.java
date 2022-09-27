import java.util.ArrayList;

public class Word {

	private String text;
	private boolean ignored;
	private boolean replaced;
	private String replacement;
	//this array list stores the lines that this word appears on
	private ArrayList<String> line = new ArrayList<String>();
	
	//constructor
	public Word() {
		
	}
	
	//Constructor with parameter
	public Word(String string) {
		this.text = string;
	}
	
	//the rest of these are just basic getter and setter methods
	public String getText() {
		return text;
		
	}
	public void setText(String text) {
		this.text = text;
		
	}
	
	public boolean isIgnored() {
		return ignored;
	}
	
	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
		
	}
	public boolean isReplaced() {
		return replaced;
		
	}
	public void setReplaced(boolean replaced) {
		this.replaced = replaced;
		
	}
	public String getReplacement() {
		return replacement;
		
	}
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}
	
	public ArrayList<String> getLine() {
		return line;
	}

	public void setLine(ArrayList<String> line) {
		this.line = line;
	}	
	
	//override toString method for the Word object class
	public String toString() {
		return this.text+"";
	}
	
	/*
	 * override compareTo method for the Word object class
	 * This works by comparing each character in the word to the text
	 * and only returns 0 if the words are completely equal.
	 */
	public int compareTo(Word o) {
		
		if(this.text.equals(o.text))
			return 0;
		
		for(int i = 0; i < this.text.length(); i ++) {
			
			char ch = o.getText().charAt(i);
			
			if(Character.isDigit(ch) == true) {
				
				if(ch < this.text.charAt(i))
					return -1;
				
				else if(ch > this.text.charAt(i))
					return 1;
			}
			
			else if(Character.isLetter(ch) == true) {
				
				if(ch < this.text.charAt(i))
					return -1;
				
				else if(ch > this.text.charAt(i))
					return 1;
			}
		}
		return 0;
	}	

}
