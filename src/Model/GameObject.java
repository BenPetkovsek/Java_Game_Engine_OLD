
package Model;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
/*
 * The base class for every object in the game	
 * Abstract class
 */
public abstract class GameObject {

	protected float x,y;
	protected float dx,dy;
	protected float xOffset, yOffset;	//offsets of the screen caused by map scrolling by character

	public GameObject(float x, float y){
		this.x = x;
		this.y =y;
		//animations= new ArrayList<Animation>();
	}
	
	//Types of Updates
	public void update(){ };
	
	public void update(GameObject e){ };
	
	
	
	//GETTERS
	public float getX(){ return x; }
	
	public float getY(){ return y; }
	
	public float getDx(){ return dx; }
	
	public float getDy(){ return dy; }
	
	//SETTERS
	public void setXOffset(float newX){ xOffset = newX; }
	public void setYOffset(float newY){ yOffset = newY; }
	
	public void setDx(float dx) { this.dx = dx; }
	public void setDy(float dy) { this.dy = dy; }
	



	
	
	
	
}
