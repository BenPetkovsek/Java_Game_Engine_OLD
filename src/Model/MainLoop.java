/*
 * Main thread of the game, contains all game initializes (i think) and main(String args[])
 */
package Model;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
	
	private ArrayList<GameObject> things;	//list of objects in the scene minus the player for some reason...
	private PlayerController controller;
	
	private boolean gameRunning=false;
	
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
		controller =new PlayerController(hero);
		renderer.addKeyListener(controller);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		renderer.setFocusable(true); 
		renderer.addNotify();
		renderer.grabFocus();
		
		//Init stuff
		gameRunning =true;
		things = new ArrayList<GameObject>();
		things.add(new Rock(200,200));
		things.add(new Rock(500,300));
		things.add(new Enemy(300,300));

		
	}
	
	
	//main game update;
	//TODO Make the game run at a consistent refresh rate, this will help with stabilizing fps
	//use timers or some shit, just google it

	/* 
	 * UPDATE: I have no idea what im doing but i have somewhat standardized so that each loop is essentially 10 milliseconds...i think idk
	 * This is a very crude way of doing it but it prevents the loop from running fast if the processing is easier etc.
	 */
	public void run(){
		long now;
		long before;
		long diff;
		long varWait;
		long fixedWaitTime= 10;
		int i=0;
		while(gameRunning){
			
			before=System.nanoTime();	//time before
			
			gameUpdate();
			
			now=System.nanoTime();	//time after
			diff= now -before;	//difference between two times
			varWait = fixedWaitTime - (int) diff/1000000;	//the time to wait additionally in milliseconds (1ms =10^-9 ns)
			//System.out.println("diff:" + diff);
			//System.out.println("var Wait:"+varWait);
			try {
				if(varWait >=0){
					this.sleep(varWait);	//I dont want to use thread.sleep because of its inaccuracies but im sure its good enough...i think
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	/*
	 * The main game update, updates objects and renders
	 */
	public void gameUpdate(){
		hero.update(things);
		for (GameObject e: things){
			if(e instanceof Enemy){
				((Enemy) e).update(hero);
			}
			else{
				((Rock) e).update();
			}
			
		}
		renderer.draw(hero,things);
	}
	
	//starting main method
	public static void main(String[] args){
		MainLoop main = new MainLoop();
		main.start();
	}
}
