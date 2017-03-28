package AnimationModel;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CollisionBoxAnimation extends IAnimation {

	private ArrayList<Rectangle2D.Float> frames;	//list of images in order of the animation
	
	public CollisionBoxAnimation(boolean repeat, int priority) {
		super(repeat, priority);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param colBox the Rectangle2D.Float to add to the frame
	 * adds a frame to the animation
	 */
	public CollisionBoxAnimation addFrame(Object colBox){
		if(colBox instanceof Rectangle2D.Float){
			frames.add((Rectangle2D.Float) colBox);
			framesLengths.add((int) refreshRate);
			totalDuration += refreshRate;
			addFrameBackEnd();
			
		}
		else{
			System.out.println("wrong type of frame [CollisonBoxAnimation]");
		}
		return this;
	}
	
	/**
	 * @param colBox the Rectangle2D.Float to add to the frame
	 * @param how long
	 * adds a frame to the animation with certain length
	 */
	public CollisionBoxAnimation addFrame(Object colBox,int length){
		if(colBox instanceof Rectangle2D.Float){
			frames.add((Rectangle2D.Float) colBox);
			//cant have length 0
			if(length != 0){
				framesLengths.add(length);
			}
			else{
				System.out.println(this + " attempted to add a frame with length 0");
				framesLengths.add(1);
			}
		}
		else{
			System.out.println("wrong type of frame [CollisonBoxAnimation]");
		}
		addFrameBackEnd();
		return this;
	}
	
	//gets the current image in the animation
	public Rectangle2D.Float getCurrFrame(){
		//return a null image if empty
		if (frames.size() == 0){
			return null;
		}
		else{
			return frames.get(getIndex());
		}
	}

}
