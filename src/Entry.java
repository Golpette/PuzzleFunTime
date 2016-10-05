	/**
	 *  Object for each crossword entry.
	 *  Holds starting position, direction, word, definition.
	 */
public class Entry {
	int start_x;
    int start_y;
    int end_x;
    int end_y;
    int wordLength;
    Boolean across;
    String word;
    String direction;
    String definition;
    int clue_number;
    int entryAcross, entryDown;
   
   public Entry(int start_x, int start_y, Boolean across, String word, String definition){
	   this.start_x = start_x;
	   this.start_y = start_y;
	   wordLength = word.length();
	   this.across = across;
	   this.word = word;
	   this.definition = definition;
	   clue_number = 0;
	   entryAcross = 0;
	   entryDown = 0;
   }
   
	public int getWordLength() {
		  return wordLength;
	}

	public void setWordLength(int wordLength) {
	   this.wordLength = wordLength;
	}

public Entry(int start_x, int start_y, Boolean across, String word, String definition, String direction){
	   this.start_x = start_x;
	   this.start_y = start_y;
	   this.across = across;
	   this.word = word;
	   this.definition = definition;
	   this.direction = direction;
	   clue_number = 0;
	   entryAcross = 0;
	   entryDown = 0;
	   if(direction.equals("across")){
		   end_x = start_x + wordLength;
		   end_y = start_y;
	   }else if(direction.equals("backwards")){
		   end_x = start_x - wordLength;
		   end_y = start_y;
	   }else if(direction.equals("down")){
		   end_x = start_x;
		   end_y = start_y + wordLength;
	   }else if(direction.equals("up")){
		   end_x = start_x;
		   end_y = start_y - wordLength;
	   }else if(direction.equals("backwardsdiagonal")){
		   end_x = start_x - wordLength;
		   end_y = start_y - wordLength;
	   }else if(direction.equals("diagonal")){
		   end_x = start_x + wordLength;
		   end_y = start_y + wordLength;
	   }else if(direction.equals("BLTRdiagonal")){
		   end_x = start_x + wordLength;
		   end_y = start_y - wordLength;
	   }else if(direction.equals("backwardsBLTRdiagonal")){
		   end_x = start_x - wordLength;
		   end_y = start_y + wordLength;
	   }
   }
   
   public int getEnd_x() {
	return end_x;
}

public void setEnd_x(int end_x) {
	this.end_x = end_x;
}

public int getEnd_y() {
	return end_y;
}

public void setEnd_y(int end_y) {
	this.end_y = end_y;
}

public String getDirection() {
	   return direction;
   }

   public void setDirection(String direction) {
	   this.direction = direction;
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
   
   public int getClueNumber(){
	   return this.clue_number;
   }
   
   public int getEntryAcross(){
	   return this.entryAcross;
   }
   
   public int getEntryDown(){
	   return this.entryDown;
   }
   
   public void setClueNumber(int clue){
	   this.clue_number = clue;
   }
   
   public void setEntryAcross(int entry){
	   this.entryAcross = entry;
   }
   
   public void setEntryDown(int entry){
	   this.entryDown = entry;
   }
}
