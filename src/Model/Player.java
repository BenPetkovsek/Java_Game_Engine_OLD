package Model;

import java.awt.image.BufferedImage;

import View.*;

public class Player {
	int HP, totalHP, str, def, intel;
	String name;
	private final String artDir= "Art/";
	private BufferedImage art;
	private int x,y;
	private float scale=1;
	private final int dx=20;
	private final int dy=20;
	public Player(int x, int y,String initName, int initHP, int initStr, int initDef, int initIntel ){
		this.x= x;
		this.y= y;
		HP = initHP;
		totalHP = initHP;
		str = initStr;
		def = initDef;
		intel = initIntel;
		name = initName;
		art= ImageStyler.loadImg(artDir + "hero.png");
		scale= 0.01f;
	}

	public Player(int x,int y){
		this.x= x;
		this.y= y;
		art= ImageStyler.loadImg(artDir + "hero.png");
		scale= 0.5f;
	}
	
	public void takeDamage(int dmg){
		HP -= dmg;
		checkDeath();
	}
	
	private void checkDeath(){
		if(HP <= 0){
			System.out.println(name + " has died!");
		}
	}
	
	//GETTERS
	public BufferedImage getImage(){ return art; }
	
	public int getX(){ return x; }
	
	public int getY(){ return y; }
	
	//scales the image to the screen
	public float getScale(){ return scale; }
	
	
	//SETTERS
	public void setX(int x){
		this.x =x;
	}
	
	public void setY(int y){
		this.y =y;
	}
	
	//MOVEMENT/ CONTROLS
	public void moveLeft(){
		this.x-=dx;
	}
	
	public void moveRight(){
		this.x+=dx;
	}
	
	public void moveUp(){
		this.y-=dy;
	}
	
	public void moveDown(){
		this.y+=dy;
	}
}//end class
