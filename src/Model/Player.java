package Model;

import java.awt.image.BufferedImage;

import View.*;

public class Player extends GameObject {
	int HP, totalHP, str, def, intel;
	String name;
	private final String artDir= "Art/";
	
	/*
	 *	MOVEMENT VARIABLES
	 */
	private float moveSpeedX=1f;
	private float moveSpeedY=1f;
	//max speed variables
	private float maxXSpeed= 1f; 
	private float maxYSpeed =1f;
	//delta variables
	//NOTE: I have no idea what im doing
	private float dx;
	private float dy;
	
	
	private float tempDx;
	//complicated constructor for future releases with stats
	public Player(int x, int y,String initName, int initHP, int initStr, int initDef, int initIntel ){
		this(x,y);
		HP = initHP;
		totalHP = initHP;
		str = initStr;
		def = initDef;
		intel = initIntel;
		name = initName;
		isCollidable = true;
	}
	
	//constructor for simple people like me
	public Player(int x,int y){
		this.x= x;
		this.y= y;
		sprite= ImageStyler.loadImg(artDir + "hero.png");
		scale= 0.5f;
	}
	
	//main update for the object, is called every loop
	@Override
	public void update(){
		//if(tempDx != dx){ System.out.println("DX:" + dx); }
		x +=dx;
		y +=dy;
		tempDx = dx;
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
	
	public float getDx(){ return dx; }
	
	public float getDy(){ return dy; }

	//MOVEMENT/ CONTROLS
	public void moveLeft(){
		if(dx> -maxXSpeed){
			if(dx>0){
				dx=0;
			}
			this.dx-=moveSpeedX;
		}
	}
	
	public void stopMovingLeft(){
		if (dx <0){
			dx=0;
		}
		
	}
	
	public void moveRight(){
		if(dx< maxXSpeed){
			if(dx<0){
				dx=0;
			}
			this.dx+=moveSpeedX;
		}
	}
	
	public void stopMovingRight(){
		if (dx >0){
			dx=0;
		}
	}
	
	public void moveUp(){
		if(dy> -maxYSpeed){
			this.dy-=moveSpeedY;
		}
	}
	
	public void stopMovingUp(){
		dy=0;
	}
	
	public void moveDown(){
		if(dy< maxYSpeed){
			this.dy+=moveSpeedY;
		}
	}
	
	public void stopMovingDown(){
		dy=0;
	}

	@Override
	public void spawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}
}//end class
