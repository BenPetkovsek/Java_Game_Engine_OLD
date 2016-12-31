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

import javax.swing.JPanel;

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
	public void draw(Player hero,Rock rock){
		offImg = createImage(width, height);
		offGraph = offImg.getGraphics();
		/***DRAWING STUFF IN ORDER***/
		drawBackGround(offGraph);
		drawHero(offGraph,hero);
		drawRock(offGraph,rock);
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
	}
	
	public void drawRock(Graphics g,Rock hero){
		//scaling if possible
		//if animation is not empty then draw, else dont bother drawing
		if(hero.getAnim().getCurrFrame() != null){
			
			int width =  (int) (hero.getWidth());
			int height = (int) (hero.getHeight());

			//NOTE: uses Math.round to get the pixel location or some shit
			// Im not entirely sure if we should have it round here or in the getters themselves
			g.drawImage(hero.getAnim().getCurrFrame(),Math.round(hero.getX()),Math.round(hero.getY()),width,height,this);
		}
		
	}
}
