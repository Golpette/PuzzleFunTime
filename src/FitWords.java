import java.util.*;
import java.lang.Math;

/**
 * Randomly fit words into crossword if there is a space above and to the left in grid.
 * Added attempt to make symmetrical crossword
 */
public class FitWords {

	public FitWords(String[][] grid, int xLength, int yLength, ArrayList<Word> wordlist, ArrayList<Entry> entries, boolean across) {
		int init_x = (int) (Math.random() * (xLength - 2) + 1);
		int init_y = (int) (Math.random() * (yLength - 2) + 1);
		if (grid[init_x][init_y - 1].equals("_") && grid[init_x - 1][init_y].equals("_")) {
			String toWorkWith = "";
			String toWorkWith2 = "";
			if (across) {
				int maxPossLength = (xLength - 1) - init_x;
				for (int i = 0; i < maxPossLength; i++) {
					if (grid[init_x + i][init_y] == "_") {
						if (grid[init_x + i][init_y - 1].equals("_") && grid[init_x + i][init_y + 1].equals("_")) {
							toWorkWith = toWorkWith + grid[init_x + i][init_y];
							//toWorkWith2 = toWorkWith2 + grid[xLength - init_x -i][yLength - init_y];
						} else {
							break;
						}
					}else{
						toWorkWith = toWorkWith + grid[init_x + i][init_y];
						//toWorkWith2 = toWorkWith2 + grid[xLength - init_x -i][yLength - init_y];
					}
				}
			} else {
				int maxPossLength = (yLength - 1) - init_y;
				for (int i = 0; i < maxPossLength; i++) {
					if (grid[init_x][init_y + i] == "_") {
						if (grid[init_x - 1][init_y + i].equals("_") && grid[init_x + 1][init_y + i].equals("_")) {
							toWorkWith = toWorkWith + grid[init_x][init_y + i];
							//toWorkWith2 = toWorkWith2 + grid[xLength - init_x -i][yLength - init_y];
						} else {
							break;
						}
					} else {
						toWorkWith = toWorkWith + grid[init_x][init_y + i];
						//toWorkWith2 = toWorkWith2 + grid[xLength - init_x -i][yLength - init_y];
					}
				}
			}
			String[] word_and_def = new String[2];
			//String[] word_and_def2 = new String[2];
			String word = "";
			//String word2 = "";
			String definition = "";
			//String definition2 = "";
			int maxWordLength = toWorkWith.length();
			if (maxWordLength > 2 && toWorkWith.contains("_")) {
				word_and_def = Search.findWord(wordlist, toWorkWith);
				//word_and_def2 = Search.findWord(wordlist, toWorkWith2);
				word = word_and_def[0];
				//word2 = word_and_def2[0];
				definition = word_and_def[1];
				//definition2 = word_and_def2[1];
			}
			if (!word.equals("")) {
				boolean seenBefore = false;
				for (int j = 0; j < entries.size(); j++) {
					if (entries.get(j).getWord().equals(word)) {
						seenBefore = true;
					}
				}
				if (!seenBefore) {
					for (int g = 0; g < word.length(); g++) {
						if (across) {
							grid[init_x + g][init_y] = "" + word.charAt(g);
							//grid[xLength - init_x - word.length() + g][yLength - init_y] = "" + word2.charAt(g);
						} else {
							grid[init_x][init_y + g] = "" + word.charAt(g);
							//grid[xLength - init_x][yLength - init_y - word.length() + g] = "" + word2.charAt(g);
						}
					}
					Entry entry = new Entry(init_x, init_y, across, word, definition);
					//Entry entry2 = new Entry(xLength - init_x, yLength - init_y, across, word2, definition2);
					entries.add(entry);
					//entries.add(entry2);
				}
			}
		}
	}
}
