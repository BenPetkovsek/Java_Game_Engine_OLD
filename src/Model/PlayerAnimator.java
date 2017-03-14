package Model;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import View.ImageStyler;
/**
 * Models all animation changes done to the player
 * Needed a whole class because the players animator will easily be the most complex
 * Its nice to have out of the player class
 * @author Michael
 *
 */
public class PlayerAnimator {
	
	
	//Animation Variables
	//holds all animation/images for external pulls in case needed
	private Map<String,BufferedImage[]> imageCollection = new HashMap<String,BufferedImage[]>();
	private Map<String,Animation> animationCollection = new HashMap<String,Animation>();
	
	private BufferedImage[] walkRight= {ImageStyler.loadImg("heroWalk1.png"),ImageStyler.loadImg("heroWalk2.png"),ImageStyler.loadImg("heroWalk3.png")};
	private BufferedImage[] walkLeft = ImageStyler.flipImgs(walkRight);
	
	private Animation walkRightAnim;
	private Animation walkLeftAnim;
	
	private BufferedImage[] idleRight = {ImageStyler.loadImg("heroIdle.png")};
	private BufferedImage[] idleLeft = ImageStyler.flipImgs(idleRight);
	
	private Animation idleRightAnim;
	private Animation idleLeftAnim;
	
	private BufferedImage[] attackRight = {ImageStyler.loadImg("heroAttack.png")};
	private BufferedImage[] attackLeft =ImageStyler.flipImgs(attackRight);
	
	private Animation attackRAnim;
	private Animation attackLAnim;
	
	private BufferedImage[] hurtRight = {ImageStyler.loadImg("heroHurt.png")};
	private BufferedImage[] hurtLeft = ImageStyler.flipImgs(hurtRight);
	
	private Animation hurtRightAnim;
	private Animation hurtLeftAnim;
	
	private Animation oldAnim;
	private Animation currentAnim;
	
	//player
	private Player player;
	
	/**
	 * Creates a player animator for player
	 * @param player the Player to animate
	 */
	public PlayerAnimator(Player player){
		this.player = player;
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
		attackRAnim = new Animation(false,1).addFrame(attackRight[0]);
		attackLAnim = new Animation(false,1).addFrame(attackLeft[0]);
		hurtRightAnim = new Animation(false,2).addFrameWithLength(hurtRight[0],8);
		hurtLeftAnim = new Animation(false,2).addFrameWithLength(hurtLeft[0],8);
		
		
		//init first anim
		if (player.facingRight()){
			currentAnim = idleRightAnim;
		}
		else{
			currentAnim = idleLeftAnim;
		}
		oldAnim = currentAnim;
		
	}
	
	//inits the maps and puts all elements in
	private void mapInit(){
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
	 * Updates the player's animation
	 */
	public void update(){
		Animation possibleNewAnim = currentAnim;
		
		//these should be in the same order of the priority
		/*if(player.getDx() <0){
			possibleNewAnim = walkLeftAnim; 
		}
		else if(player.getDx() >0){
			possibleNewAnim = walkRightAnim;
		}*/
		if(player.getDx() != 0){
			if(player.facingRight()){
				possibleNewAnim = walkRightAnim;
			}
			else{
				possibleNewAnim = walkLeftAnim;
			}
		}
		else if(player.getDx()==0){
			possibleNewAnim = (player.facingRight()) ? idleRightAnim : idleLeftAnim;
		}
		//getting attacked, do better
		if(player.isFrozen()){
			possibleNewAnim = (player.facingRight()) ? hurtRightAnim : hurtLeftAnim;
		}
		//attacking overrides movement animation
		if(player.isAttacking()){
			possibleNewAnim = (player.facingRight()) ? attackRAnim: attackLAnim;
		}
		
		
		//priority checking, animation change
		if(possibleNewAnim.getPriority() >= currentAnim.getPriority() || currentAnim.isFinished()){
			currentAnim = possibleNewAnim;
			if(oldAnim != currentAnim){
				currentAnim.reset();
			}
			player.setAnim(currentAnim);
		}
		oldAnim = currentAnim;
		currentAnim.update();

	}
	
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
			System.out.println("PlayerAnimator-getImage: Can't find image");
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
			System.out.println("PlayerAnimator-getAnimationCollection: Can't find image");
			return null;
		}
	}
	
	/**
	 * Gets current animation of the player
	 * @return current animation
	 */
	public Animation getCurrentAnim() { return currentAnim; }
}
