package EnemyModel;

import java.awt.geom.Point2D;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import PlayerModel.Player;

/**
 * Models a "random" AI that walks in random directions
 * @author Michael
 *
 */
public class RandomAI extends AI {

	/*****Random Direction Vars*****/
	

	
	//min and max distance they can walk in x dir
	float maxWalkX;
	float minWalkX;
	
	//min and max distance they can walk in y dir
	float maxWalkY;
	float minWalkY;
	
	/**
	 * @see AI#AI(Enemy, boolean, boolean, float, float, int, int)
	 * @param maxX max walk distance in x
	 * @param minX min walk distance in x
	 * @param maxY max walk distance in y
	 * @param minY min walk distance in y
	 */
	public RandomAI(Enemy enemy,boolean smart, boolean dumb,float proxX, float proxY,int idleTimeMax, int idleTimeMin, float maxX, float minX, float maxY, float minY){
		super(enemy,smart,dumb,proxX,proxY,idleTimeMax, idleTimeMin);
		destination = new Point2D.Float(enemy.getX(), enemy.getY());	//init point at spawn
		maxWalkX =maxX;
		minWalkX = minX;
		maxWalkY =maxY;
		minWalkY = minY;
	}
	
	/**
	 * Updates AI behaviour based on states
	 * 1. no path and not idling : set new destination based on random point (setDestination)
	 * 2. On path : update movement to go to point
	 * 3. Idling: randomize idle wait time
	 */
	@Override
	public void update(Player hero){
		super.update(hero);
		if(!super.isChasing()){		//only do own movement if not chasing
			if(!onPath && !idling){	//if not idling or on path
				onPath =true;
				setDestination();
			}
			else if(onPath){		//if on path currently
				updateMovement(destination);
			}
			else if(idling){	//idle for random time
				idleClock++;
				if(idleClock >= idleTime){
					idling=false;
				}
			}
		}
	}
	
	//sets random destination point given bounds
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
