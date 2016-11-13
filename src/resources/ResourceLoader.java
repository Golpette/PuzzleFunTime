package resources;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceLoader {

	
	static ResourceLoader rl = new ResourceLoader();
	
	public static Image getImage(String filename){
		
		return Toolkit.getDefaultToolkit().getImage(rl.getClass().getResource(filename));
	}
	
	public static BufferedReader getBufferedReader(String filename){
		System.out.println("filename: "+filename);
		System.out.println("toolkit stuff: "+ Toolkit.getDefaultToolkit().toString());
		//InputStream in = Toolkit.getDefaultToolkit().getClass().getResourceAsStream(filename);
		InputStream in = ResourceLoader.class.getResourceAsStream(filename);
		System.out.println("in: "+in.toString());
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		
		return input;
	}

	
}
