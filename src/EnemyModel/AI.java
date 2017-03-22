package EnemyModel;

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
	
	float proxX;		//chasing proximity in x direction
	float proxY;		//chasing proximity in y direction
	
	boolean chasing=false;	//if currently chasing
	
	/****walking vars*****/
	protected boolean onPath=false; //if currently heading to destination
	protected float destinationError= 5; 	//grace zone of allowed destination arrival
	
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
	public void update(){
		
	}
	
	/**
	 * Chase player
	 */
	protected void chase(){
		
	}
	
	public boolean isChasing(){
		return chasing;
	}
}
