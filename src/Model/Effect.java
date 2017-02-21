package Model;
/**
 * Models general effects that can be applied to gameobjects in the game
 * There are two modes, timed and not timed, if an effect is not instaniated with a duration than it is not time dependent
 * @author Michael
 *
 */
public class Effect {
	
	private Collidable objectToAffect;
	
	private boolean running=true;
	
	private int currentTime=0;

	private int duration;
	
	private boolean timed=true;
	
	/**
	 * Creates an effect with a duration
	 * @param duration How long the effect runs for
	 */
	public Effect(int duration){
		this.duration = duration;
	}
	
	/**
	 * Creates an effect for a gameobject with duration
	 * @param duration how long the effect runs for
	 * @param actor which gameobject to affect
	 */
	public Effect(int duration, Collidable actor){
		this(duration);
		objectToAffect = actor;
	}
	
	/**
	 * Creates a time independent effect
	 */
	public Effect(){
		timed=false;
	}
	/**
	 * Creates a time independent effect
	 * @param actor which gameobject to affect
	 */
	public Effect(Collidable actor){
		this();
		objectToAffect = actor;
	}
	
	/**
	 * Updates the Effect
	 */
	public void update(){
		if(timed){
			currentTime++;
			if(currentTime > duration){
				running = false;
			}
		}
	}
	
	/**
	 * Starts/ resumes the effect
	 */
	public void start(){
		running=true; 
	}
	/**
	 * Pauses the effect
	 */
	public void pause(){
		running=false;
	}
	
	/**
	 * Stops/resets the effect
	 */
	public void stop(){
		currentTime = 0;
		running=false;
	}
	
	/**
	 * Returns if the effect is running
	 * @return if the effect is running or not
	 */
	public boolean getStatus(){ return running; }
	
	/**
	 * Gets the internal time of the effect
	 * @return The current time of the effect
	 */
	public int getTime(){ return currentTime; }
	
	/**
	 * Gets the duration of the effect
	 * @return The duration
	 */
	public int getDuration(){ return duration; }
	
	/**
	 * Returns the object affect if there is any
	 * @return the game object being affected by this effect
	 */
	public Collidable getActor(){ return objectToAffect; }
	


}
