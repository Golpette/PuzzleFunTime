	/**
	 *  Object for each crossword entry.
	 *  Holds starting position, direction, word, definition.
	 */
public class Entry {
	int start_x;
    int start_y;
    Boolean across;
    String word;
    String definition;
   
   public Entry(int start_x, int start_y, Boolean direction, String word, String definition){
	   this.start_x = start_x;
	   this.start_y = start_y;
	   this.across = direction;
	   this.word = word;
	   this.definition = definition;
   }
   
   public String toString(){
	   return "("+ this.start_x + ", " + this.start_y + ") " + this.across + ": " + this.word + " Def:" + this.definition;
   }
   
   public int getX(){
	   return this.start_x;
   }
   
   public int getY(){
	return this.start_y;   
   }
   
   public Boolean isAcross(){
	   return across;
   }
   
   public String getWord(){
	   return this.word;
   }
   
   public String getDefinition(){
	   return this.definition;
   }
}
