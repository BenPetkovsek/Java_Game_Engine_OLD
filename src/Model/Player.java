package Model;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import View.*;

/**
 * Models the main player of the game
 * @author Michael
 *
 */
public class Player extends Collidable {
	int HP, totalHP, str, def, intel;
	String name;
	
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
/*	private float dx;
	private float dy;*/
	
	//movement booleans
	private boolean movingLeft=false;
	private boolean movingRight=false;
	private boolean movingUp=false;
	private boolean movingDown=false;
	
	private boolean noMovement=false;	//if the player can't move
	
	
	//Animator Variables
	private PlayerAnimator animator;
	
	
	//attacking variables
	private boolean attacking=false;
	private Attack currentAttack;	//TODO make a list of attacks , right now there will be one
	
	//Scrolling/deadzone variables
	float bgX =  x;
	float bgY =  y;
	static int bgWidth = GameRender.width;
	static int bgHeight = GameRender.height;
	static int windowWidth = MainLoop.getWindowWidth();
	static int windowHeight = MainLoop.getWindowHeight();
	
	static float deadzoneXOffset = 100;
	static float deadzoneYOffset = 50;
	
	static float deadzoneMaxX = windowWidth - deadzoneXOffset-40;
	static float deadzoneMinX = deadzoneXOffset;
	static float deadzoneMaxY = windowHeight - deadzoneYOffset - 150;
	static float deadzoneMinY = deadzoneYOffset;
	

	
	boolean maxXHit = false;
	boolean minXHit = false;
	boolean maxYHit = false;
	boolean minYHit = false;
	
	
	
	//constructor for simple people like me
	public Player(float x,float y){
		super(x,y);
		animator = new PlayerAnimator(this);
		HP =100;
		setScale(5f);
		drawBorders=true;
		attackInit();
		offsetInit();
		
		//sets the grace period for the player
		updateWindowVars();
	}
	
	private void attackInit(){
		BufferedImage attackWidth;
		BufferedImage idle;
		//current punch attack
		//xOffset is the difference in the sprite compared to idle animation
		float offset = getScale()*(animator.getImage("attackRight", 0).getWidth() - animator.getImage("idleRight", 0).getWidth());
		currentAttack = new Attack(this,getWidth(),0f,30,offset,getHeight(), (int) animator.getAnimationCollection("attackLeft").getDuration(),offset);
	}
	private void offsetInit(){
		//offSetDir is whether the offset was applied to the sprite when it was facing right or not
		//this is important because the offset should reflect when the player also reflects
		offsetDir = facingRight();
		/*setOffsets(getWidth() *0.3f,-getWidth() *0.1f,getHeight() *0.3f,0);*/
		
		collisionBox = new Rectangle2D.Float(x, y, getWidth(), getHeight());
	}
	//main update for the object, is called every loop

