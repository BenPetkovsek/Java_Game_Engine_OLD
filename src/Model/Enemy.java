package Model;

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
		currentAnim = idle;
		scale = 5f;
		setTrigger(true);
		HP =100;
		collisionBox = new Rectangle2D.Float(x,y,getWidth(),getHeight());
		
	}

	public void takeDamage(int dmg,Player hero){
		if(!isInvulnerable()){
			EffectManager.addEffect(new SimpleKnockBack(hero, this, 100, 5));
			EffectManager.addEffect(new Invulnerability(80, 10,this));
			HP -= dmg;
			checkDeath();
			currentAnim = hurt;
			currentAnim.reset();
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
		//checks if the enemy gets hit by player, also checks if the hero is collide if so hero gets hurt then
		if(checkCollision(hero.getAttack()) && hero.getAttack().isActive() && !checkCollision(hero)){	
			takeDamage(20,hero);
			
			
		}
		else if(checkCollision(hero)){
			hero.takeDamage(20,this);
		}
		//animation updates
		if(currentAnim == hurt && currentAnim.isFinished()){
			currentAnim = idle;
		}
		currentAnim.update();

	}
	

	
}//end class
