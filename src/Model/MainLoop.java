/*
 * Main thread of the game, contains all game initializes (i think) and main(String args[])
 */
package Model;

import java.awt.image.BufferedImage;

import View.*;

public class MainLoop extends Thread {
	
	private GameRender renderer; 
	private BufferedImage background;
	private int panelWidth;
	private int panelHeight;
	//initialization of game settings
	public MainLoop(){
		//game frame init
		GameFrame gameframe = new GameFrame("Test");
		renderer = new GameRender();
		gameframe.add(renderer);
		
		//setting background of game
		background = ImageStyler.loadImg("Art/background1.png");
		panelWidth = background.getWidth();
		panelHeight = background.getHeight();
		
		
		//non scaled background to renderer
		renderer.setBackground(background);
		gameframe.setSize(panelWidth,panelHeight);
	}
	
	//main game update;
	public void run(){
		while(true){
			renderer.draw();
			System.out.println("start");
		}
		
	}
	
	public static void main(String[] args){
		MainLoop main = new MainLoop();
		main.start();
	}
}
