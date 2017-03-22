package EnemyModel;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import EffectModel.EffectManager;
import EffectModel.Invulnerability;
import EffectModel.SimpleKnockBack;
import GameObjectModel.Collidable;
import MiscModel.Animation;
import PlayerModel.Player;
import View.ImageStyler;

public class Enemy extends Collidable {
	
	//TODO HAVE A FREEZE OPTION- make creature parent class for enemy and player
	int HP, totalHP, str, def, intel;
	String name;
	int direction = 0;
	
	/******AI Vars******/

	private AI behaviour;
	
	/******Physics Vars**********/
	
	
	private final float MOVESPEEDX = 1f;
	private final float MOVESPEEDY = 1f;
	
	
	/******Animation Vars*******/
	
	private String pre = "Enemy/";	//folder location
	
	private BufferedImage[] idles = {ImageStyler.loadImg(pre+"walkR1.png"), 	//0 - right, 1 - left, 2 - up, 3 - down
			ImageStyler.flip(ImageStyler.loadImg(pre+"walkR1.png")),
			ImageStyler.loadImg(pre+"walkU1.png"),
			ImageStyler.loadImg(pre+"walkD1.png")};
	
	private BufferedImage[] walkRight = {ImageStyler.loadImg(pre+"walkR1.png"),ImageStyler.loadImg(pre+"walkR2.png"),ImageStyler.loadImg(pre+"walkR3.png"),ImageStyler.loadImg(pre+"walkR4.png")};
	private BufferedImage[] walkLeft =ImageStyler.flipImgs(walkRight);
	private BufferedImage[] walkUp = {ImageStyler.loadImg(pre+"walkU1.png"),ImageStyler.loadImg(pre+"walkU2.png"),ImageStyler.loadImg(pre+"walkU3.png"),ImageStyler.loadImg(pre+"walkU4.png")};	
	private BufferedImage[] walkDown = {ImageStyler.loadImg(pre+"walkD1.png"),ImageStyler.loadImg(pre+"walkD2.png"),ImageStyler.loadImg(pre+"walkD3.png"),ImageStyler.loadImg(pre+"walkD4.png")};
	
	private BufferedImage[] attackRight = {ImageStyler.loadImg(pre+"attackR1.png"),ImageStyler.loadImg(pre+"attackR2.png"),ImageStyler.loadImg(pre+"attackR3.png"),ImageStyler.loadImg(pre+"attackR4.png")};
	private BufferedImage[] attackLeft = ImageStyler.flipImgs(attackRight);
	private BufferedImage[] attackUp = {ImageStyler.loadImg(pre+"attackU1.png"),ImageStyler.loadImg(pre+"attackU2.png"),ImageStyler.loadImg(pre+"attackU3.png"),ImageStyler.loadImg(pre+"attackU4.png")};
	private BufferedImage[] attackDown = {ImageStyler.loadImg(pre+"attackD1.png"),ImageStyler.loadImg(pre+"attackD2.png"),ImageStyler.loadImg(pre+"attackD3.png"),ImageStyler.loadImg(pre+"attackD4.png")};
	
	
	private Animation idleR = new Animation(true, 0).addFrame(idles[0]);
	private Animation idleL = new Animation(true, 0).addFrame(idles[1]);
	private Animation idleU = new Animation(true, 0).addFrame(idles[2]);
	private Animation idleD = new Animation(true, 0).addFrame(idles[3]);
	
	private Animation walkRightAnim = new Animation(true,1).addFrame(walkRight[0]).addFrame(walkRight[1]).addFrame(walkRight[2]).addFrame(walkRight[3]);
	private Animation walkLeftAnim = new Animation(true,1).addFrame(walkLeft[0]).addFrame(walkLeft[1]).addFrame(walkLeft[2]).addFrame(walkLeft[3]);
	private Animation walkUpAnim = new Animation(true,1).addFrame(walkUp[0]).addFrame(walkUp[1]).addFrame(walkUp[2]).addFrame(walkUp[3]);
	private Animation walkDownAnim = new Animation(true,1).addFrame(walkDown[0]).addFrame(walkDown[1]).addFrame(walkDown[2]).addFrame(walkDown[3]);
	
