package PlayerModel;

import java.awt.image.BufferedImage;	
import java.util.HashMap;
import java.util.Map;

import AnimationModel.Animation;
import GameObjectModel.Animator;
import GameObjectModel.Direction;
import View.ImageStyler;
/**
 * Models all animation changes done to the player
 * Only allows The player class to have
 * @author Michael
 *
 */
public class PlayerAnimator<T extends Player> extends Animator<T> {
	
	
	private BufferedImage[] walkRight= {ImageStyler.loadImg("heroWalk1.png"),ImageStyler.loadImg("heroWalk2.png"),ImageStyler.loadImg("heroWalk3.png")};
	private BufferedImage[] walkLeft = ImageStyler.flipImgs(walkRight);
	
	private Animation walkRightAnim;
	private Animation walkLeftAnim;
	
	private BufferedImage[] idleRight = {ImageStyler.loadImg("heroIdle.png")};
	private BufferedImage[] idleLeft = ImageStyler.flipImgs(idleRight);
	
	private Animation idleRightAnim;
	private Animation idleLeftAnim;
	
	private BufferedImage[] attackRight = {ImageStyler.loadImg("heroAttack2.png"),ImageStyler.loadImg("heroAttack.png"),ImageStyler.loadImg("heroAttack2.png")};
	private BufferedImage[] attackLeft =ImageStyler.flipImgs(attackRight);
	
	private Animation attackRAnim;
	private Animation attackLAnim;
	
	private BufferedImage[] hurtRight = {ImageStyler.loadImg("heroHurt.png")};
	private BufferedImage[] hurtLeft = ImageStyler.flipImgs(hurtRight);
	
	private Animation hurtRightAnim;
	private Animation hurtLeftAnim;
	
	
	
	/**
	 * Creates a player animator for player
	 * @param player the Player to animate
	 */
	public PlayerAnimator(T player){
		super(player);
		animInit();
		mapInit();
	}
	
	//init for all anims
	private void animInit(){
		//initialize images for all anims
		walkRightAnim= new Animation(true,0);
		walkRightAnim.addFrame(walkRight[0]).addFrame(walkRight[1]).addFrame(walkRight[2]);
		walkLeftAnim= new Animation(true,0);
		walkLeftAnim.addFrame(walkLeft[0]).addFrame(walkLeft[1]).addFrame(walkLeft[2]);
		idleRightAnim = new Animation(true,0);
		idleRightAnim.addFrame(idleRight[0]);
		idleLeftAnim = new Animation(true,0);
		idleLeftAnim.addFrame(idleLeft[0]);
		attackRAnim = new Animation(false,1).addFrame(attackRight[0]).addFrame(attackRight[1]).addFrame(attackRight[2]);
		attackLAnim = new Animation(false,1).addFrame(attackLeft[0],10).addFrame(attackLeft[1],10).addFrame(attackLeft[2],10);
		hurtRightAnim = new Animation(false,2).addFrame(hurtRight[0],8);
		hurtLeftAnim = new Animation(false,2).addFrame(hurtLeft[0],8);
		
		
		//init first anim
		if (thisObject.getDirection() == Direction.RIGHT){
			currentAnim = idleRightAnim;
		}
		else{
			currentAnim = idleLeftAnim;
		}
		oldAnim = currentAnim;
		
	}
	
	//inits the maps and puts all elements in
	@Override
	protected void mapInit(){
		imageCollection.put("walkRight", walkRight);
		imageCollection.put("walkLeft", walkLeft);
		imageCollection.put("idleRight", idleRight);
		imageCollection.put("idleLeft", idleLeft);
		imageCollection.put("attackRight", attackRight);
		imageCollection.put("attackLeft", attackLeft);
		imageCollection.put("hurtRight", hurtRight);
		imageCollection.put("hurtLeft", hurtLeft);
		
		animationCollection.put("walkRight", walkRightAnim);
		animationCollection.put("walkLeft", walkLeftAnim);
		animationCollection.put("idleRight", idleRightAnim);
		animationCollection.put("idleLeft", idleLeftAnim);
		animationCollection.put("attackRight", attackRAnim);
		animationCollection.put("attackLeft", attackLAnim);
		animationCollection.put("hurtRight", hurtRightAnim);
		animationCollection.put("hurtLeft", hurtLeftAnim);
	}
	
	
	/**
	 * Updates the player's animation, does not use the animation standard update
	 */
	public void update(){
		
		// Sets walk anims
		if(thisObject.getDx() != 0){
			if(thisObject.getDirection() == Direction.RIGHT){
				possibleNewAnim = walkRightAnim;
			}
			else{
				possibleNewAnim = walkLeftAnim;
			}
		}
		else if(thisObject.getDx()==0){
			possibleNewAnim = (thisObject.getDirection() == Direction.RIGHT) ? idleRightAnim : idleLeftAnim;
		}
		//getting attacked, do better, dont make it dependent on if frozen but if hurt
		if(thisObject.isFrozen()){
			possibleNewAnim = (thisObject.getDirection() == Direction.RIGHT) ? hurtRightAnim : hurtLeftAnim;
		}
		//attacking overrides movement animation
		if(thisObject.isAttacking()){
			possibleNewAnim = (thisObject.getDirection() == Direction.RIGHT) ? attackRAnim: attackLAnim;
			// TODO remove this
			if (currentAnim.isFinished()){
				thisObject.stopAttacking();
			}
		}
	

	}
	

}
