package Model;

import View.ImageStyler;

public class Enemy extends GameObject {
	
	int HP, totalHP, str, def, intel;
	String name;
	
	private Animation idle;
	
	public Enemy(float x, float y){
		super(x,y);
		idle = new Animation(false).addFrame(ImageStyler.loadImg("dog.png"));
		currentAnim = idle;
		scale = 5f;
		drawBorders=true;
		isCollidable =false;
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
		HP -= dmg;
		checkDeath();
	}
	
	private void checkDeath(){
		if(HP <= 0){
			System.out.println(name + " has died!");
		}	
	}
	
	public void update(Player hero){
		currentAnim.update();
		if(checkAllCollision(hero.getAttack())){
			System.out.println("buh");
		}
		else if(checkAllCollision(hero)){
			hero.takeDamage(20,this);
		}
	}

	
}//end class
