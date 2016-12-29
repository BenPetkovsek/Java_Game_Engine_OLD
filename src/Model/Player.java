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
	
	private boolean facingRight=true;
	
	//Animation Variables
	private BufferedImage[] walkRight= {ImageStyler.loadImg(artDir+"cat1.png"),ImageStyler.loadImg(artDir+"cat2.png"),ImageStyler.loadImg(artDir+"cat3.png")};
	private BufferedImage[] walkLeft = ImageStyler.flipImgs(walkRight);
	private BufferedImage[] idleRight = {ImageStyler.loadImg(artDir+"cat2.png")};
	private BufferedImage[] idleLeft = ImageStyler.flipImgs(idleRight);
	private Animation walkRightAnim;
	private Animation walkLeftAnim;
	private Animation idleRightAnim;
	private Animation idleLeftAnim;
	
	private Animation currentAnim;
	
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
		animInit();
		this.x= x;
		this.y= y;
		scale= 5f;
	}
	//init for all anims
	private void animInit(){
		walkRightAnim= new Animation(true);
		walkRightAnim.addFrame(walkRight[0]).addFrame(walkRight[1]).addFrame(walkRight[2]);
		walkLeftAnim= new Animation(true);
		walkLeftAnim.addFrame(walkLeft[0]).addFrame(walkLeft[1]).addFrame(walkLeft[2]);
		idleRightAnim = new Animation(true);
		idleRightAnim.addFrame(idleRight[0]);
		idleLeftAnim = new Animation(true);
		idleLeftAnim.addFrame(idleLeft[0]);
		
	}
	//main update for the object, is called every loop
	@Override
	public void update(){
		//movement updates
		x +=dx;
		y +=dy;
		if(dx <0){
			currentAnim = walkLeftAnim;
		}
		else if(dx >0){
			currentAnim = walkRightAnim;
		}
		else if(dx==0){
			if(facingRight){
				currentAnim =idleRightAnim;
			}
			else{
				currentAnim = idleLeftAnim;
			}
		}
		currentAnim.update();
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
	public float getDx(){ return dx; }
	
	public float getDy(){ return dy; }
	
	public Animation getAnim(){ return currentAnim; }

	//MOVEMENT/ CONTROLS
	public void moveLeft(){
		if(dx> -maxXSpeed){
			if(dx>0){
				dx=0;
			}
			facingRight=false;
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
			facingRight=true;
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
