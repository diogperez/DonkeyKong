package objects;

import pt.iscte.poo.utils.Point2D;

public class Floor extends Block { 

	public Floor(Point2D position) {
		super("Floor", position, false); //Camada 0, background de tudo
	}

}
