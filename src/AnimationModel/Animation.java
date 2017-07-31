/*
 * This class will be responsible for simulating animation, change of frames with the main loop as a timer of sorts
 */
package AnimationModel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Still deciding on how to run the animation, do we use timers or do we use the main loop as a form of measurement and standardize the refresh rate there
//So many options, and i have no idea what it best 
public class Animation extends IAnimation{
	
	private ArrayList<BufferedImage> frames;	//list of images in order of the animation frames
	//creates an empty animation and if it repeats
	public Animation( boolean repeat, int priority){
		super(repeat,priority);
		frames= new ArrayList<BufferedImage>();
		offsetFrames= new ArrayList<float[]>();
	}
	
	
	/**
	 * @param colBox the BufferedImage to add to the frame
	 * adds a frame to the animation
	 */
	public Animation addFrame(BufferedImage sprite){
		frames.add(sprite);
		framesLengths.add((int) refreshRate);
		totalDuration += refreshRate;
		addFrameBackEnd();
		return this;
	}
	
	public Animation addFrame(BufferedImage sprite, float[] diff){
		addFrame(sprite);
		offsetFrames.add(diff);
		return this;
	}
	
	/**
	 * @param colBox the BufferedImage to add to the frame
	 * @param how long
	 * adds a frame to the animation with certain length
	 */
	public Animation addFrame(BufferedImage sprite,int length){
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

	public Animation addFrame(BufferedImage sprite, int length, float[] diff){
		addFrame(sprite,length);
		offsetFrames.add(diff);
		return this;
	}
	
	
	//gets the current image in the animation
	//for the renderer
	public BufferedImage getCurrFrame(){
		//return a null image if empty
		if (frames.size() == 0){
			return null;
		}
		else{
			return frames.get(getIndex());
		}
	}
	
	public float[] getOffsetFrame(){
		if (offsetFrames.size() ==0){
			return null;
		}
		else{
			return offsetFrames.get(getIndex());
		}
	}
}
