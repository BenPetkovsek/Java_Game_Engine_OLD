package Model;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import View.ImageStyler;
/**
 * Models the Weapon for the player
 * @author Michael
 *
 */
public class Weapon extends Collidable{
	
	//Sword Anims
	private BufferedImage[] sword= {ImageStyler.loadImg("sword.png")};
	private Animation swordIdleR = new Animation(true, 0).addFrame(sword[0]);
	private Animation swordIdleL = new Animation(true,0).addFrame(ImageStyler.flip(sword[0]));
	
	//Ax Animes
	private BufferedImage[] axe= {ImageStyler.loadImg("axe2.png")};
	private Animation axeIdleR = new Animation(true, 0).addFrame(axe[0]);
	private Animation axeIdleL = new Animation(true,0).addFrame(ImageStyler.flip(axe[0]));
	
	private String[] weapons = {"sword","axe"};
	private int index;
	
	private Player hero;
	
	private int offSetX;
	private int offSetY;
	
	private Double angle;
	private Double angleOffset= -45d;	//angle offset corresponds to the sprite angle, ex. sprite is drawn on 45 degree angle in this case
	
	/**
	 * Creates a new weapon
	 * @param hero the player the weapon belongs to
	 * @param index the index of the weapon
	 */
	public Weapon(Player hero, int index) {
		super(hero.getX(), hero.getY());
		this.hero = hero;
		this.index =index;
		setScale(1f);
		init();
	}
	
	/*
	 * Initialization on weapon change
	 */
	private void init(){
		if(index ==0){
			setAnim(swordIdleR);
		}
		else if(index ==1){
			setAnim(axeIdleR);
		}
		offSetX = (int) (hero.getWidth()/2);
		offSetY = (int) -(getHeight() -hero.getHeight()/2);
		collisionBox = new Rectangle2D.Float(0,0,getWidth(),getHeight());
	}
	
	/**
	 * Updates The weapon position/animation/collisionbox
	 * @param angle
	 */
	public void update(Double angle ){
		super.update();
		//update position to player +offset
		this.x = (float) (hero.getCollisionBox().getX() + offSetX);
		this.y = (float) (hero.getCollisionBox().getY() + offSetY);
		//If mouse is right of player
		if((angle<=90 && angle >= 0) || (angle<360 && angle >=270)){
			setFacingRight(true);
			if(index ==0){
				setAnim(swordIdleR);
			}
			else if(index ==1){
				setAnim(axeIdleR);
			}
		}
		else{		//mouse right of player
			setFacingRight(false);
			this.x = (float) hero.getCollisionBox().getX() + (hero.getWidth()/2 -getWidth());
			if(index ==0){
				setAnim(swordIdleL);
			}
			else if(index ==1){
				setAnim(axeIdleL);
			}
		}
		this.angle =angle+angleOffset;
	}
	
	public Shape getNewCollisionBox(){
		AffineTransform trans  = new AffineTransform();
		double angle = facingRight() ? getAngle() : getAngle()-90;
		
		//rotate anchor point
		double xAnchor = facingRight() ? 0 : getWidth()/getScale();
		double yAnchor = getHeight()/getScale();

		//Affine transformations
		trans.translate(getX(), getY());
		trans.scale(getScale(),getScale());
		trans.rotate(-Math.toRadians(angle),xAnchor,yAnchor);
		return trans.createTransformedShape(collisionBox);
		
	}
	
	/**
	 * Change Weapon
	 * @param up if the weapon index is increased or decreased
	 */
	public void change(boolean up){
		if(up){
			index++;
			index = (index) % weapons.length;
		}
		else{
			index--;
			if (index <0) index = weapons.length -1;
		}
		init();
	}
	
	/**
	 * @return weapon offset in x dir
	 */
	public int getOffsetX(){ return offSetX; }
	/**
	 * @return weapon offset in y dir
	 */
	public int getOffsetY(){ return offSetY; }
	/**
	 * @return angle of weapon in degrees
	 */
	public Double getAngle(){ return angle; }
	
	
}