	public void update(ArrayList<Collidable> objs){

		checkDeadzoneX();
		checkDeadzoneY();
		//LARGE UPDATE OF DELTA MOVEMENT 
		updateDeltaMovement();
		
		boolean movedX=false;
		boolean movedY=false;
		
		boolean movedBgX=false;
		boolean movedBgY= false;
		//movement updates
		//if we hit any X-edges of background
		if(minXHit ||maxXHit ){	
			//move the character, not background
			x +=dx;
			movedX=true;
			//System.out.println("Moving char - x");
			//if we are not in contact with an X-edge
		}else{
			//move background
			bgX += dx;
			movedBgX=true;	
			//System.out.println("Moving BG - x");
		}
		//if we hit any Y-EDGES
		if(minYHit ||maxYHit){
			//move player
			y +=dy;
			movedY=true;
			//System.out.println("Moving char - y");
			//if we are not in contact with an edge
		}else{
			//move background
			bgY += dy;
			movedBgY=true;
			//System.out.println("Moving BG - y");
		
		}

		
		
		//collision updates
		//idk if this is good practise but i just reverse the changes if it collides
		//then i test each direction (x,y) collisions then give the player back its dx or dy if its not colliding
		//this just translate to the player  being allowed to hitting a wall from the right but still being able to move up and down
		for (Collidable obj: objs){
			obj.setXOffset(-bgX);
			obj.setYOffset(-bgY);
			if (this.checkCollision(obj) && !obj.isTrigger()){
				if(movedX) x-=dx;
				if(movedY) y-=dy;
				if(movedBgX) bgX-=dx;
				if(movedBgY) bgY-=dy;
				
				/*checks if the player is hitting the object from the top or bottom
				 * This means the player can still move in left or right direction
				 */
				obj.setXOffset(-bgX);
				obj.setYOffset(-bgY);
				if(!this.checkTBCollision(obj)){
					if(movedX) x+=dx;
					if(movedBgX) bgX+=dx;

				}
				/*checks if the player is hitting the object from the right or left
				 * This means the player can still move in up or down direction
				 */
				obj.setXOffset(-bgX);
				obj.setYOffset(-bgY);
				if(!this.checkLRCollision(obj)){
					if(movedY) y+=dy;
					if(movedBgY) bgY+=dy;
	

				}
				
			
			}
			GameRender.setBackgroundOffset(-Math.round(bgX), -Math.round(bgY));
		}


		//attacking updates
		//TODO Do attack interruption better
		if(currentAttack != null){
			//if currently attacking, update else dont
			if(attacking){
				if(!currentAttack.isActive() || noMovement){
					if(!facingRight()){
						x+=currentAttack.getOffset();
					}
					currentAttack.stop();
					attacking =false;
				}
				currentAttack.update();
			}
			
		}
		
		animator.update();
		
	}
	
	private void checkDeadzoneX(){
		//System.out.println("PLAYER X POS: " + x);
		//System.out.println("PLAYER Y POS: " + y);
		//System.out.println("WINWIDTH/2: " + windowWidth/2);
		//if window hits the right side of background
		if(bgX >= bgWidth - windowWidth){
			maxXHit = true;
			//System.out.println("MAX X = TRUE");
			//if window hits left side of background
			}else if(bgX <=0){
			minXHit = true;
			//System.out.println("MIN X = TRUE");	
		}
		//if we hit the left side, but are now walking right and get back to the middle
		if(minXHit & x >= windowWidth/2 & facingRight()){
			minXHit = false;
		//if we hit the right side but are now walking left back to the middle
		}else if(maxXHit & x <= windowWidth/2 & !facingRight()){
			maxXHit = false;
		}
		
	}
	

	private void checkDeadzoneY(){
		//if the window hits the bottom of the background
		if(bgY >= bgHeight - windowHeight){
			maxYHit = true;
			//System.out.println("MAX Y = TRUE");
			//if the window hits the top of the background
			}else if(bgY <= 0){
			minYHit = true;
			//System.out.println("MIN Y = TRUE");
			}
		
		//if we hit the top of the background but are now walking down to the middle
		if(minYHit & y >= windowWidth/2 & dy >0){
			minYHit = false;
			//if we hit the bottom of the background but are now walking up towards the middle
		}else if(maxYHit & y <= windowWidth/2 & dy < 0){
			maxYHit = false;
		}
			
	}
	
	/**
	 * movement update based on user input
	 * Now runs on main update loop as opposed to own button listener thread or some shit
	 */
	private void updateDeltaMovement(){
		//LEFT press
		if(movingLeft){
			if(dx> -maxXSpeed && !noMovement){
				if(dx>0){
					dx=0;
				}
				//change direction
				if(facingRight()){
					flip();
				}
				this.dx-=moveSpeedX;
			}
		}
		
		//RIGHT press
		if(movingRight){
			if(dx< maxXSpeed && !noMovement){
				if(dx<0){
					dx=0;
				}
				//change direction
				if(!facingRight()){
					flip();
				}
				this.dx+=moveSpeedX;
			}
		}
		//stop dx if no buttons are pressed and movement is allowed
		if(!movingRight && !movingLeft && !noMovement){
			dx =0;
		}
		//UP press
		if(movingUp){
			if(dy> -maxYSpeed && !noMovement){
				this.dy-=moveSpeedY;
			}	
		}
		
		//DOWN press
		if(movingDown){
			if(dy< maxYSpeed && !noMovement){
				this.dy+=moveSpeedY;
			}	
		}
		
		//stop dy if no buttons are pressed and movement is allowed
		if(!movingDown && !movingUp && !noMovement){
			dy=0;
		}
		
		//movement updates
		/*x +=dx;
		y +=dy;
		getCollisionBox().x +=dx;
		getCollisionBox().y +=dy;*/
	}
	
