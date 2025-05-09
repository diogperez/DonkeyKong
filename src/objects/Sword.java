package objects;

import pt.iscte.poo.utils.Point2D;

public class Sword extends Consumable {
	
	private static final int DAMAGE = 15;
	
	public Sword(Point2D position) {
		super("Sword", position, DAMAGE); //Sword acrescenta por default 15 de ataque
	}

}
