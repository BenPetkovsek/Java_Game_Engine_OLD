package EnemyModel;

import java.awt.geom.Point2D;

/**
 * Models an AI that walks in a fixed path, in random order or not
 * @author Michael
 *
 */
public class PathingAI extends AI{
	
	boolean random;	//if path is random

	Point2D.Float[] path;	//the set of points to follow, can be empty = enemy stays still, always idling
	
	int pathIndex;
	/**
	 * @see AI#AI(boolean, boolean, float, float)
	 * @param path path to follow
	 * @param random if AI follows path in order
	 */
	public PathingAI(Enemy enemy,boolean smart, boolean dumb,float proxX, float proxY,int idleTimeMax, int idleTimeMin, Point2D.Float[] path,boolean random){
		super(enemy,smart,dumb,proxX,proxY,idleTimeMax, idleTimeMin);
		this.path = path;
		this.random = random;
	}
	
	public Point2D.Float[] getPath(){
		return path;
	}
	
	public boolean isRandom(){
		return random;
	}
	
	@Override
	public void update(){
		
	}
}
