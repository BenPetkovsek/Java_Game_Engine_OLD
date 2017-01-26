package Model;

import java.awt.geom.Rectangle2D;

public class LoadTrigger extends GameObject {
	String destination;
	public LoadTrigger(float x, float y, String initDestination) {
		super(x, y);
		this.destination = initDestination;
		drawBorders = true;
		isCollidable = false;
		collisionBox = new Rectangle2D.Double(x,y,50,50);
	}
	
	public void update(Player hero){
		if(checkCollision(hero)){
			MainLoop.changeCurrentLevel(destination);
			Player.updateWindowVars();
			System.out.println("Changed level to: " + destination);
		}
	}
	
	@Override
	public float getWidth(){
		return 50;
	}
	@Override
	public float getHeight(){
		return 50;
	}
	
	
	

}
