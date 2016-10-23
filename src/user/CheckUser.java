package user;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to check the user exists in the system and that the password is verified.  
 * This could check a file initially, but eventually query a database of users.
 * @author Andrew
 *
 */
public class CheckUser {

	String user;
	String password;
	String file = "Users.txt";
	
	public CheckUser(String user, String password){
		this.user = user;
		this.password = password;
	}
	
	public void readInputFile(String input){

			BufferedReader bufferedReader = null;

			try {

				String sCurrentLine;

				bufferedReader = new BufferedReader(new FileReader(input));

				while ((sCurrentLine = bufferedReader.readLine()) != null) {
					System.out.println(sCurrentLine);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bufferedReader != null)bufferedReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
}
