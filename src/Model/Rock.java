package Model;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import View.ImageStyler;

public class Rock extends GameObject{

	private Animation idleAnim;
	private BufferedImage idle = ImageStyler.loadImg("rock.png");
	
	public Rock(float x, float y){
		super(x,y);
		idleAnim = new Animation(false);
		idleAnim.addFrame(idle);
		currentAnim  = idleAnim;
		scale=0.5f;
		drawBorders=true;
		collisionBox = new Rectangle2D.Double(x,y,getWidth(),getHeight());
		
	}
	
	@Override
	public void update(){
		currentAnim.update();
	}
	
}
