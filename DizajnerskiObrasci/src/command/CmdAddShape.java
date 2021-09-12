package command;

import model.DrawingModel;
import shapes.Shape;

public class CmdAddShape implements Command {
	
	private Shape shape;
	private DrawingModel model;
	
	public CmdAddShape(Shape shape, DrawingModel model) {
		this.shape=shape;
		this.model=model;
	}

	@Override
	public void execute() {
		this.model.addShape(shape);
		
	}

	@Override
	public void unexecute() {
		this.model.removeShape(shape);
		
	}

}
