package Model;

public class Enemy {
	
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
	
}//end class
