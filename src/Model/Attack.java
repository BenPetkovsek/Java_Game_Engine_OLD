/*
 * This class will model attacks in the game
 * game objects, specifically player and enemies can have, possibly NPC's if developed
 * should hold attack dmg, duraction, animation etc.
 */
package Model;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Attack extends Collidable{

	//Gameobject that is attacking
	private Collidable holder;
	//dmg
	private int damage;
	
	//col box
	private float width;
	private float height;
	
	//the local x,y position
	private float xDiff;
	private float yDiff;

	
	//extension of attack sprite	//idk if this is even still valid but yolo
	float xOffset;
	
	//state
	private boolean active=false;
	
	private int duration;
	private int time;
	
	/**
	 * @brief creates an attack object with certain specs
	 * @param source object that spawns the attack
	 * @param x local x pos
	 * @param y local y pos
	 * @param DMG damage of attack
	 * @param width collision width of attack
	 * @param height collision height of attack
	 * @param duration duration in time of attack
	 * @param xOffset how much the attack sprite extends the regular sprite	//deprecated or some shit
	 */
	public Attack(Collidable source,float x, float y,int DMG, float width, float height, int duration, float xOffset){
		super(source.getX()+x,source.getY()+y);	//init the attack at the same x,y as player then local axis
		this.xDiff = x;
		this.yDiff =y;
		
		this.holder =source;
		this.damage= DMG;
		
		this.width = width;
		this.height =height;
		this.duration  =duration;
		
		this.xOffset = xOffset;
		
		
		time = 0;
		collisionBox = new Rectangle2D.Float(x,y,getWidth(),getHeight());

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
	
	public float getOffset(){ return xOffset; }
	
	//activates the attack
	public void activate(){ 
		active =true;
		time =0;
	}
	//force stop
	public void stop(){
		active=false;
	}
	//returns if the attack is active
	public boolean isActive(){
		return active;
	}
	//updates the attack's position and internal timer
	@Override
	public void update(){
		//timer for attack
		if(active){
			//matchs it with right side of border;
			if(!holder.facingRight()){
				x= holder.getX();
			}
			else{
				x = holder.getX() + xDiff;
			}
			//no need for y-dir yet
			y = holder.getY() + yDiff;
			if(time == duration){
				active =false;
			}
			time++;
		}
	}
	
}
