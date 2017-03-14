package Controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import Model.Player;

public class MouseTracking implements MouseMotionListener{

	private Player hero;
	public MouseTracking(Player hero){
		this.hero =hero;
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		hero.recieveMousePos(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		hero.recieveMousePos(e.getX(), e.getY());
	}
	

}
