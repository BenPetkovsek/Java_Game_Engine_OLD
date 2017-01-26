package Model;

public class LoadTrigger extends GameObject {
	String destination;
	public LoadTrigger(float x, float y, String initDestination) {
		super(x, y);
		this.destination = initDestination;
		drawBorders = true;
		isCollidable = false;
	}
	
	public void update(Player hero){
		if(checkAllCollision(hero)){
			MainLoop.changeCurrentLevel(destination);
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
