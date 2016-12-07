package View;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GameRender extends JPanel {

	BufferedImage backgroundImg; 	//the background img
	boolean scaled =false;	//determines if the background should be scaled
	int panelWidth;
	int panelHeight;
	int offsetX;
	int offsetY;
	
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
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // paint the background image and scale it to fill the entire space
        drawBackGround(g);
        
        
	}
	
	private void drawBackGround(Graphics g){
        if (scaled){
        	g.drawImage(backgroundImg, offsetX, offsetY,panelWidth,panelHeight, this);
        }
        else{
        	g.drawImage(backgroundImg, offsetX, offsetY, this);
        }
	}
}
