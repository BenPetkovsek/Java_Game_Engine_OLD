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
	private Rock rock;
	private PlayerController controller;
	//initialization of game settings
	public MainLoop(){
		//game frame init
		GameFrame gameframe = new GameFrame("Test");
		renderer = new GameRender();
		gameframe.add(renderer);
		
		//setting background of game
		background = ImageStyler.loadImg("Art/background1.png");
		
		
		//scaled background
		//renderer.setBackground(background);
		renderer.setBackground(background,background.getWidth(), background.getHeight());
		
		//setting the panel size based on background
		//this can change as the game might not have the background fully cover
		//yolo
		gameframe.setSize(renderer.getWidth(),renderer.getHeight());
		panelWidth =gameframe.getWidth();
		panelHeight = gameframe.getHeight();
		
		//main player controller init
		hero = new Player(0,0);
		rock= new Rock(200,200);
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

	/* OKAY OKAY THIS IS A BIGGER DEAL THAN I THOUGHT
	 * Bigger backgrounds were causing the game to run literally slower,
	 * Like the while loop would run twice as fast if the background was smaller,
	 * now you might think why is this bad? Well if the loop runs twice as fast, the update runs twice as fast, therefore the animation is twice as fast,
	 * therefore everything is fucked. We REALLY need to stabilize the loop so it runs consistently at x milli/nano seconds per loop.
	 */
	public void run(){
		while(true){
			hero.update();
			rock.update(hero);
			renderer.draw(hero,rock);
		}
		
	}
	
	//starting main method
	public static void main(String[] args){
		MainLoop main = new MainLoop();
		main.start();
	}
}
