/*
 * The base class for every object in the game
 * Abstract class
 */
package Model;

import java.awt.image.BufferedImage;

public abstract class GameObject {

	protected float x,y;
	protected float scale;
	protected BufferedImage sprite;
	protected boolean isCollidable;
	
	public abstract void update();
	
	public abstract void spawn();
	
	public abstract void die();
	
	public float getX(){ return x; }
	
	public float getY(){ return y; }
	
	public float getScale(){ return scale; }
	
	public BufferedImage getSprite(){ return sprite; }
	
	public float getWidth(){
		if(sprite !=null){
			return ((float) getSprite().getWidth())*scale;
		}
		return 0;
		
	}
	
	public float getHeight(){
		if (sprite != null){
			return ((float) getSprite().getHeight())*scale;
		}
		return 0;
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
