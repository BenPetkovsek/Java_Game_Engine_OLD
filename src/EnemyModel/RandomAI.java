package EnemyModel;

import java.awt.geom.Point2D;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Models a "random" AI that walks in random directions
 * @author Michael
 *
 */
public class RandomAI extends AI {

	/*****Random Direction Vars*****/
	
	Point2D.Float destination;	//next point to go, determined randomly
	
	//min and max distance they can walk in x dir
	float maxWalkX;
	float minWalkX;
	
	//min and max distance they can walk in y dir
	float maxWalkY;
	float minWalkY;
	
	/**
	 * @see AI#AI(boolean, boolean, float, float, int, int)
	 * @param maxX max walk distance in x
	 * @param minX min walk distance in x
	 * @param maxY max walk distance in y
	 * @param minY min walk distance in y
	 */
	public RandomAI(Enemy enemy,boolean smart, boolean dumb,float proxX, float proxY,int idleTimeMax, int idleTimeMin, float maxX, float minX, float maxY, float minY){
		super(enemy,smart,dumb,proxX,proxY,idleTimeMax, idleTimeMin);
		destination = new Point2D.Float(enemy.getX(), enemy.getY());
		maxWalkX =maxX;
		minWalkX = minX;
		maxWalkY =maxY;
		minWalkY = minY;
	}
	
	@Override
	public void update(){
		super.update();
		if(!super.isChasing()){		//only do own movement if not chasing
			if(!onPath && !idling){	//if not idling or on path
				onPath =true;
				setDestination();
			}
			else if(onPath){		//if on path currently
				float xDiff = enemy.getX() - destination.x;
				boolean reachedX=false;
				boolean reachedY=false;
				if(Math.abs(xDiff) < destinationError){	//reached 
					enemy.stopXWalk();
					reachedX= true;
				}
				else if(xDiff >0){	//to the right of dest
					enemy.walkLeft();
				}
				else if(xDiff <0){	//to the left of dest
					enemy.walkRight();
				}
				
				float yDiff = enemy.getY() - destination.y;
				if(Math.abs(yDiff) < destinationError){	//reached 
					enemy.stopYWalk();
					reachedY=true;
				}
				else if(yDiff >0){	//above dest
					enemy.walkUp();
				}
				else if(yDiff <0){	//below dest
					enemy.walkDown();
				}
				if(reachedX && reachedY){
					onPath =false;
				}
				if(!onPath){
					idling =true;
					idleClock =0;
					idleTime = ThreadLocalRandom.current().nextInt(idleTimeMin, idleTimeMax + 1);	//set random idle time
				}
			}
			else if(idling){	//idle for random time
				idleClock++;
				if(idleClock >= idleTime){
					idling=false;
				}
			}
		}
	}
	
	//sets the destination for the enemy
	private void setDestination(){
		double rng = Math.random();
		float xWalk = ThreadLocalRandom.current().nextInt((int) minWalkX, (int) maxWalkX + 1);
		float yWalk = ThreadLocalRandom.current().nextInt((int) minWalkY, (int) maxWalkY + 1);
		
		if(rng <0.25 ){	//walk right
			xWalk = destination.x + xWalk;
		}
		else if (rng <0.50){	//walk left
			xWalk = destination.x - xWalk;
		}
		else{		//dont walk in y
			xWalk = destination.x;
		}
		rng = Math.random();	//reset
		if(rng <0.25 ){	//walk up
			yWalk = destination.y -yWalk;
		}
		else if (rng <0.50){	//walk down
			yWalk = destination.y +yWalk;
		}
		else{		//dont walk in y
			yWalk = destination.y;
		}
		destination = new Point2D.Float(xWalk, yWalk);
	}
}
