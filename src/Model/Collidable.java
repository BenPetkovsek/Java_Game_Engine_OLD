package Model;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * Models a physical object in the game meaning it can be interacted with.
 * Collision and collision box/trigger box is kept in this classes privacy
 * @author Michael
 *
 */
public abstract class Collidable extends Animatable{
	
	private boolean invulnerable=false;
	
	private boolean isTrigger=false;
	
	protected boolean drawBorders=true;
	
	protected boolean offsetDir = true;
	
	protected Rectangle2D.Float collisionBox;
	
	protected float colXOffset, collYOffset;
	
	public Collidable(float x, float y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Returns the collision Box and updates collision box also
	 * @return The Collision Box
	 */
	public Rectangle2D.Float getCollisionBox(){
		collisionBox.x =x + xOffset;
		collisionBox.y =y + yOffset;
		return collisionBox;
	}
	
	public Shape getCollisionShape(){
		collisionBox.x =x + xOffset;
		collisionBox.y =y + yOffset;
		return collisionBox;
	}
	
	//checks if the object has collided at all, used for interactions between objects
	//Uses built in intersect functions
	public boolean checkCollision(Collidable otherObject){

		return getCollisionBox().intersects(otherObject.getCollisionBox());
	}
	
	/**
	 * Checks if the two shapes have collided
	 * @param otherObject to collide with
	 * @return if the two shapes have collided
	 */
	public boolean checkShapeCollision(Collidable otherObject){
		Area a= new Area(getCollisionShape());
		Area b= new Area(otherObject.getCollisionShape());
		a.intersect(b);
		return !a.isEmpty();
	}
	//checks collision from (L)eft and (R)ight side
	//uses intersecting number ranges
	//currently only works for rectangles
	/**
	 * Checks if the two collidable objects collide in the LEFT or RIGHT plane
	 * @param otherObject Other Object to check for collision
	 */
	public boolean checkLRCollision(Collidable otherObject){
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
	/**
	 * Checks if the two collidable objects collide in the TOP or BOTTOM plane
	 * @param otherObject Other Object to check for collision
	 */
	public boolean checkTBCollision(Collidable otherObject){
		boolean yCollide = false;
		Double colY = getCollisionBox().getY();
		Double colHeight = getCollisionBox().getHeight();
		
		Double otherY = otherObject.getCollisionBox().getY();
		Double otherColHeight = otherObject.getCollisionBox().getHeight();
		
		yCollide = (colY < (otherY+otherColHeight)) && (otherY < (colY+colHeight));
		
		return yCollide;

	}
	
	//SETTERS
	public void setTrigger(boolean isTrigger){ this.isTrigger = isTrigger; }
	public void setInvulnerablity(boolean active){ invulnerable = active; }

	//GETTERS
	public boolean isTrigger(){ return isTrigger; }
	
	public boolean offSetDir() {return offsetDir; }
	
	public boolean isInvulnerable() { return invulnerable; }
	
	public boolean debug(){ return drawBorders; }
	
}
