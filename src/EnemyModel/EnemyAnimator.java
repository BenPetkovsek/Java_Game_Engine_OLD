package EnemyModel;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import AnimationModel.Animation;
import AnimationModel.CollisionBoxAnim;
import GameObjectModel.Animator;
import View.ImageStyler;
/**
 * Models all animation changes done to the enemy
 * Only allows enemys to have
 * @author Michael
 *
 */
//TODO Change for specific enemy, most likely wont have a EnemyAnimator as a parent class
public class EnemyAnimator<T extends Enemy> extends Animator<T> {
	
	
	private String pre = "Enemy/";	//folder location
	
	private BufferedImage[] idles = {ImageStyler.loadImg(pre+"walkR1.png"), 	//0 - right, 1 - left, 2 - up, 3 - down
			ImageStyler.flip(ImageStyler.loadImg(pre+"walkR1.png")),
			ImageStyler.loadImg(pre+"walkU1.png"),
			ImageStyler.loadImg(pre+"walkD1.png")};
	
	private BufferedImage[] walkRight = {ImageStyler.loadImg(pre+"walkR1.png"),ImageStyler.loadImg(pre+"walkR2.png"),ImageStyler.loadImg(pre+"walkR3.png"),ImageStyler.loadImg(pre+"walkR4.png")};
	private BufferedImage[] walkLeft =ImageStyler.flipImgs(walkRight);
	private BufferedImage[] walkUp = {ImageStyler.loadImg(pre+"walkU1.png"),ImageStyler.loadImg(pre+"walkU2.png"),ImageStyler.loadImg(pre+"walkU3.png"),ImageStyler.loadImg(pre+"walkU4.png")};	
	private BufferedImage[] walkDown = {ImageStyler.loadImg(pre+"walkD1.png"),ImageStyler.loadImg(pre+"walkD2.png"),ImageStyler.loadImg(pre+"walkD3.png"),ImageStyler.loadImg(pre+"walkD4.png")};
	
	private BufferedImage[] attackRight = {ImageStyler.loadImg(pre+"attackR1.png"),ImageStyler.loadImg(pre+"attackR2.png"),ImageStyler.loadImg(pre+"attackR3.png"),ImageStyler.loadImg(pre+"attackR4.png")};
	private BufferedImage[] attackLeft = ImageStyler.flipImgs(attackRight);
	private BufferedImage[] attackUp = {ImageStyler.loadImg(pre+"attackU1.png"),ImageStyler.loadImg(pre+"attackU2.png"),ImageStyler.loadImg(pre+"attackU3.png"),ImageStyler.loadImg(pre+"attackU4.png")};
	private BufferedImage[] attackDown = {ImageStyler.loadImg(pre+"attackD1.png"),ImageStyler.loadImg(pre+"attackD2.png"),ImageStyler.loadImg(pre+"attackD3.png"),ImageStyler.loadImg(pre+"attackD4.png")};
	
	
	private Animation idleR = new Animation(true, 0).addFrame(idles[0]);
	private Animation idleL = new Animation(true, 0).addFrame(idles[1]);
	private Animation idleU = new Animation(true, 0).addFrame(idles[2]);
	private Animation idleD = new Animation(true, 0).addFrame(idles[3]);
	
	private Animation walkRightAnim = new Animation(true,0).addFrame(walkRight[0]).addFrame(walkRight[1]).addFrame(walkRight[2]).addFrame(walkRight[3]);
	private Animation walkLeftAnim = new Animation(true,0).addFrame(walkLeft[0]).addFrame(walkLeft[1]).addFrame(walkLeft[2]).addFrame(walkLeft[3]);
	private Animation walkUpAnim = new Animation(true,0).addFrame(walkUp[0]).addFrame(walkUp[1]).addFrame(walkUp[2]).addFrame(walkUp[3]);
	private Animation walkDownAnim = new Animation(true,0).addFrame(walkDown[0]).addFrame(walkDown[1]).addFrame(walkDown[2]).addFrame(walkDown[3]);
	
