package View;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GameRender extends JPanel {

	BufferedImage backgroundImg; 	//the background img
	boolean scaled =false;	//determines if the background should be scaled
	int panelWidth;
	int panelHeight;
	int offsetX = 0;
	int offsetY = 0;
	
	//set static image
	public void setBackground(BufferedImage img){
		backgroundImg =img;
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
	public void draw(){
		Graphics g= this.getGraphics();
		drawBackGround(g);
		//draw other shit
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
}
