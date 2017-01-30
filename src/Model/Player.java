package Model;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import View.*;

public class Player extends GameObject {
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
	
	//Animation Variables
	private BufferedImage[] walkRight= {ImageStyler.loadImg("heroWalk1.png"),ImageStyler.loadImg("heroWalk2.png"),ImageStyler.loadImg("heroWalk3.png")};
	private BufferedImage[] walkLeft = ImageStyler.flipImgs(walkRight);
	
	private Animation walkRightAnim;
	private Animation walkLeftAnim;
	
	private BufferedImage[] idleRight = {ImageStyler.loadImg("heroIdle.png")};
	private BufferedImage[] idleLeft = ImageStyler.flipImgs(idleRight);
	
	private Animation idleRightAnim;
	private Animation idleLeftAnim;
	
	private BufferedImage[] attackRight = {ImageStyler.loadImg("heroAttack.png")};
	private BufferedImage[] attackLeft =ImageStyler.flipImgs(attackRight);
	
	private Animation attackRAnim;
	private Animation attackLAnim;
	
	private Animation oldAnim;
	
	//effects variables
	private Invulnerability grace;	//this is when the player gets hit, it allows them to be invincible for a second to prevent insta death
	
	private KnockBack knockback;
	
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
	
	static float deadzoneXOffset = 300;
	static float deadzoneYOffset = 200;
	
	static float deadzoneMaxX = windowWidth - deadzoneXOffset-40;
	static float deadzoneMinX = deadzoneXOffset;
	static float deadzoneMaxY = windowHeight - deadzoneYOffset - 150;
	static float deadzoneMinY = deadzoneYOffset;
	

	
	boolean maxXHit = false;
	boolean minXHit = false;
	boolean maxYHit = false;
	boolean minYHit = false;
	
	
/*	//complicated constructor for future releases with stats
	public Player(int x, int y,String initName, int initHP, int initStr, int initDef, int initIntel ){
		this(x,y);
		HP = initHP;
		totalHP = initHP;
		str = initStr;
		def = initDef;
		intel = initIntel;
		name = initName;
		
	}*/
	
	//constructor for simple people like me
	public Player(float x,float y){
		super(x,y);
		HP =100;
		isCollidable = true;
		scale= 5f;
		drawBorders=true;
		animInit();
		attackInit();
		offsetInit();
		
		//sets the grace period for the player
		grace= new Invulnerability(70, 10);
		updateWindowVars();
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
		attackRAnim = new Animation(false).addFrame(attackRight[0]);
		attackRAnim.setInterruptable(false);
		attackLAnim = new Animation(false).addFrame(attackLeft[0]);
		attackLAnim.setInterruptable(false);
		
		
		//init first anim
		if (facingRight){
			currentAnim = idleRightAnim;
		}
		else{
			currentAnim = idleLeftAnim;
		}
		oldAnim = currentAnim;
		
	}
	
	private void attackInit(){
		//current punch attack
		//xOffset is the difference in the sprite compared to idle animation
		float offset = scale*(attackRight[0].getWidth() - idleRight[0].getWidth());
		currentAttack = new Attack(this,getWidth(),0f,30,offset,getHeight(), (int) attackLAnim.getDuration(),offset);
	}
	private void offsetInit(){
		//offSetDir is whether the offset was applied to the sprite when it was facing right or not
		//this is important because the offset should reflect when the player also reflects
		offsetDir = facingRight;
		/*setOffsets(getWidth() *0.3f,-getWidth() *0.1f,getHeight() *0.3f,0);*/
		
		collisionBox = new Rectangle2D.Float(x, y, getWidth(), getHeight());
	}
	//main update for the object, is called every loop

