/*
 * Main thread of the game, contains all game initializes (i think) and main(String args[])
 */
package Model;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import Controller.MouseWheelController;
import Controller.MouseTracking;
import Controller.PlayerController;
import View.*;

public class MainLoop extends Thread {
	
	private static GameRender renderer; 
	private static BufferedImage background;
	
	private static int panelWidth;
	private static int panelHeight;
	
	private Player hero;
	
	private PlayerController controller;
	
	private boolean gameRunning=false;
	public boolean freeze = false;	//debugging pause
	
	//level managing
	static LevelManager levelManager = new LevelManager();
	public static Level currentLevel;
	public static Level previousLevel;
	
	
	//initialization of game settings
	public MainLoop(){
		//game frame init
		GameFrame gameframe = new GameFrame("Test");
		renderer = new GameRender();
		gameframe.add(renderer);
		
		//Map init stuff
		//load maps, set current map
		levelManager.initializeLevels();
		currentLevel = levelManager.getLevel("Test1");
		//System.out.println(currentLevel.fileName);
		
		//setting background of game
		background = ImageStyler.loadImg(currentLevel.background);
		
		
		//scaled background
		//renderer.setBackground(background);
		renderer.setBackground(background,background.getWidth(), background.getHeight());
		
		//setting the panel size based on background
		//this can change as the game might not have the background fully cover
		//yolo
		gameframe.setSize(600,600);
		panelWidth =gameframe.getWidth();
		panelHeight = gameframe.getHeight();
		
		//Adding Controllers
		hero = new Player(350,300);
		renderer.addKeyListener(new PlayerController(hero));
		renderer.addKeyListener(new GameListener());
		renderer.addMouseMotionListener(new MouseTracking(hero));
		renderer.addMouseWheelListener(new MouseWheelController(hero));
		
		
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		renderer.setFocusable(true); 
		renderer.addNotify();
		renderer.grabFocus();
	
		
		//Init stuff
		gameRunning =true;

		
	}
	
	
	//main game update;
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
				
				//update game if not paused
				if(!freeze){
					gameUpdate();
				}
				
				
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
		//effect manager update
		EffectManager.update();
		
		hero.update(currentLevel.collidableObjects);
		
		for (GameObject e: currentLevel.levelObjects){
			if(e instanceof Enemy){
				((Enemy) e).update(hero);
				e.setXOffset(GameRender.getBGOffsetX());
				e.setYOffset(GameRender.getBGOffsetY());
			}
			else if (e instanceof LoadTrigger){
				((LoadTrigger) e).update(hero);
				e.setXOffset(GameRender.getBGOffsetX());
				e.setYOffset(GameRender.getBGOffsetY());
			}else{
				((TerrainObject) e).update();
				e.setXOffset(GameRender.getBGOffsetX());
				e.setYOffset(GameRender.getBGOffsetY());
			}
			
		}
		renderer.draw(hero,currentLevel.levelObjects);
	}
	public static  int getWindowWidth(){
		return panelWidth;
	}
	public  static int getWindowHeight(){
		return panelHeight;
	}
	
	public boolean isRunning(){ return gameRunning; }
	
	public void setRunning(boolean state) { gameRunning= state; }
	
	public static void changeCurrentLevel(String newLevelName){
		Level newLevel = levelManager.getLevel(newLevelName);
		previousLevel = currentLevel;
		currentLevel = newLevel;
		background = ImageStyler.loadImg(currentLevel.background);
		renderer.setBackground(background,background.getWidth(), background.getHeight());
		PlayerPhysics.updateWindowVars();

	}
	
	
	//starting main method
	public static void main(String[] args){
		MainLoop main = new MainLoop();
		main.start();
	}
	
	//debugger listner
	// z to pause
	// hold or press x to go frame by frame
	//ty mario man
	private class GameListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_Z) { // pause
				MainLoop.this.freeze = !MainLoop.this.freeze;
	        }
			if (key == KeyEvent.VK_X){
				if(MainLoop.this.freeze){
					MainLoop.this.gameUpdate();
				}
			}
		}

	}
	
	
}
