/**
 * This class models a knockback action 	
 * Given a source, the knock back is modelled so that the actor (not part of the class, idk why) can move its dx and dy
 * from the source, with a certain power
 */
package Model;

public class KnockBack {

	private float pushDistance=0f;
	
	private double[] xy;	//push power in x and y
	
	private float sourceX;
	private float sourceY;
	private GameObject actor;		//thing to push
	
	private float currTime;
	private float length=0;
	
	private boolean running=false;
	
	/**
	 * 
	 * @param s -source point
	 * @param p - length of push in pixels
	 * @param l - Length of push in time
	 */
	public KnockBack(GameObject s,GameObject a,float p, float l) {
		this(s.getX()+s.getWidth()/2,s.getY()+s.getHeight()/2,a,p,l);
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
		xy = new double[2];
		//distance between mid point of each sprite
		float diffX = sourceX - (actor.getX()+actor.getWidth()/2);		//delta x between center points
		float diffY = sourceY - (actor.getY()+actor.getHeight()/2);		//delta y between center points
		
		double angle =  Math.atan2(diffY, diffX);	//angle in rads
	
		double xComponent = -pushDistance*Math.cos(angle);		//multiply by -1 since you want to go in opposite direction
		double yComponent = -pushDistance*Math.sin(angle);		//same tings
		xy[0] = (float) (xComponent/Math.pow(length, 2));
		xy[1] = (float) (yComponent/Math.pow(length, 2));
		System.out.println(xy[0]);
		System.out.println(xy[1]);
		
		
	}
	
	public void update(){
		if(running){
			currTime+=1;
			if(currTime >= length){
				running=false;
			}
		}
	}
	
	//returns if the knockback is still playing
	public boolean getStatus(){ return running; }
	
	public double[] getKnockback(){ return xy; }
}
