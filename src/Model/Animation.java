/*
 * This class will be responsible for simulating animation, change of frames with the main loop as a timer of sorts
 */
package Model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Still deciding on how to run the animation, do we use timers or do we use the main loop as a form of measurement and standardize the refresh rate there
//So many options, and i have no idea what it best 
public class Animation {
	
	private final long refreshRate= 50;	//The time between each animation frame, not sure how to do this lol
	
	private long totalDuration;
	private long timeElapsed;
	
	private boolean repeating;	//if the animation loops when it is done
	
	private ArrayList<BufferedImage> frames;
	public int frameIndex=0;
	
	//creates an empty animation that is duration time long
	public Animation( boolean repeat){
		repeating = repeat;
		frames= new ArrayList<BufferedImage>();
	}
	
	public void update(){
		//if the time elapsed is on a time period to change frame
		if(timeElapsed % refreshRate == 0){
			if(timeElapsed == totalDuration){
				timeElapsed=0;
				if(repeating){
					frameIndex =0;
				}
			}
			else{
				frameIndex = (frameIndex+1) % frames.size();
				
			}
		}
		timeElapsed++;
	}
	
	//gets the current image in the animation
	//for the renderer
	public BufferedImage getCurrFrame(){
		return frames.get(frameIndex);
	}
	
	//adds a frame to the animation
	public Animation addFrame(BufferedImage sprite){
		frames.add(sprite);
		totalDuration += refreshRate;
		return this;
	}
	
	//adds a frame to the animation at a certain index
	public Animation addFrame(BufferedImage sprite,int index){
		frames.add(index, sprite);
		totalDuration += refreshRate;
		return this;
	}
	
	//restarting anim
	public void reset(){
		timeElapsed=0;
		frameIndex=0;
	}
}
