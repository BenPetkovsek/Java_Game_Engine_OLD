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
import MiscModel.Attack;
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
	
	/*****Attacking******/
	protected boolean attacking=false;
	protected boolean freeze=false;
	//assume attackXProx is smaller than chase proximity
	private float attackXProx= 200;	//x direction of attack proximity circle
	private float attackYProx = 200;	//y direction of attack proximity cicle
	private Attack attack;
	
	private boolean cooldown=false;	//if on cooldown
	private int attackCooldown= 70;	//cooldown for attack
	private int cooldownTimer=0;
	
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
	
	private Animation attackRightAnim = new Animation(false,2).addFrameWithLength(attackRight[0],40).addFrameWithLength(attackRight[1],40).addFrame(attackRight[2]).addFrame(attackRight[3]);
	private Animation attackLeftAnim = new Animation(false,2).addFrameWithLength(attackLeft[0],40).addFrameWithLength(attackLeft[1],40).addFrame(attackLeft[2]).addFrame(attackLeft[3]);
	private Animation attackUpAnim = new Animation(false,2).addFrameWithLength(attackUp[0],40).addFrameWithLength(attackUp[1],40).addFrame(attackUp[2]).addFrame(attackUp[3]);
	private Animation attackDownAnim = new Animation(false,2).addFrameWithLength(attackDown[0],40).addFrameWithLength(attackDown[1],40).addFrame(attackDown[2]).addFrame(attackDown[3]);
	
	
	
	
	//TODO gameobject sub class should take care of movement
	//...what
/*	private float dx;
	private float dy;*/
	
	public Enemy(float x, float y){
		super(x,y);
		Point2D.Float[] path = {new Point2D.Float(x,y),new Point2D.Float(x+100,y+300),new Point2D.Float(x-200,y+100)};
		setAnim(idleR);
		setScale(5f);
		setTrigger(true);
		
		attack = new Attack(this,0, getHeight()/2, 30, 100, getHeight()/2, 40,40);
		
		behaviour = new PathingAI(this, true, false, 300, 300, 100, 70, path, false);
		//behaviour = new RandomAI(this,true,false,300,300,100,75,200,100, 200, 100);
		
	
		HP =100;
		collisionBox = new Rectangle2D.Float(x,y,getWidth(),getHeight());
		
	}

	public void takeDamage(int dmg,Collidable a){
		if(!isInvulnerable()){
			EffectManager.addEffect(new SimpleKnockBack(a, this, 200, 5));
			EffectManager.addEffect(new Invulnerability(80, 10,this));
			HP -= dmg;
			checkDeath();
			getAnim().reset();
		}
		
	}
	
	private void checkDeath(){
		if(HP <= 0){
			System.out.println(name + " has died!");
			
		}	
	}
	
	/**
	 * Main update for Enemy
	 * Updates AI, direction and animation
	 * @param hero the player to update in reference to
	 */
	public void update(Player hero){
		x += dx;
		y += dy;
		
		//Collision Updates
		//checks if the enemy gets hit by player, also checks if the hero is collide if so hero gets hurt then
		if(checkCollision(hero)){		//collide with player
			hero.takeDamage(20,this);
		}
		else if(attack.checkCollision(hero) && attack.isLive()){	//attacked player
			hero.takeDamage(20, this);
		}
		else if(checkShapeCollision(hero.getWeapon())){	//collide with enemy
			takeDamage(20,hero);
		}
		
		//AI
		if(!freeze){
			behaviour.update(hero);
			directionUpdate(hero);
		}
		
		if(!cooldown)
			checkAttack(hero);
		//direction updates
		
		
		
		
		//attack update()
		//if currently attacking or on cooldown
		if(attacking){
			//slow down
			dx/=2;
			dy/=2;
			attack.update();
			if(!attack.isActive()){
				/*if(!facingRight()){
					x+=attack.getOffset();
				}*/
				attack.stop();
				attacking =false;
				freeze=false;
				//only put on cooldown when attack is over
				cooldown =true;
				cooldownTimer=0;
			}
			
		}
		
		//cooldown behaviour
		if(cooldown){
			if(cooldownTimer >= attackCooldown) cooldown = false;
			cooldownTimer++;
		}
		
		
		animationUpdates();
		

	}
	//TODO do this better
	protected void checkAttack(Player hero){
		double playerX = hero.getCollisionBox().getCenterX();
		double playerY = hero.getCollisionBox().getCenterY();
		
		double enemyX = getCollisionBox().getCenterX();
		double enemyY = getCollisionBox().getCenterY();
		
		double diffX = playerX - enemyX;
		double diffY = playerY - enemyY;
		
		if(Math.abs(diffX) < attackXProx && Math.abs(diffY) < attackYProx && !attacking){		//making sure you cant reset attack while attacking
			attacking =true;
			attack.activate();
			freeze=true;
		}
	}
	//updates the direction of the enemy
	private void directionUpdate(Player hero){
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
		if(attacking){
			switch(direction){
			case 0: setAnim(attackRightAnim); break; 
			case 1: setAnim(attackLeftAnim); break;
			case 2: setAnim(attackUpAnim); break;
			case 3: setAnim(attackDownAnim); break;
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


	public AI getAI(){
		return behaviour;
	}
	
	/**
	 * Returns the direction of enemy
	 * TODO move this to Animatable
	 * @return 0 - right 1 - left 2 - up 3 down
	 */
	public int getDirection(){ return direction; }
	
	public Attack getAttack(){
		return attack;
	}
	
	public boolean isAttacking(){
		return attacking;
	}
}//end class
