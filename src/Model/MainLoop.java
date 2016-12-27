/*
 * Main thread of the game, contains all game initializes (i think) and main(String args[])
 */
package Model;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.util.Timer;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import Controller.PlayerController;
import View.*;

public class MainLoop extends Thread {
	
	private GameRender renderer; 
	private BufferedImage background;
	private int panelWidth;
	private int panelHeight;
	
	private Player hero;
	private Player enemy;
	private PlayerController controller;
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
		
		hero = new Player(0,0);
		
		controller =new PlayerController(hero);
		renderer.addKeyListener(controller);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		renderer.setFocusable(true); 
		renderer.addNotify();
		renderer.grabFocus();

		
	}
	
	
	//main game update;
	//TODO Make the game run at a consistent refresh rate, this will help with stabilizing fps
	//use timers or some shit, just google it
	public void run(){
		while(true){
			hero.update();
			renderer.draw(hero); 
		}
		
	}
	
	//starting main method
	public static void main(String[] args){
		MainLoop main = new MainLoop();
		main.start();
	}
}
