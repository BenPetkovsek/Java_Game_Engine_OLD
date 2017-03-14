package Model;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;


import View.ImageStyler;

public class Enemy extends Collidable {
	
	int HP, totalHP, str, def, intel;
	String name;
	
	private Animation idle;
	private Animation hurt;
	private Animation dead;
	
	
	
	//TODO gameobject sub class should take care of movement
/*	private float dx;
	private float dy;*/
	
	public Enemy(float x, float y){
		super(x,y);
		idle = new Animation(false,0).addFrame(ImageStyler.loadImg("dog.png"));
		hurt = new Animation(false,1).addFrame(ImageStyler.loadImg("dogHurt.png")).addFrame(ImageStyler.loadImg("dogHurt.png"));
		hurt.setRefreshRate(20);
		setAnim(idle);
		setScale(5f);
		setTrigger(true);
		HP =100;
		collisionBox = new Rectangle2D.Float(x,y,getWidth(),getHeight());
		
	}

	public void takeDamage(int dmg,Collidable a){
		if(!isInvulnerable()){
			EffectManager.addEffect(new SimpleKnockBack(a, this, 100, 5));
			EffectManager.addEffect(new Invulnerability(80, 10,this));
			HP -= dmg;
			checkDeath();
			setAnim(hurt);
			getAnim().reset();
		}
		
	}
	
	private void checkDeath(){
		if(HP <= 0){
			System.out.println(name + " has died!");
			setAnim(dead);
			
		}	
	}
	
	public void update(Player hero){
		x += dx;
		y += dy;
		//checks if the enemy gets hit by player, also checks if the hero is collide if so hero gets hurt then
		if(checkCollision(hero.getAttack()) && hero.getAttack().isActive() && !checkCollision(hero)){	
			takeDamage(20,hero);
			
			
		}
		else if(checkCollision(hero)){		//collide with player
			hero.takeDamage(20,this);
		}
		else if(checkWeaponCollision(hero.getWeapon())){	//collide with enemy
			takeDamage(20,hero);
		}
		//animation updates
		if(getAnim() == hurt && getAnim().isFinished()){
			setAnim(idle);
		}
		getAnim().update();

	}
	
	
	//PLACEHOLDER PROTOTYPE
	private boolean checkWeaponCollision(Weapon weap){
		Area a =new Area(getCollisionBox());
		Area b = new Area(weap.getNewCollisionBox());
		a.intersect(b);
		return !a.isEmpty();
	}
	

	
}//end class
