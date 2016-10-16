package resources;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class SetUpImages {
	String operatingSystem;
	String imagePath = "";
	String imageName;
	String [] imageNames;
	int imageX;
	int imageY;
	Icon icon;
	Icon [] icons;
	
	public SetUpImages(String imageName, int imageY, int imageX, Icon icon){
		this.imageName = imageName;
		this.imageX = imageX;
		this.imageY = imageY;
		
		/**
		 *  Set image path depending on OS
		 */
		operatingSystem = System.getProperty("os.name").toLowerCase();
		if(operatingSystem.equals("linux")){
			imagePath = "src/resources/";
		}
		else if(operatingSystem.contains("windows")){
			imagePath = "src\\resources\\";
		}		
		icon = setImage(setPath(imageName), imageX, imageY);
	}
	
	public SetUpImages(String [] imageNames, int imageY, int imageX, Icon [] icons){
		this.imageNames = imageNames;
		this.imageX = imageX;
		this.imageY = imageY;
		
		/**
		 *  Set image path depending on OS
		 */
		operatingSystem = System.getProperty("os.name").toLowerCase();
		if(operatingSystem.equals("linux")){
			imagePath = "src/resources/";
		}
		else if(operatingSystem.contains("windows")){
			imagePath = "src\\resources\\";
		}		
		for(int i = 0; i < imageNames.length; i++){
			icons[i] = setImage(setPath(imageNames[i]), imageX, imageY);
		}
	}
	
	/**
	 * Method which takes String name of image (eg top for the top facing loop image) and appends the 
	 * appropriate path according to operating system to the beginning and then appends ".png" to the end.
	 */
	public String setPath(String imageName){
		return (imagePath + imageName + ".png");	
	}

	/**
	 * This sets the imageIcon from a given name of image and creates the corresponding image, 
	 * from it.  The image is then scaled and placed into a temporary image
	 * and finally this is set back to the imageIcon itself.  
	 */
	public Icon setImage(String path, int scaleX, int scaleY){
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(scaleX, scaleY, java.awt.Image.SCALE_SMOOTH ) ; 
		icon = new ImageIcon(newimg);
		return icon;	
	}
}
