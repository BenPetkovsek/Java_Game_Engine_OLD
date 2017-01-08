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
	private float moveSpeedX=3f;
	private float moveSpeedY=3f;
	//max speed variables
	private float maxXSpeed= 3f; 
	private float maxYSpeed =3f;
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
		
	}
	
	//constructor for simple people like me
	public Player(float x,float y){
		super(x,y);
		HP =100;
		isCollidable = true;
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
	
	private void offsetInit(){
		//offSetDir is whether the offset was applied to the sprite when it was facing right or not
		//this is important because the offset should reflect when the player also reflects
		offsetDir = facingRight;
		setOffsets(getWidth() *0.3f,-getWidth() *0.1f,getHeight() *0.3f,0);
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
				
				if(this.checkYCollision(obj) && !this.checkXCollision(obj)){
					x+=dx; 
				}
				if(this.checkXCollision(obj) && !this.checkYCollision(obj)){
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
			//change direction
			if(facingRight){
				facingRight=false;
				offsetXFix(false);
			}
			this.dx-=moveSpeedX;
			
		}
	}
	//stops the player from moving left
	public void stopMovingLeft(){
		if (dx <0){
			dx=0;
		}
		
	}
	
	//stops the player from moving right
	public void moveRight(){
		if(dx< maxXSpeed){
			if(dx<0){
				dx=0;
			}
			//change direction
			if(!facingRight){
				facingRight=true;
				offsetXFix(true);
				
			}
			this.dx+=moveSpeedX;
			
		}
	}
	
	public void stopMovingRight(){
		if (dx >0){
			dx=0;
		}
	}
	//WIP
	public void moveUp(){
		if(dy> -maxYSpeed){
			this.dy-=moveSpeedY;
		}
	}
	//WIP
	public void stopMovingUp(){
		dy=0;
	}
	//WIP
	public void moveDown(){
		if(dy< maxYSpeed){
			this.dy+=moveSpeedY;
		}
	}
	//WIP
	public void stopMovingDown(){
		dy=0;
	}

	/**
	 * Calculates the offset in x when changing direction
	 * based on difference of offset
	 * @param right - if the player is switching to left and right direction (right == true)
	 * TODO - account for difference cases of offset: both negative, both positive, one positive one negative etc.
	 */
	private void offsetXFix(boolean right){
		float diff = Math.abs(xAOffset) - Math.abs(xBOffset);
		if(right){	//switch direction to right
			x-=diff;
		}
		else{	//switch direction to left

			x+=diff;
		}
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
