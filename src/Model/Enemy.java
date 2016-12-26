package Model;

public class Enemy extends GameObject {
	
	int HP, totalHP, str, def, intel;
	String name;
	public Enemy(String initName, int initHP, int initStr, int initDef, int initIntel ){
		HP = initHP;
		totalHP = initHP;
		str = initStr;
		def = initDef;
		intel = initIntel;
		name = initName;
		
	}
	
	public void takeDamage(int dmg){
		HP -= dmg;
		checkDeath();
	}
	
	private void checkDeath(){
		if(HP <= 0){
			System.out.println(name + " has died!");
		}	
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}
	
}//end class
