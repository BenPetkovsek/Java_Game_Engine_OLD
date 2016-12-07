/*
 * This class should contain static methods that are useful for image manipulation
 */
package View;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageStyler {

	public static BufferedImage loadImg(String filename){
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println(filename+" not found");
		}
		return img;
	}
}