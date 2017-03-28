/*
 * This class will model a basic attack with a variable delay before
 * game objects, specifically player and enemies can have, possibly NPC's if developed
 * should hold attack dmg, duraction, animation etc.
 * Currently models attacks attached to enemyies, not projectiles for example 
 */
package MiscModel;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import EnemyModel.Enemy;
import GameObjectModel.Collidable;
import GameObjectModel.Direction;

public class Attack extends Collidable{

	//Gameobject that is attacking
	private Enemy holder;	//TODO change to Collidable (or Creature if implemented)
	//dmg
	private int damage;
	
	//col box
	//use for rotations
	private float width;	
	private float height;
	
	//the x and y offset the edges of the sources collision box
	private float xDiff;
	private float yDiff;

	

	
	//state
	private boolean active=false;		//active means the attack is updating
	private boolean live =false;		//live means it will do damage (different from active since there can be a delay)
	
	private int duration;		//duration of live attack
	private int delayDuration;	//duration of delay before
	private int time;			//internal timer
	
	
	
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
	//TODO change from enemy
	public Attack(Enemy source,float x, float y,int DMG, float width, float height, int duration, int delayDuration){
		super(source.getX()+x,source.getY()+y);	//init the attack at the same x,y as player then local axis
		this.xDiff = x;
		this.yDiff =y;
		
		this.holder =source;
		this.damage= DMG;
		
		this.width = width;
		this.height =height;
		this.duration  =duration;
		this.delayDuration = delayDuration;
		
		
		time = 0;
		collisionBox = new Rectangle2D.Float(x,y,width,height);

	}
	//updates the attack's position and internal timer
	@Override
	public void update(){
		//timer for attack
		if(active){
			if(time >= delayDuration && !live){	//delay timer
				time=0;
				live =true;
			}
			else{
				time++;
			}
			if(live){					//live timer
				attackPlacement();
				if(time >= duration){
					active =false;
					live=false;
				}
				else{
					time++;	
				}
			} 
		}
	}
	private void attackPlacement(){
		//if the collision box needs to be rotated (L/R will be rotated version of U/D)
		if(holder.getDirection() == Direction.RIGHT || holder.getDirection() ==Direction.LEFT){
			collisionBox = new Rectangle2D.Float((float) collisionBox.getX(),(float) collisionBox.getY(),width,height);
		}
		else{
			collisionBox = new Rectangle2D.Float((float) collisionBox.getX(),(float) collisionBox.getY(), height, width);
		}
		//set x,y to holders x y
		x = (float) holder.getCollisionBox().getX();
		y = (float) holder.getCollisionBox().getY();
		//collision orientation based on direction of holder because (x,y) is situated at top left it causes ALL KINDS OF PROBLEMS
		switch(holder.getDirection()){
			case RIGHT: x += holder.getCollisionBox().getWidth() + xDiff;			//right
					y += yDiff;
					break;
			case LEFT: x += -getWidth() - xDiff;		//left
					y += yDiff;
					break;
			case UP: x += yDiff;		//up
					y += -getHeight() - xDiff;
					break;
			case DOWN: x += yDiff;		//down
					y += holder.getCollisionBox().getHeight() + xDiff;
					break;
		}
	}
	@Override
	public float getWidth(){
		return (float) collisionBox.getWidth();
	}
	@Override
	public float getHeight(){
		return (float) collisionBox.getHeight();
	}
	
	public int getDmg(){ return damage; }
	
	
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
	//returns if the attack is live
	public boolean isLive(){
		return live;
	}
	
	
	
}
