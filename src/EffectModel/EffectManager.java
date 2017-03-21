package EffectModel;

import java.util.ArrayList;
/**
 * Handles and Manages all currrent effects for any gameobject
 * Static class that can be accessed by any object
 * Every objet has access to that they may add to it
 * Only the main loop is responsible for updating though
 * @author Michael Bitzos
 *
 */
public class EffectManager{

	private static ArrayList<Effect> effects= new ArrayList<Effect>();
	private static ArrayList<Effect> disposeList= new ArrayList<Effect>();
	
	/**
	 * Adds effect
	 * @param e Effect to add
	 */
	public static void addEffect(Effect e){
		effects.add(e);
	}
	
	/**
	 * Removes effect 
	 * @param e effect to remove
	 */
	public static void removeEffect(Effect e){
		effects.remove(e);
	}
	
	/**
	 * removes effect at index
	 * @param i index of effect to remove
	 */
	public static void removeEffect(int i){
		effects.remove(i);
	}
	
	/**
	 * Updates all effects
	 * Removes all effects from effect manager if done
	 * Uses custom garbage collector
	 */
	public static void update(){
		disposeList.clear();
		for (Effect e: effects){
			e.update();
			if(e.getStatus() == false){
				disposeList.add(e);
			}
		}
		//cleaning up
		for (Effect e: disposeList){
			effects.remove(e);
		}
		
	}
}
