/*
 * This class is responsible for the drawing in the game
 * JPanel is the thing being drawn to so it makes the most sense to do all the rendering here
 * Uses a double buffering system to create the image on the screen
 * idk what im doing
 */
package View;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import EnemyModel.Enemy;
import EnemyModel.PathingAI;
import GameObjectModel.Animatable;
import GameObjectModel.Collidable;
import GameObjectModel.GameObject;
import GameObjectModel.LoadTrigger;
import GameObjectModel.TerrainObject;
import MiscModel.Attack;
import MiscModel.MainLoop;
import PlayerModel.Player;
import PlayerModel.Weapon;

public class GameRender extends JPanel {

	BufferedImage backgroundImg; 	//the background img
	public static int width;
	public static int height;
	static int offsetX = 0; 
	static int offsetY = 0;
	
	Image offImg;
	Graphics2D offGraph;
	
	public GameRender(){
		
	}
	
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	
	//set static image
	public void setBackground(BufferedImage img){
		backgroundImg =img;
		width =backgroundImg.getWidth();
		height =backgroundImg.getHeight();
	}
	
	//get height of specified image
	public static int getHeightOf(String img){
		return ImageStyler.loadImg(img).getHeight();
	}
	
	//get width of specified image
	public static int getWidthOf(String img){
		return ImageStyler.loadImg(img).getWidth();
	}
	
	//set static image with offset
	public static void setBackgroundOffset(int x, int y){
		offsetX = x;
		offsetY =y;
	}
	
	//set image to scale to screen
	public void setBackground(BufferedImage img, int width, int height){
		backgroundImg =img;
		this.width = width;
		this.height = height;
	}
	
	//The draw method that should be called from model
	//DOUBLE BUFFERING MAGIC YO
	public void draw(Player hero,ArrayList<Animatable> levelObjects){
		offImg = createImage(width, height);
		offGraph = (Graphics2D) offImg.getGraphics();
		/***DRAWING STUFF IN ORDER***/
		drawBackGround(offGraph);
		drawHero(offGraph,hero);
		for (Animatable e: levelObjects){
			if(!(e instanceof LoadTrigger)){
				drawObj(offGraph,e);
				if(e instanceof Enemy){
					drawPath(offGraph,(Enemy) e);
					if(((Enemy) e).getAttack().isLive())
						drawCollisionBox(offGraph,((Enemy) e).getAttack());
				}
			}
			
			if(e instanceof Collidable){
				if(((Collidable) e).debug()){
					drawCollisionBox(offGraph,(Collidable) e);
				}
			}
		}
		
		//drawing to screen
		Graphics2D g = (Graphics2D) this.getGraphics();
		g.drawImage(offImg,0,0,null);
		g.dispose();
	}
	
	//Draws the background
	private void drawBackGround(Graphics2D g){		
        g.drawImage(backgroundImg, offsetX, offsetY,width,height, this);
	}
	
	public void drawHero(Graphics2D g, Player hero){
		int width =  (int) (hero.getWidth());
		int height = (int) (hero.getHeight());
		
		//Weapon rendering
		Weapon weapon = hero.getWeapon();
		
		
		//same as drawObj method
		g.drawImage(hero.getAnim().getCurrFrame(),Math.round(hero.getX()),Math.round(hero.getY()),width,height,this);
		
		drawWeapon(g,weapon,hero);
		if(hero.debug()){ 
			drawCollisionBox(g,hero);
		}
		/*if (hero.isAttacking()){
			Attack a = hero.getAttack();
			g.draw3DRect((int) a.getX(),(int) a.getY(),(int) a.getWidth(),(int) a.getHeight(), true);
		}*/
	}
	/*
	 * Draws the Weapon attached to the player
	 */
	private void drawWeapon(Graphics2D g, Weapon weapon, Player hero){
		
		BufferedImage weaponImg = weapon.getAnim().getCurrFrame();
		Graphics2D weaponGraphics = (Graphics2D) weaponImg.getGraphics();
		
		weaponGraphics.transform(weapon.getTransform());
		
		g.drawImage(weaponImg, weapon.getTransform(),this);
		
		drawWeaponCollisionBox(g,weapon);
	}
	
	//draws game objects to screen
	public void drawObj(Graphics2D g,Animatable obj){
		//scaling if possible
		int width =  (int) (obj.getWidth());
		int height = (int) (obj.getHeight());

		//NOTE: uses Math.round to get the pixel location or some shit
		// Im not entirely sure if we should have it round here or in the getters themselves
		obj.setXOffset(offsetX);
		obj.setYOffset(offsetY);
		g.drawImage(obj.getAnim().getCurrFrame(),Math.round(obj.getX() + offsetX),Math.round(obj.getY() + offsetY),width,height,this);
	}
	
	//lmao see how easy it is with shapes
	private void drawCollisionBox(Graphics2D g, Collidable obj){
		//some objects dont have col boxs so dont do it
		if(obj.getCollisionBox() != null){
			g.draw(obj.getCollisionBox());
		}
		if(obj.useCenterPoints()){
			g.draw3DRect((int) obj.getCenterX(),(int) obj.getCenterY(), 10, 10, false);
		}

	}
	
	/*
	 * Modified version of drawCollisionBox to draw weapon col box
	 */
	private void drawWeaponCollisionBox(Graphics2D g, Weapon obj){
		//some objects dont have col boxs so dont do it
		if(obj.getCollisionShape() != null){
			g.draw(obj.getCollisionShape());
		}

	}
	
	private void drawPath(Graphics2D g,Enemy e){
		if(e.getAI() instanceof PathingAI){
			Point2D.Float[] path = ((PathingAI) e.getAI()).getPath();
			for (Point2D.Float pt : path){
				g.draw3DRect((int) pt.x,(int) pt.y, 10, 10, false);
			}
		}
	}
	
	public static int getBGOffsetX(){
		return offsetX;
	}
	public static int getBGOffsetY(){
		return offsetY;
	}
}
