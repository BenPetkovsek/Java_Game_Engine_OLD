package Model;

import java.awt.geom.Rectangle2D;

import View.ImageStyler;

public class Enemy extends GameObject {
	
	int HP, totalHP, str, def, intel;
	String name;
	
	private Animation idle;
	private Animation hurt;
	private Animation dead;
	
	private KnockBack hit;
	
	private Invulnerability grace;
	
	//TODO gameobject sub class should take care of movement
/*	private float dx;
	private float dy;*/
	
	public Enemy(float x, float y){
		super(x,y);
		idle = new Animation(false).addFrame(ImageStyler.loadImg("dog.png"));
		hurt = new Animation(false).addFrame(ImageStyler.loadImg("dogHurt.png")).addFrame(ImageStyler.loadImg("dogHurt.png"));
		hurt.setRefreshRate(20);
		currentAnim = idle;
		scale = 5f;
		drawBorders=true;
		isCollidable =false;
		HP =100;
		grace= new Invulnerability(80, 10);
		collisionBox = new Rectangle2D.Float(x,y,getWidth(),getHeight());
		
	}
/*	public Enemy(String initName, int initHP, int initStr, int initDef, int initIntel ){
		HP = initHP;
		totalHP = initHP;
		str = initStr;
		def = initDef;
		intel = initIntel;
		name = initName;
		
	}*/
	
	
	public void takeDamage(int dmg){
		if(!grace.going()){
			HP -= dmg;
			checkDeath();
			currentAnim = hurt;
			currentAnim.reset();
			grace.start();
		}
		
	}
	
	private void checkDeath(){
		if(HP <= 0){
			System.out.println(name + " has died!");
			currentAnim = dead;
			
		}	
	}
	
	public void update(Player hero){
		x += dx;
		y += dy;
		//knockback updates
		if(hit !=null){
			if(hit.getStatus()){
				hit.update();
				takeDamage(hero.getAttack().getDmg());
			}
			else{	
				hit=null;	//reset
			}
		}
		//checks if the enemy gets hit by player, also checks if the hero is collide if so hero gets hurt then
		if(checkCollision(hero.getAttack()) && hero.getAttack().isActive() &&!checkCollision(hero)){	
			if(!grace.going()){
				hit = new SimpleKnockBack(hero, this, 150, 3);
			}
			
			
		}
		else if(checkCollision(hero)){
			hero.takeDamage(20,this);
		}
		//animation updates
		if(currentAnim == hurt && currentAnim.isFinished()){
			currentAnim = idle;
		}
		currentAnim.update();
		
		//grace updates
		if(grace.going()){
			grace.update();
		}
	}
	
	public boolean isGraced(){ return grace.going(); }

	
}//end class
