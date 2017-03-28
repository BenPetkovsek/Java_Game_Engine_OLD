package AnimationModel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Models the basic semantics of an animation with updates and shit
 * Not an interface even though being called one
 * @author Michael
 *
 */
public abstract class IAnimation {
protected long refreshRate= 20;	//The time between each animation frame, not sure how to do this lol
	
	protected long totalDuration;
	private long timeElapsed=0;
	private int frameElapsedTime;
	
	private boolean repeating;	//if the animation loops when it is done
	private boolean singleImage=false;
	
	private boolean interruptable=true;
	protected ArrayList<Integer> framesLengths;	//list of frame lengths in order of the animation
	private int frameIndex=0;
	
	private boolean finished =false;
	
	private int priority=0;
	
	
	//creates an empty animation and if it repeats
	public IAnimation( boolean repeat, int priority){
		this.priority = priority;
		repeating = repeat;
		framesLengths = new ArrayList<Integer>();
	}
	
	public void update(){
		
		//if the time elapsed is on a time period to change frame
		if(frameElapsedTime >= framesLengths.get(frameIndex) && timeElapsed != 0){		//if not at starting position and at refresh mark, then do shit
			frameElapsedTime =0;
			if(timeElapsed >= totalDuration){
				timeElapsed=0;
				if(repeating){
					frameIndex =0;
				}
				else{
					finished=true;
					//System.out.println("increase frame with mod");
					//frameIndex = (frameIndex+1) % frames.size();
					
				}
			}
			else{
				frameIndex = (frameIndex+1) % framesLengths.size();
				
			}
			timeElapsed++;
			//System.out.println("time: "+timeElapsed);
			//System.out.println("frameIndex: " +frameIndex);
		}
		timeElapsed++;
		frameElapsedTime++;
	}
	
	
	//to make it so code isnt repeated, called by all four of the add frame methods
	protected void addFrameBackEnd(){
		totalDuration += refreshRate;
		if(framesLengths.size() > 1){
			singleImage= false;
		}
	}
	
	//restarting anim
	public void reset(){
		timeElapsed=0;
		frameElapsedTime = 0;
		frameIndex=0;
		finished=false;
	}
	
	public void setInterruptable(boolean val){ 
		if(repeating && !val){
			System.out.println("BEWARE this animation is infinite and can't be interrupted");
		}
		interruptable =val; 
	}
	public boolean interruptable(){ return interruptable; }
	
	public boolean isFinished(){ return finished; }
	
	public int getPriority() { return priority; }
	/**
	 * Sets the refresh rate
	 * @param refreshRate - new refresh rate
	 */
	public void setRefreshRate(int refreshRate){ 
		//go through each frame and update length if not modified 
		for (int e: framesLengths){
			if(e ==this.refreshRate){
				e=refreshRate;
			}
		}
		this.refreshRate = refreshRate;
		totalDuration = framesLengths.size() *refreshRate;	//change the duration of animation since refresh time was changed
	}
	
	public long getDuration(){ return totalDuration; }
	
	public int getIndex(){ return frameIndex; } 
}
