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
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import Model.Attack;
import Model.Enemy;
import Model.GameObject;
import Model.LoadTrigger;
import Model.Player;
import Model.Rock;

public class GameRender extends JPanel {

	BufferedImage backgroundImg; 	//the background img
	int width;
	int height;
	int offsetX = 0;
	int offsetY = 0;
	
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
	
	//set static image with offset
	public void setBackgroundOffset(int x, int y){
		offsetX = x;
		offsetY =x;
	}
	
	//set image to scale to screen
	public void setBackground(BufferedImage img, int width, int height){
		backgroundImg =img;
		this.width = width;
		this.height = height;
	}
	
	//The draw method that should be called from model
	//DOUBLE BUFFERING MAGIC YO
	public void draw(Player hero,ArrayList<GameObject> things){
		offImg = createImage(width, height);
		offGraph = (Graphics2D) offImg.getGraphics();
		/***DRAWING STUFF IN ORDER***/
		drawBackGround(offGraph);
		drawHero(offGraph,hero);
		for (GameObject e: things){
			if(!(e instanceof LoadTrigger)){
				drawObj(offGraph,e);
			}
			else{
				drawCollisionBox(offGraph,e);
			}
			
			
		}
		
		
		
		//draw other shit
		
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
		if(!hero.isBlinked()){
			drawObj(g,hero);
			if (hero.isAttacking()){
				Attack a = hero.getAttack();
				g.draw3DRect((int) a.getX(),(int) a.getY(),(int) a.getWidth(),(int) a.getHeight(), true);
			}
		}
	}
	//draws game objects to screen
	public void drawObj(Graphics2D g,GameObject obj){
		//scaling if possible
		int width =  (int) (obj.getWidth());
		int height = (int) (obj.getHeight());

		//NOTE: uses Math.round to get the pixel location or some shit
		// Im not entirely sure if we should have it round here or in the getters themselves
		g.drawImage(obj.getAnim().getCurrFrame(),Math.round(obj.getX()),Math.round(obj.getY()),width,height,this);
		if(obj.debug()){ 
			drawCollisionBox(g,obj);
		}
	}
	
	//lmao see how easy it is with shapes
	private void drawCollisionBox(Graphics2D g, GameObject obj){
		//some objects dont have col boxs so dont do it
		if(obj.getCollisionBox() != null){
			g.draw(obj.getCollisionBox());
		}
		
		/*int width =  (int) (obj.getWidth());
		int height = (int) (obj.getHeight());
		float xAO = obj.getOffsets()[0];
		float xBO = obj.getOffsets()[1];
		float yAO = obj.getOffsets()[2];
		float yBO = obj.getOffsets()[3];
		
		//System.out.println(xBO);
		
		int xA =(int) (obj.getX() + xAO);
		int xB =(int) (obj.getX() + width + xBO);
		int yA =(int) (obj.getY() +yAO);
		int yB =(int) (obj.getY() + height + yBO); 
		int borderWidth = Math.abs(xB - xA);
		int borderHeight = Math.abs(yB - yA);
		//if the player is facing the direction that the offset was intended for, do it regular,
		//if not then reverse it
		if(obj.offSetDir() == obj.facingRight()){
			
			g.draw3DRect(xA, yA, borderWidth, borderHeight, true);
		}
		else{
			//reversing x offset
			xA =(int) (obj.getX() - xBO);
			xB =(int) (obj.getX() + width - xAO);
			borderWidth = Math.abs(xB - xA);
			borderHeight = Math.abs(yB - yA);
			g.draw3DRect(xA, yA, borderWidth, borderHeight, true);
		}*/
	}
}
