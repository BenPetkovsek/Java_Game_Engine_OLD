/*
 * The base class for every object in the game
 * Abstract class
 */
package Model;

import java.awt.image.BufferedImage;

public class GameObject {

	protected float x,y;
	protected float scale;
	protected boolean isCollidable;
	
	protected Animation currentAnim;
	
	public GameObject(){
		
	}
	public GameObject(float x, float y){
		
	}
	
	public void update(){ };
	
	public void spawn(){ };
	
	public void die(){ };
	
	public float getX(){ return x; }
	
	public float getY(){ return y; }
	
	public float getScale(){ return scale; }
	
	
	public float getWidth(){
		if(currentAnim !=null){
			return ((float) currentAnim.getCurrFrame().getWidth())*scale;
		}
		return 0;
		
	}
	
	public float getHeight(){
		if(currentAnim !=null){
			return ((float) currentAnim.getCurrFrame().getHeight())*scale;
		}
		return 0;
	}
	
	public Animation getAnim(){
		return currentAnim;
	}
	
	
	public boolean isCollidable(){ return isCollidable; }
	
	//checks if the object has collided at all, not to be used for movement calc, used for interactions between objects
	public boolean checkAllCollision(GameObject otherObject){
		boolean collided= checkXCollision(otherObject) || checkYCollision(otherObject);

		return collided;
	}
	//checks if x collision is detected
	//NOTE: Does not check for percentage of collision or offsets YET
	public boolean checkXCollision(GameObject otherObject){
		boolean xCollide = false;
		float width = getWidth();
		float otherX = otherObject.getX();
		float otherWidth = otherObject.getWidth();
		
		xCollide =((x <= otherX && otherX <= x+width) || (x<=otherX+otherWidth && otherX+otherWidth <= x+width));
		return xCollide;
	}
	//checks if y collision is detected
	//NOTE: Does not check for percentage of collision or offsets YET
	public boolean checkYCollision(GameObject otherObject){
		boolean yCollide = false;
		float height = getHeight();
		float otherY = otherObject.getY();
		float otherHeight = otherObject.getHeight();
		
		yCollide =((y <= otherY && otherY <= y+height) || (y<=otherY+otherHeight && otherY+otherHeight <= y+height));
		return yCollide;
	}
	
	
	
	
}
