package GameObjectModel;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import AnimationModel.Animation;
import PlayerModel.Player;

/**
 * Models the animator that controls all the transitions and animations
 * To be attached to an animatible object
 * Animator only allows classes that are subclasses of Animatable (T extends Animatable)
 * @author Michael
 *
 */
public abstract class Animator<T extends Animatable> {
	
	protected Map<String,BufferedImage[]> imageCollection = new HashMap<String,BufferedImage[]>();	//holds images 

	protected Map<String,Animation> animationCollection = new HashMap<String,Animation>();		//holds animations
	
	protected Animation currentAnim;		//holds the current animation
	protected Animation possibleNewAnim;	//holds what the next animation could be
	protected Animation oldAnim;			//holds what the last animation was
	
	protected BufferedImage oldFrame;		//what the previous frame was
	protected BufferedImage newFrame;		//what the new frame is
	
	
	protected T thisObject;			//The Animatable object this animator is attatched to 
	
	public Animator(T entity){
		this.thisObject = entity;
	}
	
	protected abstract void mapInit();
	
	/*
	 * Operations that must be run before the abstract update method
	 */
	public void preUpdate(){
		oldAnim = currentAnim;
		possibleNewAnim = currentAnim;
		oldFrame = currentAnim.getCurrFrame();
	}
	
	
	// This abstract method is to be implemented by other animators
	// The only requirement for a update is to set possibleNewAnim for any changes
	public abstract void update();

	
	/**
	 * Stuff that must occur after the abstract update method
	 * priority checking, animation change
	 * TODO Change to priortiy queue instead of single variable
	 */
	public void postUpdate(){
		
		//Change animation based on priority
		if(possibleNewAnim.getPriority() >= currentAnim.getPriority() || currentAnim.isFinished()){
			//reset overrides if animation is done
			currentAnim = possibleNewAnim;
			if(oldAnim != currentAnim){
				currentAnim.reset();
				//if this has collision box, reset collision override caused from sprite changes if switched to new animation
				// prevents any carry over if the animation changes before the collision box resets
				if (thisObject instanceof Collidable){
					((Collidable) thisObject).setColXOverride(0);
					((Collidable) thisObject).setColYOverride(0);
			/*		((Collidable) thisObject).setCenterXOverride(0);
					((Collidable) thisObject).setCenterYOverride(0);*/
				}
			}
		}
		oldAnim = currentAnim;
	
		currentAnim.update();
		newFrame = currentAnim.getCurrFrame();

		// Dynamic sprite difference mover thing that makes the sprite not push themselves back, but keeps them technically in the same position
		// TODO FIX ENEMY COLLISIONBOX 
		if (thisObject.getDirection() == Direction.LEFT || thisObject.getDirection() == Direction.UP){
			if(newFrame != oldFrame){	//only are if the frames r different
				//difference between frames
				float xDiff = (newFrame.getWidth() - oldFrame.getWidth())*thisObject.getScale();
				float yDiff = (newFrame.getHeight() - oldFrame.getHeight())* thisObject.getScale();
				
				//only care if the differences are non zero
				if(xDiff != 0 || yDiff != 0){
					thisObject.addX(-xDiff);
					thisObject.addY(yDiff);
					//if collidable also shift center point
					if (thisObject instanceof Collidable){
						((Collidable) thisObject).setCenterXOverride(xDiff);
						((Collidable) thisObject).setCenterYOverride(yDiff);
					}
				}
				// If the object the animator is attached to is collidable, move the collision box as well
				// TODO Figure out if this should be here or in a seperate class
				if (thisObject instanceof Collidable){
					float xColDiff = 0;
					float yColDiff = 0;
					xDiff = (newFrame.getWidth() - oldFrame.getWidth())*thisObject.getScale();
					yDiff = (newFrame.getHeight() - oldFrame.getHeight())* thisObject.getScale();
					
					// Keeping collision box back in original position
					// Have to do this all the time incase it needs to be set to zero
					xColDiff = (float) (newFrame.getWidth() * thisObject.getScale() - ((Collidable) thisObject).getCollisionBox().getWidth());
					yColDiff = (float) (newFrame.getHeight() * thisObject.getScale() - ((Collidable) thisObject).getCollisionBox().getHeight());
					((Collidable) thisObject).setColXOverride(xColDiff);
					((Collidable) thisObject).setColYOverride(yColDiff);
				}
			}
		}
		// add offset if the current animation just got a new frame
		if(currentAnim.newFrame() && currentAnim.getOffsetFrame() != null){
			thisObject.addX(currentAnim.getOffsetFrame()[0]);
			thisObject.addY(currentAnim.getOffsetFrame()[1]);
		}
	}
	
	public Animation getAnim(){ return currentAnim; }	//returns the current Animation
	
	
	/**
	 * Gets image from animation
	 * @param searchKey the key of the bufferedimage array
	 * @param index the index in the array
	 * @return the bufferedimage specified
	 */
	public BufferedImage getImage(String searchKey, int index){
		if(imageCollection.containsKey(searchKey)){
			return imageCollection.get(searchKey)[index];
		}
		else{
			System.out.println("Animator-getImage: Can't find image");
			return null;
		}
	}
	
	/**
	 * Gets animation
	 * @param searchKey key to find animation in map
	 * @return the animation specified
	 */
	public Animation getAnimationCollection(String searchKey){
		if(animationCollection.containsKey(searchKey)){
			return animationCollection.get(searchKey);
		}
		else{
			System.out.println("Animator-getAnimationCollection: Can't find image");
			return null;
		}
	}
}
