package EffectModel;

import java.awt.geom.Rectangle2D;

import GameObjectModel.Collidable;


/**
 * Models a knockback of a gameobject but simpler...
 * jk the direction of the push is either up, down, left, or right.
 * angle of impact does not matter
 * used for different types of attacks and shit
 * @author Michael
 *
 */
public class SimpleKnockBack extends KnockBack {

	/**
	 * Creates a simple knockback at (x,y)
	 * @param x -source x of push
	 * @param y -source y of push
	 * @param pushDistance - length of push in pixel
	 * @param duration - length of push in time;
	 */
	public SimpleKnockBack(float x, float y, Collidable actor, float pushDistance, int duration) {
		super(x, y, actor, pushDistance, duration);
		calculateXY();	
	}
	/**
	 * Creates a simple knockback using a gameobject's position as the source
	 * @param source -source gameobject of originpoint
	 * @param pushDistance - length of push in pixels
	 * @param duration - Length of push in time
	 */
	public SimpleKnockBack(Collidable source,Collidable actor,float pushDistance, int duration) {
		super(source,actor,pushDistance,duration);
		calculateXY();	//override calculateXY
	}
	
	//Simple Knockback does not do omni directional push just left right up or down
	private void calculateXY(){
		Rectangle2D.Float colBox = getActor().getCollisionBox();
		xy = new float[2];
		float xComponent=0;
		float yComponent=0;
		
		boolean inX = (colBox.x <= sourceX) && (sourceX <=colBox.x+colBox.getWidth());
		boolean inY = (colBox.y <= sourceY) && (sourceY <=colBox.y+colBox.getHeight());
		if(inX && !inY){	//push up or down
			if(colBox.y >= sourceY){
				yComponent = pushDistance;
			}
			else{
				yComponent = -pushDistance;
			}
		}
		else if(inY && !inX){	//push left or right
			if(colBox.x >= sourceX){
				xComponent = pushDistance;
			}
			else{
				xComponent = -pushDistance;
			}
		}
		else{	//diagonal??
			if(colBox.y >= sourceY){
				yComponent = pushDistance;
			}
			else{
				yComponent = -pushDistance;
			}
			if(colBox.x >= sourceX){
				xComponent = pushDistance;
			}
			else{
				xComponent = -pushDistance;
			}
		}
		
		xy[0] = (float) (xComponent/Math.pow(getDuration(), 2));		//dx= dx/t^2
		xy[1] = (float) (yComponent/Math.pow(getDuration(), 2));		//dy =dy/t^2
	}
	
	

}
