/*
 * Window for the game	
 */
package View;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


public class GameFrame extends JFrame {
	
	/**
	 *  generated serial number
	 */
	private static final long serialVersionUID = 3586519093165768249L;	
	
	//NOTE: Make game frame created from the main loop, model should call view
	public GameFrame(String title){
		//Set the size after the background is made?
		setResizable(false);
		setTitle(title); 	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}

	

}
