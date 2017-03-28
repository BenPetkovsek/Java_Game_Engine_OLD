package GameObjectModel;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import AnimationModel.Animation;
import View.ImageStyler;

/**
 * Models a static collidable object used for terrain. So rocks and trees
 * Decided to make one class for this because the difference betwwen a rock and tree is only the sprite
 * @author Michael
 *
 */
public class TerrainObject extends Collidable{

	private Animation idleAnim;
	private BufferedImage rockIdle = ImageStyler.loadImg("rock.png");
	
	public TerrainObject(float x, float y, StaticType type){
		super(x,y);
		idleAnim = new Animation(false,0);
		init(type);
		setScale(0.5f);
		collisionBox = new Rectangle2D.Float(x,y,getWidth(),getHeight());
		
		
	}
	//Sets up the terrain with the correct sprite
	private void init(StaticType e){
		if(e == StaticType.ROCK){
			idleAnim.addFrame(rockIdle);
			setAnim(idleAnim);
		}
	}
	
}