	//flips image
	private void flip(){
		if(!attacking){
			setFacingRight(!facingRight());
		}
	}
	
	/**
	 * Calculates the offset in x when changing direction
	 * based on difference of offset
	 * @param right - if the player is switching to left and right direction (right == true)
	 * TODO - account for difference cases of offset: both negative, both positive, one positive one negative etc.
	 *//*
	private void offsetXFix(boolean right){
		float diff = Math.abs(xAOffset) - Math.abs(xBOffset);
		if(right){	//switch direction to right
			x-=diff;
		}
		else{	//switch direction to left

			x+=diff;
		}
	}*/
	
	
	//player takes dmg if they arent in grace mode
	//TODO Make it more modular so it takes in an attack or something
	public void takeDamage(int dmg, Enemy enemy){
		if(!isInvulnerable()){
			HP -= dmg;
			checkDeath();
			EffectManager.addEffect(new Invulnerability(70, 10,this));
			EffectManager.addEffect(new KnockBack(enemy,this,150,5));

		}
	}
	
	private void checkDeath(){
		if(HP <= 0){
			//System.out.println("Hero has died!");
		}
	}

	
	
	
	public static void updateWindowVars(){
		bgWidth = GameRender.width;
		bgHeight = GameRender.height;
		windowWidth = MainLoop.getWindowWidth();
		windowHeight = MainLoop.getWindowHeight();
		
		deadzoneMaxX = windowWidth - deadzoneXOffset - 40;
		deadzoneMinX = deadzoneXOffset;
		deadzoneMaxY = windowHeight - deadzoneYOffset - 150;
		deadzoneMinY = deadzoneYOffset;
		
	}
	//GETTERS
/*	public float getDx(){ return dx; }
	
	public float getDy(){ return dy; }*/
	
	@Override
	public Rectangle2D.Float getCollisionBox(){
		
		//mother of all fucking god do this better LMAO
		if(attacking && !facingRight()){
			collisionBox.x = x+ currentAttack.getOffset();
		}
		else{
			collisionBox.x = x;
		}
		
		collisionBox.y = y;
		return collisionBox;
	}
	
	@Override
	public Animation getAnim(){
		return animator.getCurrentAnim();
	}
	
	@Override
	public float getWidth(){
		return getAnim().getCurrFrame().getWidth() * getScale();
	}
	
	@Override
	public float getHeight(){
		return getAnim().getCurrFrame().getHeight() * getScale();
	}
	
	
	public boolean isAttacking(){ return attacking; }

	public Attack getAttack(){ return currentAttack; }
	
	public boolean isFrozen(){ return noMovement; }
	
	public void setFreeze(boolean state){ noMovement = state; }

	//MOVEMENT/ CONTROLS
	public void moveLeft(){
		movingLeft=true;
	}
	//stops the player from moving left
	public void stopMovingLeft(){
		movingLeft=false;

	}
	
	//stops the player from moving right
	public void moveRight(){
		movingRight=true;
	}
	
	public void stopMovingRight(){
		movingRight=false;
	}
	
	//WIP
	public void moveUp(){
		movingUp=true;
	}
	//WIP
	public void stopMovingUp(){
		movingUp= false;
	}
	//WIP
	public void moveDown(){
		movingDown=true;
	}
	//WIP
	public void stopMovingDown(){
		movingDown =false;
	}

	
	public void attack(){
		//cant attack if already attacking or hurt
		//might change this idk
		if(!attacking && !noMovement){
			attacking=true;
			if(!facingRight()){
				//move the character the offset designated to the attack
				x-=currentAttack.getOffset();
			}
			
			currentAttack.activate();
		}
		
	}
}
