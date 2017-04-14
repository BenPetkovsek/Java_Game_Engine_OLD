package PlayerModel;

import java.util.ArrayList;

import GameObjectModel.Collidable;
import GameObjectModel.Direction;
import MiscModel.MainLoop;
import View.GameRender;

/**
 * Models all movement, collision and general physics for the player class
 * seperated from player class as it was getting too large
 * seperation of concerns honestly was the largest factor in creating this class
 * @author Michael
 *
 */
public class PlayerPhysics {
	/*
	 *	MOVEMENT VARIABLES
	 */
	private float moveSpeedX=3f;
	private float moveSpeedY=3f;
	//max speed variables
	private float maxXSpeed= 3f; 
	private float maxYSpeed =3f;

	

	///windows scrolling variables
	private static float bgX;
	private static float bgY;
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
	
	static boolean maxXHit = false;
	static boolean minXHit = false;
	static boolean maxYHit = false;
	static boolean minYHit = false;
	static boolean smallMapY = false;
	static boolean smallMapX = false;
	
	private Player player;
	
	/**
	 * Creates a physics engine "controller" that handles all calculations
	 * @param player the player the physics engine should handle
	 */
	public PlayerPhysics(Player player){
		this.player= player;
		bgX = player.getX();
		bgY = player.getY();
		updateWindowVars();
		
	}
	
	/**
	 * Does all updates necessary needed 
	 * -Map Scrolling
	 * -Movement
	 * -Collision
	 * @param objs The list of collidable objects to check for collision with the player
	 */
	public void update(ArrayList<Collidable> objs){
		
		System.out.println("MaxX: " + maxXHit);
		System.out.println("MinX: " + minXHit);
		System.out.println("MaxY: " + maxYHit);
		System.out.println("MinY: " + minYHit);
		System.out.println("BgY: " + bgY);
		System.out.println("if bgY >= " + (bgHeight - windowHeight));
		
		//LARGE UPDATE OF DELTA MOVEMENT 
		updateDeltaMovement();
		
		boolean movedX=false;
		boolean movedY=false;
		
		boolean movedBgX=false;
		boolean movedBgY= false;
		//movement updates
		checkDeadzoneX();
		checkDeadzoneY();
		//if the window is bigger than the map in the x direction
		if(smallMapX){
		bgX = 0;
		}
		//if we hit any X-edges of background
		if(minXHit ||maxXHit || smallMapX ){	
			//move the character, not background
			player.addX(player.getDx());
			movedX=true;
			//if we are not in contact with an X-edge
		}else{
			//move background
			bgX += player.getDx();
			movedBgX=true;	
		}
		
		
		if(smallMapY){
			bgY = 0;
			}
		//if we hit any Y-EDGES
		if(minYHit ||maxYHit||smallMapY){
			//move player
			player.addY(player.getDy());
			movedY=true;
			//if we are not in contact with an edge
		}else{
			//move background
			bgY += player.getDy();
			movedBgY=true;
		
		}

		//collision updates
		//idk if this is good practise but i just reverse the changes if it collides
		//then i test each direction (x,y) collisions then give the player back its dx or dy if its not colliding
		//this just translate to the player  being allowed to hitting a wall from the right but still being able to move up and down
		for (Collidable obj: objs){
			obj.setXOffset(-bgX);
			obj.setYOffset(-bgY);
			if (player.checkCollision(obj) && !obj.isTrigger()){
				if(movedX) player.addX(-player.getDx());
				if(movedY) player.addY(-player.getDy());
				if(movedBgX) bgX-=player.getDx();
				if(movedBgY) bgY-=player.getDy();
				
				/*checks if the player is hitting the object from the top or bottom
				 * This means the player can still move in left or right direction
				 */
				obj.setXOffset(-bgX);
				obj.setYOffset(-bgY);
				if(!player.checkTBCollision(obj)){
					if(movedX) player.addX(player.getDx());
					if(movedBgX) bgX+=player.getDx();

				}
				/*checks if the player is hitting the object from the right or left
				 * This means the player can still move in up or down direction
				 */
				obj.setXOffset(-bgX);
				obj.setYOffset(-bgY);
				if(!player.checkLRCollision(obj)){
					if(movedY) player.addY(player.getDy());
					if(movedBgY) bgY+=player.getDy();
	

				}
				
			
			}
			GameRender.setBackgroundOffset(-Math.round(bgX), -Math.round(bgY));
		}
	}
	/**
	 * movement update based on user input
	 * Now runs on main update loop as opposed to own button listener thread or some shit
	 */
	private void updateDeltaMovement(){
		boolean movingUp =player.getMovementDir()[0];
		boolean movingDown = player.getMovementDir()[1];
		boolean movingLeft = player.getMovementDir()[2];
		boolean movingRight =player.getMovementDir()[3];
		int isDodging = player.getDodge();
		Double dodgeSpeedMod = 1.05;
		player.updateDodge();
		//LEFT press
		if(movingLeft && isDodging == 0){
			if(player.getDx() > -maxXSpeed && !player.isFrozen()){
				if(player.getDx()>0){
					player.setDx(0);
				}
				//change direction
				if(player.getDirection() == Direction.RIGHT){
					flip();
				}
				player.addDx(-moveSpeedX);
			}
		}else if(movingLeft && isDodging != 0){
			if (isDodging == 1){//if dodge has just started and is ramping up
				player.addDx((float) (-moveSpeedX*dodgeSpeedMod));
			}else if(isDodging == 2){//if dodge is ending and ramping down
				player.addDx((float) (moveSpeedX*dodgeSpeedMod));
			}
		}
		
		//RIGHT press
		if(movingRight && isDodging == 0){
			if(player.getDx()< maxXSpeed && !player.isFrozen()){
				if(player.getDx()<0){
					player.setDx(0);
				}
				//change direction
				if(player.getDirection() != Direction.RIGHT){
					flip();
				}
				player.addDx(moveSpeedX);
			}
		}else if(movingRight && isDodging != 0){
			if (isDodging == 1){//if dodge has just started and is ramping up
				player.addDx((float) (moveSpeedX*dodgeSpeedMod));
			}else if(isDodging == 2){//if dodge is ending and ramping down
				player.addDx((float) (-moveSpeedX*dodgeSpeedMod));
			}
		}
		
		//stop dx if no buttons are pressed and movement is allowed
		if(!movingRight && !movingLeft && !player.isFrozen()){
			player.setDx(0);
		}
		//UP press
		if(movingUp && isDodging == 0){
			if(player.getDy()> -maxYSpeed && !player.isFrozen()){
				player.addDy(-moveSpeedY);
			}	
		}else if(movingUp && isDodging != 0){	//if dodging ONLY in the UP y direction
			if (isDodging == 1){//if dodge has just started and is ramping up
				player.addDy((float) (-moveSpeedY*dodgeSpeedMod));
			}else if(isDodging == 2){//if dodge is ending and ramping down
				player.addDy((float) (moveSpeedY*dodgeSpeedMod));
			}
		}
		
		//DOWN press
		if(movingDown && isDodging == 0){
			if(player.getDy()< maxYSpeed && !player.isFrozen()){
				player.addDy(moveSpeedY);
			}	
		}else if(movingDown && isDodging != 0){//if ONLY dodging DOWN with NO X INPUT
			if (isDodging == 1){//if dodge has just started and is ramping up
				player.addDy((float) (moveSpeedY*dodgeSpeedMod));
			}else if(isDodging == 2){//if dodge is ending and ramping down
				player.addDy((float) (-moveSpeedY*dodgeSpeedMod));
			}
		}
		
		//stop dy if no buttons are pressed and movement is allowed
		if(!movingDown && !movingUp && !player.isFrozen()){
			player.setDy(0);
		}
		
	}
	
	
	//flips image
	private void flip(){
		/*if(!player.isAttacking()){
			player.setFacingRight(!player.facingRight());
		}*/
	}
	//Checks dead zone for X direction
	private void checkDeadzoneX(){
		if (bgWidth - windowWidth <=0){ smallMapX = true;}
		else{ smallMapX = false;}
		//if window hits the right side of background
		if(bgX >= bgWidth - windowWidth){
			maxXHit = true;
			//if window hits left side of background
			}else if(bgX <=0){
			minXHit = true;
		}
		//if we hit the left side, but are now walking right and get back to the middle
		if(minXHit & player.getX() >= windowWidth/2 & player.getDx() > 0){
			minXHit = false;
		//if we hit the right side but are now walking left back to the middle
		}else if(maxXHit & player.getX() <= windowWidth/2 & player.getDx() < 0){
			maxXHit = false;
		}
		
	}
	
