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
		InputStream in = ResourceLoader.class.getResourceAsStream(filename);
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		return input;
	}

	
}
