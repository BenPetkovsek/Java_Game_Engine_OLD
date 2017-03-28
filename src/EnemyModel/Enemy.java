package EnemyModel;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import AnimationModel.Animation;
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
	
	private Animation attackRightAnim = new Animation(false,2).addFrame(attackRight[0],40).addFrame(attackRight[1],40).addFrame(attackRight[2]).addFrame(attackRight[3]);
	private Animation attackLeftAnim = new Animation(false,2).addFrame(attackLeft[0],40).addFrame(attackLeft[1],40).addFrame(attackLeft[2]).addFrame(attackLeft[3]);
	private Animation attackUpAnim = new Animation(false,2).addFrame(attackUp[0],40).addFrame(attackUp[1],40).addFrame(attackUp[2]).addFrame(attackUp[3]);
	private Animation attackDownAnim = new Animation(false,2).addFrame(attackDown[0],40).addFrame(attackDown[1],40).addFrame(attackDown[2]).addFrame(attackDown[3]);
	
	
	
	
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
		dumb=true;
		attack = new Attack(this,0, getHeight()/2, 30, 100, getHeight()/2, 40,40);
		
		behaviour = new PathingAI(this, true, dumb, 300, 300, 100, 70, path, false);
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
	public void update(Player hero, ArrayList<Collidable> things){
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
			//takeDamage(20,hero);
			behaviour.triggerChase();
		}
		
		//AI
		//direction updates
		if(!freeze){
			behaviour.update(hero);
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
		
		
		animationUpdates();
		

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
			//TODO possible seperate this
			double xDiff = hero.getCollisionBox().getCenterX()-getCollisionBox().getCenterX();
			double yDiff = hero.getCollisionBox().getCenterY()-getCollisionBox().getCenterY();
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
	private void animationUpdates(){
		Animation oldAnim = getAnim();
		switch(getDirection()){
		case RIGHT:
			if (dx != 0){
				setAnim(walkRightAnim);
			}
			else{
				setAnim(idleR);
			}
			break;
		case LEFT:
			if (dx != 0){
				setAnim(walkLeftAnim);
			}
			else{
				setAnim(idleL);
			}
			break;
		case UP:
			if (dy != 0){
				setAnim(walkUpAnim);
			}
			else{
				setAnim(idleU);
			}
			break;
		case DOWN:
			if (dy != 0){
				setAnim(walkDownAnim);
			}
			else{
				setAnim(idleD);
			}
			break;
		}
		if(attacking){
			switch(getDirection()){
			case RIGHT: setAnim(attackRightAnim); break; 
			case LEFT: setAnim(attackLeftAnim); break;
			case UP: setAnim(attackUpAnim); break;
			case DOWN: setAnim(attackDownAnim); break;
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
/*	public int getDirection(){ return direction; }*/
	
	public Attack getAttack(){
		return attack;
	}
	
	public boolean isAttacking(){
		return attacking;
	}
}//end class
