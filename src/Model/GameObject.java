/*
 * The base class for every object in the game
 * Abstract class
 */
package Model;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameObject {

	protected float x,y;
	protected float dx,dy;
	protected float xOffset, yOffset;	//offsets of the screen caused by map scrolling by character
	protected float scale=1;
	protected boolean isCollidable=true;
	
	//Collision offset Variables
	//Since the collision system uses the sprites height and width,
	//this allows it to make the collision detect either smaller or larger or offset boundaries, helps with sprites that have like a tail or some shit
/*	protected float xAOffset =0;	//left offset
	protected float xBOffset =0;	//right offset
	protected float yAOffset =0;	//top offset
	protected float yBOffset =0;	//bottom offset
*/	protected boolean offsetDir = true;
	
	protected Rectangle2D.Float collisionBox;
	
	protected boolean collisionOverride=false;
	
	//direction of sprite
	protected boolean facingRight =true;
	
	protected boolean drawBorders=true;	//FOR DEBUGGING TO SEE COLLISION BORDERS
	
	//current animation
	protected Animation currentAnim;
	//protected ArrayList<Animation> animations;	//animation priority list, any animation can interrupt animations with less index
	
	public GameObject(float x, float y){
		this.x = x;
		this.y =y;
		//animations= new ArrayList<Animation>();
	}
	
	public void update(){ };
	
	public void spawn(){ };
	
	public void die(){ };
	
	
	//GETTERS
	public float getX(){ return x; }
	
	public float getY(){ return y; }
	
	public float getDx(){ return dx; }
	
	public float getDy(){ return dy; }
	
	public float getScale(){ return scale; }
	
	public boolean facingRight(){ return facingRight; }
	
	public boolean offSetDir() {return offsetDir; }
	
	public boolean debug(){ return drawBorders; }
	
/*	*//**
	 * 
	 * @return float array: 0 - xAOffset, 1 - xBOffset , 2 - yAOffset, 3 - yBOffset
	 *//*
	public float[] getOffsets(){
		if(currentAnim !=null && currentAnim.getOffsets() != null){
			return currentAnim.getOffsets();
			
		}
		else{
			float[] offs = {xAOffset,xBOffset, yAOffset,yBOffset};
			return offs;
		}
		
	}*/
	
	//returns collision box set
	//updates the x and y of the collision box whenever this is called as thats when it actually matters
	public Rectangle2D.Float getCollisionBox(){
		collisionBox.x =x + xOffset;
		collisionBox.y =y + yOffset;
		return collisionBox;
	}
	
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
	public void setXOffset(float newX){
		xOffset = newX;
	}
	public void setYOffset(float newY){
		yOffset = newY;
	}
	
	public void setDx(float dx) { this.dx = dx; }
	public void setDy(float dy) { this.dy = dy; }
	//SETTERS
	/*
	 * Sets the scale of the images of the game object
	 */
	public void setScale(float newScale) { scale = newScale; }
	
	/*
	 * sets the offsets of the collision of the object
	 * Okay this is a little weird, basically just sets the offsets for collision
	 * For X offsets, positive goes left and negative goes right
	 * For Y offsets, for some reason positive is down, and negative is up lmao
	 * NOTE: This doesn't check if offsets are negatively too big and inverts and fucks everything up. Im sure you can handle this guys
	 */
	/*public void setOffsets(float xAOffset, float xBOffset, float yAOffset, float yBOffset ){
		this.xAOffset =xAOffset;
		this.xBOffset = xBOffset;
		this.yAOffset = yAOffset;
		this.yBOffset = yBOffset;
	}*/
	
	
	//COLLISION
	public boolean isCollidable(){ return isCollidable; }
	
	//checks if the object has collided at all, used for interactions between objects
	//Uses built in intersect functions
	public boolean checkCollision(GameObject otherObject){

		return getCollisionBox().intersects(otherObject.getCollisionBox());
	}
	//checks collision from (L)eft and (R)ight side
	//uses intersecting number ranges
	//currently only works for rectangles
	public boolean checkLRCollision(GameObject otherObject){
		boolean xCollide = false;
		Double colX = getCollisionBox().getX();
		Double colWidth = getCollisionBox().getWidth();
		
		Double otherX = otherObject.getCollisionBox().getX();
		Double otherColWidth = otherObject.getCollisionBox().getWidth();
		xCollide = (colX < (otherX+otherColWidth)) && (otherX < (colX+colWidth));
		return xCollide;
		
	}
	//checks collision from (T)op and (B)ottom side
	//uses intersecting number ranges
	//currently only works for rectangles
	public boolean checkTBCollision(GameObject otherObject){
		boolean yCollide = false;
		Double colY = getCollisionBox().getY();
		Double colHeight = getCollisionBox().getHeight();
		
		Double otherY = otherObject.getCollisionBox().getY();
		Double otherColHeight = otherObject.getCollisionBox().getHeight();
		
		yCollide = (colY < (otherY+otherColHeight)) && (otherY < (colY+colHeight));
		
		return yCollide;

	}
	
	
	
	
}
