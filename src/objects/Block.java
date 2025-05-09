package objects;

import pt.iscte.poo.utils.Point2D;

public abstract class Block extends GameElement {
	
	private final boolean solid;
	private static final int LAYER = 0;

	public Block(String name, Point2D position, boolean solid) {
		super(name, position, LAYER);
		this.solid = solid;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public String toString() {
		return super.toString() + "; is solid? " + solid; 
	}

	
	
}
