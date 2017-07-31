package GameObjectModel;

import java.awt.image.BufferedImage;

import AnimationModel.Animation;
import AnimationModel.CollisionBoxAnim;

/**
 * Models any gameobject that is animatable
 * Meaning it can be drawn with a sprite
 * Just holds the info for the animatables at a black box, all animation logic should be kept in an animator
 * Although if the animatable is simple enough, the animator can be ignored and all logic should be contained within
 * the specific Class extending Animatible to keep everything nice and isolated
 * @author Michael
 *
 */


public abstract class Animatable extends GameObject{

	protected  Animator<?> animator;	//Uses animator whos type of undefined here, classes will make their own animators with own type
	private Animation currentAnim;		//if the animator is null use this
	
	private Direction direction = Direction.RIGHT;
	private float scale=1;
	
	
	public Animatable(float x, float y) {
		super(x,y);
		
	}
	
	// Runs before any specific animation changing logic
	@Override
	public void update(){
		super.update();
		// If there is no animator do nothing
		if (animator != null){
			animator.preUpdate();
			animator.update();
			animator.postUpdate();
		}
	}

	//GETTERS
	
	//gets current anim
	public Animation getAnim(){ 
		if (animator != null){
			return animator.getAnim();
		}
		else {
			return currentAnim;
		}
	}
	
	public Direction getDirection(){ return direction; }
	
	public float getScale(){ return scale; }
	
	public float getWidth(){
		if(getAnim() !=null){
			return ((float) getAnim().getCurrFrame().getWidth())*scale;
		}
		return 0;
	}
	
	public float getHeight(){
		if(getAnim() !=null){
			return ((float) getAnim().getCurrFrame().getHeight())*scale;
		}
		return 0;
	}
	
	// Get animator
	public Animator<?> getAnimator(){ return animator; }

	//SETTERS
	// TODO reevaluate if this should be a thing
	public void setAnim(Animation anim){ 
		if (animator != null){
			animator.currentAnim = anim; 
		}
		else{
			currentAnim = anim;
		}
	}
		
	
	public void setDirection(Direction newDir){ this.direction = newDir; }
	public void setScale(float newScale){ this.scale=  newScale; }
	
}
