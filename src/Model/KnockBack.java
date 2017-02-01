/**
 * This class models a knockback action 		
 * Given a source, the knock back is modelled so that the actor (not part of the class, idk why) can move its dx and dy
 * from the source, with a certain power
 * TODO Have all movement done from the knockback done in this class, as opposed to the source using the values
 */
package Model;

import java.awt.geom.Rectangle2D;

public class KnockBack {

	protected float pushDistance=0f;
	
	protected float[] xy;	//push power in dx and dy
	
	protected float sourceX;
	protected float sourceY;
	protected GameObject actor;		//thing to push
	protected float currTime;
	protected float length=0;
	
	private boolean running=false;
	
	/**
	 * 
	 * @param s -source point
	 * @param p - length of push in pixels
	 * @param l - Length of push in time
	 */
	public KnockBack(GameObject s,GameObject a,float p, float l) {
		//takes the collision box coordinates as they are more accurate
		this((float) (s.getCollisionBox().x+s.getCollisionBox().getWidth()/2),(float) (s.getCollisionBox().y+s.getCollisionBox().getHeight()/2),a,p,l);
	} 
	
	/**
	 * 
	 * @param x -source x of push
	 * @param y -source y of push
	 * @param p - length of push in pixel
	 * @param l - length of push in time;
	 */
	public KnockBack(float x, float y,GameObject a, float p, float l){
		sourceX = x;
		sourceY = y;
		actor =a;
		pushDistance =p;
		length =l;
		currTime =0;
		running=true;
		calculateXY();
	}
	
	/**
	 * Calculates the push dx and dy to be added to the player
	 */
	private void calculateXY(){
		Rectangle2D.Float colBox = actor.getCollisionBox();
		xy = new float[2];
		//distance between mid point of each sprite
		float diffX = (float) (sourceX - (colBox.x+colBox.getWidth()/2));		//delta x between center points
		float diffY = (float) (sourceY - (colBox.y+colBox.getHeight()/2));		//delta y between center points
		
		double angle =  Math.atan2(diffY, diffX);	//angle in rads
	
		double xComponent = -pushDistance*Math.cos(angle);		//multiply by -1 since you want to go in opposite direction
		double yComponent = -pushDistance*Math.sin(angle);		//same tings
		xy[0] = (float) (xComponent/Math.pow(length, 2));
		xy[1] = (float) (yComponent/Math.pow(length, 2));
		
	}
	
	//knockback now deals with dx and dy
	public void update(){
		if(running){
			
			actor.setDx(actor.getDx()+xy[0]);
			actor.setDy(actor.getDy()+xy[1]);
			if(currTime >= length){
				running=false;
				if(actor.getDx() != 0){
					actor.setDx(0);
				}
				if(actor.getDy() != 0){
					actor.setDy(0);
				}
			}
			currTime+=1;
		}
	}
	
	//returns if the knockback is still playing
	public boolean getStatus(){ return running; }
	
	public float[] getKnockback(){ return xy; }
	
}
