package EnemyModel;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import AnimationModel.Animation;
import AnimationModel.CollisionBoxAnim;
import EffectModel.EffectManager;
import EffectModel.Invulnerability;
import EffectModel.SimpleKnockBack;
import GameObjectModel.Collidable;
import GameObjectModel.Direction;
import MiscModel.Attack;
import PlayerModel.Player;
import View.GameRender;
import View.ImageStyler;

public class Enemy extends Collidable {
	
	//TODO HAVE A FREEZE OPTION- make creature parent class for enemy and player
	int HP, totalHP, str, def, intel;
	String name;
	//int direction = 0;
	
	/******AI Vars******/

	private AI behaviour;
	private boolean dumb;
	private boolean smart;
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
	
	
	

	
	
	
	//TODO gameobject sub class should take care of movement
	//...what
/*	private float dx;
	private float dy;*/
	
	public Enemy(float x, float y){
		super(x,y);
		setScale(5f);
		animator = new EnemyAnimator<Enemy>(this);
//		Point2D.Float[] path = {new Point2D.Float(x+150,y),new Point2D.Float(x+400,y+300),new Point2D.Float(x-100,y+100)};
		Point2D.Float[] path = {new Point2D.Float(x+200, y) , new Point2D.Float(x+200, y+250)};
		setTrigger(true);
		dumb=true;
		smart=false;
		attack = new Attack(this,0, getHeight()/2, 30, 100, getHeight()/2, 40,40);
		behaviour = new PathingAI(this, smart, dumb, 300, 300, 100, 70, path, false);
		
	
		HP =100;
		//collision settings
		collisionBox = new Rectangle2D.Float(x,y,getWidth(),getHeight());		//sets collision box to currentAnim dimensions (IDLE)
		centerX = (float) (collisionBox.getWidth()/2);
		centerY = (float) (collisionBox.getHeight()/2);
		useCenterPoint=true;
		
	}

	/**
	 * Makes the enemy take damage
	 * @param dmg
	 * @param a
	 */
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
	public void update(Player hero, ArrayList<Collidable> things){
		super.update();
		x += dx;
		y += dy;
		
		//Collision Updates
		//checks if the enemy gets hit by player, also checks if the hero is collide if so hero gets hurt then
		for (Collidable obj: things){
			if (checkCollision(obj) && !obj.isTrigger() && (obj != this)){
				x -=dx;
				y -=dy;
				if(!checkTBCollision(obj)){
					x+=dx;
				}
				if(!checkLRCollision(obj)){
					y+=dy;
				}
			}
		}
		if(checkCollision(hero)){		//collide with player
			hero.takeDamage(20,this);
			behaviour.triggerChase();
		}
		else if(attack.checkCollision(hero) && attack.isLive()){	//attacked player
			hero.takeDamage(20, this);
			behaviour.triggerChase();
		}
		else if(checkShapeCollision(hero.getWeapon())){	//collide with enemy
			takeDamage(20,hero);
			behaviour.triggerChase();
		}
		
		//AI
		//direction updates
		behaviour.update(hero);
		//direction updates
		if(!freeze){
			directionUpdate(hero);
		}
		
		if(!cooldown && !attacking)
			checkAttack(hero);
		
		//attack update()
		//if currently attacking or on cooldown
		if(attacking){
			//slow down
			dx/=2;
			dy/=2;
			attack.update();
			if(!attack.isActive()){
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
		
		//collisionBoxUpdates();

	}
	//TODO do this better
	protected void checkAttack(Player hero){
		boolean facingPlayer = dumbCheck(hero);
		boolean inProximity = proximityCheck(hero,attackXProx, attackYProx);
		if((inProximity && dumb && facingPlayer) || (inProximity && !dumb)){
			attacking =true;
			attack.activate();
			freeze=true;
		}
	}
	
	/**
	 * @param hero
	 * @param proxX max distance in x to trigger
	 * @param proxY max distance in y to trigger 
	 * @return if the hero is in proximity of this enemy
	 */
	protected boolean proximityCheck(Player hero, float proxX, float proxY){
		return Math.abs(hero.diffX(this)) <= proxX && Math.abs(hero.diffY(this)) <= proxY;
	}
	/**
	 * checks if the enemy is facing the hero
	 * @return
	 */
	protected boolean dumbCheck(Player hero){
		boolean facingPlayer=false;
		double diffX = hero.diffX(this);
		double diffY = hero.diffY(this);
		if(dumb){
			switch(getDirection()){
				case RIGHT: facingPlayer = diffX > 0; break;	//right
				case LEFT: facingPlayer = diffX < 0; break;		//left
				case UP: facingPlayer = diffY < 0; break;	//up
				case DOWN: facingPlayer = diffY > 0; break;		//down
			}	
		}
		return facingPlayer;
	}
	//updates the direction of the enemy
	private void directionUpdate(Player hero){
		if(!behaviour.isChasing()){			//if not following 
			if(dx > 0){
				//direction = 0;
				setDirection(Direction.RIGHT);
			}
			else if(dx <0){
				//direction = 1;
				setDirection(Direction.LEFT);
			}
			else if(dy <0){
				//direction = 2;
				setDirection(Direction.UP);
			}
			else if(dy >0){
				//direction = 3;
				setDirection(Direction.DOWN);
			}
		}
		else{	//direction changes are different if following
			double xDiff = hero.diffX(this);
			double yDiff = hero.diffY(this);
			if(Math.abs(xDiff) >= Math.abs(yDiff)){	//if x difference is greater than y, direction is L or R
				if(dx > 0){
					//direction = 0;
					setDirection(Direction.RIGHT);
				}
				else if(dx <0){
					//direction = 1;
					setDirection(Direction.LEFT);
				}
			}
			else{						//if y diff is greater than x, direction is U or D
				if(dy <0){
					//direction = 2;
					setDirection(Direction.UP);
				}
				else if(dy >0){
					//direction = 3;
					setDirection(Direction.DOWN);
				}
			}
		}
	}
	
	//updates animation
	//TODO priority system
/*	private void collisionBoxUpdates(){

		if (getDirection() == Direction.UP){
			//setColAnim(upDown);
		}
		else if (getDirection() == Direction.DOWN){
			//setColAnim(upDown);
		}
		if(attacking){
		switch(getDirection()){
			case RIGHT: possibleNewAnim = attackRightAnim; break; 
			case LEFT: possibleNewAnim = attackLeftAnim; break;
			case UP: possibleNewAnim = attackUpAnim; setColAnim(upDown); break;
			case DOWN: possibleNewAnim = attackDownAnim; setColAnim(upDown); break;
			}
		}

	}*/
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
/*	public int getDirection(){ return direction; }*/
	
	public Attack getAttack(){
		return attack;
	}
	
	public boolean isAttacking(){
		return attacking;
	}
}//end class
