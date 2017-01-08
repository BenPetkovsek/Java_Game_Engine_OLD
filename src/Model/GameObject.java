/*
 * The base class for every object in the game
 * Abstract class
 */
package Model;

import java.awt.image.BufferedImage;

public class GameObject {

	protected float x,y;
	protected float scale=1;
	protected boolean isCollidable=true;
	
	//Collision offset Variables
	//Since the collision system uses the sprites height and width,
	//this allows it to make the collision detect either smaller or larger or offset boundaries, helps with sprites that have like a tail or some shit
	protected float xAOffset =0;	//left offset
	protected float xBOffset =0;	//right offset
	protected float yAOffset =0;	//top offset
	protected float yBOffset =0;	//bottom offset
	protected boolean offsetDir = true;
	
	//direction of sprite
	protected boolean facingRight =true;
	
	protected boolean drawBorders=false;	//FOR DEBUGGING TO SEE COLLISION BORDERS
	
	//current animation
	protected Animation currentAnim;	
	
	public GameObject(float x, float y){
		this.x = x;
		this.y =y;
	}
	
	public void update(){ };
	
	public void spawn(){ };
	
	public void die(){ };
	
	
	//GETTERS
	public float getX(){ return x; }
	
	public float getY(){ return y; }
	
	public float getScale(){ return scale; }
	
	public boolean facingRight(){ return facingRight; }
	
	public boolean offSetDir() {return offsetDir; }
	
	public boolean debug(){ return drawBorders; }
	
	/**
	 * 
	 * @return float array: 0 - xAOffset, 1 - xBOffset , 2 - yAOffset, 3 - yBOffset
	 */
	public float[] getOffsets(){
		float[] offs = {xAOffset,xBOffset, yAOffset,yBOffset};
		return offs;
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
	public void setOffsets(float xAOffset, float xBOffset, float yAOffset, float yBOffset ){
		this.xAOffset =xAOffset;
		this.xBOffset = xBOffset;
		this.yAOffset = yAOffset;
		this.yBOffset = yBOffset;
	}
	
	//COLLISION
	public boolean isCollidable(){ return isCollidable; }
	
	//checks if the object has collided at all, not to be used for movement calc, used for interactions between objects
	public boolean checkAllCollision(GameObject otherObject){
		boolean collided= checkXCollision(otherObject) && checkYCollision(otherObject);
		return collided;
	}
	//checks if y collision is detected
	//The calculations are kinda misleading but to check if there is a collision on the left or right, you must actually
	// check if the y coordinates are intersecting
	//just trust me
	//pls
	public boolean checkYCollision(GameObject otherObject){
		boolean xCollide = false;
		float width = getWidth();
		
		float otherX = otherObject.getX();
		float otherWidth = otherObject.getWidth();
		
		//offset collision detection, im a god
		
		float xA  = (facingRight == offsetDir) ? (x +xAOffset): (x-xBOffset);
		float xB =  (facingRight == offsetDir) ? (x +width +xBOffset): (x +width -xAOffset);
		
		float otherXA = (otherObject.facingRight() == otherObject.offSetDir()) ? (otherX + otherObject.getOffsets()[0]) : (otherX - otherObject.getOffsets()[1]);
		float otherXB = (otherObject.facingRight() == otherObject.offSetDir()) ? (otherX +otherWidth+ otherObject.getOffsets()[1]) : (otherX +otherWidth - otherObject.getOffsets()[0]);
		
		//Essentially this is just a simplied way of checking if range of pixels the pictures take up intersect horizontally
		xCollide = (xA <= otherXB) && (otherXA <= xB);
		return xCollide;
	}
	//checks if x collision is detected
	//The calculations are kinda misleading but to check if there is a collision on the top or bottom, you must actually
	// check if the x coordinates are intersecting
	//just trust me
	//pls
	public boolean checkXCollision(GameObject otherObject){
		boolean yCollide = false;
		float height = getHeight();
		float otherY = otherObject.getY();
		float otherHeight = otherObject.getHeight();
		
		float yA  =y +yAOffset;
		float yB = y +height +yBOffset;
		
		float otherYA = otherY + otherObject.getOffsets()[2];
		float otherYB = otherY +otherHeight+ otherObject.getOffsets()[3];
		
		//Essentially this is just a simplied way of checking if range of pixels the pictures take up intersect vertically
		yCollide = (yA <= otherYB) && (otherYA<= yB);
		return yCollide;
	}
	
	
	
	
}
