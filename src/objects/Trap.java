package objects;

import pt.iscte.poo.utils.Point2D;

public class Trap extends Block {
	
	private static final int DAMAGE = 10;
	
	public Trap(Point2D position) {
		super("Trap", position, true);
	}
	
	public int getDamage() {
		return DAMAGE;
	}
}
