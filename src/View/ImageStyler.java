/*
 * This class should contain static methods that are useful for image manipulation
 */
package View;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageStyler {

	//quick and easy way to load an image
	public static BufferedImage loadImg(String filename){
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println(filename+" not found");
		}
		return img;
	}
	
	//flips all the images in the array horizontally
	//easier to do it here
	public static BufferedImage[] flipImgs(BufferedImage[] original){
		
		BufferedImage[] flipped = new BufferedImage[original.length];
		for (int i =0; i<original.length;i++){
			flipped[i] = flip(original[i]);
		}
		return flipped;
	}
	
	//flips an horizontally
	//made public as can be used externally
	public static BufferedImage flip(BufferedImage original){
		int w = original.getWidth();   
        int h = original.getHeight();   
        BufferedImage flipped = new BufferedImage(w, h, original.getColorModel().getTransparency());     
        Graphics2D g = flipped.createGraphics();   
        g.drawImage(original, 0, 0, w, h, w, 0, 0, h, null);   
        //g.drawImage(original, 0, 0, w, h, w, 0, 0, h, null);   
        g.dispose();   
		return flipped;
	}
}