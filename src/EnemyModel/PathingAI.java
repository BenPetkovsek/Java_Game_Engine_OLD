package EnemyModel;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.concurrent.ThreadLocalRandom;

import PlayerModel.Player;
import View.GameRender;

/**
 * Models an AI that walks in a fixed path, in random order or not
 * @author Michael
 *
 */
public class PathingAI extends AI{
	
	boolean random;	//if path is random

	Point2D.Float[] path;	//the set of points to follow[base with no offsets], can be empty = enemy stays still, always idling
	Point2D.Float[] auxPath; //the real set of points with offsets
	
	int pathIndex;
	
	/**
	 * @see AI#AI(Enemy, boolean, boolean, float, float, int, int)
	 * @param path path to follow
	 * @param random if AI follows path in order
	 */
	public PathingAI(Enemy enemy,boolean smart, boolean dumb,float proxX, float proxY,int idleTimeMax, int idleTimeMin, Point2D.Float[] path,boolean random){
		super(enemy,smart,dumb,proxX,proxY,idleTimeMax, idleTimeMin);
		this.path = path;
		auxPath= new Point2D.Float[path.length];
		//copying array
		for (int i=0; i<path.length;i++){
			auxPath[i] = new Point2D.Float(path[i].x, path[i].y);
		}
		this.random = random;
	}
	
	/**
	 * Updates AI behaviour based on states
	 * 1. no path and not idling : set new destination based on path given
	 * 2. On path : update movement to go to point
	 * 3. Idling: randomize idle wait time
	 */
	@Override
	public void update(Player hero){
		updatePath(GameRender.getBGOffsetX(),GameRender.getBGOffsetY());
		super.update(hero);
		if(!enemy.freeze){
			if(!super.isChasing()){		//only do own movement if not chasing
				if(!onPath && !idling){	//if not idling or on path
					onPath =true;
					setDestination();
				}
				else if(onPath){		//if currently on path
					updateMovement(auxPath[pathIndex]);
				}
				else if(idling){		//if currently idling
					idleClock++;
					if(idleClock >= idleTime){
						idling=false;
					}
				}
			}
		}
	}
	
	//set new destination with given points (in order or random)
	private void setDestination(){
		if(random){
			pathIndex= ThreadLocalRandom.current().nextInt(path.length);	//choose random point to walk to
		}
		else{
			pathIndex = (pathIndex+1) % path.length;	//go to next point
		}
	}
	
	/**
	 * @return The path of the Enemy
	 */
	public Point2D.Float[] getPath(){ return auxPath; }
	
	private void updatePath(float newX, float newY){
		for (int i  =0; i <auxPath.length;i++){
			auxPath[i].x = path[i].x + newX;
			auxPath[i].y = path[i].y + newY;
		}
	}
}
