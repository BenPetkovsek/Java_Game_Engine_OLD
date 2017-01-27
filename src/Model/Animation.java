/*
 * This class will be responsible for simulating animation, change of frames with the main loop as a timer of sorts
 */
package Model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Still deciding on how to run the animation, do we use timers or do we use the main loop as a form of measurement and standardize the refresh rate there
//So many options, and i have no idea what it best 
public class Animation {
	
	private long refreshRate= 20;	//The time between each animation frame, not sure how to do this lol
	
	private long totalDuration;
	private long timeElapsed=0;
	private int frameElapsedTime;
	
	private boolean repeating;	//if the animation loops when it is done
	
	private boolean interruptable=true;
	
	private boolean singleImage=true;
	
	private ArrayList<BufferedImage> frames;	//list of images in order of the animation
	private ArrayList<Integer> framesLengths;	//list of frame lengths in order of the animation
	private int frameIndex=0;
	
	private boolean finished =false;
	
	
	//creates an empty animation and if it repeats
	public Animation( boolean repeat){
		repeating = repeat;
		frames= new ArrayList<BufferedImage>();
		framesLengths = new ArrayList<Integer>();
	}
	
	public void update(){
		
		//if the time elapsed is on a time period to change frame
		if(frameElapsedTime == framesLengths.get(frameIndex) && timeElapsed != 0){		//if not at starting position and at refresh mark, then do shit
			frameElapsedTime =0;
			if(timeElapsed == totalDuration){
				timeElapsed=0;
				if(repeating){
					frameIndex =0;
				}
				else{
					finished=true;
				}
			}
			else{
				frameIndex = (frameIndex+1) % frames.size();
				
			}
		}
		timeElapsed++;
		frameElapsedTime++;
	}
	
	//gets the current image in the animation
	//for the renderer
	public BufferedImage getCurrFrame(){
		//return a null image if empty
		if (frames.size() == 0){
			return null;
		}
		else{
			return frames.get(frameIndex);
		}
	}
	
	//adds a frame to the animation
	public Animation addFrame(BufferedImage sprite){
		frames.add(sprite);
		framesLengths.add((int) refreshRate);
		totalDuration += refreshRate;
		addFrameBackEnd();
		return this;
	}
	
	//adds a frame to the animation at a certain index
	public Animation addFrame(BufferedImage sprite,int index){
		frames.add(index, sprite);
		framesLengths.add((int) refreshRate);
		addFrameBackEnd();
		return this;
	}
	//adds a frame to the animation with certain length
	public Animation addFrameWithLength(BufferedImage sprite,int length){
		frames.add(sprite);
		//cant have length 0
		if(length != 0){
			framesLengths.add(length);
		}
		else{
			System.out.println(this + " attempted to add a frame with length 0");
			framesLengths.add(1);
		}
		addFrameBackEnd();
		return this;
	}
	
	//adds a frame to the animation at a certain index with certain length
	public Animation addFrameWithLength(BufferedImage sprite,int index, int length){
		frames.add(index, sprite);
		if(length != 0){
			framesLengths.add(index,length);
		}
		else{
			System.out.println(this + " attempted to add a frame with length 0");
			framesLengths.add(index,1);
		}
		addFrameBackEnd();
		return this;
	}
	
	//to make it so code isnt repeated, called by all four of the add frame methods
	private void addFrameBackEnd(){
		totalDuration += refreshRate;
		if(frames.size() > 1){
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
		totalDuration = frames.size() *refreshRate;	//change the duration of animation since refresh time was changed
	}
	
	public long getDuration(){ return totalDuration; }
	
}
