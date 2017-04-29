package GameObjectModel;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import AnimationModel.CollisionBoxAnim;
import PlayerModel.Player;

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
	
	
	protected Rectangle2D.Float collisionBox;
	protected CollisionBoxAnim currColBoxAnim;
	
	protected float colXOffset, colYOffset =0;	//local (x,y) of the collision box relative to the GameObjects (x,y)
	
	/***Proximity Variables****/
	//Fixes certain situations where moving the collisionBox screws up things, have this as a default
	protected boolean useCenterPoint=false;
	protected float centerX;	//local offset from x
	protected float centerY;	//local offset from y
	
	public Collidable(float x, float y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Returns the collision Box and updates collision box also
	 * @return The Collision Box
	 */
	public Rectangle2D.Float getCollisionBox(){
		Rectangle2D.Float colBox;
		if(currColBoxAnim != null){
			colBox = currColBoxAnim.getCurrFrame();
			colBox.x = x+ xOffset + currColBoxAnim.getCurrFrameOffsetX();
			colBox.y = y+ yOffset + currColBoxAnim.getCurrFrameOffsetY();
		}
		else{
			colBox =collisionBox;
			colBox.x =x + xOffset + colXOffset;
			colBox.y =y + yOffset + colYOffset;
		}
		
		return colBox;
	}
	
	//only used by weapon really
	public Shape getCollisionShape(){
		return getCollisionBox();
	}
	
	public CollisionBoxAnim getColAnim() { return currColBoxAnim; }
	
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
	/**
	 * @param other - other collidable
	 * @return difference in x between this and other
	 */
	public double diffX(Collidable other){
/*		double aX = this.getCollisionBox().getCenterX();
		double bX = other.getCollisionBox().getCenterX();*/
		double aX = this.useCenterPoint ? this.getCenterX() : this.getCollisionBox().getCenterX();
		
		double bX = other.useCenterPoint ? other.getCenterX() :other.getCollisionBox().getCenterX();
		
		return aX - bX;
	}
	/**
	 * @param other - other collidable
	 * @return difference in y between this and other
	 */
	public double diffY(Collidable other){
/*		double aY = this.getCollisionBox().getCenterY();
		double bY = other.getCollisionBox().getCenterY();*/
		//double aY= useCenterPoint ? this.getCenterY() : this.getCollisionBox().getCenterY();
		double aY= useCenterPoint ? this.getCenterY() : this.getCollisionBox().getCenterY();
		
		double bY = other.useCenterPoint ? other.getCenterY() :other.getCollisionBox().getCenterY();
		
		return aY - bY;
	}
	
	/**
	 * @param other - other collidable
	 * @return difference in x between this and other
	 */
	public double diffX(Point2D.Float other){
		//double aX = this.getCollisionBox().getCenterX();
		double bX = other.getX();
		double aX = this.useCenterPoint ? this.getCenterX() : this.getCollisionBox().getCenterX();

		return aX - bX;
	}
	/**
	 * @param other - other collidable
	 * @return difference in y between this and other
	 */
	public double diffY(Point2D.Float other){
		//double aY = this.getCollisionBox().getCenterY();
		double bY = other.getY();
		double aY= useCenterPoint ? this.getCenterY() : this.getCollisionBox().getCenterY();
		
		return aY - bY;
	}
	
	//SETTERS
	public void setTrigger(boolean isTrigger){ this.isTrigger = isTrigger; }
	public void setInvulnerablity(boolean active){ invulnerable = active; }
	public void setColAnim(CollisionBoxAnim newAnim){ this.currColBoxAnim= newAnim; }
	//GETTERS
	public boolean isTrigger(){ return isTrigger; }
	
	public boolean isInvulnerable() { return invulnerable; }
	
	public boolean debug(){ return drawBorders; }
	
	public float getCenterX(){ return x+xOffset+centerX; }
	public float getCenterY(){ return y+yOffset+centerY; }
	
	public boolean useCenterPoints(){ return useCenterPoint; }
	
	
}
