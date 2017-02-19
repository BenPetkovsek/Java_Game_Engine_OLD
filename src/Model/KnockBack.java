/**
 * This class models a knockback action 		
 * Given a source, the knock back is modelled so that the actor (not part of the class, idk why) can move its dx and dy
 * from the source, with a certain power
 * TODO Have all movement done from the knockback done in this class, as opposed to the source using the values
 */
package Model;

import java.awt.geom.Rectangle2D;

public class KnockBack extends Effect {

	protected float pushDistance=0f;
	
	protected float[] xy;	//push power in dx and dy
	
	protected float sourceX;
	protected float sourceY;

	
	
	/**
	 * Creates a knockback using a gameobject's position as the source
	 * @param source -source object of origin point
	 * @param pushDistance - length of push in pixels
	 * @param duration - Length of push in time
	 */
	public KnockBack(GameObject source,GameObject actor,float pushDistance, int duration) {
		//takes the collision box coordinates as they are more accurate)
		this((float) (source.getCollisionBox().getCenterX()),(float) (source.getCollisionBox().getCenterY()),actor,pushDistance,duration);
	} 
	
	/**
	 * Creates a knockback at (x,y)
	 * @param x -source x of push
	 * @param y -source y of push
	 * @param pushDistance - length of push in pixel
	 * @param duration - length of push in time;
	 */
	public KnockBack(float x, float y,GameObject actor, float pushDistance, int duration){
		super(duration,actor);
		sourceX = x;
		sourceY = y;
		this.pushDistance =pushDistance;
		calculateXY();
	}
	
	/**
	 * Calculates the push dx and dy to be added to the gameObject
	 */
	private void calculateXY(){
		Rectangle2D.Float colBox = getActor().getCollisionBox();
		xy = new float[2];
		//distance between mid point of each sprite
		float diffX = (float) (sourceX - (colBox.x+colBox.getWidth()/2));		//delta x between center points
		float diffY = (float) (sourceY - (colBox.y+colBox.getHeight()/2));		//delta y between center points
		
		double angle =  Math.atan2(diffY, diffX);	//angle in rads
	
		double xComponent = -pushDistance*Math.cos(angle);		//multiply by -1 since you want to go in opposite direction
		double yComponent = -pushDistance*Math.sin(angle);		//same tings
		xy[0] = (float) (xComponent/Math.pow(getDuration(), 2));
		xy[1] = (float) (yComponent/Math.pow(getDuration(), 2));
		
	}
	
	/**
	 * 
	 */
	@Override
	public void update(){
		super.update();
		
		//pushes gameobject
		if(getStatus()){
			getActor().setDx(getActor().getDx()+xy[0]);
			getActor().setDy(getActor().getDy()+xy[1]);
			if(getActor() instanceof Player){
				((Player)getActor()).noMovement = true;
			}
		}
		else{
			if(getActor().getDx() != 0){
				getActor().setDx(0);
			}
			if(getActor().getDy() != 0){
				getActor().setDy(0);
			}
			if(getActor() instanceof Player){
				((Player)getActor()).noMovement = false;
			}
		}
	}
	
	
}
