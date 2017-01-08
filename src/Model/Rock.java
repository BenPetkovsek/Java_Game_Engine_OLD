package Model;

import java.awt.image.BufferedImage;

import View.ImageStyler;

public class Rock extends GameObject{

	private Animation idleAnim;
	private BufferedImage idle = ImageStyler.loadImg("Art/rock.png");
	
	public Rock(float x, float y){
		super(x,y);
		idleAnim = new Animation(false);
		idleAnim.addFrame(idle);
		currentAnim  = idleAnim;
		scale=0.5f;
		drawBorders=true;
		setOffsets(0,0,20,-20);
		
	}
	
	public void update(Player hero){
		currentAnim.update();
	}
	
}