	public void update(ArrayList<GameObject> objs){
		//System.out.println("Background X: " + bgX + " Background Y: " + bgY);
		//System.out.println("Player dx: " + dx + "Player dy: " + dy);
		checkDeadzoneX();
		checkDeadzoneY();
		//LARGE UPDATE OF DELTA MOVEMENT 
		updateDeltaMovement();
		
		//movement updates
		if(checkDeadzoneX() && checkDeadzoneY()){
			x +=dx;
			y +=dy;
		}else{
			if((maxXHit && facingRight) || (minXHit && !facingRight)){
				x += dx;
			}else{
				bgX += dx;
			}
			
			if((maxYHit && dy >0) || (minYHit && dy < 0)){
				y += dy;
			}else{
				bgY += dy;
			}
				
				
		}


		
		
		//collision updates
		//idk if this is good practise but i just reverse the changes if it collides
		//then i test each direction (x,y) collisions then give the player back its dx or dy if its not colliding
		//this just translate to the player  being allowed to hitting a wall from the right but still being able to move up and down
		for (GameObject obj: objs){
			
			if (this.checkCollision(obj) && obj.isCollidable()){
				if(checkDeadzoneX() && checkDeadzoneY()){
					x -=dx;
					y -=dy;
				}else{
						bgX -= dx;
						bgY -= dy;	
				}
				//THIS PIECE OF CODE ALLOWS YOU TO MOVE IN ONE DIRECTION EVEN IF U COLLIDE IN THE OTHER
				//WITHOUT THIS IT YOU MUST RELEASE THE DIRECTION THAT IS COLLIDE AND IT SUCKS
				//srry for yelling it just took a lot of brain power for some dumb reason
				
				if(this.checkLRCollision(obj) && !this.checkTBCollision(obj)){
					if(checkDeadzoneX()){
						x +=dx;
					}else{
						bgX += dx;
						
					}
				}
				if(this.checkTBCollision(obj) && !this.checkLRCollision(obj)){
					if(checkDeadzoneY()){
						y +=dy;
					}else{
						
						bgY += dy;
					}
				}
			
			}
			GameRender.setBackgroundOffset(-Math.round(bgX), -Math.round(bgY));
		}
		
		
		
		//grace updates
		if(grace.going()){
			grace.update();
		}
		
		//knockback updates
		//TODO have it so the knockback class does the movement for the source object
		//		only problem is that the collision updates must now be a method that gets called by that type of stuff so the integrity still applies
		if(knockback !=null){
			if(knockback.getStatus()){
				knockback.update();
/*				dx += knockback.getKnockback()[0];
				dy += knockback.getKnockback()[1];*/
			}
			else{	
				//reset unless already moving
				/*if(dx != 0){
					dx =0;
				}
				if(dy != 0){
					dy =0;
				}*/
				knockback=null;	//reset
				noMovement=false;	//reset
			}
		}
		
		//attacking updates
		if(currentAttack != null){
			//if currently attacking, update else dont
			if(attacking){
				if(!currentAttack.isActive()){
					if(!facingRight){
						x+=currentAttack.getOffset();
					}
					attacking =false;
				}
				currentAttack.update();
			}
			
		}
		//animation updates
		//TODO have priority animations that will override, get rid of interruptable shit
		if(currentAnim.interruptable() || currentAnim.isFinished()){		//dont change animation unless its interruptable or done
			
			if(dx <0){
				//System.out.println("walkL");
				currentAnim = walkLeftAnim; 
			}
			else if(dx >0){
				//System.out.println("walkR");
				currentAnim = walkRightAnim;
			}
			else if(dx==0){
				//System.out.println("idle");
				//currentAnim = walkLeftAnim;
				currentAnim = (facingRight) ? idleRightAnim : idleLeftAnim;
			}
			
			//attacking overrides movement animation
			if(attacking){
				currentAnim = (facingRight) ? attackRAnim: attackLAnim;
			}
			//This just resets the current animation to start at the begining
			//if the animation changes
			
			if(oldAnim != currentAnim){
				//System.out.println("reset");
				currentAnim.reset();
			}
			oldAnim = currentAnim;
			
		}
		
		currentAnim.update();
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
				if(facingRight){
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
				if(!facingRight){
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
			facingRight = !facingRight;
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
		if(!grace.going()){
			grace.start();
			HP -= dmg;
			checkDeath();
			knockback = new KnockBack(enemy,this,150,8);
			noMovement =true;
		}
	}
	
	private void checkDeath(){
		if(HP <= 0){
			//System.out.println("Hero has died!");
		}
	}

	private boolean checkDeadzoneX(){
		if(bgX >= bgWidth - windowWidth){
			maxXHit = true;
			//System.out.println("MAX X = TRUE");
			}
		else {
			maxXHit = false;
			}
		
		if(bgX <=0){
			minXHit = true;
			//System.out.println("MIN X = TRUE");
		}else{
			minXHit = false;
		}
		
		
		if((x > deadzoneMinX && x < deadzoneMaxX)||(x < deadzoneMinX && facingRight || x > deadzoneMaxX && !facingRight || dx ==0)){
			//move character
			return true;
		}else{
			if(bgX <= 0 && !facingRight || bgX >= bgWidth - windowWidth && facingRight){
				//move character
				return true;
			}else{
				
				//move background
				return false;
			}
			
		}
	}

	private boolean checkDeadzoneY(){
		if(bgY >= bgHeight - windowHeight){
			maxYHit = true;
			//System.out.println("MAX Y = TRUE");
			}
		else {maxYHit = false;}
		
		if(bgY <= 0){
			minYHit = true;
			//System.out.println("MIN Y = TRUE");
			}
		else{minYHit = false;}
		
		
		if((y > deadzoneMinY && y < deadzoneMaxY) ||(y < deadzoneMinY && dy > 0 || y > deadzoneMaxY && dy < 0 || dy ==0)){
			//move character
			return true;
		}else{
			if(bgY <= 0 && dy <0 || bgY >= bgHeight - windowHeight && dy >0 ){
				//move character
				return true;
			}else{
				//move background
				return false;
			}
			
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
		return collisionBox;
	}
	
	public boolean isAttacking(){ return attacking; }

	public Attack getAttack(){ return currentAttack; }
	
	public boolean isBlinked(){ return grace.getBlink(); }
	
	//SETTERS
/*	public void setDx(float dx){ this.dx = dx;}
	public void setDy(float dy){ this.dy = dy;}*/
	

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
		//cant attack if already attacking
		//might change this idk
		if(!attacking){
			attacking=true;
			if(!facingRight){
				//move the character the offset designated to the attack
				x-=currentAttack.getOffset();
			}
			
			currentAttack.activate();
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
