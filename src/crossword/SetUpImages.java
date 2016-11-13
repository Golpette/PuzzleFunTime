package crossword;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import resources.ResourceLoader;

public class SetUpImages {
	String operatingSystem;
	String imagePath = "";
	String versionString = "";
	String imageName;
	String [] imageNames;
	int imageX;
	int imageY;
	int version;
	Icon icon;
	Icon [] icons;
	URL url; 
	URL [] urls;
	InputStream input;
	InputStream [] inputs;
	
	public SetUpImages(String imageName, int imageY, int imageX, Icon icon, int version){
		this.imageName = imageName;
		this.imageX = imageX;
		this.imageY = imageY;
		this.version = version;
		if(version > 0){
			versionString = Integer.toString(version);
		}
		/**
		 *  Set image path depending on OS
		 */
		operatingSystem = System.getProperty("os.name").toLowerCase();
		if(operatingSystem.equals("linux")){
			imagePath = "src/resources/";
		}
		else if(operatingSystem.contains("windows")){
			imagePath = "";
		}		
		//imageName = setPath(imageName);
//		input = SetUpImages.class.getResourceAsStream(imageName);
//		if(input == null){
//			input = SetUpImages.class.getResourceAsStream("\\"+imageName);
//		}
		
		
		//URL url = SetUpImages.class.getResource(imageName);
		
		icon = setImage(setPath(imageName), imageX, imageY);
		System.out.println("path: "+imageName);
	}
	
	public SetUpImages(String [] imageNames, int imageY, int imageX, Icon [] icons, int version){
		this.imageNames = imageNames;
		this.imageX = imageX;
		this.imageY = imageY;
		this.version = version;
		if(version > 0){
			versionString = Integer.toString(version);
		}
		/**
		 *  Set image path depending on OS
		 */
		operatingSystem = System.getProperty("os.name").toLowerCase();
		if(operatingSystem.equals("linux")){
			imagePath = "src/resources/";
		}
		else if(operatingSystem.contains("windows")){
			imagePath = "";
		}		
		
		for(int i = 0; i < imageNames.length; i++){
			//imageNames[i] = setPath(imageNames[i]);
//			inputs[i] = SetUpImages.class.getResourceAsStream(imageNames[i]);
//			if(inputs[i] == null){
//				inputs[i] = SetUpImages.class.getResourceAsStream("\\"+imageNames[i]);
//			}
			
		//	URL url = SetUpImages.class.getResource(imageNames[i]);
			
			icons[i] = setImage(setPath(imageNames[i]), imageX, imageY);
		}
	}
	
	/**
	 * Method which takes String name of image (eg top for the top facing loop image) and appends the 
	 * appropriate path according to operating system to the beginning and then appends ".png" to the end.
	 */
	public String setPath(String imageName){
		return (imagePath + imageName + versionString + ".png");	
	}

	/**
	 * This sets the imageIcon from a given name of image and creates the corresponding image, 
	 * from it.  The image is then scaled and placed into a temporary image
	 * and finally this is set back to the imageIcon itself.  
	 */
	public Icon setImage(String path, int scaleX, int scaleY){
		ImageIcon icon = new ImageIcon(path);
		//System.out.println("path: "+path);
		//Image img = icon.getImage();
		Image img = ResourceLoader.getImage(path);
		Image newimg = img.getScaledInstance(scaleX, scaleY, java.awt.Image.SCALE_SMOOTH ) ; 
		icon = new ImageIcon(newimg);
		return icon;	
	}
}
