/*
 * This is the controller for the player, controls can be set handles input on key press and key release
 * TODO Okay real talk, apparently using key listeners is a bad thing. Honestly tho i have no fucken clue why its so bad
 * everyone seems to be raving about the modularity of it which is nice. But that takes a fuck load of work and right now 
 * i just want the fucken thing to work. So for now im just going to use a controller class with key bindings.
 * In the future we will/should convert this to a java swing key bindings type module. But for now this can suck my dick.
 */
package Controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.Player;

public class PlayerController extends KeyAdapter {
	
	Player player;
	int moveLeft= KeyEvent.VK_LEFT;
	int moveRight = KeyEvent.VK_RIGHT;
	int moveUp = KeyEvent.VK_UP;
	int moveDown= KeyEvent.VK_DOWN;
	
	public PlayerController(Player player){
		this.player = player;
	}
	
	//what to do when the key is pressed
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
    	if(key == moveLeft){
    		player.moveLeft();
    	}
    	else if (key == moveRight){
    		player.moveRight();
    	}
    	else if(key == moveUp){
    		player.moveUp();
    	}
    	else if(key == moveDown){
    		player.moveDown();
    	}
	}
	//What to do when the key is released
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == moveLeft){
			player.stopMovingLeft();
    	}
    	else if (key == moveRight){
    		player.stopMovingRight();
    	}
    	else if(key == moveUp){
    		player.stopMovingUp();
    	}
    	else if(key == moveDown){
    		player.stopMovingDown();
    	}
	}
	
	/**
	 * Set the input to the controller
	 * @param moveLeft Key to move left, if 0 then doesn't change
	 * @param moveRight Key to move right , if 0 then doesn't change
	 * @param moveUp Key to move Up, if 0 then doesn't change
	 * @param moveDown Key to move Down, if 0 then doesn't change
	 */
	public void setControls(int moveLeft, int moveRight, int moveUp, int moveDown){
		if (moveLeft != 0){
			this.moveLeft = moveLeft;
		}
		if (moveRight != 0){
			this.moveRight = moveRight;
		}
		if (moveUp != 0){
			this.moveUp = moveUp;
		}
		if (moveDown != 0){
			this.moveDown = moveDown;
		}
	}

	
}
