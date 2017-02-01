package Model;

import java.awt.geom.Rectangle2D;


/**
 * Models a knockback of a gameobject but simpler...
 * jk the direction of the push is either up, down, left, or right.
 * angle of impact does not matter
 * used for different types of attacks and shit
 * @author Michael
 *
 */
public class SimpleKnockBack extends KnockBack {
	
	public SimpleKnockBack(float x, float y, GameObject a, float p, float l) {
		super(x, y, a, p, l);
		calculateXY();	
	}
	
	public SimpleKnockBack(GameObject s,GameObject a,float p, float l) {
		super(s,a,p,l);
		calculateXY();	//override calculateXY
	}
	
	//Simple Knockback does not do omni directional push just left right up or down
	private void calculateXY(){
		Rectangle2D.Float colBox = actor.getCollisionBox();
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
		
		xy[0] = (float) (xComponent/Math.pow(length, 2));		//dx= dx/t^2
		xy[1] = (float) (yComponent/Math.pow(length, 2));		//dy =dy/t^2
	}
	
	

}
