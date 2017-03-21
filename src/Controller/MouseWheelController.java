package Controller;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import PlayerModel.Player;

public class MouseWheelController implements MouseWheelListener  {

	private Player hero;
	
	public MouseWheelController(Player hero){
		this.hero=hero;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		boolean up = e.getWheelRotation() < 0;
		hero.getWeapon().change(up);
	}

}