	//checks dead zone for y direction
	private void checkDeadzoneY(){
		if (bgHeight - windowHeight <=0){ smallMapY = true;}
		else{ smallMapY = false;}
		//if the window hits the bottom of the background
		if(bgY >= bgHeight - windowHeight){
			maxYHit = true;
			//if the window hits the top of the background
			}else if(bgY <= 0){
			minYHit = true;
			}
		
		//if we hit the top of the background but are now walking down to the middle
		if(minYHit & player.getY()>= windowWidth/2 & player.getDy() >0){
			minYHit = false;
			//if we hit the bottom of the background but are now walking up towards the middle
		}else if(maxYHit & player.getY() <= windowWidth/2 & player.getDy() < 0){
			maxYHit = false;
		}
		

		
		
		
			
	}
	/**
	 * Updates window variables for scrolling
	 * TODO Ben comment this because I dont know what this does either
	 */
	public static void updateWindowVars(){
		bgWidth = GameRender.width;
		bgHeight = GameRender.height;
		windowWidth = MainLoop.getWindowWidth();
		windowHeight = MainLoop.getWindowHeight();
		minXHit = false;
		minYHit = false;
		maxXHit = false;
		maxYHit = false;
	
		
		
	}
	
	public static void setBgX(float x){
		if(x > bgWidth - windowWidth){ bgX = bgWidth - windowWidth;}
		else if(x < 0){bgX = 0;}
		else{bgX = x;}
		
		
	}
	public static void setBgY(float y){
		if(y > bgHeight - windowHeight){ bgY = bgHeight - windowHeight;}
		else if(y < 0){bgY = 0;}
		else{bgY = y;}
	}


}
