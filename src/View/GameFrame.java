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

	private final int panelWidth = 300;	//dimensions are fixed rn just because we can deal with that later
	private final int panelHeight =300;
	
	//NOTE: Make game frame created from the main loop, model should call view
	public GameFrame(){
		GameRender screen = new GameRender();
		add(screen);
		
		//Set the size after the background is made?
		setResizable(false);
		setTitle("Test"); 	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		
		
		
		BufferedImage background = ImageStyler.loadImg("Art/background1.png");
		screen.setBackground(background);
		
		setSize(panelWidth,panelHeight);
		

		//start main loop, main loop should make the panel on constructor
	}

	public static void main(String[] args){
		new GameFrame();
	}
	

}
