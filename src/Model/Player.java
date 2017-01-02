package Model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
	
	
	//Animation Variables
	private BufferedImage[] walkRight= {ImageStyler.loadImg(artDir+"cat1.png"),ImageStyler.loadImg(artDir+"cat2.png"),ImageStyler.loadImg(artDir+"cat3.png")};
	private BufferedImage[] walkLeft = ImageStyler.flipImgs(walkRight);
	private BufferedImage[] idleRight = {ImageStyler.loadImg(artDir+"cat2.png")};
	private BufferedImage[] idleLeft = ImageStyler.flipImgs(idleRight);
	private Animation walkRightAnim;
	private Animation walkLeftAnim;
	private Animation idleRightAnim;
	private Animation idleLeftAnim;
	
	private Animation oldAnim;
	
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
	public Player(float x,float y){
		super(x,y);
		animInit();
		scale= 5f;
		drawBorders=true;
		offsetInit();
	}
	//init for all anims
	private void animInit(){
		//initialize images for all anims
		walkRightAnim= new Animation(true);
		walkRightAnim.addFrame(walkRight[0]).addFrame(walkRight[1]).addFrame(walkRight[2]);
		walkLeftAnim= new Animation(true);
		walkLeftAnim.addFrame(walkLeft[0]).addFrame(walkLeft[1]).addFrame(walkLeft[2]);
		idleRightAnim = new Animation(true);
		idleRightAnim.addFrame(idleRight[0]);
		idleLeftAnim = new Animation(true);
		idleLeftAnim.addFrame(idleLeft[0]);
		
		//init first anim
		if (facingRight){
			currentAnim = idleRightAnim;
		}
		else{
			currentAnim = idleLeftAnim;
		}
		oldAnim = currentAnim;
		
	}
	/*
	 * Okay this is a little weird, basically just sets the offsets for collision
	 * For X offsets, positive goes left and negative goes right
	 * For Y offsets, for some reason positive is down, and negative is up lmao
	 */
	
	private void offsetInit(){
		//offSetDir is whether the offset was applied to the sprite when it was facing right or not
		//this is important because the offset should reflect when the player also reflects
		offsetDir = facingRight;		
		xAOffset = getWidth() *0.2f;
		xBOffset = -getWidth() *0.4f;
		yAOffset = getHeight() *0.2f;
	}
	//main update for the object, is called every loop
	public void update(ArrayList<GameObject> objs){
		
		//movement updates
		x +=dx;
		y +=dy;
		
		//collision updates
		//idk if this is good practise but i just reverse the changes if it collides
		//then i test each direction (x,y) collisions then give the player back its dx or dy if its not colliding
		//this just translate to the player  being allowed to hitting a wall from the right but still being able to move up and down
		for (GameObject obj: objs){
			
			if (this.checkAllCollision(obj)){
				x-=dx;
				y-=dy;
				//THIS PIECE OF CODE ALLOWS YOU TO MOVE IN ONE DIRECTION EVEN IF U COLLIDE IN THE OTHER
				//WITHOUT THIS IT YOU MUST RELEASE THE DIRECTION THAT IS COLLIDE AND IT SUCKS
				//srry for yelling it just took a lot of brain power for some dumb reason
				
				if(this.checkYCollision(obj)){
					x+=dx; 
				}
				if(this.checkXCollision(obj)){
					y+=dy; 
				}
			}
		}
		//animation updates
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
		//This just resets the current animation to start at the begining
		//if the animation changes
		if(oldAnim != currentAnim){
			currentAnim.reset();
		}
		oldAnim = currentAnim;
		
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
	
	//SETTERS
	public void setDx(float dx){ this.dx = dx;}
	public void setDy(float dy){ this.dy = dy;}
	

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
