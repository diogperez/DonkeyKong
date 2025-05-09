package objects;

import pt.iscte.poo.utils.Point2D;

public abstract class Consumable extends GameElement {
	
	private int effect; 
	private static final int LAYER = 1;
		
	public Consumable(String name, Point2D position, int effect) {
		super(name, position, LAYER); 
		this.effect = effect;
	}
	
	public int getEffect() {
		return effect;
	}
	
	public void setEffect(int e) {
		effect = e;
	}
	
	public String toString() {
		return super.toString() + "; effect value = " + effect;
	}

}