	private Animation attackRightAnim = new Animation(false,1).addFrame(attackRight[0],40).addFrame(attackRight[1],40).addFrame(attackRight[2]).addFrame(attackRight[3]);
	private Animation attackLeftAnim = new Animation(false,1).addFrame(attackLeft[0],40).addFrame(attackLeft[1],40).addFrame(attackLeft[2]).addFrame(attackLeft[3]);
	private Animation attackUpAnim = new Animation(false,1).addFrame(attackUp[0],40).addFrame(attackUp[1],40).addFrame(attackUp[2]).addFrame(attackUp[3]);
	private Animation attackDownAnim = new Animation(false,1).addFrame(attackDown[0],40).addFrame(attackDown[1],40).addFrame(attackDown[2]).addFrame(attackDown[3]);
	
	private CollisionBoxAnim oldColAnim;  //TODO Move out of here and into standard animator
	
	//the collision box for when the enemy's sprite is set to up/down (requires different placement
	private CollisionBoxAnim upDown = new CollisionBoxAnim(false,0);	
	
	public EnemyAnimator(T entity) {
		super(entity);
		mapInit();
		currentAnim = idleR;
		float scale = thisObject.getScale();
		// TODO fix this mess
		upDown.addFrame(thisObject.getX(),thisObject.getY(),walkUp[0].getWidth()/2*thisObject.getScale(),currentAnim.getCurrFrame().getHeight()*scale,currentAnim.getCurrFrame().getWidth()*scale/4,0);
	}
	

	//TODO Move out of here and into standard animator
	@Override
	public void preUpdate(){
		super.preUpdate();
		oldColAnim = thisObject.getColAnim();
		thisObject.setColAnim(null);
	}
	
	public void update(){
		
		//setting basic movement direction
		switch(thisObject.getDirection()){
			case RIGHT:
				if (thisObject.getDx() != 0){
					possibleNewAnim = walkRightAnim;
				}
				else{
					possibleNewAnim = idleR;
				}
				break;
			case LEFT:
				if (thisObject.getDx() != 0){
					possibleNewAnim = walkLeftAnim;
				}
				else{
					possibleNewAnim = idleL;
				}
				break;
			case UP:
				if (thisObject.getDy() != 0){
					possibleNewAnim = walkUpAnim;
				}
				else{
					possibleNewAnim = idleU;
				}
				//thisObject.setColAnim(upDown);
				break;
			case DOWN:
				if (thisObject.getDy() != 0){
					possibleNewAnim = walkDownAnim;
				}
				else{
					possibleNewAnim = idleD;
				}
				//thisObject.setColAnim(upDown);
				break;
		}
		//if attacking set that
		if(thisObject.isAttacking()){
			switch(thisObject.getDirection()){
				case RIGHT: possibleNewAnim = attackRightAnim; break; 
				case LEFT: possibleNewAnim = attackLeftAnim; break;
				case UP: possibleNewAnim = attackUpAnim; break;
				case DOWN: possibleNewAnim = attackDownAnim; break;
			}
		}
	}
	
	// take in col box anims 
	@Override
	public void postUpdate(){
		super.postUpdate();
		CollisionBoxAnim colAnim = thisObject.getColAnim();
		if(oldColAnim != colAnim && colAnim != null){
			colAnim.reset();
		}
		if(colAnim != null){ colAnim.update(); }
	}
	
	//inits the maps and puts all elements in
	@Override
	protected void mapInit(){
		imageCollection.put("walkRight", walkRight);
		imageCollection.put("walkLeft", walkLeft);
		imageCollection.put("walkUp", walkUp);
		imageCollection.put("walkDown", walkDown);
		imageCollection.put("idles", idles);
		imageCollection.put("attackRight", attackRight);
		imageCollection.put("attackLeft", attackLeft);
		imageCollection.put("attackUp", attackUp);
		imageCollection.put("attackDown", attackDown);
	
	}
	
	

}
