package View;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import Model.Player;

public class GameRender extends JPanel {

	BufferedImage backgroundImg; 	//the background img
	boolean scaled =false;	//determines if the background should be scaled
	int panelWidth;
	int panelHeight;
	int offsetX = 0;
	int offsetY = 0;
	
	Image offImg;
	Graphics offGraph;
	
	public GameRender(){
		
	}
	
	//set static image
	public void setBackground(BufferedImage img){
		backgroundImg =img;
		panelWidth =backgroundImg.getWidth();
		panelHeight =backgroundImg.getHeight();
	}
	
	//set static image with offset
	public void setBackgroundOffset(int x, int y){
		offsetX = x;
		offsetY =x;
	}
	
	//set image to scale to screen
	public void setBackground(BufferedImage img, int width, int height){
		backgroundImg =img;
		panelWidth = width;
		panelHeight = height;
		scaled =true;
	}
	
	//The draw method that should be called from model
	//DOUBLE BUFFERING MAGIC YO
	public void draw(Player hero){
		offImg = createImage(panelWidth, panelHeight);
		offGraph = offImg.getGraphics();
		/***DRAWING STUFF IN ORDER***/
		drawBackGround(offGraph);
		drawHero(offGraph,hero);
		//draw other shit
		
		//drawing to screen
		Graphics g = this.getGraphics();
		g.drawImage(offImg,0,0,null);
		g.dispose();
	}
	
	//Draws the background based on if it has to scale or not
	private void drawBackGround(Graphics g){		
        if (scaled){
        	g.drawImage(backgroundImg, offsetX, offsetY,panelWidth,panelHeight, this);
        }
        else{
        	g.drawImage(backgroundImg, offsetX, offsetY, this);
        }
	}
	//draws hero to screen
	public void drawHero(Graphics g,Player hero){
		//scaling if possible
		int width =  (int) (hero.getImage().getWidth() * hero.getScale());
		int height = (int) (hero.getImage().getHeight() * hero.getScale());
		g.drawImage(hero.getImage(),hero.getX(),hero.getY(),width,height,this);
	}
}