	private Animation attackRightAnim = new Animation(false,2).addFrame(attackRight[0]).addFrame(attackRight[1]).addFrame(attackRight[2]).addFrame(attackRight[3]);
	private Animation attackLeftAnim = new Animation(false,2).addFrame(attackLeft[0]).addFrame(attackLeft[1]).addFrame(attackLeft[2]).addFrame(attackLeft[3]);
	private Animation attackUpAnim = new Animation(false,2).addFrame(attackUp[0]).addFrame(attackUp[1]).addFrame(attackUp[2]).addFrame(attackUp[3]);
	private Animation attackDownAnim = new Animation(false,2).addFrame(attackDown[0]).addFrame(attackDown[1]).addFrame(attackDown[2]).addFrame(attackDown[3]);
	
	
	
	
	//TODO gameobject sub class should take care of movement
/*	private float dx;
	private float dy;*/
	
	public Enemy(float x, float y){
		super(x,y);
		behaviour = new RandomAI(this,true, false, 300, 300,100,70, 300, 100, 300, 100);
		setAnim(idleR);
		setScale(5f);
		setTrigger(true);
		HP =100;
		collisionBox = new Rectangle2D.Float(x,y,getWidth(),getHeight());
		
	}

	public void takeDamage(int dmg,Collidable a){
		if(!isInvulnerable()){
			EffectManager.addEffect(new SimpleKnockBack(a, this, 200, 5));
			EffectManager.addEffect(new Invulnerability(80, 10,this));
			HP -= dmg;
			checkDeath();
			//setAnim(hurt);
			getAnim().reset();
		}
		
	}
	
	private void checkDeath(){
		if(HP <= 0){
			System.out.println(name + " has died!");
			//setAnim(dead);
			
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
		else if(checkShapeCollision(hero.getWeapon())){	//collide with enemy
			//takeDamage(20,hero);
		}
		
		//AI
		behaviour.update();
		
		//direction updates
		if(!behaviour.isChasing()){			//if not following 
			if(dx > 0){
			direction = 0;
			}
			else if(dx <0){
				direction = 1;
			}
			else if(dy <0){
				direction = 2;
			}
			else if(dy >0){
				direction = 3;
			}
		}
		else{	//direction changes are different if following
			//TODO possible seperate this
			double xDiff = hero.getCollisionBox().getCenterX()-getCollisionBox().getCenterX();
			double yDiff = hero.getCollisionBox().getCenterY()-getCollisionBox().getCenterY();
			if(Math.abs(xDiff) >= Math.abs(yDiff)){	//if x difference is greater than y, direction is L or R
				if(dx > 0){
					direction = 0;
				}
				else if(dx <0){
					direction = 1;
				}
			}
			else{						//if y diff is greater than x, direction is U or D
				if(dy <0){
					direction = 2;
				}
				else if(dy >0){
					direction = 3;
				}
			}
		}
		
		animationUpdates();
		

	}
	//updates animation
	private void animationUpdates(){
		Animation oldAnim = getAnim();
		if(direction==0){
			if (dx != 0){
				setAnim(walkRightAnim);
			}
			else{
				setAnim(idleR);
			}
		}
		else if(direction==1){
			if (dx != 0){
				setAnim(walkLeftAnim);
			}
			else{
				setAnim(idleL);
			}
		}
		else if(direction==2){
			if (dy != 0){
				setAnim(walkUpAnim);
			}
			else{
				setAnim(idleU);
			}
		}
		else if(direction==3){
			if (dy != 0){
				setAnim(walkDownAnim);
			}
			else{
				setAnim(idleD);
			}
		}
		if(oldAnim != getAnim()){
			getAnim().reset();
		}
		getAnim().update();
	}
	protected void walkLeft(){ dx = -MOVESPEEDX; }
	protected void walkRight(){ dx = +MOVESPEEDX; }
	protected void walkUp(){ dy = -MOVESPEEDY; }
	protected void walkDown(){ dy = MOVESPEEDY; }
	protected void stopXWalk(){ dx= 0; }
	protected void stopYWalk(){ dy =0; }


	
}//end class
