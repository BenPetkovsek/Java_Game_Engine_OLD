package EnemyModel;

import java.awt.geom.Point2D;
import java.util.concurrent.ThreadLocalRandom;

import PlayerModel.Player;

/**
 * Models Artificial Intelligence, will be used by the basic enemies in the game.
 * @author Michael
 *
 */
public class AI {
	protected Enemy enemy;
	
	/****IDLE VARS****/
	protected boolean idling=false;	//if currently standing still
	protected int idleTimeMax;	//max time they can be idle
	protected int idleTimeMin;	//min time they can be idle
	protected int idleTime;
	protected int idleClock;
	
	/****CHASING VARS*****/
	//possibly convert this to have proximity shape that collides with player and alerts enemy
	//we shall do this for now as it deems still good by me
	boolean smart = false;	//if they chase the player
	boolean dumb = false;	//if they dont notice the player behind them
	boolean overrideDumb = false;	//overrides AI's dumb feature allowing them to notice player if #TRIGGERED
	
	float proxX;		//chasing proximity in x direction
	float proxY;		//chasing proximity in y direction
	
	boolean chasing=false;	//if currently chasing
	float chasingError=5;
	/****walking vars*****/
	protected boolean onPath=false; //if currently heading to destination
	protected float destinationError= 5; 	//grace zone of allowed destination arrival
	protected Point2D.Float destination;	//next point to go, determined randomly
	/**
	 * Constructs empty AI
	 * @param enemy the enemy to attach AI to
	 * @param smart if this AI chases player
	 * @param dumb if this AI only chases whats infront of it
	 * @param proxX chase prox in x
	 * @param proxY chase prox in y
	 * @param idleTimeMax max time to idle
	 * @param idleTimeMin min time to idle
	 */
	public AI(Enemy enemy,boolean smart, boolean dumb, float proxX, float proxY, int idleTimeMax, int idleTimeMin){
		this.enemy =enemy;
		this.smart = smart;
		this.dumb = dumb;
		this.proxX = proxX;
		this.proxY = proxY;
		this.idleTimeMax = idleTimeMax;
		this.idleTimeMin = idleTimeMin;
	}
	/**
	 * Update AI 
	 */
	public void update(Player hero){
		if(smart){
			tryChase(hero);
		}
		
	}
	
	//updates movement based on destination point;
	protected void updateMovement(Point2D.Float destination){
		//float xDiff = enemy.getCollisionBox().x - destination.x;
		float xDiff = (float) enemy.diffX(destination);
		boolean reachedX=false;
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
		
		//float yDiff = enemy.getCollisionBox().y - destination.y;
		float yDiff = (float) enemy.diffY(destination);
		boolean reachedY=false;
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
		if(reachedX && reachedY){	//if destination reached
			onPath =false;
		}
		if(!onPath){		//start idling
			idling =true;
			idleClock =0;
			idleTime = ThreadLocalRandom.current().nextInt(idleTimeMin, idleTimeMax + 1);	//set random idle time
		}
	}
	
	/**
	 * Chases player based on proximity circle, basic following
	 */
	protected void tryChase(Player hero){
		
		
		boolean facingPlayer=enemy.dumbCheck(hero);
		boolean inProximity = enemy.proximityCheck(hero, proxX, proxY);
		double diffX = hero.diffX(enemy);
		double diffY = hero.diffY(enemy);
		if( (inProximity && dumb && facingPlayer) || (inProximity && !dumb) || (inProximity && overrideDumb)){	//if within prox (dumb has to also face Player)
			chasing=true;
			if(Math.abs(diffX) <= chasingError){
				enemy.stopXWalk();
			}
			else if(diffX < 0){
				enemy.walkLeft();
			}
			else{
				enemy.walkRight();
			}
			if(Math.abs(diffY) <= chasingError){
				enemy.stopYWalk();
			}
			else if(diffY < 0){
				enemy.walkUp();
			}
			else{
				enemy.walkDown();
			}
		}
		else{
			chasing=false;
			overrideDumb = false;	//once out of proximity, turn of override
		}
	}
	

	
	/**
	 * @return If Enemy is currently chasing AI
	 */
	public boolean isChasing(){
		return chasing;
	}
	
	/**
	 * Forces enemy to chase player
	 */
	public void triggerChase(){ overrideDumb =true; }
}
