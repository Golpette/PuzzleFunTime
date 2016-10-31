package crossword;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JTextFieldLimit extends PlainDocument {

	private static final long serialVersionUID = 1L;
	private int limit;
	String goodChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	String goodNums = "123456789";
	Boolean requireLetters;
	
//	public JTextFieldLimit(int limit) {
//		super();
//		this.limit = limit;
//	}

	public JTextFieldLimit(int limit, boolean requireLetters) {
		super();
		this.limit = limit;
		this.requireLetters = requireLetters;
	}

	public void insertString2(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;

		if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
		}
	}
	
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;

		if(requireLetters){
			if ((getLength() + str.length()) <= limit) {
				if(goodChars.contains(str)){
					super.insertString(offset, str.toUpperCase(), attr);
				}
			}
		}else if(!requireLetters){
			if ((getLength() + str.length()) <= limit) {
				if(goodNums.contains(str)){
					super.insertString(offset, str, attr);
				}
			}
		}
	}
}