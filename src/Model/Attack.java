/*
 * This class will model attacks in the game
 * game objects, specifically player and enemies can have, possibly NPC's if developed
 * should hold attack dmg, duraction, animation etc.
 */
package Model;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Attack extends GameObject{

	//Gameobject that is attacking
	private GameObject holder;
	//dmg
	private int damage;
	
	//col box
	private float width;
	private float height;
	
	//the local x,y position
	private float xDiff;
	private float yDiff;

	
	//state
	private boolean active;
	
	private int duration;
	private int time;
	
	/**
	 * 
	 * @param source
	 * @param x	local x pos. in ref to source mid point
	 * @param y local y pos. in ref to source mid point
	 * @param DMG
	 * @param width
	 * @param height
	 */
	public Attack(GameObject source,float x, float y,int DMG, float width, float height, int duration){
		super((source.getX() +source.getWidth())/2+x,source.getY()+y);
		this.xDiff = x;
		this.yDiff =y;
		
		this.holder =source;
		this.damage= DMG;
		
		this.width = width;
		this.height =height;
		this.duration  =duration;
		time = 0;
		active=true;
		collisionBox = new Rectangle2D.Double(x,y,getWidth(),getHeight());

	}
	
	
	@Override
	public float getWidth(){
		return width;
	}
	@Override
	public float getHeight(){
		return height;
	}
	
	public int getDmg(){ return damage; }
	
	//activates the attack
	public void activate(){ 
		active =true;
		time =0;
	}
	//returns if the attack is active
	public boolean isActive(){
		return active;
	}
	//updates the attack
	public void update(){
		//timer for attack
		if(active){
			//matchs it with right side of border;
			// TODO do this better lmao
			x = holder.getX() + xDiff;
			if(!holder.facingRight()){
				x= holder.getX() - getWidth() + 14*holder.getScale()+20;		//14 is the difference, its really shit rn so bare with  me lmao
			}
			y = holder.getY() + yDiff;
			if(time == duration){
				active =false;
			}
			time++;
		}
	}
	
}
