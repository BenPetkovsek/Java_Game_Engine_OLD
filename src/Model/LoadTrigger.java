package Model;

import java.awt.geom.Rectangle2D;

public class LoadTrigger extends Collidable {
	
	String destination;
	
	public LoadTrigger(float x, float y, String initDestination) {
		super(x, y);
		this.destination = initDestination;
		drawBorders = true;
		setTrigger(true);
		collisionBox = new Rectangle2D.Float(x,y,50,50);
	}
	
	public void update(Player hero){
		if(checkCollision(hero)){
			MainLoop.changeCurrentLevel(destination);
			PlayerPhysics.updateWindowVars();
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
