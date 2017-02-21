package Model;

/**
 * Models any gameobject that is animatable
 * Meaning it can be drawn with a sprite
 * @author Michael
 *
 */
public abstract class Animatable extends GameObject{

	private Animation currentAnim;
	private boolean facingRight =true;
	private float scale=1;
	
	
	public Animatable(float x, float y) {
		super(x,y);
	}
	
	@Override
	public void update(){
		currentAnim.update();
	}

	//GETTERS
	
	public Animation getAnim(){ return currentAnim; }
	
	public boolean facingRight(){ return facingRight; }
	
	public float getScale(){ return scale; }
	
	public float getWidth(){
		if(currentAnim !=null){
			return ((float) currentAnim.getCurrFrame().getWidth())*scale;
		}
		return 0;
	}
	
	public float getHeight(){
		if(currentAnim !=null){
			return ((float) currentAnim.getCurrFrame().getHeight())*scale;
		}
		return 0;
	}

	//SETTERS
	public void setAnim(Animation anim){ this.currentAnim = anim; }
	
	public void setFacingRight(boolean facingRight){ this.facingRight = facingRight; }
	
	public void setScale(float newScale){ this.scale=  newScale; }
}
