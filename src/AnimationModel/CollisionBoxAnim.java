package AnimationModel;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CollisionBoxAnim extends IAnimation {

	private ArrayList<Rectangle2D.Float> frames;	//list of col boxes in order of the animation
	
	private ArrayList<float[]> offsetFrames;			//list of offsets for the frames
	public CollisionBoxAnim(boolean repeat, int priority) {
		super(repeat, priority);
		frames = new ArrayList<Rectangle2D.Float>();
		offsetFrames = new ArrayList<float[]>();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param x unused
	 * @param y unused
	 * @param width
	 * @param height
	 * @param xoff local x of collision box relative to its parent
	 * @param yoff " " y " "
	 * @return
	 */
	public CollisionBoxAnim addFrame(float x, float y, float width, float height, float xoff, float yoff){
		frames.add(new Rectangle2D.Float(x,y,width,height));
		framesLengths.add((int) refreshRate);
		float[] off = {xoff,yoff};
		offsetFrames.add(off);
		totalDuration += refreshRate;
		addFrameBackEnd();
		return this;
	}
	
	/**
	 * @param x unused
	 * @param y unused
	 * @param width
	 * @param height
	 * @param xoff local x of collision box relative to its parent
	 * @param yoff " " y " "
	 * @param length of the specific frame
	 * @return
	 */
	public CollisionBoxAnim addFrame(float x, float y, float width, float height, float xoff,float yoff,int length){
		frames.add(new Rectangle2D.Float(x,y,width,height));
		float[] off = {xoff,yoff};
		offsetFrames.add(off);
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
	
	//gets the current image in the animation
	public float getCurrFrameOffsetX(){
		//return a null image if empty
		if (frames.size() == 0){
			return 0;
		}
		else{
			return offsetFrames.get(getIndex())[0];
		}
	}
	
	public float getCurrFrameOffsetY(){
		//return a null image if empty
		if (frames.size() == 0){
			return 0;
		}
		else{
			return offsetFrames.get(getIndex())[1];
		}
	}

}
