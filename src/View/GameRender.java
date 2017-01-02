/*
 * This class is responsible for the drawing in the game
 * JPanel is the thing being drawn to so it makes the most sense to do all the rendering here
 * Uses a double buffering system to create the image on the screen
 * idk what im doing
 */
package View;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import Model.GameObject;
import Model.Player;
import Model.Rock;

public class GameRender extends JPanel {

	BufferedImage backgroundImg; 	//the background img
	int width;
	int height;
	int offsetX = 0;
	int offsetY = 0;
	
	Image offImg;
	Graphics offGraph;
	
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
		offGraph = offImg.getGraphics();
		/***DRAWING STUFF IN ORDER***/
		drawBackGround(offGraph);
		drawHero(offGraph,hero);
		for (GameObject e: things){
			drawObj(offGraph,e);
		}
		//draw other shit
		
		//drawing to screen
		Graphics g = this.getGraphics();
		g.drawImage(offImg,0,0,null);
		g.dispose();
	}
	
	//Draws the background
	private void drawBackGround(Graphics g){		
        g.drawImage(backgroundImg, offsetX, offsetY,width,height, this);
	}
	//draws hero to screen
	public void drawHero(Graphics g,Player hero){
		//scaling if possible
		int width =  (int) (hero.getWidth());
		int height = (int) (hero.getHeight());

		//NOTE: uses Math.round to get the pixel location or some shit
		// Im not entirely sure if we should have it round here or in the getters themselves
		g.drawImage(hero.getAnim().getCurrFrame(),Math.round(hero.getX()),Math.round(hero.getY()),width,height,this);
		if(hero.debug()){
			int xA =(int) (hero.getX() + hero.getOffsets()[0]);
			int xB =(int) (hero.getX() + width + hero.getOffsets()[1]);
			int yA =(int) (hero.getY() + hero.getOffsets()[2]);
			int yB =(int) (hero.getY() + height + hero.getOffsets()[3]); 
			int borderWidth = Math.abs(xB - xA);
			int borderHeight = Math.abs(yB - yA);
			//if the player is facing the direction that the offset was intended for, do it regular,
			//if not then reverse it
			if(hero.offSetDir() == hero.facingRight()){
				
				g.draw3DRect(xA, yA, borderWidth, borderHeight, true);
			}
			else{
				//reversing x offset
				xA =(int) (hero.getX() - hero.getOffsets()[1]);
				xB =(int) (hero.getX() + width - hero.getOffsets()[0]);
				borderWidth = Math.abs(xB - xA);
				borderHeight = Math.abs(yB - yA);
				g.draw3DRect(xA, yA, borderWidth, borderHeight, true);
			}
			
		}
	}
	
	public void drawObj(Graphics g,GameObject obj){
		//scaling if possible
		//if animation is not empty then draw, else dont bother drawing
		if(obj.getAnim().getCurrFrame() != null){
			
			int width =  (int) (obj.getWidth());
			int height = (int) (obj.getHeight());
			
			//NOTE: uses Math.round to get the pixel location or some shit
			// Im not entirely sure if we should have it round here or in the getters themselves
			g.drawImage(obj.getAnim().getCurrFrame(),Math.round(obj.getX()),Math.round(obj.getY()),width,height,this);
		}
		
	}
}
